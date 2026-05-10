package com.appsys.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 批次编号验证�?
 * 验证规则�?位数�?字母组合
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public class BatchNumberValidator implements ConstraintValidator<BatchNumber, String> {

    /**
     * 批次编号正则表达式：8位，必须同时包含数字和字�?
     * 格式要求：长�?位，�?-9和A-Za-z组成，且至少包含一个数字和一个字�?
     */
    private static final Pattern PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Za-z])[A-Za-z0-9]{8}$");

    @Override
    public void initialize(BatchNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return PATTERN.matcher(value).matches();
    }
}
