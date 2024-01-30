package com.dsj.csp.common.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankOrWhitespaceValidator implements ConstraintValidator<NotBlankOrWhitespace, String> {

    @Override
    public void initialize(NotBlankOrWhitespace constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 字段为null时不进行校验
        }
        return !value.trim().isEmpty(); // 判断字段是否为空或只包含空格
    }
}

