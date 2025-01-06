package com.nqvinh.rentofficebackend.domain.common.validator;

import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.common.annotation.GenericValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class GenericValidator implements ConstraintValidator<GenericValidation, Object> {

    private GenericValidation.ValidationType validationType;

    @Override
    public void initialize(GenericValidation constraintAnnotation) {
        this.validationType = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        switch (validationType) {
    case NOT_EMPTY_COLLECTION -> {
        return value instanceof Collection  && !((Collection<?>) value).isEmpty();
    }
    case USERNAME_OR_EMAIL -> {
        if (value instanceof AuthRequestDto authRequest) {
            return authRequest.getUsername() != null || authRequest.getEmail() != null;
        }
        return false;
    }
    default -> {
        return false;
    }
}
    }
}