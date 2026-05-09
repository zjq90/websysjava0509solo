package com.example.websys.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户名格式校验注解
 * 校验规则：
 * - 必须以字母开头
 * - 只能包含字母、数字和下划线
 * - 长度3-20个字符
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "用户名格式不正确，必须以字母开头，只能包含字母、数字和下划线，长度3-20个字符";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
