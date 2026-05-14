package com.appsys.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class RSAUtil {

    private RSA rsa;

    @PostConstruct
    public void init() {
        rsa = new RSA();
    }

    public String encrypt(String data) {
        return rsa.encryptBase64(data, KeyType.PublicKey);
    }

    public String decrypt(String encryptedData) {
        return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
    }

    public String getPublicKey() {
        return rsa.getPublicKeyBase64();
    }
}
