package com.appsys.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 未来日期验证注解
 * 保质期不得早于当前日期+指定月数
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = FutureMinMonthsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureMinMonths {
    String message() default "保质期不得早于当前日期+{value}个月";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 最少月数
     */
    int value() default 6;
}
