package com.hospital.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

/**
 * SM4对称加密算法工具类
 * 国密算法，用于敏感数据的存储加密
 * 
 * @author hospital
 * @version 1.0.0
 */
public class SM4Util {

    private static final String PROVIDER = "BC";
    private static final String ALGORITHM_NAME = "SM4";
    private static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS7Padding";
    private static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
    private static final int KEY_SIZE = 128;

    private static final String DEFAULT_KEY = "HospitalAppSM4Key123456";
    private static final String DEFAULT_IV = "HospitalAppSM4IV01";

    static {
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private SM4Util() {}

    /**
     * 生成SM4密钥
     * 
     * @return String Base64编码的密钥
     */
    public static String generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, PROVIDER);
        kg.init(KEY_SIZE);
        SecretKey secretKey = kg.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * SM4-CBC模式加密
     * 
     * @param data 明文数据
     * @param keyBase64 Base64编码的密钥
     * @param ivBase64 Base64编码的IV向量
     * @return String Base64编码的密文
     */
    public static String encryptCBC(String data, String keyBase64, String ivBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        byte[] ivBytes = Base64.getDecoder().decode(ivBase64);
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * SM4-CBC模式解密
     * 
     * @param encryptedData Base64编码的密文
     * @param keyBase64 Base64编码的密钥
     * @param ivBase64 Base64编码的IV向量
     * @return String 明文数据
     */
    public static String decryptCBC(String encryptedData, String keyBase64, String ivBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        byte[] ivBytes = Base64.getDecoder().decode(ivBase64);
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted, "UTF-8");
    }

    /**
     * SM4-ECB模式加密
     * 
     * @param data 明文数据
     * @param keyBase64 Base64编码的密钥
     * @return String Base64编码的密文
     */
    public static String encryptECB(String data, String keyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM_NAME);

        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * SM4-ECB模式解密
     * 
     * @param encryptedData Base64编码的密文
     * @param keyBase64 Base64编码的密钥
     * @return String 明文数据
     */
    public static String decryptECB(String encryptedData, String keyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM_NAME);

        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted, "UTF-8");
    }

    /**
     * 使用默认密钥加密（简化使用，生产环境应使用安全的密钥管理）
     * 
     * @param data 明文数据
     * @return String Base64编码的密文
     */
    public static String encrypt(String data) {
        try {
            String keyBase64 = Base64.getEncoder().encodeToString(DEFAULT_KEY.getBytes("UTF-8"));
            String ivBase64 = Base64.getEncoder().encodeToString(DEFAULT_IV.getBytes("UTF-8"));
            return encryptCBC(data, keyBase64, ivBase64);
        } catch (Exception e) {
            throw new RuntimeException("SM4加密失败", e);
        }
    }

    /**
     * 使用默认密钥解密
     * 
     * @param encryptedData Base64编码的密文
     * @return String 明文数据
     */
    public static String decrypt(String encryptedData) {
        try {
            String keyBase64 = Base64.getEncoder().encodeToString(DEFAULT_KEY.getBytes("UTF-8"));
            String ivBase64 = Base64.getEncoder().encodeToString(DEFAULT_IV.getBytes("UTF-8"));
            return decryptCBC(encryptedData, keyBase64, ivBase64);
        } catch (Exception e) {
            throw new RuntimeException("SM4解密失败", e);
        }
    }

    /**
     * 加密敏感字段（身份证、手机号等）
     * 自动处理null值
     * 
     * @param data 原始数据
     * @return String 加密后的数据
     */
    public static String encryptSensitive(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        return encrypt(data);
    }

    /**
     * 解密敏感字段
     * 自动处理null值
     * 
     * @param encryptedData 加密数据
     * @return String 解密后的数据
     */
    public static String decryptSensitive(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            return decrypt(encryptedData);
        } catch (Exception e) {
            return encryptedData;
        }
    }
}
