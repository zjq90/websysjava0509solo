package com.medical.registration.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class Sm4Util {
    
    @Value("${app.encryption.sm4-key:1234567890123456}")
    private String sm4Key;
    
    private static SM4 sm4;
    
    @PostConstruct
    public void init() {
        sm4 = SmUtil.sm4(sm4Key.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            return sm4.encryptHex(plainText);
        } catch (Exception e) {
            return plainText;
        }
    }
    
    public static String decrypt(String cipherText) {
        if (cipherText == null) {
            return null;
        }
        try {
            return sm4.decryptStr(cipherText);
        } catch (Exception e) {
            return cipherText;
        }
    }
    
    public static String encryptPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return encrypt(phone);
        }
        return encrypt(phone);
    }
    
    public static String decryptPhone(String encryptedPhone) {
        return decrypt(encryptedPhone);
    }
    
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 15) {
            return idCard;
        }
        if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "****" + idCard.substring(12);
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }
}
