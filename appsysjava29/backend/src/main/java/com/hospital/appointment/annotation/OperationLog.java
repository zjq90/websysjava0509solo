package com.hospital.appointment.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 
 * @author hospital
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    
    String module() default "";
    
    String operation() default "";
}
