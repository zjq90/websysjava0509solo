package com.appsys.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 发芽率验证注解
 * 数值范围：0–100%，精度保留1位小数
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = GerminationRateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GerminationRate {
    String message() default "发芽率必须在0-100之间，精度保留1位小数";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
