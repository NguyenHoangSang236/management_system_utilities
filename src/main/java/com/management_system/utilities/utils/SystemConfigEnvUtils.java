package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import com.management_system.utilities.config.meta_data.SystemMetaData;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigEnvUtils {
    public String getCredentials(SystemConfigKeyName key) {
        Dotenv dotenv = Dotenv
                .configure()
                .filename(SystemMetaData.CREDENTIALS_FILE_PATH)
                .load();

        return dotenv.get(key.name());
    }
}
