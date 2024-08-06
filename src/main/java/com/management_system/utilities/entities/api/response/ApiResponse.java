package com.management_system.utilities.entities.api.response;

import com.management_system.utilities.core.usecase.UseCase;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse implements UseCase.OutputValue, Serializable {
    private String result;
    private Object content;
    private String message;
    private String jwt;
    private String refreshToken;
    private HttpStatus status;

    public ApiResponse(String result, Object content, HttpStatus status) {
        this.result = result;
        this.content = content;
        this.status = status;
    }

    public ApiResponse(String result, Object content, String message) {
        this.result = result;
        this.content = content;
        this.message = message;
    }

    public ApiResponse(String result, Object content) {
        this.result = result;
        this.content = content;
    }
}
