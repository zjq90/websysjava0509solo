package com.hospital;

import com.hospital.util.SM2Util;
import com.hospital.util.SM3Util;
import com.hospital.util.SM4Util;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class SmCryptoTest {

    @Test
    public void testSM3Hash() throws Exception {
        String data = "这是一段测试数据";
        String hash1 = SM3Util.hash(data);
        String hash2 = SM3Util.hash(data);
        
        assertNotNull(hash1);
        assertEquals(hash1, hash2);
        
        String hashWithSalt = SM3Util.hashWithSalt(data, "testSalt");
        assertNotEquals(hash1, hashWithSalt);
        
        System.out.println("SM3 Hash: " + hash1);
        System.out.println("SM3 Hash with salt: " + hashWithSalt);
    }

    @Test
    public void testSM4EncryptDecrypt() throws Exception {
        String plaintext = "测试敏感数据：手机号13800138000";
        String key = "1234567890123456";
        
        String encrypted = SM4Util.encrypt(plaintext, key);
        assertNotNull(encrypted);
        assertNotEquals(plaintext, encrypted);
        
        String decrypted = SM4Util.decrypt(encrypted, key);
        assertEquals(plaintext, decrypted);
        
        System.out.println("SM4 Plaintext: " + plaintext);
        System.out.println("SM4 Encrypted: " + encrypted);
        System.out.println("SM4 Decrypted: " + decrypted);
    }

    @Test
    public void testSM2EncryptDecrypt() throws Exception {
        KeyPair keyPair = SM2Util.generateKeyPair();
        String publicKey = SM2Util.getPublicKey(keyPair);
        String privateKey = SM2Util.getPrivateKey(keyPair);
        
        String plaintext = "这是需要加密的重要数据";
        
        String encrypted = SM2Util.encrypt(plaintext, publicKey);
        assertNotNull(encrypted);
        assertNotEquals(plaintext, encrypted);
        
        String decrypted = SM2Util.decrypt(encrypted, privateKey);
        assertEquals(plaintext, decrypted);
        
        System.out.println("SM2 Public Key: " + publicKey);
        System.out.println("SM2 Plaintext: " + plaintext);
        System.out.println("SM2 Encrypted: " + encrypted);
        System.out.println("SM2 Decrypted: " + decrypted);
    }

    @Test
    public void testSM2SignVerify() throws Exception {
        KeyPair keyPair = SM2Util.generateKeyPair();
        String publicKey = SM2Util.getPublicKey(keyPair);
        String privateKey = SM2Util.getPrivateKey(keyPair);
        
        String data = "这是需要签名的数据";
        
        String signature = SM2Util.sign(data, privateKey);
        assertNotNull(signature);
        
        boolean verified = SM2Util.verify(data, signature, publicKey);
        assertTrue(verified);
        
        System.out.println("SM2 Signature: " + signature);
        System.out.println("SM2 Verify Result: " + verified);
    }
}
