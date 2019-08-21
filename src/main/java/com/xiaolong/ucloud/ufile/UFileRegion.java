
package com.xiaolong.ucloud.ufile;

public enum UFileRegion
{
    CN_BJ("cn-bj"),
    HK("hk"),
    CN_GD("cn-gd"),
    CN_SH2("cn-sh2"),
    US_CA("us-ca");

    private String value;

    UFileRegion(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public static UFileRegion getEnum(String value)
    {
        for (UFileRegion v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
