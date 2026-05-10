package com.appsys.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * 发芽率验证器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
public class GerminationRateValidator implements ConstraintValidator<GerminationRate, Object> {

    @Override
    public void initialize(GerminationRate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BigDecimal bd;
        if (value instanceof BigDecimal) {
            bd = (BigDecimal) value;
        } else if (value instanceof Double) {
            bd = BigDecimal.valueOf((Double) value);
        } else if (value instanceof Float) {
            bd = BigDecimal.valueOf((Float) value);
        } else {
            return true;
        }

        // 检查范围：0-100
        if (bd.compareTo(BigDecimal.ZERO) < 0 || bd.compareTo(new BigDecimal("100")) > 0) {
            return false;
        }

        // 检查小数位数：最多1位
        int scale = bd.stripTrailingZeros().scale();
        return scale <= 1;
    }
}
