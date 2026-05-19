package com.petclinic.aspect;

import com.petclinic.dto.ReviewResult;
import com.petclinic.service.ReviewRuleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 内容审核AOP切面
 * 自动拦截包含@NeedReview注解的方法，对参数进行敏感词审核
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Aspect
@Component
public class ContentReviewAspect {

    @Autowired
    private ReviewRuleService reviewRuleService;

    /**
     * 定义切点：拦截所有使用@NeedReview注解的方法
     */
    @Pointcut("@annotation(com.petclinic.annotation.NeedReview)")
    public void needReviewPointcut() {}

    /**
     * 环绕通知：在方法执行前后进行内容审核
     */
    @Around("needReviewPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        
        if (args != null && args.length > 0) {
            Map<String, String> fieldsToReview = new HashMap<>();
            
            // 收集所有需要审核的字符串字段
            for (Object arg : args) {
                if (arg != null) {
                    collectStringFields(arg, fieldsToReview);
                }
            }
            
            // 执行内容审核
            if (!fieldsToReview.isEmpty()) {
                ReviewResult result = reviewRuleService.reviewMultipleFields(fieldsToReview);
                
                // 如果发现中高级违规，阻止操作
                if (result.isHasSensitiveContent() && 
                    ("MEDIUM".equals(result.getViolationLevel()) || "HIGH".equals(result.getViolationLevel()))) {
                    throw new RuntimeException("内容审核不通过：" + result.getMessage() + 
                        "，检测到敏感词：" + result.getSensitiveWordsFound());
                }
                
                // 如果发现低级违规，自动过滤并更新参数字段
                if (result.isHasSensitiveContent()) {
                    updateFilteredFields(args, fieldsToReview, result);
                }
            }
        }
        
        // 执行原方法
        return joinPoint.proceed(args);
    }

    /**
     * 收集对象中的所有字符串字段
     */
    private void collectStringFields(Object obj, Map<String, String> fields) {
        if (obj == null) {
            return;
        }
        
        // 如果是字符串类型，直接添加
        if (obj instanceof String) {
            fields.put("stringParam", (String) obj);
            return;
        }
        
        // 反射获取所有字段
        Class<?> clazz = obj.getClass();
        while (clazz != null && !clazz.equals(Object.class)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getType().equals(String.class)) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(obj);
                        if (value != null && !value.trim().isEmpty()) {
                            fields.put(field.getName(), value);
                        }
                    } catch (Exception e) {
                        // 忽略无法访问的字段
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 更新过滤后的字段值
     */
    private void updateFilteredFields(Object[] args, Map<String, String> originalFields, ReviewResult result) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> filteredFields = com.alibaba.fastjson.JSON.parseObject(
                result.getFilteredContent(), Map.class);
            
            for (Object arg : args) {
                if (arg != null && !(arg instanceof String)) {
                    Class<?> clazz = arg.getClass();
                    while (clazz != null && !clazz.equals(Object.class)) {
                        for (Map.Entry<String, String> entry : filteredFields.entrySet()) {
                            String fieldName = entry.getKey();
                            String filteredValue = entry.getValue();
                            
                            try {
                                Field field = clazz.getDeclaredField(fieldName);
                                if (field.getType().equals(String.class)) {
                                    field.setAccessible(true);
                                    field.set(arg, filteredValue);
                                }
                            } catch (NoSuchFieldException e) {
                                // 字段不存在，继续下一个
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        } catch (Exception e) {
            // 更新失败，不影响主流程
        }
    }
}
