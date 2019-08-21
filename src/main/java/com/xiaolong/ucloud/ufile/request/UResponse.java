
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonObject;
import org.apache.http.Header;

import java.io.InputStream;

public class UResponse
{
    /**
     * Http Response的Headers
     */
    private Header[] headers;

    /**
     * UFile 应答内容
     */
    private JsonObject response;

    /**
     * Http 返回的文件内容
     */
    private InputStream content;

    private int retCode;

    private String retMsg;

    public UResponse(int retCode) {
        this.retCode = retCode;
    }

    public UResponse(JsonObject response)
    {
        this.response = ObjectsUtil.requireNonNull(response, "response is null");
    }

    public UResponse(Header[] headers)
    {
        this.headers = headers;
    }

    public UResponse(JsonObject response, Header[] headers)
    {
        this.response = ObjectsUtil.requireNonNull(response, "response is null");
        this.headers = ObjectsUtil.requireNonNull(headers, "headers is null");
    }

    public UResponse(Header[] headers, InputStream content)
    {
        this.headers = ObjectsUtil.requireNonNull(headers, "headers is null");
        this.content = ObjectsUtil.requireNonNull(content, "content is null");
    }

    public Header[] getHeaders()
    {
        return headers;
    }

    public UResponse setHeaders(Header[] allHeaders)
    {
        this.headers = allHeaders;
        return this;
    }

    public JsonObject getResponse()
    {
        return this.response;
    }

    public UResponse setResponse(JsonObject response)
    {
        this.response = response;
        return this;
    }

    public InputStream getContent()
    {
        return content;
    }

    public UResponse setContent(InputStream content)
    {
        this.content = content;
        return this;
    }

    public int getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
