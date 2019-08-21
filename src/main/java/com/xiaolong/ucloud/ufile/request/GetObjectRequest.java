
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileHeaders;
import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.UObject;
import com.xiaolong.ucloud.ufile.model.UObjectInputStream;
import com.xiaolong.ucloud.ufile.model.UObjectMetadata;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import org.apache.http.Header;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 *
 * Request Parameters
 *      None
 *
 * Request Headers
 *   Authorization : 下载请求的授权签名
 *   Range : bytes=<byte_range>   分片下载的文件范围
 *   If-Modified-Since : <timestamp>   只返回从某时修改过的文件， 否则返回304
 *
 * Response Headers
 *   Content-Type:  文件类型
 *   Content-Length: 文件长度
 *   Content-Range:  文件的范围
 *   ETag:  哈希值
 *   X-SessionId:  会话Id
 *
 * Response Elements
 *   RetCode:  错误代码
 *   ErrMsg:  错误提示
 */

public final class GetObjectRequest
        extends UObjectRequest
{
    private static final String CONTENT_RANGE_REGEX = "^bytes ([0-9]+)-([0-9]+)/[0-9]+";

    public GetObjectRequest(UFileRegion region, String bucketName, String objectKey)
    {
        this(region, bucketName, objectKey, null);
    }

    public GetObjectRequest(UFileRegion region, String bucketName, String objectKey, String objectRange)
    {
        super(HttpType.GET, region, bucketName);

        ObjectsUtil.requireNonNull(objectKey, "Object key is null");
        this.setObjectKey(objectKey);
        if (objectRange != null) {
            this.addHeader("Range", objectRange);
        }
    }

    private String getContentRange(Header[] headers)
    {
        ObjectsUtil.requireNonNull(headers, "headers is null");

        for (Header header : headers) {
            if (header.getName().equals(UFileHeaders.CONTENT_RANGE)) {
                return header.getValue();
            }
        }
        return null;
    }

    @Override
    public UObject execute(ObjectExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "executor is null.");

        UResponse response = executor.execute(this, this.getObjectKey());
        Header[] headers = response.getHeaders();
        InputStream content = response.getContent();

        // 解析Content-Range
        // 格式： bytes 0-31/24135125
        Long rangeOffset = 0L;
        String contentRange = getContentRange(headers);
        if (contentRange != null) {
            Pattern r = Pattern.compile(CONTENT_RANGE_REGEX);
            Matcher matcher = r.matcher(contentRange);
            if (matcher.find()) {
                rangeOffset = Long.valueOf(matcher.group(1));
            }
        }

        UObject object = new UObject();
        object.setBucketName(this.getBucketName());
        object.setObjectKey(this.getObjectKey());
        object.setObjectContent(new UObjectInputStream(content, rangeOffset));
        object.setObjectMetadata(new UObjectMetadata(headers));

        return object;
    }
}
