package com.management_system.utilities.config.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.constant.enumuration.ExceptionType;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.IdNotFoundException;
import com.management_system.utilities.entities.exceptions.InvalidFileExtensionException;
import com.mongodb.MongoWriteException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(ApiResponse.builder()
                .result(ResponseResult.failed.name())
                .content(ConstantValue.UNAUTHORIZED_ERROR_MESSAGE)
                .message(ExceptionType.AUTHENTICATION.getValue())
                .status(HttpStatus.UNAUTHORIZED)
                .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ConstantValue.UNAUTHORIZED_ERROR_MESSAGE)
                        .message(ExceptionType.AUTHENTICATION.getValue())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ConstantValue.ACCESS_ERROR_MESSAGE)
                        .message(ExceptionType.ACCESS.getValue())
                        .status(HttpStatus.FORBIDDEN)
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {FirebaseMessagingException.class})
    protected ResponseEntity<ApiResponse> handleFirebaseMessagingException(FirebaseMessagingException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE)
                        .message(ExceptionType.FIREBASE.getValue())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<ApiResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ex.getMessage())
                        .message(ExceptionType.DATA_NOT_FOUND.getValue())
                        .status(HttpStatus.NO_CONTENT)
                        .build(),
                HttpStatus.NO_CONTENT
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {IdNotFoundException.class})
    protected ResponseEntity<ApiResponse> handleIdNotFoundException(IdNotFoundException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ex.getMessage())
                        .message(ExceptionType.DATA_NOT_FOUND.getValue())
                        .status(HttpStatus.NOT_FOUND)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicateKeyException.class})
    protected ResponseEntity<ApiResponse> handleMongoWriteException(DuplicateKeyException ex) {
        ex.printStackTrace();

        if (ex.getCause() instanceof MongoWriteException mongoWriteEx) {

            int code = mongoWriteEx.getCode();

            if (code == 11000) {
                String jsonStr = mongoWriteEx.getError().getMessage().substring(mongoWriteEx.getError().getMessage().indexOf("{"));

                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result(ResponseResult.failed.name())
                                .content("Duplicated data: " + jsonStr)
                                .message(ExceptionType.DATA_DUPLICATED.getValue())
                                .status(HttpStatus.BAD_REQUEST)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            } else {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result(ResponseResult.failed.name())
                                .content(mongoWriteEx.getMessage())
                                .message(ExceptionType.DATA_DUPLICATED.getValue())
                                .status(HttpStatus.BAD_REQUEST)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }

        } else {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .result(ResponseResult.failed.name())
                            .content(ex.getMessage())
                            .message(ExceptionType.DATA_DUPLICATED.getValue())
                            .status(HttpStatus.BAD_REQUEST)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String[] errorMessage = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Field ")
                            .append(getFieldName(constraintViolation.getPropertyPath().toString()))
                            .append(" ")
                            .append(constraintViolation.getMessage());

                    return builder;
                }).toArray(String[]::new);
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ex.getMessage())
                        .message(ExceptionType.VALIDATOR.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {ExpiredJwtException.class})
    protected ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ex.getMessage())
                        .message(ExceptionType.JWT_EXPIRED.getValue())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidFormatException.class})
    protected ResponseEntity<ApiResponse> handleInvalidFormatException(InvalidFormatException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.INVALID_FORMAT.getValue())
                        .content("JSON parse error, there must be some variable does not exist or invalid, please check again")
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        StringBuilder errorMessage = new StringBuilder("Validation failed for arguments: ");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessageForField = error.getDefaultMessage();
                errorMessage.append(String.format("Field '%s': %s; ", fieldName, errorMessageForField));
            }
        });

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(errorMessage.toString())
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        StringBuilder errorMessage = new StringBuilder("Validation failed for arguments: ");

        ex.getAllErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append(", ");
        });

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(errorMessage.toString())
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE)
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleInvalidFileExtensionException(InvalidFileExtensionException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.FILE.getValue())
                        .content(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.FILE.getValue())
                        .content("Field " + ex.getParameterName() + " must not be null")
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE)
                        .message(ExceptionType.INTERNAL.getValue())
                        .status(HttpStatus.valueOf(status.value()))
                        .build(),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    private String getFieldName(String propertyPath) {
        return propertyPath.substring(propertyPath.indexOf(".") + 1);
    }
}
