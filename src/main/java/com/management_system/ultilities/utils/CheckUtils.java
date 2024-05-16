package com.management_system.ultilities.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CheckUtils {
    //Check email is valid or not
    //    It allows numeric values from 0 to 9.
    //    Both uppercase and lowercase letters from a to z are allowed.
    //    Allowed are underscore “_”, hyphen “-“, and dot “.”
    //    Dot isn't allowed at the start and end of the local part.
    //    Consecutive dots aren't allowed.
    //    For the local part, a maximum of 64 characters are allowed.
    public boolean isValidEmail(String email) {
        return patternMatches(email, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }


    //check if the text has special sign
    public boolean hasSpecialSign(String input) {
        char[] testCharArr = input.toCharArray();

        for (int i = 0; i < testCharArr.length - 1; i++) {
            if (testCharArr[i] != 32 && testCharArr[i] < 65 && testCharArr[i] > 90 && testCharArr[i] < 97 && testCharArr[i] > 122) {
                return true;
            }
        }

        return false;
    }


    //check if String matches using Regex
    public boolean patternMatches(String content, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(content)
                .matches();
    }
}
