package com.xiaolong.ucloud.ufile.utils;

/**
 * Created by lanxiaolong on 2019/8/15.
 */
public class ObjectsUtil {
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }
}
