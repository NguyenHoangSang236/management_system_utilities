package com.management_system.utilities.core.validator.file;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ImageFileValid {
    boolean nullable() default false;
    String message() default "Invalid image file. Allowed types: PNG, JPEG, JPG, GIF.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
