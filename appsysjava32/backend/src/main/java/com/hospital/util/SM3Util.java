package com.hospital.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;

/**
 * SM3哈希算法工具类
 * 国密算法，用于数据完整性校验和密码哈希
 * 
 * @author hospital
 * @version 1.0.0
 */
public class SM3Util {

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private SM3Util() {}

    /**
     * SM3哈希计算
     * 
     * @param data 输入数据
     * @return byte[] 哈希值
     */
    public static byte[] hash(byte[] data) {
        SM3Digest digest = new SM3Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * SM3哈希计算（字符串输入）
     * 
     * @param data 输入字符串
     * @return String 十六进制哈希值
     */
    public static String hash(String data) {
        try {
            byte[] hash = hash(data.getBytes("UTF-8"));
            return Hex.toHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("SM3哈希计算失败", e);
        }
    }

    /**
     * SM3哈希计算（带盐值）
     * 
     * @param data 输入数据
     * @param salt 盐值
     * @return String 十六进制哈希值
     */
    public static String hashWithSalt(String data, String salt) {
        return hash(data + salt);
    }

    /**
     * 验证哈希
     * 
     * @param data 原始数据
     * @param hash 哈希值
     * @return boolean 是否匹配
     */
    public static boolean verify(String data, String hash) {
        return hash(data).equals(hash);
    }

    /**
     * 验证哈希（带盐值）
     * 
     * @param data 原始数据
     * @param salt 盐值
     * @param hash 哈希值
     * @return boolean 是否匹配
     */
    public static boolean verifyWithSalt(String data, String salt, String hash) {
        return hashWithSalt(data, salt).equals(hash);
    }

    /**
     * 生成随机盐值
     * 
     * @return String 盐值
     */
    public static String generateSalt() {
        return Long.toHexString(System.currentTimeMillis()) + 
               Integer.toHexString((int) (Math.random() * 1000000));
    }
}
