package com.xiaolong.ucloud.ufile.model;

/**
 * UFile Bucket
 */
public class UBucket
{
    private String id;

    private String name;

    public UBucket() {}

    public UBucket(String name)
    {
        this.name = name;
    }

    public UBucket(String bucketId, String bucketName)
    {
        this.id = bucketId;
        this.name = bucketName;
    }

    public String getId()
    {
        return this.id;
    }

    public UBucket setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public UBucket setName(String name)
    {
        this.name = name;
        return this;
    }
}
