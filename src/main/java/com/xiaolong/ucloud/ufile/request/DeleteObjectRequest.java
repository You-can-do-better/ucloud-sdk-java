
package com.xiaolong.ucloud.ufile.request;


import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

public final class DeleteObjectRequest
        extends UObjectRequest
{
    public DeleteObjectRequest(UFileRegion region, String bucketName, String objectKey)
    {
        super(HttpType.DELETE, region, bucketName);
        this.setObjectKey(objectKey);
    }

    @Override
    public UResponse execute(ObjectExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Object executor is null");
        UResponse response = executor.execute(this, getObjectKey());
        return response;
    }
}
