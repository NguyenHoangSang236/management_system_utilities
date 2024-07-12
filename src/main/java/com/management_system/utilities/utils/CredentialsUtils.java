package com.management_system.utilities.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class CredentialsUtils {
    public String getCredentials(String key) {
        Dotenv dotenv = Dotenv
                .configure()
                .filename("system_credentials.env")
                .load();

        return dotenv.get(key);
    }
}
