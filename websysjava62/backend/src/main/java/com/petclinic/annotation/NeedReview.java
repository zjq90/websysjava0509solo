package com.petclinic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要内容审核的注解
 * 标记在需要进行敏感词审核的方法上
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedReview {
    
    /**
     * 审核级别：STRICT-严格（发现敏感词即阻止），NORMAL-正常（中高级才阻止）
     */
    String level() default "NORMAL";
    
    /**
     * 需要审核的字段名称数组，如果为空则审核所有字符串字段
     */
    String[] fields() default {};
}
