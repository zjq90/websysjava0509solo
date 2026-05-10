package com.appsys.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 中国手机号验证器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
public class ChinesePhoneValidator implements ConstraintValidator<ChinesePhone, String> {

    // 中国手机号正则：1开头，11位数字
    private static final String PHONE_PATTERN = "^1\\d{10}$";

    @Override
    public void initialize(ChinesePhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 非空由@NotBlank验证
        }
        return value.matches(PHONE_PATTERN);
    }
}
