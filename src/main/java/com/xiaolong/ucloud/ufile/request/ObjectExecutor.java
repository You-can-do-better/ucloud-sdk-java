package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.ufile.UFileHeaders;
import com.xiaolong.ucloud.ufile.UFileSignatureBuilder;
import com.xiaolong.ucloud.ufile.exception.UFileClientException;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

public final class ObjectExecutor
        extends AbstractExcector
{
    public static final String AUTHORIZATION = "Authorization";

    public ObjectExecutor(UCloudCredentials credentials)
    {
        super(credentials);
    }

    private String getContentType(Header[] headers)
    {
        for (Header header : headers) {
            if (header.getName().equals(UFileHeaders.CONTENT_TYPE)) {
                return header.getValue();
            }
        }
        return null;
    }

    private InputStream cloneContent(InputStream stream)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[1024];
        int len;
        while ((len = stream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        // Open new InputStreams using the recorded bytes
        // Can be repeated as many times as you wish
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public UResponse execute(UObjectRequest request, String objectKey)
            throws UFileClientException
    {
        ObjectsUtil.requireNonNull(request, "Object request is null");
        ObjectsUtil.requireNonNull(objectKey, "Object key is null");

        // 生成Http请求
        HttpUriRequest httpRequest = null;
        try {
            String uri = null;
            if (request.getObjectKey() != null) {
                uri = "http://"
                        + request.getBucketName() + "." + request.getRegion().getValue() + ".ufileos.com"
                        + "/" + URLEncoder.encode(request.getObjectKey(), "UTF-8");
            }
            else {
                uri = "http://"
                        + request.getBucketName() + "." + request.getRegion().getValue() + ".ufileos.com"
                        + "/?list";
            }
            URIBuilder builder = new URIBuilder(uri);
            for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
            switch (request.getHttpType()) {
                case GET:
                    httpRequest = new HttpGet(builder.build());
                    break;
                case POST:
                    httpRequest = new HttpPost(builder.build());
                    break;
                case DELETE:
                    httpRequest = new HttpDelete(builder.build());
                    break;
                case PUT:
                    HttpPut put = new HttpPut(builder.build());
                    if (request.getObjectStream() != null) {
                        InputStreamEntity entity = new InputStreamEntity(
                                request.getObjectStream(), request.getObjectStreamLength());
                        put.setEntity(entity);
                    }
                    httpRequest = put;
                    break;
                case HEAD:
                    httpRequest = new HttpHead(builder.build());
            }

            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                httpRequest.addHeader(entry.getKey(), entry.getValue());
            }
            // 计算API的签名
            String signature = UFileSignatureBuilder.getSignature(request, objectKey, getCredentials());
            httpRequest.addHeader(AUTHORIZATION, signature);
        }
        catch (UnsupportedEncodingException e) {
            throw new UFileClientException("URI Encode Error.", e);
        }
        catch (URISyntaxException e) {
            throw new UFileClientException("URI Syntax Error.", e);
        }

        CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
        }
        catch (Exception e) {
            throw new UFileClientException("Http Client Execute Failed.", e);
        }

        try {
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = httpResponse.getEntity();
                UResponse response = new UResponse(200);
                response.setHeaders(httpResponse.getAllHeaders());
                if (resEntity == null) {
                    return response;
                }
                else {
                    if (resEntity.getContentLength() <= 0) {
                        return response;
                    }
                    String contentType = getContentType(httpResponse.getAllHeaders());
                    if (contentType.equals("text/plain") || contentType.equals("application/json")) {
                        Reader reader = new InputStreamReader(resEntity.getContent());
                        JsonObject json = (new JsonParser()).parse(reader).getAsJsonObject();
                        if (!json.has("RetCode")) {
                            // List Objects请求时没有RetCode
                            response.setResponse(json);
                            return response;
                        }
                        Long returnCode = json.get("RetCode").getAsLong();
                        if (returnCode != 0) {
                            if (json.has("ErrMsg")) {
                                String errorMessage = json.get("ErrMsg").getAsString();
                                throw new UFileServiceException(200, returnCode, errorMessage);
                            }
                            throw new UFileServiceException(200, "Return Code: " + returnCode);
                        }
                        else {
                            json.remove("RetCode");
                            if (json.has("ErrMsg")) {
                                json.remove("ErrMsg");
                            }
                            response.setResponse(json);
                            return response;
                        }
                    }
                    else {
                        // 二进制的数据
                        response.setContent(cloneContent(resEntity.getContent()));
                        return response;
                    }
                }
            }
            else if (httpResponse.getStatusLine().getStatusCode() == 204) {
                UResponse response = new UResponse(204);
                response.setHeaders(httpResponse.getAllHeaders());
                return response;
            }
            else if (httpResponse.getStatusLine().getStatusCode() == 206) {
                // 请求对象部分数据
                HttpEntity resEntity = httpResponse.getEntity();
                UResponse response = new UResponse(206);
                response.setHeaders(httpResponse.getAllHeaders());
                if (resEntity == null) {
                    return response;
                }
                else {
                    response.setContent(cloneContent(resEntity.getContent()));
                    return response;
                }
            }
            else {
                // 服务器返回错误
                int code = httpResponse.getStatusLine().getStatusCode();
                UResponse response = new UResponse(code);
                response.setHeaders(httpResponse.getAllHeaders());
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    String content = getContentAsString(resEntity.getContent());
                    JsonParser parser = new JsonParser();
                    JsonObject json = parser.parse(content).getAsJsonObject();
                    if (!json.has("RetCode")) {
                        response.setRetMsg("RetCode missing");
                        return response;
                    }
                    Long returnCode = json.get("RetCode").getAsLong();
                    if (returnCode != 0) {
                        if (json.has("ErrMsg")) {
                            String errorMessage = json.get("ErrMsg").getAsString();
                            response.setRetMsg(errorMessage);
                            return response;
                        }
                        return response;
                    }
                    else {
                        return response;
                    }
                }
                else {
                    return response;
                }
            }
        }
        catch (UFileServiceException se) {
            throw se;
        }
        catch (Exception e) {
            throw new UFileClientException(e);
        }
        finally {
            try {
                if (httpResponse != null)
                    httpResponse.close();
            }
            catch (IOException e) {
                throw new UFileClientException(e);
            }
        }
    }
}
