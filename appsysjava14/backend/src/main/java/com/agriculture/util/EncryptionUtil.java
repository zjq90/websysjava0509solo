package com.agriculture.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES-256加密工具类
 * 用于敏感信息（如客户联系方式、财务数据）的加密存储
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Component
public class EncryptionUtil {

    @Value("${encryption.aes.key:3ZgQ6mJ2aB8rT9xX}")
    private String secretKey;

    @Value("${encryption.aes.iv:2xS5cV8nM9pQ2bD7}")
    private String initVector;

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";

    /**
     * 加密字符串
     * 
     * @param value 原始字符串
     * @return 加密后的Base64字符串，如果输入为null则返回null
     */
    public String encrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
            return value;
        }
    }

    /**
     * 解密字符串
     * 
     * @param encrypted 加密的Base64字符串
     * @return 解密后的原始字符串，如果输入为null则返回null
     */
    public String decrypt(String encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return encrypted;
        }
    }

    /**
     * 简单的密码哈希（用于用户密码存储）
     * 实际生产环境建议使用BCrypt等更强的哈希算法
     * 
     * @param password 原始密码
     * @return 哈希后的密码
     */
    public String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((password + secretKey).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return password;
        }
    }
}
