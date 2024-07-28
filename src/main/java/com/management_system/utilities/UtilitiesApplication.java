package com.management_system.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {
                "com.management_system.utilities",
                "com.management_system.utilities.constant",
                "com.management_system.utilities.core",
                "com.management_system.utilities.entities",
                "com.management_system.utilities.repository",
                "com.management_system.utilities.utils",
                "com.management_system.utilities.web",
        }
)
@ComponentScan(basePackages = {
        "com.management_system.utilities",
        "com.management_system.utilities.constant",
        "com.management_system.utilities.core",
        "com.management_system.utilities.entities",
        "com.management_system.utilities.repository",
        "com.management_system.utilities.utils",
        "com.management_system.utilities.web",
})
public class UtilitiesApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilitiesApplication.class, args);
    }

}
