package com.medical.registration.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    
    String operationType();
    
    String operationDesc();
    
    String module() default "";
}
