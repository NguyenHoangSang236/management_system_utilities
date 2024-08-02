package com.management_system.utilities.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is5xxServerError()) {
            return new RuntimeException(response.reason());
        } else if (responseStatus.is4xxClientError()) {
            return new RuntimeException(response.reason());
        } else {
            return new Exception("Generic exception");
        }
    }
}
