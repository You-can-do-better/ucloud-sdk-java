
package com.xiaolong.ucloud.ufile.body;

public class FinishMultiBody
{
    private String bucket;
    private String key;
    private String fileSize;

    public FinishMultiBody()
    {
    }

    public String getBucket()
    {
        return bucket;
    }

    public void setBucket(String bucket)
    {
        bucket = bucket;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        key = key;
    }

    public String getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(String fileSize)
    {
        fileSize = fileSize;
    }
}
