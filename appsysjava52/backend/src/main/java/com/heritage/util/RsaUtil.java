package com.heritage.util;

import com.heritage.config.RsaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * RSA加密工具类
 * 提供加密、解密、签名验证功能
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Component
public class RsaUtil {

    @Autowired
    private RsaConfig rsaConfig;

    private static final String RSA_ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 245;
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * 使用公钥加密
     * @param data 待加密数据
     * @return Base64编码的加密结果
     */
    public String encrypt(String data) throws Exception {
        return encrypt(data, rsaConfig.getPublicKey());
    }

    /**
     * 使用指定公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return Base64编码的加密结果
     */
    public String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        byte[] dataBytes = data.getBytes("UTF-8");
        int inputLen = dataBytes.length;
        StringBuilder encryptedData = new StringBuilder();
        
        for (int i = 0; i < inputLen; i += MAX_ENCRYPT_BLOCK) {
            int end = Math.min(inputLen, i + MAX_ENCRYPT_BLOCK);
            byte[] encrypted = cipher.doFinal(dataBytes, i, end - i);
            encryptedData.append(Base64.getEncoder().encodeToString(encrypted));
            if (end < inputLen) {
                encryptedData.append(",");
            }
        }
        
        return encryptedData.toString();
    }

    /**
     * 使用私钥解密
     * @param encryptedData Base64编码的加密数据
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData) throws Exception {
        return decrypt(encryptedData, rsaConfig.getPrivateKey());
    }

    /**
     * 使用指定私钥解密
     * @param encryptedData Base64编码的加密数据
     * @param privateKey 私钥
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        String[] blocks = encryptedData.split(",");
        StringBuilder decryptedData = new StringBuilder();
        
        for (String block : blocks) {
            byte[] encryptedBytes = Base64.getDecoder().decode(block);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            decryptedData.append(new String(decrypted, "UTF-8"));
        }
        
        return decryptedData.toString();
    }

    /**
     * 验证数据是否已加密（简单检测）
     * @param data 待检测数据
     * @return true表示可能已加密
     */
    public boolean isEncrypted(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        try {
            Base64.getDecoder().decode(data.split(",")[0]);
            return data.length() > 100;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 敏感字段解密
     * 用于自动解密请求中的敏感字段
     * @param encryptedValue 加密值
     * @return 解密后的值，如果解密失败返回原值
     */
    public String decryptSensitiveField(String encryptedValue) {
        if (encryptedValue == null || !isEncrypted(encryptedValue)) {
            return encryptedValue;
        }
        try {
            return decrypt(encryptedValue);
        } catch (Exception e) {
            return encryptedValue;
        }
    }

    /**
     * 获取公钥（供前端使用）
     * @return Base64编码的公钥
     */
    public String getPublicKey() {
        return rsaConfig.getPublicKeyBase64();
    }
}
