package com.example.websys.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 统计项编码格式校验注解
 * 校验规则：
 * - 必须以小写字母开头
 * - 只能包含小写字母、数字和下划线
 * - 长度3-50个字符
 * 示例：today_sales, order_count_2024
 */
@Documented
@Constraint(validatedBy = ItemCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidItemCode {

    String message() default "编码格式不正确，必须以小写字母开头，只能包含小写字母、数字和下划线，长度3-50个字符";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
