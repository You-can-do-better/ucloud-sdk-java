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
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.UBucket;
import com.xiaolong.ucloud.ufile.model.UBucketListing;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class ListBucketRequest
        extends UBucketRequest
{
    // 输入参数
    private int offset;
    private int limit = 20;

    public ListBucketRequest(UFileRegion region)
    {
        this(region, 0, 20);
    }

    public ListBucketRequest(UFileRegion region, int offset, int limit)
    {
        super(HttpType.GET, "DescribeBucket", region);
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public Object execute(BucketExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Bucket executor is null.");

        UResponse response = executor.execute(this);
        JsonObject json = response.getResponse();
        if (!json.has("DataSet")) {
            throw new UFileServiceException(200, "DataSet missing.");
        }
        JsonArray dataSet = json.getAsJsonArray("DataSet");
        UBucketListing buckets = new UBucketListing();
        for (int i = 0; i < dataSet.size(); i++) {
            UBucket bucket = new UBucket();
            String bucketId = dataSet.get(i).getAsJsonObject().get("BucketId").getAsString();
            bucket.setId(bucketId);
            String bucketName = dataSet.get(i).getAsJsonObject().get("BucketName").getAsString();
            bucket.setName(bucketName);
            buckets.putBucket(bucket);
        }
        return buckets;
    }
}
