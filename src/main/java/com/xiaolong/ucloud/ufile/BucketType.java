
package com.xiaolong.ucloud.ufile;

public enum BucketType
{
    PUBLIC("public"),
    PIRVATE("private");

    String value;

    BucketType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public static BucketType getEnum(String value)
    {
        for (BucketType v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
