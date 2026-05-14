package com.broadband.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密工具类
 * 用于敏感数据加密传输
 * 
 * @author broadband
 * @version 1.0.0
 */
public class RSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private static String publicKey;
    private static String privateKey;

    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKeyObj = keyPair.getPublic();
            PrivateKey privateKeyObj = keyPair.getPrivate();

            publicKey = Base64.getEncoder().encodeToString(publicKeyObj.getEncoded());
            privateKey = Base64.getEncoder().encodeToString(privateKeyObj.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static String encrypt(String content) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String content) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(content));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
