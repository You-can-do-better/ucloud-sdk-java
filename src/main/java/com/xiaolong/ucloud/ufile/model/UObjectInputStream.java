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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UObjectInputStream
        extends FilterInputStream
{
    /**
     * 数据流在整个对象中的偏移
     */
    private long rangeOffset;

    public UObjectInputStream(InputStream in, long rangeOffset)
    {
        super(in);
        this.rangeOffset = rangeOffset;
    }

    public long getRangeOffset()
    {
        return this.rangeOffset;
    }

    @Override
    public int read()
            throws IOException
    {
        return in.read();
    }

    @Override
    public int available()
            throws IOException
    {
        int estimate = super.available();
        return estimate == 0 ? 1 : estimate;
    }

    @Override
    public void close() throws IOException
    {
        super.close();
    }
}
