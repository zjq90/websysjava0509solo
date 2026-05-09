package com.example.websys.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 统计项编码格式校验器
 * 支持下划线分隔的字母数字编码
 */
public class ItemCodeValidator implements ConstraintValidator<ValidItemCode, String> {

    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-z][a-z0-9_]{2,49}$");

    @Override
    public void initialize(ValidItemCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        String trimmedCode = code.trim();
        return CODE_PATTERN.matcher(trimmedCode).matches();
    }
}
