package com.xiaolong.ucloud.ufile.model;

import java.util.ArrayList;
import java.util.List;

public class UBucketListing
{
    private List<UBucket> objectSummaries = new ArrayList<UBucket>();

    public List<UBucket> getBuckets()
    {
        return this.objectSummaries;
    }

    public void putBucket(UBucket bucket)
    {
        this.objectSummaries.add(bucket);
    }

    public int getSize()
    {
        return this.objectSummaries.size();
    }
}
