package com.xiaolong.ucloud.ufile.request;


import com.xiaolong.ucloud.ufile.UFileRegion;
import com.xiaolong.ucloud.ufile.exception.UFileServiceException;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

public abstract class UBucketRequest
        extends URequest
{
    public UBucketRequest(HttpType httpType, String actionName, UFileRegion region)
    {
        super(httpType, region);

        ObjectsUtil.requireNonNull(actionName, "action name is null.");
        this.addParameter("Action", actionName);
    }

    public String getActionName()
    {
        return getParameter("Action");
    }

    public abstract Object execute(BucketExecutor executor)
            throws UFileServiceException;
}
