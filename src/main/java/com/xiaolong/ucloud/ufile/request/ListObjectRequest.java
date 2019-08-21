
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.UObjectListing;
import com.xiaolong.ucloud.ufile.model.UObjectSummary;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Request Parameters
 *   None
 *
 * Request Headers
 *   prefix : 对象prefix
 *   marker : 标志字符串
 *   limit  : 文件列表数目
 *   Authorization: 授权签名
 *
 * Request Elements
 *   None
 *
 * Response Elements
 *   BucketName:
 *   BucketId:
 *   NextMarker:
 *   DataSet:
 *
 */
public final class ListObjectRequest
        extends UObjectRequest
{
    private String prefix;
    private String marker;
    private int limit = 20;

    public ListObjectRequest(UFileRegion region, String bucketName,
                             String prefix)
    {
        this(region, bucketName, prefix, 20);
    }

    public ListObjectRequest(UFileRegion region, String bucketName,
            String prefix, int limit)
    {
        super(HttpType.GET, region, bucketName);
        this.prefix = prefix;
        this.limit = limit;
        this.addParameter("limit", Integer.toString(limit));
    }

    public ListObjectRequest(UFileRegion region, String bucketName,
            String prefix, int limit, String marker)
    {
        super(HttpType.GET, region, bucketName);
        this.prefix = prefix;
        this.addParameter("marker", marker);
        this.addParameter("limit", Integer.toString(limit));
    }

    @Override
    public Object execute(ObjectExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Object executor is null");

        UResponse response = executor.execute(this, "");
        JsonObject json = response.getResponse();

        if (!json.get("BucketName").getAsString().equals(getBucketName())) {
            throw new UFileServiceException(200, "Bucket Name mismatch.");
        }
        UObjectListing objectListing = new UObjectListing();
        objectListing.setBucketName(getBucketName());
        String bucketId = json.get("BucketId").getAsString();
        if (bucketId == null) {
            throw new UFileServiceException(200, "Bucket Id missing.");
        }
        String nextMarker = json.get("NextMarker").getAsString();
        if (nextMarker != null && nextMarker.length() > 0) {
            objectListing.setNextMarker(nextMarker);
        }
        else {
            objectListing.setTruncated(false);
        }

        objectListing.setLimit(limit);
        objectListing.setPrefix(prefix);

        JsonArray array = json.get("DataSet").getAsJsonArray();
        for (JsonElement entry : array) {
            UObjectSummary objectSummary = new UObjectSummary();
            String fileName = entry.getAsJsonObject().get("FileName").getAsString();
            objectSummary.setObjectKey(fileName);
            Long size = entry.getAsJsonObject().get("Size").getAsLong();
            objectSummary.setSize(size);
            String hash = entry.getAsJsonObject().get("Hash").getAsString();
            objectSummary.setHash(hash);
            String mimeType = entry.getAsJsonObject().get("MimeType").getAsString();
            objectSummary.setMimeType(mimeType);
            Long created = entry.getAsJsonObject().get("CreateTime").getAsLong();
            objectSummary.setCreated(new Date(created));
            Long modifyTimestamp = entry.getAsJsonObject().get("ModifyTime").getAsLong();
            objectSummary.setLastModified(new Date(modifyTimestamp));
            objectListing.putObjectSummary(objectSummary);
        }

        return objectListing;
    }
}
