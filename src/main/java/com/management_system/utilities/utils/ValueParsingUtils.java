package com.management_system.utilities.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

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


    // parse a string to ID string with given pattern
    public String parseStringToId(String str, String spacePattern, boolean isUpperCase) {
        String idStr = isUpperCase ? str.trim().toUpperCase() : str.trim().toLowerCase();
        return idStr.replace(" ", spacePattern);
    }


    // parse object to Map
    public Map<String, Object> parseMongoDbMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(obj, Map.class);

        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                Map<String, Object> subMap = (Map<String, Object>) map.get(key);

                for (String k : subMap.keySet()) {
                    String newKey = key + "." + k;
                    map.put(newKey, subMap.get(k));
                }

                map.remove(key);
            }
        }

        return map;
    }


    // convert snake case text to camel text
    public String fromSnakeCaseToCamel(String text) {
        String[] strArr = text.split("");
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals(" ")) {
                return null;
            }

            if (!strArr[i].equals("-") && !strArr[i].equals("_")) {
                strBuilder.append(strArr[i]);
            } else {
                i++;
                strBuilder.append(strArr[i].toUpperCase());
            }
        }

        return strBuilder.toString();
    }
}
