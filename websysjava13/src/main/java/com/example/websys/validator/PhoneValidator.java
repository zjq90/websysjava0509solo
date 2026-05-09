package com.example.websys.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号格式校验器
 * 支持中国大陆手机号格式校验
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final Pattern CHINA_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private boolean required;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (!required && (phone == null || phone.trim().isEmpty())) {
            return true;
        }

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        String trimmedPhone = phone.trim();
        return CHINA_PHONE_PATTERN.matcher(trimmedPhone).matches();
    }
}
