package com.management_system.utilities.web.logging;

import com.management_system.utilities.utils.LoggingUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CustomBodyResponseLogger implements ResponseBodyAdvice<Object> {
    @Autowired
    LoggingUtils loggingUtils;


    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
            loggingUtils.logHttpServletResponse(
                    ((ServletServerHttpRequest) request).getServletRequest(),
                    ((ServletServerHttpResponse) response).getServletResponse(),
                    body
            );
        }

        return body;
    }
}
