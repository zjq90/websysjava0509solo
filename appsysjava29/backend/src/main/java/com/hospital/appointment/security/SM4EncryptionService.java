package com.hospital.appointment.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

/**
 * 国密SM4加密服务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Component
public class SM4EncryptionService {

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Value("${encryption.sm4-key:0123456789abcdef0123456789abcdef}")
    private String sm4Key;

    private static final String ALGORITHM_NAME = "SM4";
    private static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    public String encrypt(String plainText) {
        try {
            byte[] keyBytes = hexStringToBytes(sm4Key);
            byte[] encrypted = sm4EncryptEcbPadding(keyBytes, plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("SM4加密失败", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] keyBytes = hexStringToBytes(sm4Key);
            byte[] decrypted = sm4DecryptEcbPadding(keyBytes, Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("SM4解密失败", e);
        }
    }

    private byte[] sm4EncryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, "BC");
        SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
        return cipher.doFinal(data);
    }

    private byte[] sm4DecryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, "BC");
        SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, sm4Key);
        return cipher.doFinal(data);
    }

    private byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String encryptPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return encrypt(phone);
    }

    public String encryptIdCard(String idCard) {
        if (idCard == null) {
            return idCard;
        }
        return encrypt(idCard);
    }

    public String encryptPaymentData(String data) {
        if (data == null) {
            return data;
        }
        return encrypt(data);
    }
}
