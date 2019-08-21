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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UFile URI解析器
 */
public class UFileURI
{
    private static final Pattern ENDPOINT_PATTERN =
            Pattern.compile("^(.+\\.)?(.+)\\.ufileos\\.com");

    private static final Pattern UFILE_URI_PATTERN =
            Pattern.compile("^([a-z0-9-]+)(\\..+)?");

    private final URI uri;

    /**
     * 对象所属的Bucket
     */
    private final String bucket;

    /**
     * 对象的Key
     */
    private final String key;

    /**
     * 对象所在Region
     */
    private final String region;

    public UFileURI(final String uri)
    {
        this(uri, true);
    }

    public UFileURI(final String uri, boolean urlEncode)
    {
        this(URI.create(preprocessUrlStr(uri, urlEncode)), urlEncode);
    }

    public UFileURI(final URI uri)
    {
        this(uri, false);
    }

    public UFileURI(final URI uri, boolean urlEncode)
    {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        this.uri = uri;

        // ufile://*
        if ("ufile".equalsIgnoreCase(this.uri.getScheme())) {
            String authority = uri.getAuthority();
            if (authority == null) {
                throw new IllegalArgumentException("Invalid UFile URI: " + uri);
            }

            Matcher matcher = UFILE_URI_PATTERN.matcher(authority);
            if (!matcher.find()) {
                throw new IllegalArgumentException(
                        "Invalid UFile URI: authority does not appear to be a valid UFile " + uri);
            }

            this.bucket = matcher.group(1);
            if (this.bucket == null) {
                throw new IllegalArgumentException(
                        "Invalid UFile URI: bucket not found " + uri);
            }

            String endpoint = matcher.group(2);
            if (endpoint != null) {
                region = endpoint.substring(1);
            }
            else {
                region = null;
            }

            if (uri.getPath().length() <= 1) {
                // ufile://bucket
                // ufile://bucket/
                this.key = null;
            }
            else {
                // ufile://bucket/key
                this.key = uri.getPath().substring(1);
            }

            return;
        }

        // http://bucket.region/key
        String host = uri.getHost();
        if (host == null) {
            throw new IllegalArgumentException("Invalid UFile URI: no host.");
        }

        Matcher matcher = ENDPOINT_PATTERN.matcher(host);
        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "Invalid UFile URI: hostname does not appear to be a valid UFile "
                            + "endpoint: " + uri);
        }

        String prefix = matcher.group(1);
        if (prefix == null || prefix.isEmpty()) {
            // No bucket name in the authority; parse it from the path.

            // Use the raw path to avoid running afoul of '/'s in the
            // bucket name if we have not performed full URL encoding
            String path = urlEncode ? uri.getPath() : uri.getRawPath();

            if ("/".equals(path)) {
                this.bucket = null;
                this.key = null;
            }
            else {
                int index = path.indexOf('/', 1);
                if (index == -1) {
                    // http://cn-bj.ufileos.com/bucket
                    this.bucket = decode(path.substring(1));
                    this.key = null;
                }
                else if (index == (path.length() - 1)) {
                    // http://cn-bj.ufileos.com/bucket/
                    this.bucket = decode(path.substring(1, index));
                    this.key = null;
                }
                else {
                    // http://cn-bj.ufileos.com/bucket/key
                    this.bucket = decode(path.substring(1, index));
                    this.key = decode(path.substring(index + 1));
                }
            }
        }
        else {
            // <bucket>.<region>.ufileos.com
            // Bucket name was found in the host; path is the key.

            // Remove the trailing '.' from the prefix to get the bucket.
            this.bucket = prefix.substring(0, prefix.length() - 1);

            String path = uri.getPath();
            if (path == null || path.isEmpty() || "/".equals(uri.getPath())) {
                this.key = null;
            }
            else {
                // Remove the leading '/'.
                this.key = uri.getPath().substring(1);
            }
        }

        this.region = matcher.group(2);
    }

    public String getRegion()
    {
        return this.region;
    }

    public String getBucket()
    {
        return this.bucket;
    }

    public String getKey()
    {
        return this.key;
    }

    private static String preprocessUrlStr(final String str, final boolean encode)
    {
        if (encode) {
            try {
                return (URLEncoder.encode(str, "UTF-8")
                        .replace("%3A", ":")
                        .replace("%2F", "/")
                        .replace("+", "%20"));
            }
            catch (UnsupportedEncodingException e) {
                // This should never happen unless there is something
                // fundamentally broken with the running JVM.
                throw new RuntimeException(e);
            }
        }
        return str;
    }

    private static String decode(final String str)
    {
        if (str == null) {
            return null;
        }

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '%') {
                return decode(str, i);
            }
        }

        return str;
    }

    private static String decode(final String str, final int firstPercent)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0, firstPercent));

        appendDecoded(builder, str, firstPercent);

        for (int i = firstPercent + 3; i < str.length(); ++i) {
            if (str.charAt(i) == '%') {
                appendDecoded(builder, str, i);
                i += 2;
            }
            else {
                builder.append(str.charAt(i));
            }
        }

        return builder.toString();
    }

    private static void appendDecoded(final StringBuilder builder,
            final String str,
            final int index)
    {
        if (index > str.length() - 3) {
            throw new IllegalStateException("Invalid percent-encoded string:"
                    + "\"" + str + "\".");
        }

        char first = str.charAt(index + 1);
        char second = str.charAt(index + 2);

        char decoded = (char) ((fromHex(first) << 4) | fromHex(second));
        builder.append(decoded);
    }

    private static int fromHex(final char c)
    {
        if (c < '0') {
            throw new IllegalStateException(
                    "Invalid percent-encoded string: bad character '" + c + "' in "
                            + "escape sequence.");
        }
        if (c <= '9') {
            return (c - '0');
        }

        if (c < 'A') {
            throw new IllegalStateException(
                    "Invalid percent-encoded string: bad character '" + c + "' in "
                            + "escape sequence.");
        }
        if (c <= 'F') {
            return (c - 'A') + 10;
        }

        if (c < 'a') {
            throw new IllegalStateException(
                    "Invalid percent-encoded string: bad character '" + c + "' in "
                            + "escape sequence.");
        }
        if (c <= 'f') {
            return (c - 'a') + 10;
        }

        throw new IllegalStateException(
                "Invalid percent-encoded string: bad character '" + c + "' in "
                        + "escape sequence.");
    }
}
