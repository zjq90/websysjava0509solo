package com.appsys.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES加密工具类
 * 用于加密存储敏感信息，如客户联系方式、财务数据等
 * 使用128位AES以兼容所有Java版本
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Component
public class AESUtil {

    @Value("${aes.secret}")
    private String aesSecret;

    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_MODE = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 16; // 128位 = 16字节（兼容所有Java版本）

    /**
     * 生成AES密钥
     * 
     * @return 密钥字符串
     */
    public String generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128, new SecureRandom()); // 使用128位
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    /**
     * 使用SHA-256哈希派生128位密钥
     * 确保密钥在所有Java版本上都能正常工作
     * 
     * @return SecretKey对象
     */
    private SecretKey getSecretKey() {
        try {
            // 使用SHA-256对密钥进行哈希，然后取前16字节作为AES-128密钥
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(aesSecret.getBytes(StandardCharsets.UTF_8));
            // 取前16字节作为128位密钥
            byte[] key = Arrays.copyOf(keyBytes, KEY_SIZE);
            return new SecretKeySpec(key, ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("获取AES密钥失败", e);
        }
    }

    /**
     * 加密
     * 
     * @param content 待加密内容
     * @return 加密后的Base64字符串
     */
    public String encrypt(String content) {
        if (content == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 解密
     * 
     * @param encryptedContent 加密后的Base64字符串
     * @return 解密后的原始内容
     */
    public String decrypt(String encryptedContent) {
        if (encryptedContent == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] encrypted = Base64.getDecoder().decode(encryptedContent);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 加密手机号（隐藏中间4位显示）
     * 
     * @param phone 原始手机号
     * @return 加密后的数据
     */
    public String encryptPhone(String phone) {
        return encrypt(phone);
    }

    /**
     * 解密手机号并脱敏显示
     * 
     * @param encryptedPhone 加密后的手机号
     * @param mask 是否脱敏显示
     * @return 手机号（脱敏或完整）
     */
    public String decryptPhone(String encryptedPhone, boolean mask) {
        String phone = decrypt(encryptedPhone);
        if (phone != null && mask && phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }
}
