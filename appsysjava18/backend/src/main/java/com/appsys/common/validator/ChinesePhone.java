package com.appsys.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 中国手机号验证注解
 * 1开头，11位
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = ChinesePhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChinesePhone {
    String message() default "手机号格式不正确，必须是1开头的11位数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
