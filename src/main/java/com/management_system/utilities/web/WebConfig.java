package com.management_system.utilities.web;

import com.management_system.utilities.web.security.JwtAuthenticationFilter;
import com.management_system.utilities.web.logging.RequestGetLogger;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Autowired
            RequestGetLogger requestLogger;


            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .allowedMethods("*");
            }

            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(requestLogger);
            }
        };
    }


    @Bean
    public FilterRegistrationBean<CustomUrlFilter> filterRegistrationBean() {
        FilterRegistrationBean<CustomUrlFilter> registrationBean = new FilterRegistrationBean<>();
        CustomUrlFilter customURLFilter = new CustomUrlFilter();

        registrationBean.setFilter(customURLFilter);
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> oncePerRequestFilterFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();

        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.setOrder(3);

        return registrationBean;
    }

    // config websocket
//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxSessionIdleTimeout(86400000L);
//
//        return container;
//    }
}
