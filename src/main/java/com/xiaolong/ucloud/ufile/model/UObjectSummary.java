/*
 * Copyright © 2018 UCloud (上海优刻得信息科技有限公司)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaolong.ucloud.ufile.model;

import java.io.Serializable;
import java.util.Date;

public class UObjectSummary
        implements Serializable
{
    /**
     * The name of the bucket in which this object is stored
     */
    protected String bucketName;

    /**
     * The key under which this object is stored
     */
    protected String objectKey;

    /**
     * Hex encoded MD5 hash of this object's contents, as computed by UCloud UFile
     */
    protected String hash;

    /**
     * The size of this object, in bytes
     */
    protected long size;

    /**
     * The date, according to UCloud UFile, when this object was created
     */
    protected Date created;

    /**
     * The date, according to UCloud UFile, when this object was last modified
     */
    protected Date lastModified;

    /**
     * The MIME type of this object's content type
     */
    protected String mimeType;

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
        return this.objectKey;
    }

    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }

    public String getHash()
    {
        return this.hash;
    }

    public void setHash(String eTag)
    {
        this.hash = eTag;
    }

    public long getSize()
    {
        return this.size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public Date getLastModified()
    {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public Date getCreated()
    {
        return this.created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public String getMimeType()
    {
        return this.mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    @Override
    public String toString()
    {
        return "S3ObjectSummary{" +
                "bucketName='" + bucketName + '\'' +
                ", key='" + objectKey + '\'' +
                ", hash='" + hash + '\'' +
                ", size=" + size +
                ", lastModified=" + lastModified +
                '}';
    }
}
