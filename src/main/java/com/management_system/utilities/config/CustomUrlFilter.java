package com.management_system.utilities.config;


import com.management_system.utilities.constant.ConstantValue;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class CustomUrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // generate request ID
        String requestId = UUID.randomUUID().toString();
        servletRequest.setAttribute(ConstantValue.REQUEST_ID, requestId);

        System.out.println(servletRequest.getAttribute(ConstantValue.REQUEST_ID));

        // filter request and response
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
