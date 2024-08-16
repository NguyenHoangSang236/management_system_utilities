package com.management_system.utilities.config;

import com.management_system.utilities.config.logging.RequestGetLogger;
import com.management_system.utilities.config.security.JwtAuthenticationFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig {
    final JwtAuthenticationFilter jwtAuthenticationFilter;

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


//    @Bean
//    public FilterRegistrationBean<CustomUrlFilter> filterRegistrationBean() {
//        FilterRegistrationBean<CustomUrlFilter> registrationBean = new FilterRegistrationBean<>();
//        CustomUrlFilter customURLFilter = new CustomUrlFilter();
//
//        registrationBean.setFilter(customURLFilter);
//        registrationBean.setOrder(2);
//
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> oncePerRequestFilterFilterRegistrationBean() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.setOrder(1);

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
