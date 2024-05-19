package com.management_system.utilities.entities;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse implements Serializable {
    private String result;
    private Object content;
    private String message;
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

    @Override
    public String toString() {
        return "ApiResponse{" +
                "result='" + result + '\'' +
                ", content=" + content +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
