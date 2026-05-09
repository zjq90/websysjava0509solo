package com.example.websys.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码强度校验注解
 * 校验规则：
 * - 长度6-20个字符（可配置）
 * - 必须包含至少一个字母和一个数字
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "密码格式不正确，长度6-20个字符，必须包含至少一个字母和一个数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minLength() default 6;

    int maxLength() default 20;

    boolean required() default true;
}
