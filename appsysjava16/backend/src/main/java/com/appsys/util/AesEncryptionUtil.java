package com.appsys.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES-256加密工具类
 * 用于加密存储敏感信息，如客户联系方式、财务数据等
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public class AesEncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static SecretKeySpec secretKey;

    /**
     * 初始化密钥
     * @param key 原始密钥字符串
     */
    public static void setKey(String key) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
            keyBytes = Arrays.copyOf(keyBytes, 32);
            secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("密钥初始化失败", e);
        }
    }

    /**
     * 加密字符串
     * @param strToEncrypt 待加密的字符串
     * @param secret 密钥
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密字符串
     * @param strToDecrypt 待解密的Base64编码字符串
     * @param secret 密钥
     * @return 解密后的原始字符串
     */
    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 敏感字段转换器接口
     * 用于JPA实体中加密敏感字段
     */
    public static class Encryptor {
        private static String secretKey = "appsys-256-bit-encryption-key-12345678";

        public static void setSecretKey(String key) {
            secretKey = key;
        }

        public static String encrypt(String data) {
            if (data == null) return null;
            return AesEncryptionUtil.encrypt(data, secretKey);
        }

        public static String decrypt(String data) {
            if (data == null) return null;
            return AesEncryptionUtil.decrypt(data, secretKey);
        }
    }
}
