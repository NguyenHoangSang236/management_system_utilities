package com.management_system.utilities.utils;

import com.management_system.utilities.config.meta_data.CredentialsKeyName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class CredentialsUtilsTest {
    final static CredentialsKeyName[] VALID_KEYS = CredentialsKeyName.values();

    CredentialsUtils credentialsUtils = new CredentialsUtils();

    @Test
    public void getValidCredentialsKey_returnValue() {
        for(CredentialsKeyName key: VALID_KEYS) {
            if(!key.equals(CredentialsKeyName.TEST_KEY)) {
                assertNotNull(credentialsUtils.getCredentials(key), key.name());
            }
        }
    }

    @Test
    public void getInvalidCredentialsKey_returnNull() {
        assertNull(credentialsUtils.getCredentials(CredentialsKeyName.TEST_KEY), CredentialsKeyName.TEST_KEY.name());
    }
}
