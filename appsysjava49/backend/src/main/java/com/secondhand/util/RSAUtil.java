package com.secondhand.util;

import org.springframework.stereotype.Component;

@Component
public class RSAUtil {

    public String encrypt(String data) {
        return data;
    }

    public String decrypt(String encryptedData) {
        return encryptedData;
    }

    public String sign(String data) {
        return data.hashCode() + "";
    }

    public boolean verify(String data, String signStr) {
        return (data.hashCode() + "").equals(signStr);
    }
}
