package com.appsys.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 批次号验证器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
public class BatchNumberValidator implements ConstraintValidator<BatchNumber, String> {

    // 8位数字和字母组合的正则表达式
    private static final String BATCH_NUMBER_PATTERN = "^[A-Za-z0-9]{8}$";

    @Override
    public void initialize(BatchNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 非空由@NotBlank验证
        }
        return value.matches(BATCH_NUMBER_PATTERN);
    }
}
