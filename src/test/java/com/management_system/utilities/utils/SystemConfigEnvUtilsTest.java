package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class SystemConfigEnvUtilsTest {
    final static SystemConfigKeyName[] VALID_KEYS = SystemConfigKeyName.values();

    SystemConfigEnvUtils credentialsUtils = new SystemConfigEnvUtils();

    @Test
    public void getValidCredentialsKey_returnValue() {
        for(SystemConfigKeyName key: VALID_KEYS) {
            if(!key.equals(SystemConfigKeyName.TEST_KEY)) {
                assertNotNull(credentialsUtils.getCredentials(key), key.name());
            }
        }
    }

    @Test
    public void getInvalidCredentialsKey_returnNull() {
        assertNull(credentialsUtils.getCredentials(SystemConfigKeyName.TEST_KEY), SystemConfigKeyName.TEST_KEY.name());
    }
}
