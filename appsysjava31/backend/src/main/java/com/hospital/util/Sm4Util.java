package com.hospital.util;

import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Security;
import java.util.Base64;

/**
 * 国密SM4加密工具类
 * 用于敏感数据（身份证、手机号、支付数据）的加密解密
 * 
 * @author hospital
 * @version 1.0.0
 */
@Component
public class Sm4Util {

    /**
     * SM4密钥
     */
    @Value("${encryption.sm4.key}")
    private String sm4Key;

    /**
     * 初始化向量（IV）
     */
    private static final byte[] IV = {
            0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
            (byte) 0xFE, (byte) 0xDC, (byte) 0xBA, (byte) 0x98, 0x76, 0x54, 0x32, 0x10
    };

    /**
     * 初始化BouncyCastle安全提供者
     */
    @PostConstruct
    public void init() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 加密数据
     * 
     * @param plainText 明文
     * @return 加密后的Base64字符串
     */
    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        
        try {
            byte[] keyBytes = getKeyBytes();
            byte[] plainBytes = plainText.getBytes("UTF-8");
            
            byte[] encrypted = doSM4(keyBytes, IV, plainBytes, true);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("SM4加密失败", e);
        }
    }

    /**
     * 解密数据
     * 
     * @param cipherText 加密后的Base64字符串
     * @return 明文
     */
    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }
        
        try {
            byte[] keyBytes = getKeyBytes();
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            
            byte[] decrypted = doSM4(keyBytes, IV, cipherBytes, false);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("SM4解密失败", e);
        }
    }

    /**
     * 获取密钥字节数组
     * 
     * @return 16字节的密钥
     */
    private byte[] getKeyBytes() {
        byte[] keyBytes = sm4Key.getBytes();
        byte[] result = new byte[16];
        
        System.arraycopy(keyBytes, 0, result, 0, Math.min(keyBytes.length, 16));
        return result;
    }

    /**
     * 执行SM4加解密操作
     * 
     * @param key 密钥
     * @param iv 初始化向量
     * @param input 输入数据
     * @param encrypt true-加密，false-解密
     * @return 处理后的数据
     */
    private byte[] doSM4(byte[] key, byte[] iv, byte[] input, boolean encrypt) throws Exception {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new SM4Engine()));
        
        ParametersWithIV parameters = new ParametersWithIV(new KeyParameter(key), iv);
        cipher.init(encrypt, parameters);
        
        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int length = cipher.processBytes(input, 0, input.length, output, 0);
        length += cipher.doFinal(output, length);
        
        byte[] result = new byte[length];
        System.arraycopy(output, 0, result, 0, length);
        return result;
    }

    /**
     * 脱敏手机号（中间4位用*代替）
     * 
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏身份证号（中间10位用*代替）
     * 
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 15) {
            return idCard;
        }
        if (idCard.length() == 15) {
            return idCard.substring(0, 4) + "*******" + idCard.substring(11);
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }
}
