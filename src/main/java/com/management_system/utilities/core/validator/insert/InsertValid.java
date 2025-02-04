package com.management_system.utilities.core.validator.insert;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InsertValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertValid {
    String message() default "InsertValid Error: Invalid !!";

    String phoneMessage() default "InsertValid Error: Invalid phone number !!";

    String emailMessage() default "InsertValid Error: Invalid email !!";

    String nullMessage() default "InsertValid Error: Can not be null !!";

    boolean isPhoneNumber() default false;

    boolean isEmail() default false;

    boolean nullable() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
