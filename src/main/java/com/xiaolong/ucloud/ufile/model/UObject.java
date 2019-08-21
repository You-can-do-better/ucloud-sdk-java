
package com.xiaolong.ucloud.ufile.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * UFile Object
 */
public class UObject
{
    /** The name of the bucket in which this object is contained */
    private String bucketName;

    /** The key under which this object is stored */
    private String key;

    private Long objectSize;

    private transient UObjectInputStream objectContent;

    private Long objectRangeOffset;

    private int objectRangeLength;

    private UObjectMetadata objectMetadata;

    private String hash;

    public UObject() {}

    public String getBucketName()
    {
        return this.bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getObjectKey()
    {
        return key;
    }

    public void setObjectKey(String key)
    {
        this.key = key;
    }

    public UObjectInputStream getObjectContent()
    {
        return objectContent;
    }

    public void setObjectContent(UObjectInputStream content)
    {
        this.objectContent = content;
    }

    public UObjectMetadata getObjectMetadata()
    {
        return objectMetadata;
    }

    public void setObjectMetadata(UObjectMetadata metadata)
    {
        this.objectMetadata = metadata;
    }

    public void close() throws IOException
    {
        InputStream is = getObjectContent();
        if (is != null) {
            is.close();
        }
    }
}
