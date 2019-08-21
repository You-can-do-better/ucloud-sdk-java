
package com.xiaolong.ucloud.ufile.request;

import com.xiaolong.ucloud.UCloudCredentials;
import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AbstractExcector
{
    private final UCloudCredentials credentials;

    protected final CloseableHttpClient httpClient;

    public AbstractExcector(UCloudCredentials credentials)
    {
        this.credentials = ObjectsUtil.requireNonNull(credentials, "UCloud credentials is null");
        this.httpClient = new DefaultHttpClient();
    }

    public UCloudCredentials getCredentials()
    {
        return this.credentials;
    }

    protected String getContentAsString(InputStream content)
    {
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

    public void close()
    {
        httpClient.getConnectionManager().shutdown();
    }
}
