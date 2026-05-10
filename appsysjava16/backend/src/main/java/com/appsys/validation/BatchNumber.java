package com.appsys.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

/**
 * 批次编号验证注解
 * 要求�?位数�?字母组合，全局唯一
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = BatchNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchNumber {
    String message() default "批次编号必须�?位数�?字母组合";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
