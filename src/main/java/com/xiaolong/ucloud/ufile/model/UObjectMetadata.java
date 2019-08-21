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

import com.xiaolong.ucloud.ufile.UFileHeaders;
import org.apache.http.Header;

import java.util.Date;

public class UObjectMetadata
{
    /**
     * All other (non user custom) headers such as Content-Length, Content-Type,
     * etc.
     */

    private String contentDisposition;

    private String contentEncoding;

    private String contentLanguage;

    private Long contentLength;

    private String contentMD5;

    private String contentType;

    private String eTag;

    private Date lastModified;

    private String contentRange;

    public UObjectMetadata() {}

    public UObjectMetadata(Header[] headers)
    {
        for (Header header : headers) {
            if (header.getName().equals(UFileHeaders.CONTENT_DISPOSITION)) {
                this.contentDisposition = header.getValue();
            }
            else if (header.getName().equals(UFileHeaders.CONTENT_ENCODING)) {
                this.contentEncoding = header.getValue();
            }
            else if (header.getName().equals(UFileHeaders.CONTENT_LANGUAGE)) {
                this.contentLanguage = header.getValue();
            }
            else if (header.getName().equals(UFileHeaders.CONTENT_LENGTH)) {
                this.contentLength = Long.parseLong(header.getValue());
            }
            else if (header.getName().equals(UFileHeaders.CONTENT_MD5)) {
                this.contentMD5 = header.getValue();
            }
            else if (header.getName().equals(UFileHeaders.ETAG)) {
                this.eTag = header.getValue();
            }
//            else if (header.getName().equals(UFileHeaders.LAST_MODIFIED)) {
//                this.lastModified = new Date(Long.parseLong(header.getValue()));
//            }
            else if (header.getName().equals(UFileHeaders.CONTENT_TYPE)) {
                this.contentType = header.getValue();
            }
            else if (header.getName().equals(UFileHeaders.CONTENT_RANGE)) {
                this.contentRange = header.getValue();
            }
            else {
                continue;
            }
        }
    }

    public String getETag()
    {
        return eTag;
    }

    public void setETag(String eTag)
    {
        this.eTag = eTag;
    }

    public String getContentDisposition()
    {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition)
    {
        this.contentDisposition = contentDisposition;
    }

    public String getContentMD5()
    {
        return this.contentMD5;
    }

    public void setContentMD5(String contentMD5)
    {
        this.contentMD5 = contentMD5;
    }

    public String getContentEncoding()
    {
        return this.contentEncoding;
    }

    public void setContentEncoding(String encoding)
    {
        this.contentEncoding = encoding;
    }

    public String getContentLanguage()
    {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage)
    {
        this.contentLanguage = contentLanguage;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public long getContentLength()
    {
        return contentLength;
    }

    public void setContentLength(Long contentLength)
    {
        this.contentLength = contentLength;
    }

    public void setContentLength(String contentLength)
    {
        this.contentLength = Long.parseLong(contentLength, 10);
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
}
