package com.management_system.utilities.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.entities.api.request.ApiRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoggingUtils {
    final ValueParsingUtils valueParsingUtils;


    public void logHttpServletRequest(HttpServletRequest request, Object body) {
        try {
            Object requestId = request.getAttribute(ConstantValue.REQUEST_ID);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
            Date requestTime = new Date();
            String bodyJsonString;

            if (body instanceof List<?> apiRequests) {
                bodyJsonString = convertListApiRequestToString((List<? extends ApiRequest>) apiRequests);
            } else if (body instanceof ApiRequest apiRequest) {
                bodyJsonString = convertApiRequestToString(apiRequest);
            } else bodyJsonString = body.toString();

            StringBuilder data = new StringBuilder();
            data.append("\n\n------------------------LOGGING REQUEST-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n")
                    .append("[TIME]: ").append(simpleDateFormat.format(requestTime)).append("\n")
                    .append("[METHOD]: ").append(request.getMethod()).append("\n")
                    .append("[PATH]: ").append(request.getRequestURI()).append("\n")
                    .append("[QUERIES]: ").append(request.getQueryString()).append("\n")
                    .append("[PAYLOAD]: ").append(bodyJsonString).append("\n");

            Enumeration<String> payloadNames = request.getHeaderNames();
            while (payloadNames.hasMoreElements()) {
                String key = payloadNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value).append("\n");
            }

            data.append("[HEADERS]: ").append("\n");

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value).append("\n");
            }
            data.append("------------------------END LOGGING REQUEST-----------------------------------\n\n");

            log.info(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logHttpServletRequest(HttpServletRequest request) {
        try {
            Object requestId = request.getAttribute(ConstantValue.REQUEST_ID);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
            Date requestTime = new Date();

            StringBuilder payload = new StringBuilder();
            String line;

            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    payload.append(line.trim());
                }
            }

            StringBuilder data = new StringBuilder();
            data.append("\n\n------------------------LOGGING REQUEST-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n")
                    .append("[TIME]: ").append(simpleDateFormat.format(requestTime)).append("\n")
                    .append("[METHOD]: ").append(request.getMethod()).append("\n")
                    .append("[PATH]: ").append(request.getRequestURI()).append("\n")
                    .append("[QUERIES]: ").append(request.getQueryString()).append("\n")
                    .append("[PAYLOAD]: ").append(payload).append("\n");

            Enumeration<String> payloadNames = request.getHeaderNames();
            while (payloadNames.hasMoreElements()) {
                String key = payloadNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value).append("\n");
            }

            data.append("[HEADERS]: ").append("\n");

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value).append("\n");
            }
            data.append("------------------------END LOGGING REQUEST-----------------------------------\n\n");

            log.info(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logHttpServletResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
        try {
            Object requestId = request.getAttribute(ConstantValue.REQUEST_ID);

            String data = "\n\n------------------------LOGGING RESPONSE-----------------------------------\n" +
                    "[REQUEST-ID]: " + requestId.toString() + "\n" +
                    "[URL]: " + request.getRequestURL() + "\n" +
                    "[BODY RESPONSE]: " + valueParsingUtils.parseObjectToString(body) +
                    "\n------------------------END LOGGING RESPONSE-----------------------------------\n";

            log.info(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logThirdPartyRequestAndResponse(String url, HttpHeaders headers, Map<String, Object> body, ResponseEntity response) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
            Date requestTime = new Date();

            StringBuilder data = new StringBuilder();
            data.append("\n\n------------------------LOGGING THIRD-PARTY REQUEST-----------------------------------\n")
                    .append("[TIME]: ").append(simpleDateFormat.format(requestTime)).append("\n")
                    .append("[METHOD]: ").append(body == null ? "GET" : "POST").append("\n")
                    .append("[PATH]: ").append(url).append("\n")
                    .append("[PAYLOAD]: ").append(body != null ? body.toString() : null).append("\n");

            data.append("[HEADERS]: ").append("\n");

            headers.forEach((key, value) -> {
                data.append("---").append(key).append(" : ").append(value).append("\n");
            });
            data.append("------------------------END LOGGING THIRD-PARTY REQUEST-----------------------------------\n\n");

            log.info(data.toString());

            Object requestId = response.getHeaders().get(ConstantValue.REQUEST_ID);

            String resData = "\n\n------------------------LOGGING THIRD-PARTY RESPONSE-----------------------------------\n" +
                    "[BODY RESPONSE]: " + valueParsingUtils.parseObjectToString(response.getBody()) +
                    "\n------------------------END LOGGING THIRD-PARTY RESPONSE-----------------------------------\n";

            log.info(resData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertListApiRequestToString(List<? extends ApiRequest> requests) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(requests);
    }

    private String convertApiRequestToString(ApiRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(request);
    }
}
