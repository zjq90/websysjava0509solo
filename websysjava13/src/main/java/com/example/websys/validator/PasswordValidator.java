package com.example.websys.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 密码强度校验器
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private int minLength;
    private int maxLength;
    private boolean required;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!required && (password == null || password.isEmpty())) {
            return true;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasLetter && hasDigit;
    }
}
