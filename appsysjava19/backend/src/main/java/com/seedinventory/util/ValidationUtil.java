package com.seedinventory.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * 数据校验工具类
 * 提供各类业务数据验证功能
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
public class ValidationUtil {
    
    /**
     * 批次号正则：8位数字+字母组合
     * 必须包含至少一个数字和一个字母
     */
    private static final Pattern BATCH_NO_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8}$");
    
    /**
     * 中国大陆手机号正则：1开头，11位数字
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    /**
     * 发芽率范围：0.0 - 100.0
     */
    private static final double GERMINATION_RATE_MIN = 0.0;
    private static final double GERMINATION_RATE_MAX = 100.0;
    
    /**
     * 验证批次号
     * 规则：必须为8位数字+字母组合，且必须同时包含数字和字母
     * 
     * @param batchNo 批次号
     * @return 验证结果对象
     */
    public static ValidationResult validateBatchNo(String batchNo) {
        if (batchNo == null || batchNo.isEmpty()) {
            return ValidationResult.fail("批次号不能为空");
        }
        if (!BATCH_NO_PATTERN.matcher(batchNo).matches()) {
            return ValidationResult.fail("批次号必须为8位数字+字母组合，且同时包含数字和字母");
        }
        return ValidationResult.success();
    }
    
    /**
     * 验证手机号
     * 规则：符合中国大陆手机号格式（1开头，11位数字）
     * 
     * @param phone 手机号
     * @return 验证结果对象
     */
    public static ValidationResult validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return ValidationResult.fail("手机号不能为空");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return ValidationResult.fail("手机号格式不正确，必须为1开头的11位数字");
        }
        return ValidationResult.success();
    }
    
    /**
     * 验证发芽率
     * 规则：数值范围 0–100%，精度保留1位小数
     * 
     * @param rate 发芽率
     * @return 验证结果对象
     */
    public static ValidationResult validateGerminationRate(Double rate) {
        if (rate == null) {
            return ValidationResult.fail("发芽率不能为空");
        }
        if (rate < GERMINATION_RATE_MIN || rate > GERMINATION_RATE_MAX) {
            return ValidationResult.fail("发芽率必须在0.0-100.0之间");
        }
        String rateStr = String.valueOf(rate);
        if (rateStr.contains(".")) {
            String decimalPart = rateStr.split("\\.")[1];
            if (decimalPart.length() > 1) {
                return ValidationResult.fail("发芽率精度只能保留1位小数");
            }
        }
        return ValidationResult.success();
    }
    
    /**
     * 验证保质期
     * 规则：不得早于当前日期+6个月
     * 
     * @param expiryDate 保质期日期
     * @return 验证结果对象
     */
    public static ValidationResult validateExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            return ValidationResult.fail("保质期不能为空");
        }
        LocalDate minDate = LocalDate.now().plusMonths(6);
        if (expiryDate.isBefore(minDate)) {
            return ValidationResult.fail("保质期不得早于当前日期+6个月（即 " + minDate + " 之后）");
        }
        return ValidationResult.success();
    }
    
    /**
     * 验证结果内部类
     */
    public static class ValidationResult {
        private boolean success;
        private String message;
        
        private ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult fail(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
