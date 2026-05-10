package com.appsys.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 中国手机号验证器
 * 验证规则开头，11位数
 * @author appsys-team
 * @version 1.0.0
 */
public class ChinesePhoneValidator implements ConstraintValidator<ChinesePhone, String> {

    /**
     * 中国大陆手机号正则表达式
     * 格式要求开头，第二-9，后位数字    */
    private static final Pattern PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public void initialize(ChinesePhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return PATTERN.matcher(value).matches();
    }
}
