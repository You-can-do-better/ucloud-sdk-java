package com.xiaolong.ucloud.ufile.exception;

/**
 * UFile SDK Client 异常
 */
public class UFileClientException
        extends RuntimeException
{
    public UFileClientException(String message, Throwable t)
    {
        super(message, t);
    }

    public UFileClientException(String message)
    {
        super(message);
    }

    public UFileClientException(Throwable t)
    {
        super(t);
    }
}
