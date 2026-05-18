package com.heritage.controller;

import com.heritage.util.RsaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共接口控制器
 * 提供公钥获取、加密测试等公共接口
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/common")
@Tag(name = "公共接口", description = "公钥获取、加密测试等公共功能")
public class CommonController {

    @Autowired
    private RsaUtil rsaUtil;

    /**
     * 获取RSA公钥
     * 前端使用此公钥对敏感数据进行加密
     */
    @GetMapping("/public-key")
    @Operation(summary = "获取RSA公钥", description = "返回Base64编码的RSA公钥，用于前端加密敏感数据")
    public ResponseEntity<Map<String, Object>> getPublicKey() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("publicKey", rsaUtil.getPublicKey());
        result.put("algorithm", "RSA");
        result.put("keySize", 2048);
        return ResponseEntity.ok(result);
    }

    /**
     * 加密测试接口
     * 用于测试加密解密功能
     */
    @PostMapping("/encrypt-test")
    @Operation(summary = "加密测试", description = "测试RSA加密解密功能")
    public ResponseEntity<Map<String, Object>> encryptTest(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String originalData = request.get("data");
            String encryptedData = rsaUtil.encrypt(originalData);
            String decryptedData = rsaUtil.decrypt(encryptedData);

            result.put("success", true);
            result.put("original", originalData);
            result.put("encrypted", encryptedData);
            result.put("decrypted", decryptedData);
            result.put("match", originalData.equals(decryptedData));
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 解密测试接口
     * 用于验证前端加密的数据是否能正确解密
     */
    @PostMapping("/decrypt-test")
    @Operation(summary = "解密测试", description = "测试前端加密数据的解密功能")
    public ResponseEntity<Map<String, Object>> decryptTest(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String encryptedData = request.get("encryptedData");
            String decryptedData = rsaUtil.decrypt(encryptedData);

            result.put("success", true);
            result.put("decrypted", decryptedData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("timestamp", System.currentTimeMillis());
        result.put("service", "heritage-collection");
        result.put("version", "1.0.0");
        return ResponseEntity.ok(result);
    }
}
