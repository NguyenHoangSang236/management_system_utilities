package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SystemConfigEnvUtilsTest {
    final static SystemConfigKeyName[] VALID_KEYS = SystemConfigKeyName.values();
    SystemConfigEnvUtils credentialsUtils = new SystemConfigEnvUtils();


    static Stream<Arguments> providedSystemEnvData() {
        return Stream.of(
                Arguments.of("SECRET_SIGNING_KEY", "bfe5592125967470d11815718f8af95ca5b1a4214926dfa028b290002ebcd158"),
                Arguments.of("FIREBASE_PRIVATE_KEY_FILE_PATH", "managementsystem-firebase-private-key.json"),
                Arguments.of("FIREBASE_STORAGE_BUCKET_NAME", "managementsystem-8bbd0.appspot.com"),
                Arguments.of("SSL_KEYSTORE_PASSWORD", "hoangsang236"),
                Arguments.of("SSL_KEYSTORE_TYPE", "PKCS12"),
                Arguments.of("SSL_KEYSTORE_ALIAS", "tomcat"),
                Arguments.of("SSL_FILE_DIRECTORY", "keystore.p12"),
                Arguments.of("SERVER_PORT", "1234"),
                Arguments.of("EUREKA_SERVER_PORT", "8761"),
                Arguments.of("RESOURCE_SERVICE_SERVER_PORT", "8082"),
                Arguments.of("AUTHENTICATION_SERVICE_SERVER_PORT", "8081"),
                Arguments.of("REDIS_SERVICE_SERVER_PORT", "8079"),
                Arguments.of("SECURITY_IGNORED_REQUEST_MATCHERS", "/redis/unauthen/**, /resource/unauthen/**, /authentication/unauthen/**, /eureka/**, /error/**")
        );
    }


    @ParameterizedTest
    @MethodSource("providedSystemEnvData")
    public void getValidCredentialsKey_returnValue(String key, String value) {
        assertEquals(credentialsUtils.getCredentials(SystemConfigKeyName.valueOf(key)), value);
    }
}
