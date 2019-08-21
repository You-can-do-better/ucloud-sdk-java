
package com.xiaolong.ucloud.ufile.body;

public class InitMultiBody
{
    private String uploadId;
    private int blkSize;
    private String bucket;
    private String key;

    public InitMultiBody()
    {
    }

    public String getUploadId()
    {
        return uploadId;
    }

    public void setUploadId(String uploadId)
    {
        uploadId = uploadId;
    }

    public int getBlkSize()
    {
        return blkSize;
    }

    public void setBlkSize(int blkSize)
    {
        blkSize = blkSize;
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
}
