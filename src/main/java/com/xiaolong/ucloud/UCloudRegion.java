package com.xiaolong.ucloud;

public enum UCloudRegion
{
    CN_BJ1("cn-bj1"), // 北京一
    CN_BJ2("cn-bj2"), // 北京二
    CN_ZJ("cn-zj"),   // 浙江
    CN_SH("cn-sh"),   // 上海一
    CN_SH2("cn-sh2"), // 上海二
    CN_GD("cn-gd"),   // 广州
    HK("hk"),         // 香港
    US_CA("us-ca"),   // 洛杉矶
    US_WS("us-ws"),   // 华盛顿
    GE_FRA("ge-fra"), // 法兰克福
    TH_BKK("th-bkk"),     // 曼谷
    KR_SEOUL("kr-seoul"),    // 首尔
    SG("sg"),     // 新加坡
    TW_TP("tw-tp"),    // 台北
    TW_KH("tw-kh"),       // 高雄
    JPN_TKY("jpn-tky"), // 东京
    RUS_MOSC("rus-mosc"),      // 莫斯科
    UAE_DUBAI("uae-dubai"),     // 迪拜
    IDN_JAKARTA("idn-jakarta");     // 雅加达

    private String value;

    UCloudRegion(String value)
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

    public static UCloudRegion getEnum(String value)
    {
        for (UCloudRegion v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
