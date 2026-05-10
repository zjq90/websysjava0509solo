package com.appsys.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

/**
 * 中国手机号验证注�? * 要求�?开头，11位数�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = ChinesePhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChinesePhone {
    String message() default "请输入正确的中国大陆手机�?;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
