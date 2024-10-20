package com.management_system.utilities.config.web;

import com.management_system.utilities.config.meta_data.CredentialsKeyName;
import com.management_system.utilities.config.meta_data.SystemMetaData;
import com.management_system.utilities.utils.CredentialsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SslConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    final CredentialsUtils credentialsUtils;


    @Override
    public void customize(TomcatServletWebServerFactory server) {
        Ssl ssl = new Ssl();
        ssl.setKeyStore(SystemMetaData.SSL_KEYSTORE_FILE_PATH);
        ssl.setKeyStorePassword(credentialsUtils.getCredentials(CredentialsKeyName.SSL_KEYSTORE_PASSWORD));
        ssl.setKeyStoreType(credentialsUtils.getCredentials(CredentialsKeyName.SSL_KEYSTORE_TYPE));
        ssl.setKeyAlias(credentialsUtils.getCredentials(CredentialsKeyName.SSL_KEYSTORE_ALIAS));

        server.setSsl(ssl);
        server.setPort(Integer.parseInt(credentialsUtils.getCredentials(CredentialsKeyName.SERVER_PORT).trim()));
    }
}
