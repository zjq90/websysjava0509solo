package com.hospital.util;

import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * SM2非对称加密算法工具类
 * 国密算法，用于敏感数据的加密传输
 * 
 * @author hospital
 * @version 1.0.0
 */
public class SM2Util {

    private static final String PROVIDER = "BC";
    private static final String CURVE_NAME = "sm2p256v1";
    private static final String ALGORITHM_NAME = "EC";
    
    static {
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private static KeyPair cachedKeyPair;

    private SM2Util() {}

    /**
     * 生成SM2密钥对
     * 
     * @return KeyPair 密钥对
     */
    public static KeyPair generateKeyPair() throws Exception {
        X9ECParameters ecP = ECNamedCurveTable.getByName(CURVE_NAME);
        ECDomainParameters ecParams = new ECDomainParameters(
                ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH());

        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(
                ecParams, new SecureRandom());
        keyPairGenerator.init(keyGenParams);

        AsymmetricCipherKeyPair pair = keyPairGenerator.generateKeyPair();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) pair.getPublic();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) pair.getPrivate();

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME, PROVIDER);
        
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(
                pubParams.getQ().getEncoded(false));
        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(
                privParams.getD().toByteArray());
        PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 获取缓存的密钥对（简化使用，生产环境应使用安全的密钥管理）
     * 
     * @return KeyPair 密钥对
     */
    public static KeyPair getCachedKeyPair() throws Exception {
        if (cachedKeyPair == null) {
            synchronized (SM2Util.class) {
                if (cachedKeyPair == null) {
                    cachedKeyPair = generateKeyPair();
                }
            }
        }
        return cachedKeyPair;
    }

    /**
     * 获取Base64编码的公钥
     * 
     * @return String 公钥字符串
     */
    public static String getPublicKeyBase64() throws Exception {
        return Base64.getEncoder().encodeToString(getCachedKeyPair().getPublic().getEncoded());
    }

    /**
     * SM2加密
     * 
     * @param data 明文数据
     * @param publicKeyBase64 Base64编码的公钥
     * @return String Base64编码的密文
     */
    public static String encrypt(String data, String publicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME, PROVIDER);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("SM2", PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * SM2解密
     * 
     * @param encryptedData Base64编码的密文
     * @param privateKeyBase64 Base64编码的私钥
     * @return String 明文数据
     */
    public static String decrypt(String encryptedData, String privateKeyBase64) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME, PROVIDER);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("SM2", PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted, "UTF-8");
    }

    /**
     * 使用缓存密钥对加密
     * 
     * @param data 明文数据
     * @return String Base64编码的密文
     */
    public static String encryptWithCachedKey(String data) throws Exception {
        return encrypt(data, getPublicKeyBase64());
    }

    /**
     * 使用缓存密钥对解密
     * 
     * @param encryptedData Base64编码的密文
     * @return String 明文数据
     */
    public static String decryptWithCachedKey(String encryptedData) throws Exception {
        String privateKeyBase64 = Base64.getEncoder().encodeToString(
                getCachedKeyPair().getPrivate().getEncoded());
        return decrypt(encryptedData, privateKeyBase64);
    }
}
