package com.secondhand.util;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;

/**
 * RSA密钥生成工具
 *
 * @author secondhand
 * @version 1.0.0
 */
public class KeyGenerator {

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKeyStr = RSAUtil.getPublicKeyBase64(keyPair);
        String privateKeyStr = RSAUtil.getPrivateKeyBase64(keyPair);

        System.out.println("公钥(Base64):");
        System.out.println(publicKeyStr);
        System.out.println();
        System.out.println("私钥(Base64):");
        System.out.println(privateKeyStr);
    }

}
