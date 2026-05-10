package com.appsys.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 未来日期验证器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
public class FutureMinMonthsValidator implements ConstraintValidator<FutureMinMonths, Object> {

    private int minMonths;

    @Override
    public void initialize(FutureMinMonths constraintAnnotation) {
        this.minMonths = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate minDate = LocalDate.now().plusMonths(minMonths);

        if (value instanceof LocalDate) {
            return ((LocalDate) value).isAfter(minDate) || ((LocalDate) value).isEqual(minDate);
        }

        if (value instanceof LocalDateTime) {
            LocalDate date = ((LocalDateTime) value).toLocalDate();
            return date.isAfter(minDate) || date.isEqual(minDate);
        }

        return true;
    }
}
