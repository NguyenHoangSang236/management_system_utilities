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

    /**
     * Log information of Http Servlet request
     *
     * @param request The Http Servlet request
     * @param body    The request body
     */
    public void logHttpServletRequest(HttpServletRequest request, Object body) {
        try {
            Object requestId = request.getAttribute(ConstantValue.REQUEST_ID);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
            Date requestTime = new Date();
            String bodyJsonString;

            if (body == null) {
                bodyJsonString = null;
            } else if (body instanceof List<?> apiRequests) {
                bodyJsonString = convertListApiRequestToString((List<? extends ApiRequest>) apiRequests);
            } else if (body instanceof ApiRequest apiRequest) {
                bodyJsonString = convertApiRequestToString(apiRequest);
            } else bodyJsonString = body.toString();

            StringBuilder data = new StringBuilder();
            data.append("\n\n------------------------LOGGING REQUEST-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n")
                    .append("[TIME]: ").append(simpleDateFormat.format(requestTime)).append("\n")
                    .append("[METHOD]: ").append(request.getMethod()).append("\n")
                    .append("[URL]: ").append(request.getRequestURI()).append("\n")
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


    /**
     * Log information of Http Servlet request
     *
     * @param request The Http Servlet Request
     */
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
                    .append("[URL]: ").append(request.getRequestURI()).append("\n")
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

    /**
     * Log information of Http Servlet response
     *
     * @param request  The Http Servlet request
     * @param response The Http Servlet response
     * @param body     The response body
     */
    public void logHttpServletResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
        try {
            Object requestId = request.getAttribute(ConstantValue.REQUEST_ID);
            int statusCode = response.getStatus();

            String data = "\n\n------------------------LOGGING RESPONSE-----------------------------------\n" +
                    "[REQUEST-ID]: " + requestId.toString() + "\n" +
                    "[URL]: " + request.getRequestURL() + "\n" +
                    "[STATUS CODE]: " + statusCode + "\n" +
                    "[BODY RESPONSE]: " + valueParsingUtils.parseObjectToString(body) +
                    "\n------------------------END LOGGING RESPONSE-----------------------------------\n";

            log.info(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Log information of third party request and response
     *
     * @param url         URL of the third party API
     * @param headers     Request header information
     * @param response    The response of the third party API
     * @param requestBody The request body
     */
    public void logThirdPartyRequestAndResponse(String url, HttpHeaders headers, Map<String, Object> requestBody, ResponseEntity response) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy");
            Date requestTime = new Date();

            StringBuilder data = new StringBuilder();
            data.append("\n\n------------------------LOGGING THIRD-PARTY REQUEST-----------------------------------\n")
                    .append("[TIME]: ").append(simpleDateFormat.format(requestTime)).append("\n")
                    .append("[METHOD]: ").append(requestBody == null ? "GET" : "POST").append("\n")
                    .append("[URL]: ").append(url).append("\n")
                    .append("[PAYLOAD]: ").append(requestBody != null ? requestBody.toString() : null).append("\n")
                    .append("[HEADERS]: ").append("\n");

            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                 data.append("---").append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }

            data.append("------------------------END LOGGING THIRD-PARTY REQUEST-----------------------------------\n\n");

            log.info(data.toString());

            data = new StringBuilder();

            data.append("\n\n------------------------LOGGING THIRD-PARTY RESPONSE-----------------------------------\n")
                    .append("[URL]: ").append(url).append("\n")
                    .append("[BODY RESPONSE]: ").append(valueParsingUtils.parseObjectToString(response.getBody())).append("\n")
                    .append("[STATUS CODE]: ").append(response.getStatusCode()).append("\n")
                    .append("------------------------END LOGGING THIRD-PARTY RESPONSE-----------------------------------\n");

            log.info(data.toString());
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
