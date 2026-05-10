package com.seedinventory.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES-256加密工具类
 * 用于加密存储敏感信息，如客户联系方式、财务数据等
 * 使用AES/CBC/PKCS5Padding模式
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
public class AES256Util {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    
    /**
     * 加密敏感数据
     * 
     * @param plainText 原始明文
     * @param secretKey 密钥（必须32字节）
     * @param initVector 初始化向量（必须16字节）
     * @return 加密后的Base64编码字符串
     * @throws Exception 加密异常
     */
    public static String encrypt(String plainText, String secretKey, String initVector) throws Exception {
        if (plainText == null || plainText.isEmpty()) {
            return null;
        }
        
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    /**
     * 解密敏感数据
     * 
     * @param encryptedText 加密后的Base64编码字符串
     * @param secretKey 密钥（必须32字节）
     * @param initVector 初始化向量（必须16字节）
     * @return 解密后的明文字符串
     * @throws Exception 解密异常
     */
    public static String decrypt(String encryptedText, String secretKey, String initVector) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return null;
        }
        
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
