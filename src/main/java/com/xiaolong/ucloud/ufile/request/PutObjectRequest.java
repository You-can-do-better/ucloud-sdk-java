
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

import java.io.InputStream;

/*
 * Request Parameters
 *   Authorization: 授权签名
 *   Content-Length: 文件长度
 *   Content-Type:   文件类型
 *   Content-MD5:    文件Hash
 *
 * Request Headers
 *   None
 *
 * Request Elements
 *   RetCode：  错误代码
 *   ErrMsg:    错误信息
 *
 * Request Headers
 *   Content-Type:
 *   Content-Length:
 *   ETag:   文件哈希值
 *   X-SessionId:   会话ID
 */
public final class PutObjectRequest
        extends UObjectRequest
{
    public PutObjectRequest(UFileRegion region, String bucketName,
                            String objectKey, InputStream objectStream, Long objectLength)
    {
        super(HttpType.PUT, region, bucketName);
        this.setObjectKey(objectKey);
        this.setObjectStream(objectStream);
        this.setObjectStreamLength(objectLength);
    }

    public PutObjectRequest(UFileRegion region, String bucketName,
            String objectKey, InputStream objectStream, Long objectLength, String objectType)
    {
        super(HttpType.PUT, region, bucketName);
        this.setObjectKey(objectKey);
        this.addHeader("Content-Type", objectType);
        this.setObjectStream(objectStream);
        this.setObjectStreamLength(objectLength);
    }

    public PutObjectRequest(UFileRegion region, String bucketName,
            String objectKey, InputStream objectStream, Long objectLength, String objectType, String objectMd5)
    {
        super(HttpType.PUT, region, bucketName);
        this.setObjectKey(objectKey);
        this.addHeader("Content-Type", objectType);
        this.addHeader("Content-MD5", objectMd5);
        this.setObjectStream(objectStream);
        this.setObjectStreamLength(objectLength);
    }

    @Override
    public UResponse execute(ObjectExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Object executor is null");
        UResponse response = executor.execute(this, getObjectKey());
        /*Header[] headers = response.getHeaders();
        return new UObjectMetadata(headers);*/
        return response;
    }
}
