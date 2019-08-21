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
package com.xiaolong.ucloud.ufile.exception;

/**
 * UFile Server 异常
 */
public class UFileServiceException
        extends RuntimeException
{
    private static final long serialVersionUID = -1760901983040241570L;

    private final String requestId;

    private final Long returnCode;

    private final String errorMessage;

    private final int httpStatusCode;

    public UFileServiceException(int httpStatusCode)
    {
        super((String) null);
        this.httpStatusCode = httpStatusCode;
        this.returnCode = 0L;
        this.requestId = "0";
        this.errorMessage = "";
    }

    public UFileServiceException(int httpStatusCode, Long returnCode)
    {
        super((String) null);
        this.returnCode = returnCode;
        this.requestId = "0";
        this.errorMessage = "";
        this.httpStatusCode = httpStatusCode;
    }

    public UFileServiceException(int httpStatusCode, String errorMessage)
    {
        super((String) null);
        requestId = "0";
        this.returnCode = Long.valueOf(-1);
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public UFileServiceException(int httpStatusCode, String requestId, String errorMessage)
    {
        super((String) null);
        this.returnCode = Long.valueOf(-1);
        this.requestId = requestId;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public UFileServiceException(int httpStatusCode, Long returnCode, String errorMessage)
    {
        super((String) null);
        this.requestId = "0";
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public UFileServiceException(int httpStatusCode, String requestId, String errorMessage, Exception cause)
    {
        super(null, cause);
        this.requestId = requestId;
        this.returnCode = Long.valueOf(-1);
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode()
    {
        return httpStatusCode;
    }

    public Long getReturnCode()
    {
        return this.returnCode;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    @Override
    public String getMessage()
    {
        return getErrorMessage()
                + " (Service: UFile"
                + "; RequestId: " + getRequestId()
                + "; Message: " + getErrorMessage() + ")";
    }
}
