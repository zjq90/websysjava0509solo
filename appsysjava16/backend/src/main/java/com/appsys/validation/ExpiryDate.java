package com.appsys.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

/**
 * 保质期验证注�? * 要求：不得早于当前日�?6个月
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpiryDate {
    String message() default "保质期不得早于当前日�?6个月";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
