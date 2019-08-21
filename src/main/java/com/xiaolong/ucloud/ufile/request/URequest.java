
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

import java.util.HashMap;
import java.util.Map;

public class URequest
{
    private final UFileRegion region;

    private final HttpType httpType;

    /**
     * 请求的URL Parameters
     */
    private final Map<String, String> parameters = new HashMap<String, String>();

    /**
     * 请求的URL Headers
     */
    private Map<String, String> headers = new HashMap<String, String>();

    public URequest(HttpType httpType, UFileRegion region)
    {
        this.httpType = ObjectsUtil.requireNonNull(httpType, "Http type is null");
        this.region = ObjectsUtil.requireNonNull(region, "region is null");
    }

    public UFileRegion getRegion()
    {
        return this.region;
    }

    public HttpType getHttpType()
    {
        return this.httpType;
    }

    public void addParameter(String name, String value)
    {
        ObjectsUtil.requireNonNull(name, "Parameter name is null");
        ObjectsUtil.requireNonNull(value, "Parameter value is null");
        this.parameters.put(name, value);
    }

    public String getParameter(String name)
    {
        ObjectsUtil.requireNonNull(name, "Parameter name is null");
        return this.parameters.get(name);
    }

    public Map<String, String> getParameters()
    {
        return this.parameters;
    }

    public void addHeader(String name, String value)
    {
        ObjectsUtil.requireNonNull(name, "Parameter name is null");
        ObjectsUtil.requireNonNull(value, "Parameter value is null");
        this.headers.put(name, value);
    }

    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    public String getHeader(String name)
    {
        ObjectsUtil.requireNonNull(name, "Parameter name is null");
        return headers.get(name);
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }
}
