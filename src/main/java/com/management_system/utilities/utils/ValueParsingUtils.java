package com.management_system.utilities.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ValueParsingUtils {
    // parse object to json string
    public String parseObjectToString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse res = new ApiResponse(ResponseResult.failed.name(), "Error at JSON parsing", HttpStatus.INTERNAL_SERVER_ERROR);
            return res.toString();
        }
    }


    // parse list object to list map
    public List<Map<String, Object>> parseObjectListToHashMapList(List<?> objects) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (objects == null || objects.isEmpty()) {
            return result;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (Object object : objects) {
            try {
                Map<String, Object> map = objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
                result.add(map);
            } catch (Exception e) {
                e.printStackTrace();
                result.add(Collections.emptyMap());
            }
        }

        return result;
    }


    // parse a string to ID string with given pattern
    public String parseStringToId(String spacePattern, boolean isUpperCase, String... str) {
        StringBuilder strBuilder = new StringBuilder();
        Pattern specialCharsPattern = Pattern.compile("[^a-zA-Z0-9\\s]");

        for (int i = 0; i < str.length; i++) {
            if (str[i] != null) {
                String normalized = Normalizer.normalize(str[i], Normalizer.Form.NFD).replaceAll("\\p{M}", "");

                String cleaned = specialCharsPattern.matcher(normalized).replaceAll("");

                strBuilder.append(isUpperCase
                        ? cleaned.trim().replace(" ", spacePattern).toUpperCase()
                        : cleaned.trim().replace(" ", spacePattern).toLowerCase()
                );

                if (i != str.length - 1) {
                    strBuilder.append(spacePattern);
                }
            }
        }

        System.out.println(strBuilder);

        return strBuilder.toString();
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
