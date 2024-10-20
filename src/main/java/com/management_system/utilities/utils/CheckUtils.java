package com.management_system.utilities.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CheckUtils {
    // Check email is valid or not
    //    It allows numeric values from 0 to 9.
    //    Both uppercase and lowercase letters from a to z are allowed.
    //    Allowed are underscore “_”, hyphen “-“, and dot “.”
    //    Dot isn't allowed at the start and end of the local part.
    //    Consecutive dots aren't allowed.
    //    For the local part, a maximum of 64 characters are allowed.
    public boolean isValidEmail(String email) {
        return patternMatches(
                email,
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
        );
    }


    // check if the text has special sign (not A-Z, a-z and space)
    public boolean hasSpecialSign(String input) {
        char[] testCharArr = input.trim().toCharArray();

        for (char c : testCharArr) {
            if (c != 32 && (c < 65 || (c > 90 && c < 97) || c > 122)) {
                return true;
            }
        }
        return false;
    }


    // check if String matches using Regex
    public boolean patternMatches(String content, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(content)
                .matches();
    }


    // check if phone's format is valid with VN format
    // TODO: Upgrade it for all countries' format
    public boolean isValidPhoneNumber(String phone) {
        return (phone.length() == 10 && phone.indexOf("0") == 0) || (phone.length() == 12 && phone.indexOf("+84") == 0);
    }
}
