package com.management_system.utilities.config.web;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import com.management_system.utilities.config.meta_data.SystemMetaData;
import com.management_system.utilities.utils.SystemConfigEnvUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    final SystemConfigEnvUtils credentialsUtils;


    @Override
    public void customize(TomcatServletWebServerFactory server) {
        Ssl ssl = new Ssl();
        ssl.setKeyStore(SystemMetaData.SSL_KEYSTORE_FILE_PATH);
        ssl.setKeyStorePassword(credentialsUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD));
        ssl.setKeyStoreType(credentialsUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_TYPE));
        ssl.setKeyAlias(credentialsUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_ALIAS));

        server.setSsl(ssl);
        server.setPort(Integer.parseInt(credentialsUtils.getCredentials(SystemConfigKeyName.SERVER_PORT).trim()));
    }
}
