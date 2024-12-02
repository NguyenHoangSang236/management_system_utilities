package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitiateConfigUtils {
    final SystemConfigEnvUtils systemConfigEnvUtils;


    public void initSslConfig() {
        // SSL configs
        String sslKeyStoreAlias = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_ALIAS);
        String sslKeyStoreType = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_TYPE);
        String sslKeyStorePassword = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD);
        String sslFileDirectory = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_FILE_DIRECTORY);

        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_ALIAS.name(), sslKeyStoreAlias);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_TYPE.name(), sslKeyStoreType);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD.name(), sslKeyStorePassword);
        System.setProperty(SystemConfigKeyName.SSL_FILE_DIRECTORY.name(), sslFileDirectory);


        // Server ports
        String eurekaServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.EUREKA_SERVER_PORT);
        String authenServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.AUTHENTICATION_SERVICE_SERVER_PORT);
        String resourceServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.RESOURCE_SERVICE_SERVER_PORT);
        String redisServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.REDIS_SERVICE_SERVER_PORT);

        System.setProperty(SystemConfigKeyName.EUREKA_SERVER_PORT.name(), eurekaServerPort);
        System.setProperty(SystemConfigKeyName.AUTHENTICATION_SERVICE_SERVER_PORT.name(), authenServerPort);
        System.setProperty(SystemConfigKeyName.RESOURCE_SERVICE_SERVER_PORT.name(), resourceServerPort);
        System.setProperty(SystemConfigKeyName.REDIS_SERVICE_SERVER_PORT.name(), redisServerPort);

        log.info("Finished initiating SSL config");
    }
}
