
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonObject;


public final class DeleteBucketRequest
        extends UBucketRequest
{
    public DeleteBucketRequest(UFileRegion region, String bucketName)
    {
        super(HttpType.DELETE, "DeleteBucket", region);
        this.addParameter("BucketName", bucketName);
    }

    @Override
    public Object execute(BucketExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Bucket executor is null");
        UResponse response = executor.execute(this);
        JsonObject json = response.getResponse();
        if (!json.has("BucketId")) {
            throw new UFileServiceException(200, "Bucket Id missing.");
        }
        return json.get("BucketId").getAsString();
    }
}
