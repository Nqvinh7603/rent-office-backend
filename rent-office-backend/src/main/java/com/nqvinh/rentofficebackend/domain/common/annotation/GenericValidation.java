package com.nqvinh.rentofficebackend.domain.common.annotation;

import com.nqvinh.rentofficebackend.domain.common.validator.GenericValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenericValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenericValidation {
    String message() default "Invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ValidationType type();

    enum ValidationType {
        NOT_EMPTY_COLLECTION,
        EMAIL
    }
}