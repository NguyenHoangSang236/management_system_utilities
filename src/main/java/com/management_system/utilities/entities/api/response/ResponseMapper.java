package com.management_system.utilities.entities.api.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseMapper {
    public static ResponseEntity<ApiResponse> map(ApiResponse response) {
        HttpStatus status = response.getStatus();

        return switch (status.name()) {
            case "OK" -> ResponseEntity.ok(response);
            case "INTERNAL_SERVER_ERROR" -> ResponseEntity.internalServerError().body(response);
            case "BAD_REQUEST" -> ResponseEntity.badRequest().body(response);
            case "UNAUTHORIZED", "NO_CONTENT" -> new ResponseEntity<>(response, status);
            default -> ResponseEntity.notFound().build();
        };
    }
}
