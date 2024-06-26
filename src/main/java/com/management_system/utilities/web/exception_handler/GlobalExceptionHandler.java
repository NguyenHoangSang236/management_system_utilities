package com.management_system.utilities.web.exception_handler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(value = {BadCredentialsException.class})
    ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(value = {AccessDeniedException.class})
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(value = {FirebaseMessagingException.class})
    ResponseEntity<ApiResponse> handleFirebaseMessagingException(FirebaseMessagingException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = {DataNotFoundException.class})
    ResponseEntity<ApiResponse> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ApiResponse> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ApiResponse("failed", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
