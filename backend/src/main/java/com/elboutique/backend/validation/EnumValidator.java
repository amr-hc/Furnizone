package com.elboutique.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private String[] enumValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        // Get all enum constants as strings
        enumValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
            .map(enumValue -> enumValue.name().toLowerCase())
            .toArray(String[]::new);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // Value is required
        }
        // Check if the value matches any enum constant
        return Arrays.asList(enumValues).contains(value.toLowerCase());
    }
}
