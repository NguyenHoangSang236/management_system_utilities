package com.management_system.utilities.core.validator.insert;

import com.management_system.utilities.utils.CheckUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InsertValidator implements ConstraintValidator<InsertValid, Object> {
    final CheckUtils checkUtils;
    String message;
    String phoneMessage;
    String emailMessage;
    String nullMessage;
    boolean isPhoneNumber;
    boolean isEmail;
    boolean nullable;

    @Override
    public void initialize(InsertValid constraintAnnotation) {
        this.emailMessage = constraintAnnotation.emailMessage();
        this.nullMessage = constraintAnnotation.nullMessage();
        this.message = constraintAnnotation.message();
        this.phoneMessage = constraintAnnotation.phoneMessage();
        this.isPhoneNumber = constraintAnnotation.isPhoneNumber();
        this.isEmail = constraintAnnotation.isEmail();
        this.nullable = constraintAnnotation.nullable();

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!this.nullable && (value == null || value.toString().isBlank())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.nullMessage).addConstraintViolation();

            return false;
        }
        else {
            if(value == null || value.toString().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.nullMessage).addConstraintViolation();

                return false;
            }

            if (isEmail && !checkUtils.isValidEmail(value.toString())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.emailMessage).addConstraintViolation();

                return false;
            }

            if (isPhoneNumber && !checkUtils.isValidPhoneNumber(value.toString())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.phoneMessage).addConstraintViolation();

                return false;
            }

            return true;
        }
    }
}
