package com.management_system.utilities.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValueParsingTest {
    @Test
    public void testParseStringToId() {
        ValueParsingUtils valueParsingUtils = new ValueParsingUtils();

        Assertions.assertFalse(
                valueParsingUtils.parseStringToId("-", false, "Tea making machine") == null &&
                valueParsingUtils.parseStringToId("-", false, "Tea making machine").isBlank()
        );
    }
}
