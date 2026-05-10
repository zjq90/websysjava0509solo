package com.appsys.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 批次号验证注解
 * 必须为8位数字+字母组合
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = BatchNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchNumber {
    String message() default "批次号必须为8位数字和字母组合";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
