package com.heritage.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密工具类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Component
public class RSAUtil {

    @Value("${rsa.private-key:}")
    private String privateKeyStr;

    @Value("${rsa.public-key:}")
    private String publicKeyStr;

    private RSA rsa;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            if (privateKeyStr.isEmpty() || publicKeyStr.isEmpty()) {
                generateKeyPair();
            } else {
                privateKey = loadPrivateKey(privateKeyStr);
                publicKey = loadPublicKey(publicKeyStr);
                rsa = new RSA(privateKey, publicKey);
            }
        } catch (Exception e) {
            generateKeyPair();
        }
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            rsa = new RSA(privateKey, publicKey);
        } catch (Exception e) {
            throw new RuntimeException("RSA密钥对生成失败", e);
        }
    }

    private PrivateKey loadPrivateKey(String keyStr) throws Exception {
        keyStr = keyStr.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String keyStr) throws Exception {
        keyStr = keyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public String encrypt(String content) {
        byte[] encryptBytes = rsa.encrypt(content.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encryptBytes);
    }

    public String decrypt(String encryptStr) {
        byte[] decryptBytes = rsa.decrypt(Base64.getDecoder().decode(encryptStr), KeyType.PrivateKey);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    public String sign(String content) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new RuntimeException("RSA签名失败", e);
        }
    }

    public boolean verify(String content, String signStr) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = Base64.getDecoder().decode(signStr);
            return signature.verify(signBytes);
        } catch (Exception e) {
            return false;
        }
    }
}
