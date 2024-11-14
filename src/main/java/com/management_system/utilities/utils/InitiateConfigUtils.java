package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitiateConfigUtils {
    final SystemConfigEnvUtils systemConfigEnvUtils;


    public void initSslConfig() {
        String sslKeyStoreAlias = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_ALIAS);
        String sslKeyStoreType = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_TYPE);
        String sslKeyStorePassword = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD);
        String sslFileDirectory = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_FILE_DIRECTORY);

        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_ALIAS.name(), sslKeyStoreAlias);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_TYPE.name(), sslKeyStoreType);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD.name(), sslKeyStorePassword);
        System.setProperty(SystemConfigKeyName.SSL_FILE_DIRECTORY.name(), sslFileDirectory);
    }
}
