package com.management_system.utilities.config.meta_data;

import org.springframework.stereotype.Component;

@Component
public class SystemMetaData {
    public final static String CREDENTIALS_FILE_PATH = "src/main/resources/system_configs.env";
    public final static String SSL_KEYSTORE_FILE_PATH = "classpath:keystore.p12";
}
