package com.management_system.utilities.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.entities.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ValueParsingUtils {
    // parse object to json string
    public String parseObjectToString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse res = new ApiResponse("failed", "Error at JSON parsing", HttpStatus.INTERNAL_SERVER_ERROR);
            return res.toString();
        }

    }
}
