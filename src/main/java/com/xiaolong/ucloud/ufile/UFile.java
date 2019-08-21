package com.xiaolong.ucloud.ufile;


import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.ufile.exception.UFileClientException;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.*;
import com.xiaolong.ucloud.ufile.request.UResponse;

import java.io.File;
import java.io.InputStream;

public interface UFile
{
    /**
     * 获取UFile的身份认证信息
     *
     * @return ucloud credentials
     */
    UCloudCredentials getCredentials();

    /**
     * 获取客户端默认的Region
     *
     * @return ufile region
     */
    UFileRegion getDefaultRegion();

    /**
     * 设置操作的默认Region
     *
     * @param region  区域
     * @return ufile client
     */
    UFile setDefaultRegion(UFileRegion region);

    /**
     * 在默认的Region下创建Public权限的Bucket
     *
     * @param bucketName
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UBucket createBucket(String bucketName)
            throws UFileClientException, UFileServiceException;

    /**
     * 在默认的Region下创建指定权限的UFile Bucket
     *
     * @param bucketName  Bucket名称
     * @param bucketType  Bucket类型
     * @return ufile bucket
     * @throws UFileClientException   SDK客户端异常
     * @throws UFileServiceException  UFile服务异常
     */
    UBucket createBucket(String bucketName, BucketType bucketType)
            throws UFileClientException, UFileServiceException;

    /**
     * 在指定的Region中创建UFile Bucket
     *
     * @param bucketName
     * @param bucketType
     * @param region
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UBucket createBucket(String bucketName, BucketType bucketType, UFileRegion region)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取指定Region下的所有Bucket
     *
     * @return listing of buckets
     * @throws UFileClientException SDK客户端异常
     * @throws UFileServiceException UFile服务异常
     */
    UBucketListing listBuckets()
            throws UFileClientException, UFileServiceException;

    /**
     * 获取Bucket详细信息
     *
     * @param bucketName  Bucket名称
     * @return ufile bucket
     * @throws UFileClientException SDK客户端异常
     * @throws UFileServiceException UFile服务异常
     */
    UBucket getBucket(String bucketName)
            throws UFileClientException, UFileServiceException;

    /**
     * 删除指定的Bucket
     *
     * @param bucketName
     * @return deleted bucket name
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    String deleteBucket(String bucketName)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取UFile对象
     *
     * @param bucketName
     * @param objectKey
     * @return ufile object
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObject getObject(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException;

    /**
     * 从对象的指定位置开始读取对象内容
     *
     * @param bucketName
     * @param objectKey
     * @param offset
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObject getObject(String bucketName, String objectKey, long offset)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取对象指定偏移和长度的内容
     *
     * @param bucketName
     * @param objectKey
     * @param offset
     * @param length
     * @return ufile object
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObject getObject(String bucketName, String objectKey, long offset, int length)
            throws UFileClientException, UFileServiceException;

    /**
     * 下载UFile对象到指定的文件中
     *
     * @param bucketName
     * @param objectKey
     * @param destinationFile
     * @return ufile object meatadata
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObjectMetadata getObject(String bucketName, String objectKey, File destinationFile)
            throws UFileClientException, UFileServiceException;

    /**
     * 将指定的UFile内容以String的方式返回
     *
     * @param bucketName
     * @param objectKey
     * @return object content
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    String getObjectAsString(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取UFile对象的元数据
     *
     * @param bucketName
     * @param objectKey
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UResponse getObjectMetadata(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException;

    /**
     * 上传文件到UFile中
     *
     * @param bucketName
     * @param objectKey
     * @param file
     * @return metadata of ufile object
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UResponse putObject(String bucketName, String objectKey, File file)
            throws UFileClientException, UFileServiceException;

    /**
     * 将文件以指定的Content-Type上传到UFile中
     *
     * @param bucketName
     * @param objectKey
     * @param file
     * @param contentType
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UResponse putObject(String bucketName, String objectKey, File file, String contentType)
            throws UFileClientException, UFileServiceException;

    /**
     * 将数据流上传到UFile中
     *
     * @param bucketName
     * @param objectKey
     * @param input
     * @param contentType
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UResponse putObject(String bucketName, String objectKey, InputStream input, String contentType)
            throws UFileClientException, UFileServiceException;

    /**
     * 检查对象是否存在
     *
     * @param bucketName
     * @param objectKey
     * @return
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    boolean doesObjectExist(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取指定的Bucket下所有的UFile对象列表
     *
     * @param bucketName
     * @param prefix
     * @param limit
     * @return listing of ufile objects
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObjectListing listObjects(String bucketName, String prefix, Integer limit)
            throws UFileClientException, UFileServiceException;

    /**
     * 获取下一页的对象列表
     *
     * @param perviousObjectListing
     * @return listing of ufile objects
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UObjectListing listNextBatchOfObjects(UObjectListing perviousObjectListing)
            throws UFileClientException, UFileServiceException;

    /**
     * 删除UFile对象
     *
     * @param bucketName
     * @param objectKey
     * @return deleted object key
     * @throws UFileClientException
     * @throws UFileServiceException
     */
    UResponse deleteObject(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException;

    /**
     * 关闭UFile客户端
     */
    void shutdown();
}
