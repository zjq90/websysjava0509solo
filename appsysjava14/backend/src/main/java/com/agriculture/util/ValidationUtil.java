package com.agriculture.util;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * 提供各种业务数据的验证方法
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
public class ValidationUtil {

    private static final Pattern BATCH_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]{8}$");
    private static final Pattern CHINA_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 验证批次编号
     * 必须为8位数字+字母组合，且全局唯一（唯一性需结合数据库检查）
     * 
     * @param batchCode 批次编号
     * @return 验证结果
     */
    public static ValidationResult validateBatchCode(String batchCode) {
        if (batchCode == null || batchCode.trim().isEmpty()) {
            return ValidationResult.fail("批次编号不能为空");
        }
        if (!BATCH_CODE_PATTERN.matcher(batchCode).matches()) {
            return ValidationResult.fail("批次编号必须为8位数字+字母组合");
        }
        return ValidationResult.success();
    }

    /**
     * 验证保质期
     * 不得早于当前日期+6个月
     * 
     * @param shelfLife 保质期
     * @return 验证结果
     */
    public static ValidationResult validateShelfLife(LocalDateTime shelfLife) {
        if (shelfLife == null) {
            return ValidationResult.fail("保质期不能为空");
        }
        LocalDateTime minDate = LocalDateTime.now().plusMonths(6);
        if (shelfLife.isBefore(minDate)) {
            return ValidationResult.fail("保质期不得早于当前日期+6个月");
        }
        return ValidationResult.success();
    }

    /**
     * 验证发芽率
     * 数值范围：0–100%，精度保留1位小数
     * 
     * @param germinationRate 发芽率
     * @return 验证结果
     */
    public static ValidationResult validateGerminationRate(Double germinationRate) {
        if (germinationRate == null) {
            return ValidationResult.success();
        }
        if (germinationRate < 0 || germinationRate > 100) {
            return ValidationResult.fail("发芽率必须在0-100%范围内");
        }
        String rateStr = String.valueOf(germinationRate);
        if (rateStr.contains(".")) {
            String decimalPart = rateStr.split("\\.")[1];
            if (decimalPart.length() > 1) {
                return ValidationResult.fail("发芽率精度需保留1位小数");
            }
        }
        return ValidationResult.success();
    }

    /**
     * 验证中国大陆手机号
     * 符合中国大陆手机号格式（1开头，11位）
     * 
     * @param phone 手机号
     * @return 验证结果
     */
    public static ValidationResult validateChinaPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return ValidationResult.success();
        }
        if (!CHINA_PHONE_PATTERN.matcher(phone).matches()) {
            return ValidationResult.fail("手机号格式不正确，必须为1开头的11位数字");
        }
        return ValidationResult.success();
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @return 验证结果
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.success();
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.fail("邮箱格式不正确");
        }
        return ValidationResult.success();
    }

    /**
     * 验证结果类
     */
    public static class ValidationResult {
        private boolean valid;
        private String message;

        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, "验证通过");
        }

        public static ValidationResult fail(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
