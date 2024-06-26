package com.management_system.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class},
		scanBasePackages = {"com.management_system.utilities"}
)
public class UtilitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilitiesApplication.class, args);
	}

}
