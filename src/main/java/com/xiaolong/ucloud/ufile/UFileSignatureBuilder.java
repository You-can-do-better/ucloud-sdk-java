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
package com.xiaolong.ucloud.ufile;

import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.ufile.request.UObjectRequest;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * UFile API操作有两种签名
 * 1. Bucket操作走的是UCloud统一的API， 使用UCloud API签名算法
 * 2. Object操作走的是UFile产品的API， 使用UFile API签名个算法
 * 该类是对Object操作的API进行签名
 */
public class UFileSignatureBuilder
{
    private static final String CANONICAL_PREFIX = "X-UCloud";

    private UFileSignatureBuilder()
    {
    }

    public static String spliceCanonicalHeaders(UObjectRequest request)
    {
        Map<String, String> headers = request.getHeaders();
        Map<String, String> sortedMap = new TreeMap<String, String>();

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey().startsWith(CANONICAL_PREFIX)) {
                    sortedMap.put(entry.getKey().toLowerCase(), entry.getValue());
                }
            }
            String result = "";
            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
                result += entry.getKey() + ":" + entry.getValue() + "\n";
            }
            return result;
        }
        else {
            return "";
        }
    }

    public static String getSignature(UObjectRequest request, String objectKey, UCloudCredentials credentials)
    {
        ObjectsUtil.requireNonNull(request, "Object request is null");
        ObjectsUtil.requireNonNull(objectKey, "Object key is null");
        ObjectsUtil.requireNonNull(credentials, "UCloud credentials is null");

        String contentMD5 = request.getContentMD5();
        String contentType = request.getContentType();
        String date = request.getDate();
        String canonicalizedUcloudHeaders = spliceCanonicalHeaders(request);
        String canonicalizedResource = "/" + request.getBucketName() + "/" + objectKey;
        String stringToSign = request.getHttpType() + "\n"
                + contentMD5 + "\n"
                + contentType + "\n"
                + date + "\n"
                + canonicalizedUcloudHeaders
                + canonicalizedResource;
        String signature = new HmacSHA1().sign(credentials.getPrivateKey(), stringToSign);
        return "UCloud" + " " + credentials.getPublicKey() + ":" + signature;
    }
}
