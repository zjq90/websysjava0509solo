package com.flowerstore.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密工具类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class RSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSAUtil() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            log.info("RSA密钥对生成成功");
        } catch (Exception e) {
            log.error("RSA密钥对生成失败", e);
        }
    }

    /**
     * 获取公钥字符串
     */
    public String getPublicKeyStr() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 公钥加密
     */
    public String encryptByPublicKey(String content) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("RSA加密失败", e);
            return null;
        }
    }

    /**
     * 私钥解密
     */
    public String decryptByPrivateKey(String encryptedContent) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA解密失败", e);
            return null;
        }
    }

    /**
     * 加密敏感信息
     */
    public String encryptSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        return encryptByPublicKey(data);
    }

    /**
     * 解密敏感信息
     */
    public String decryptSensitiveData(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        return decryptByPrivateKey(encryptedData);
    }
}
