package com.dsj.csp.common.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankOrWhitespaceValidator.class)
public @interface NotBlankOrWhitespace {
    String message() default "字段不能只包含空格";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

