package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.UCloudSignatureBuilder;
import com.xiaolong.ucloud.ufile.exception.UFileClientException;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Map;

public final class BucketExecutor
        extends AbstractExcector
{
    protected static final String UCLOUD_API_HOST = "api.ucloud.cn";

    public BucketExecutor(UCloudCredentials credentials)
    {
        super(credentials);
    }

    public UResponse execute(UBucketRequest request)
            throws UFileClientException
    {
        ObjectsUtil.requireNonNull(request, "bucket request is null.");
        // 计算API请求的签名
        String signature = UCloudSignatureBuilder.getSignature(request, getCredentials());

        // 构建Http URI
        HttpUriRequest httpRequest = null;
        try {
            URIBuilder builder = new URIBuilder("https://" + UCLOUD_API_HOST + "/");
            for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
            builder.setParameter("PublicKey", getCredentials().getPublicKey());
            builder.setParameter("Signature", signature);

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
                    httpRequest = new HttpPut(builder.build());
                    break;
                case HEAD:
                    httpRequest = new HttpHead(builder.build());
                    break;
            }
        }
        catch (URISyntaxException e) {
            throw new UFileClientException("Create Http Request Error.", e);
        }

        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            int httpStatusCode =  httpResponse.getStatusLine().getStatusCode();
            if (httpStatusCode == 200) {
                String content = getContentAsString(httpResponse.getEntity().getContent());
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(content).getAsJsonObject();

                if (!json.has("RetCode")) {
                    throw new UFileServiceException(httpStatusCode, "RetCode missing.");
                }
                Long returnCode = json.get("RetCode").getAsLong();
                if (returnCode != 0) {
                    throw new UFileServiceException(httpStatusCode, "Return Code error: " + content);
                }
                json.remove("RetCode");

                if (!json.has("Action")) {
                    throw new UFileServiceException(httpStatusCode, "Action missing.");
                }
                String action = json.get("Action").getAsString();
                if (!action.equals(request.getActionName() + "Response")) {
                    throw new UFileServiceException(httpStatusCode, "Action mismatch: " + action);
                }
                json.remove("Action");
                return new UResponse(json);
            }
            else {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    String content = getContentAsString(resEntity.getContent());
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = parser.parse(content).getAsJsonObject();
                    if (!jsonResponse.has("RetCode")) {
                        throw new UFileServiceException(httpStatusCode, "RetCode missing.");
                    }
                    Long returnCode = jsonResponse.get("RetCode").getAsLong();
                    if (returnCode != 0) {
                        throw new UFileServiceException(httpStatusCode, "Return Code:" + returnCode);
                    }

                    if (jsonResponse.has("Message")) {
                        String message = jsonResponse.get("Message").getAsString();
                        throw new UFileServiceException(httpStatusCode, returnCode, message);
                    }
                    throw new UFileServiceException(httpStatusCode, returnCode);
                }
                else {
                    throw new UFileServiceException(httpStatusCode);
                }
            }
        }
        catch (Exception e) {
            throw new UFileClientException(e);
        }
    }
}
