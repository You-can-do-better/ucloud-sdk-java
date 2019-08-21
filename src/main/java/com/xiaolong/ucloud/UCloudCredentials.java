package com.xiaolong.ucloud;


import com.xiaolong.ucloud.ufile.utils.ObjectsUtil;

/**
 * UCloud User Key
 * 支持从配置文件中加载
 *
 */
public class UCloudCredentials
{
    private final String publicKey;
    private final String privateKey;

    public UCloudCredentials(String publicKey, String privateKey)
    {
        ObjectsUtil.requireNonNull(publicKey, "publicKey is null.");
        ObjectsUtil.requireNonNull(privateKey, "privateKey is null.");

        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey()
    {
        return this.publicKey;
    }

    public String getPrivateKey()
    {
        return this.privateKey;
    }
}
