package com.elboutique.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass(); // The enum class to validate against
    String message() default "Invalid value.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
