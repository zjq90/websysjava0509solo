package com.secondhand.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密工具类
 * 提供RSA加密、解密、签名、验签功能
 *
 * @author secondhand
 * @version 1.0.0
 */
@Component
public class RSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    @Value("${rsa.public-key}")
    private Resource publicKeyResource;

    @Value("${rsa.private-key}")
    private Resource privateKeyResource;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 读取InputStream所有字节（Java 8兼容）
     */
    private byte[] readAllBytes(InputStream is) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        is.close();
        return buffer.toByteArray();
    }

    /**
     * 初始化密钥
     */
    private void initKeys() throws Exception {
        if (publicKey == null) {
            byte[] publicKeyBytes = readAllBytes(publicKeyResource.getInputStream());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBytes));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            publicKey = keyFactory.generatePublic(keySpec);
        }
        if (privateKey == null) {
            byte[] privateKeyBytes = readAllBytes(privateKeyResource.getInputStream());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBytes));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            privateKey = keyFactory.generatePrivate(keySpec);
        }
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @return 加密后的数据（Base64编码）
     */
    public String encrypt(String data) throws Exception {
        initKeys();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 加密数据（Base64编码）
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData) throws Exception {
        initKeys();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(encryptedData);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 生成RSA密钥对
     *
     * @return 密钥对
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(1024, new SecureRandom());
        return keyPairGen.generateKeyPair();
    }

    /**
     * 获取Base64编码的公钥
     */
    public static String getPublicKeyBase64(KeyPair keyPair) {
        return Base64.encodeBase64String(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取Base64编码的私钥
     */
    public static String getPrivateKeyBase64(KeyPair keyPair) {
        return Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
    }

}
