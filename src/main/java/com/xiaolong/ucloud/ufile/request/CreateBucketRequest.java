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

import com.xiaolong.ucloud.ufile.BucketType;
import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.model.UBucket;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import com.google.gson.JsonObject;

public final class CreateBucketRequest
        extends UBucketRequest
{
    public CreateBucketRequest(UFileRegion region, String bucketName)
    {
        this(region, bucketName, BucketType.PUBLIC);
    }

    public CreateBucketRequest(UFileRegion region, String bucketName, BucketType bucketType)
    {
        super(HttpType.GET, "CreateBucket", region);
        this.addParameter("BucketName", bucketName);
        this.addParameter("Type", bucketType.getValue());
        this.addParameter("Region", region.getValue());
    }

    @Override
    public Object execute(BucketExecutor executor)
            throws UFileServiceException
    {
        ObjectsUtil.requireNonNull(executor, "Bucket executor is null");
        UResponse response = executor.execute(this);
        JsonObject result = response.getResponse();
        if (!result.has("BucketName")) {
            throw new UFileServiceException(200, "Bucket Name missing.");
        }
        String bucketName = result.get("BucketName").getAsString();

        if (!result.has("BucketId")) {
            throw new UFileServiceException(200, "Bucket Id missing.");
        }
        String bucketId = result.get("BucketId").getAsString();

        return new UBucket(bucketId, bucketName);
    }
}
