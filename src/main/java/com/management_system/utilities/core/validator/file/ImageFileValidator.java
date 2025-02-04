package com.management_system.utilities.core.validator.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
public class ImageFileValidator implements ConstraintValidator<ImageFileValid, MultipartFile> {
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg", "image/jpg", "image/gif");
    boolean nullable;


    @Override
    public void initialize(ImageFileValid constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return this.nullable;
        }

        return ALLOWED_TYPES.contains(file.getContentType());
    }
}
