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

import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Mimetypes
{
    /**
     * The default XML mimetype: application/xml
     */
    public static final String MIMETYPE_XML = "application/xml";

    /**
     * The default HTML mimetype: text/html
     */
    public static final String MIMETYPE_HTML = "text/html";

    /**
     * The default binary mimetype: application/octet-stream
     */
    public static final String MIMETYPE_OCTET_STREAM = "application/octet-stream";

    /**
     * The default gzip mimetype: application/x-gzip
     */
    public static final String MIMETYPE_GZIP = "application/x-gzip";

    private static Mimetypes mimetypes = null;

    /**
     * Map that stores file extensions as keys, and the corresponding mimetype as values.
     */
    private HashMap<String, String> extensionToMimetypeMap = new HashMap<String, String>();

    /**
     * Mime type set without file extensions
     */
    private Set<String> mimeTypeSet = new TreeSet<String>();

    private Mimetypes() {}

    /**
     * 获取MimeType解析器
     * 该解析器全局单实例对象
     *
     * @return
     * @throws IOException
     */
    public synchronized static Mimetypes getInstance()
    {
        if (mimetypes != null) { return mimetypes; }

        mimetypes = new Mimetypes();
        InputStream is = mimetypes.getClass().getResourceAsStream("/mime.types");
        ObjectsUtil.requireNonNull(is, "mime.types input stream is null");
        try {
            mimetypes.load(is);
        } catch (IOException e) {
            throw new RuntimeException("load mime.types error.", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("close mime.types error.", e);
            }
        }
        return mimetypes;
    }

    /**
     * 加载mimetype字典数据
     *
     * @param is
     * @throws IOException
     */
    public void load(InputStream is)
            throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
        String line = null;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("#") || line.length() == 0) {
                // Ignore comments and empty lines.
            }
            else {
                StringTokenizer st = new StringTokenizer(line, " \t");
                if (st.countTokens() > 1) {
                    String mimetype = st.nextToken();
                    while (st.hasMoreTokens()) {
                        String extension = st.nextToken();
                        extensionToMimetypeMap.put(extension.toLowerCase(), mimetype);
                    }
                    mimeTypeSet.add(mimetype);
                } else if (st.countTokens() == 1) {
                    String mimetype = st.nextToken();
                    mimeTypeSet.add(mimetype);
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 根据文件名获取内容的MimeType
     *
     * @param fileName
     * @return
     */
    public String getMimetype(String fileName)
    {
        int lastPeriodIndex = fileName.lastIndexOf(".");
        if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < fileName.length()) {
            String ext = fileName.substring(lastPeriodIndex + 1).toLowerCase();
            if (extensionToMimetypeMap.keySet().contains(ext)) {
                String mimetype = (String) extensionToMimetypeMap.get(ext);
                return mimetype;
            }
        }
        return MIMETYPE_OCTET_STREAM;
    }

    /**
     * 根据文件对象获取MimeType
     *
     * @param file
     * @return
     */
    public String getMimetype(File file)
    {
        return getMimetype(file.getName());
    }

    /**
     * 检查Mimetype是否存在
     *
     * @param mimeType
     * @return
     */
    public boolean existMimeType(String mimeType)
    {
        return mimeTypeSet.contains(mimeType);
    }
}
