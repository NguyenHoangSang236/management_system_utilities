package com.management_system.utilities.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class CheckUtilsTest {
    final List<String> TEST_VALID_EMAILS = List.of("sang123@gmail.com", "abc@company.com", "hehehe@yahoo.vn");
    final List<String> TEST_INVALID_EMAILS = List.of("username@.com", "afsafasg", "gasasfaas!gmail.com", ".aaa@gmail.com", "gvvv@gmail.com.", "@gmail.com");
    final List<String> TEST_SPECIAL_SIGN_TEXTS = List.of("sgsdg325&^&$^%&  ", "aa.hAAx");
    final List<String>  TEST_NO_SPECIAL_SIGN_TEXTS = List.of(" Nguyen Hoang Sang ");

    CheckUtils checkUtils = new CheckUtils();

    @Test
    public void validEmail_returnTrue() {
        for(String email: TEST_VALID_EMAILS) {
            assertTrue(checkUtils.isValidEmail(email), email);
        }
    }

    @Test
    public void invalidEmail_returnFalse() {
        for(String email: TEST_INVALID_EMAILS) {
            assertFalse(checkUtils.isValidEmail(email), email);
        }
    }

    @Test
    public void textHasSpecialSign_returnTrue() {
        for(String text: TEST_SPECIAL_SIGN_TEXTS) {
            assertTrue(checkUtils.hasSpecialSign(text), text);
        }
    }

    @Test
    public void textHasNoSpecialSign_returnFalse() {
        for(String text: TEST_NO_SPECIAL_SIGN_TEXTS) {
            assertFalse(checkUtils.hasSpecialSign(text), text);
        }
    }
}
