package com.example.websys.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 颜色值格式校验注解
 * 校验规则：必须是有效的HEX颜色格式
 * 示例：#FF5733, #fff, #123ABC
 */
@Documented
@Constraint(validatedBy = ColorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidColor {

    String message() default "颜色格式不正确，请输入有效的HEX颜色值（如：#FF5733 或 #fff）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}
