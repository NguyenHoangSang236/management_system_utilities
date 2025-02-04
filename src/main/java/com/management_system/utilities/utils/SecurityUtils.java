package com.management_system.utilities.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUtils {
    public static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();


    public static void storeSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("Current security context: {}", context);

        if (context != null) {
            contextHolder.set(context);
        }
    }

    public static void clearSecurityContext() {
        contextHolder.remove();
    }

    public static void restoreSecurityContext() {
        SecurityContext context = contextHolder.get();
        log.info("Current security context: {}", context);

        if (context != null) {
            SecurityContextHolder.setContext(context);
        }
    }
}
