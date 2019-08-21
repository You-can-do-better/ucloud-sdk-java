
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.UBucket;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class GetBucketRequest
        extends UBucketRequest
{
    public GetBucketRequest(UFileRegion region, String bucketName)
    {
        super(HttpType.GET, "DescribeBucket", region);
        ObjectsUtil.requireNonNull(bucketName, "Bucket name is null");
        this.addParameter("BucketName", bucketName);
    }

    @Override
    public Object execute(BucketExecutor executor) throws UFileServiceException
    {
        UResponse response = executor.execute(this);
        JsonObject json = response.getResponse();
        if (!json.has("DataSet")) {
            throw new UFileServiceException(200, "DataSet missing.");
        }
        JsonArray dataSet = json.getAsJsonArray("DataSet");
        if (dataSet.size() != 1) {
            throw new UFileServiceException(200, "Multiple bucket exists in GetBucketResponse.");
        }
        JsonObject jsonBucket = dataSet.get(0).getAsJsonObject();
        if (!jsonBucket.has("BucketName")) {
            throw new UFileServiceException(200, "Bucket Name missing.");
        }
        String bucketName = jsonBucket.get("BucketName").getAsString();

        if (!jsonBucket.has("BucketId")) {
            throw new UFileServiceException(200, "Bucket Id missing.");
        }
        String bucketId = jsonBucket.get("BucketId").getAsString();

        return new UBucket(bucketId, bucketName);
    }
}
