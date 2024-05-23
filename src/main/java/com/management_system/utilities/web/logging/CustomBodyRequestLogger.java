package com.management_system.utilities.web.logging;

import com.management_system.utilities.utils.LoggingUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ControllerAdvice
public class CustomBodyRequestLogger extends RequestBodyAdviceAdapter {
    @Autowired
    LoggingUtils loggingUtils;
    @Autowired
    HttpServletRequest httpServletRequest;


    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Type targetType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @NonNull
    public Object afterBodyRead(@NonNull Object body,
                                @NonNull HttpInputMessage inputMessage,
                                @NonNull MethodParameter parameter,
                                @NonNull Type targetType,
                                @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        loggingUtils.logHttpServletRequest(httpServletRequest, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
