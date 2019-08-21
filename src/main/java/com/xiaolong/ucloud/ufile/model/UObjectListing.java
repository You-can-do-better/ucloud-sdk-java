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
package com.xiaolong.ucloud.ufile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UObjectListing
        implements Serializable
{
    /**
     * A list of the common prefixes included in this object listing - common
     * prefixes will only be populated for requests that specified a delimiter
     */
    private List<String> commonPrefixes = new ArrayList<String>();

    private List<UObjectSummary> objectSummaries = new ArrayList<UObjectSummary>();

    private String prefix;

    private String bucketName;

    /**
     * The marker to use in order to request the next page of results - only
     * populated if the isTruncated member indicates that this object listing is
     * truncated
     */
    private String nextMarker = "";

    /**
     * Indicates if this is a complete listing, or if the caller needs to make
     * additional requests to  UFile to see the full object listing for an UFile
     * bucket
     */
    private boolean isTruncated = true;

    private int limit;

    public List<UObjectSummary> getObjectSummaries()
    {
        return objectSummaries;
    }

    public void putObjectSummary(UObjectSummary objectSummary)
    {
        objectSummaries.add(objectSummary);
    }

    public String getNextMarker()
    {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker)
    {
        this.nextMarker = nextMarker;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public boolean isTruncated()
    {
        return isTruncated;
    }

    public void setTruncated(boolean isTruncated)
    {
        this.isTruncated = isTruncated;
    }

    public List<String> getCommonPrefixes()
    {
        return commonPrefixes;
    }

    public int getLimit()
    {
        return this.limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }
}
