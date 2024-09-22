package com.management_system.utilities.config.exception_handler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.IdNotFoundException;
import com.mongodb.MongoWriteException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ApiResponse.builder()
                .result("failed")
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {BadCredentialsException.class})
    ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {AccessDeniedException.class})
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.FORBIDDEN)
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {FirebaseMessagingException.class})
    ResponseEntity<ApiResponse> handleFirebaseMessagingException(FirebaseMessagingException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {DataNotFoundException.class})
    ResponseEntity<ApiResponse> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.NO_CONTENT)
                        .build(),
                HttpStatus.NO_CONTENT
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {IdNotFoundException.class})
    ResponseEntity<ApiResponse> handleIdNotFoundException(IdNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {DuplicateKeyException.class})
    ResponseEntity<ApiResponse> handleMongoWriteException(DuplicateKeyException ex) {
        if (ex.getCause() instanceof MongoWriteException mongoWriteEx) {

            int code = mongoWriteEx.getCode();

            if (code == 11000) {
                String jsonStr = mongoWriteEx.getError().getMessage().substring(mongoWriteEx.getError().getMessage().indexOf("{"));

                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result("failed")
                                .message("Duplicated data: " + jsonStr)
                                .status(HttpStatus.BAD_REQUEST)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            } else {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result("failed")
                                .message(mongoWriteEx.getMessage())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

        } else {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .result("failed")
                            .message(ex.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(errorMessage)
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {ExpiredJwtException.class})
    ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder("Validation failed for arguments: ");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessageForField = error.getDefaultMessage();
            errorMessage.append(String.format("Field '%s': %s; ", fieldName, errorMessageForField));
        });
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(errorMessage.toString())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponse response = ApiResponse.builder()
                .result("failed")
                .message(ex.getMessage())
                .status(HttpStatus.valueOf(status.value()))
                .build();

        return new ResponseEntity<>(response, headers, HttpStatus.valueOf(status.value()));
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ApiResponse> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result("failed")
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
