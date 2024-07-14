package com.management_system.utilities;

import com.management_system.utilities.utils.CredentialsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class},
		scanBasePackages = {"com.management_system.utilities"}
)
public class UtilitiesApplication {
	@Autowired
	CredentialsUtils credentialsUtils;

	public static void main(String[] args) {
		SpringApplication.run(UtilitiesApplication.class, args);
	}

}
