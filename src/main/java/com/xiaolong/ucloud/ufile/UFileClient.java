package com.xiaolong.ucloud.ufile;

import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.ufile.exception.UFileClientException;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.*;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.xiaolong.ucloud.ufile.request.*;

import java.io.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


public class UFileClient
        implements UFile
{
    /**
     * 用户的UCloud身份信息
     */
    private final UCloudCredentials credentials;

    /**
     * UFile操作默认的目标Region
     */
    private UFileRegion defaultRegion;

    /**
     * Bucket操作执行器
     */
    private final BucketExecutor bucketExecutor;

    /**
     * Object操作执行器
     */
    private final ObjectExecutor objectExecutor;

    public UFileClient(
            UCloudCredentials credentials,
            BucketExecutor bucketExecutor,
            ObjectExecutor objectExecutor)
    {
        this.credentials = ObjectsUtil.requireNonNull(credentials, "UCloud credentials is null");
        this.bucketExecutor = ObjectsUtil.requireNonNull(bucketExecutor, "Bucket executor is null");
        this.objectExecutor = ObjectsUtil.requireNonNull(objectExecutor, "Object executor is null");
    }

    @Override
    public UFileRegion getDefaultRegion()
    {
        return defaultRegion;
    }

    @Override
    public UFile setDefaultRegion(UFileRegion defaultRegion)
    {
        this.defaultRegion = defaultRegion;
        return this;
    }

    @Override
    public UCloudCredentials getCredentials()
    {
        return this.credentials;
    }

//    private void printResponseHeaders(UFileResponse response)
//    {
//        System.out.println("status line: " + response.getStatusLine());
//        Header[] headers = response.getHeaders();
//        for (int i = 0; i < headers.length; i++) {
//            System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
//        }
//        System.out.println("body length: " + response.getContentLength());
//    }

    @Override
    public UBucket createBucket(String bucketName)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");

        CreateBucketRequest request = new CreateBucketRequest(defaultRegion, bucketName, BucketType.PUBLIC);
        return (UBucket) request.execute(bucketExecutor);
    }

    @Override
    public UBucket createBucket(String bucketName, BucketType bucketType)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(bucketType, "bucketType is null");

        CreateBucketRequest request = new CreateBucketRequest(defaultRegion, bucketName, bucketType);
        return (UBucket) request.execute(bucketExecutor);
    }

    @Override
    public UBucket createBucket(String bucketName, BucketType type, UFileRegion region)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null.");
        ObjectsUtil.requireNonNull(type, "type is null.");
        ObjectsUtil.requireNonNull(region, "region is null.");

        CreateBucketRequest request = new CreateBucketRequest(region, bucketName, type);
        return (UBucket) request.execute(bucketExecutor);
    }

    @Override
    public UBucket getBucket(String bucketName)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");

        GetBucketRequest request = new GetBucketRequest(defaultRegion, bucketName);
        return (UBucket) request.execute(bucketExecutor);
    }

    @Override
    public UBucketListing listBuckets()
            throws UFileClientException, UFileServiceException
    {
        ListBucketRequest request = new ListBucketRequest(defaultRegion);
        return (UBucketListing) request.execute(bucketExecutor);
    }

    @Override
    public String deleteBucket(String bucketName)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");

        DeleteBucketRequest request = new DeleteBucketRequest(defaultRegion, bucketName);
        return (String) request.execute(bucketExecutor);
    }

    @Override
    public UObject getObject(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "key is null");

        GetObjectRequest request = new GetObjectRequest(defaultRegion, bucketName, objectKey);
        return request.execute(objectExecutor);
    }

    @Override
    public UObject getObject(String bucketName, String objectKey, long offset)
            throws UFileClientException, UFileServiceException
    {
        return getObject(bucketName, objectKey, offset, Integer.MAX_VALUE - 1);
    }

    @Override
    public UObject getObject(String bucketName, String objectKey, long offset, int length)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");
        checkArgument(offset >= 0, "offset is negative: %d", offset);
        checkArgument(length > 0, "length must be greater than 0");

        String range = String.format("bytes=%d-%d", offset, offset + length - 1);
        GetObjectRequest request = new GetObjectRequest(defaultRegion, bucketName, objectKey, range);
        return (UObject) request.execute(objectExecutor);
    }

    @Override
    public UObjectMetadata getObject(String bucketName, String objectKey, File destinationFile)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");
        ObjectsUtil.requireNonNull(destinationFile, "Destination file is null");

        UObject object = getObject(bucketName, objectKey);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = object.getObjectContent();
            outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
            int bufSize = 1024 * 8;
            byte[] buffer = new byte[bufSize];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            throw new UFileClientException(e);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (Exception e) {
                throw new UFileClientException(e);
            }
        }

        return object.getObjectMetadata();
    }

    @Override
    public String getObjectAsString(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");

        UObject object = getObject(bucketName, objectKey);
        InputStream content = object.getObjectContent();
        if (content != null) {
            // Error Message
            try {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = content.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return result.toString("UTF-8");
            }
            catch (IOException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public UResponse getObjectMetadata(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");

        GetObjectMetaRequest request = new GetObjectMetaRequest(defaultRegion, bucketName, objectKey);
        return request.execute(objectExecutor);
    }

    @Override
    public UResponse putObject(String bucketName, String objectKey, File file)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");
        ObjectsUtil.requireNonNull(file, "targetFile is null");

        String mimeType = Mimetypes.getInstance().getMimetype(file);
        return putObject(bucketName, objectKey, file, mimeType);
    }

    @Override
    public UResponse putObject(String bucketName, String objectKey, File file, String contentType)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null");

        ObjectsUtil.requireNonNull(file, "targetFile is null");
        checkState(file.isFile(), "Input file must be a file.");
        checkState(file.canRead(), "Input file must can read.");

        ObjectsUtil.requireNonNull(contentType, "contentType is null");
//        checkState(!Mimetypes.getInstance().existMimeType(contentType), "contentType is illegal");

        InputStream objectStream;
        try {
            objectStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            throw new UFileClientException(e);
        }
        return putObject(bucketName, objectKey, objectStream, contentType);
    }

    @Override
    public UResponse putObject(String bucketName, String objectKey, InputStream objectStream, String contentType)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null.");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null.");
        ObjectsUtil.requireNonNull(objectStream, "objectStream is null.");
        ObjectsUtil.requireNonNull(contentType, "contentType is null");
        //checkState(!Mimetypes.getInstance().existMimeType(contentType), "contentType is illegal");

        int length = 0;
        try {
            length = objectStream.available();
        }
        catch (IOException e) {
            throw new UFileClientException(e);
        }

        PutObjectRequest request = new PutObjectRequest(defaultRegion, bucketName, objectKey,
                objectStream, Long.valueOf((long) length), contentType);
        return request.execute(objectExecutor);
    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectKey)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null.");
        ObjectsUtil.requireNonNull(objectKey, "objectKey is null.");

        try {
            UResponse response = getObjectMetadata(bucketName, objectKey);
            if (response.getRetCode() == 200)
                return true;
            else
                return false;
        }
        catch (UFileServiceException e) {
            if (e.getHttpStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public UObjectListing listObjects(String bucketName, String prefix, Integer limit)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");

        ListObjectRequest request = new ListObjectRequest(defaultRegion, bucketName, prefix, limit);
        return (UObjectListing) request.execute(objectExecutor);
    }

    @Override
    public UObjectListing listNextBatchOfObjects(UObjectListing perviousObjectListing)
            throws UFileClientException, UFileServiceException
    {
        if (perviousObjectListing.getNextMarker().equals("")) {
            return null;
        }
        ListObjectRequest request = new ListObjectRequest(defaultRegion,
                perviousObjectListing.getBucketName(),
                perviousObjectListing.getPrefix(),
                perviousObjectListing.getLimit(),
                perviousObjectListing.getNextMarker());
        return (UObjectListing) request.execute(objectExecutor);
    }

    @Override
    public UResponse deleteObject(String bucketName, String key)
            throws UFileClientException, UFileServiceException
    {
        ObjectsUtil.requireNonNull(bucketName, "bucketName is null");
        ObjectsUtil.requireNonNull(key, "objectKey is null");

        DeleteObjectRequest request = new DeleteObjectRequest(defaultRegion, bucketName, key);
        return request.execute(objectExecutor);
    }

    @Override
    public void shutdown()
    {
        if (objectExecutor != null) {
            objectExecutor.close();
        }
        if (bucketExecutor != null) {
            bucketExecutor.close();
        }
    }
}
