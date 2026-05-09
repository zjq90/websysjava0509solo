package com.example.websys.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 颜色值格式校验器
 * 支持HEX格式颜色值
 */
public class ColorValidator implements ConstraintValidator<ValidColor, String> {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    private boolean required;

    @Override
    public void initialize(ValidColor constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        if (!required && (color == null || color.trim().isEmpty())) {
            return true;
        }

        if (color == null || color.trim().isEmpty()) {
            return false;
        }

        String trimmedColor = color.trim();
        return HEX_COLOR_PATTERN.matcher(trimmedColor).matches();
    }
}
