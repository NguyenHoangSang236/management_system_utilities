package com.management_system.utilities.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();


    public static void storeSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            contextHolder.set(context);
        }
    }

    public static void clearSecurityContext() {
        contextHolder.remove();
    }

    public static void restoreSecurityContext() {
        SecurityContext context = contextHolder.get();
        if (context != null) {
            SecurityContextHolder.setContext(context);
        }
    }
}
