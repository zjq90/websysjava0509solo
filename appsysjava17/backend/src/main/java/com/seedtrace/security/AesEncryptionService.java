package com.seedtrace.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256 加密服务类
 * 
 * <p>该类提供AES-256位加密和解密功能，用于保护系统中的敏感信息：</p>
 * <ul>
 *   <li>客户手机号</li>
 *   <li>客户身份证号</li>
 *   <li>其他敏感数据</li>
 * </ul>
 * 
 * <p>加密特点：
 * <ul>
 *   <li>算法：AES/CBC/PKCS5Padding</li>
 *   <li>密钥长度：256位</li>
 *   <li>初始化向量(IV)：随机生成，与密文一起存储</li>
 *   <li>编码：Base64</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Component
public class AesEncryptionService {

    /** 加密算法 */
    private static final String ALGORITHM = "AES";
    /** 加密模式和填充 */
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /** 加密密钥（从配置文件读取） */
    @Value("${app.encryption.key}")
    private String encryptionKey;

    /** 初始化向量密钥 */
    @Value("${app.encryption.iv}")
    private String ivKey;

    /**
     * 加密字符串
     * 
     * @param plaintext 明文
     * @return 加密后的Base64编码字符串
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }

        try {
            // 生成随机IV
            byte[] iv = generateIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 生成密钥
            SecretKeySpec secretKey = generateSecretKey(encryptionKey);

            // 初始化加密器
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            // 执行加密
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // 将IV和密文合并，然后Base64编码
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密字符串
     * 
     * @param ciphertext 加密后的Base64编码字符串
     * @return 明文
     */
    public String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }

        try {
            // Base64解码
            byte[] combined = Base64.getDecoder().decode(ciphertext);

            // 提取IV和密文
            byte[] iv = new byte[16];
            byte[] encryptedBytes = new byte[combined.length - 16];
            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = generateSecretKey(encryptionKey);

            // 初始化解密器
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            // 执行解密
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 生成AES-256密钥
     * 使用SHA-256对密钥进行哈希处理，确保256位
     * 
     * @param key 原始密钥字符串
     * @return SecretKeySpec对象
     */
    private SecretKeySpec generateSecretKey(String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * 生成随机初始化向量(IV)
     * 16字节（128位）
     * 
     * @return IV字节数组
     */
    private byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }
}
