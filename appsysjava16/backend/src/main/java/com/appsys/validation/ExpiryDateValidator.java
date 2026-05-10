package com.appsys.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * 保质期验证器
 * 验证规则：不得早于当前日6个月
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public class ExpiryDateValidator implements ConstraintValidator<ExpiryDate, LocalDate> {

    @Override
    public void initialize(ExpiryDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        LocalDate minExpiryDate = LocalDate.now().plusMonths(6);
        return !value.isBefore(minExpiryDate);
    }
}
