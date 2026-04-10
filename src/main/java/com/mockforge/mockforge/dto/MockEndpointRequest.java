package com.mockforge.mockforge.dto;

import com.mockforge.mockforge.model.HttpMethodType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class MockEndpointRequest {

    @NotBlank
    private String name;

    @NotNull
    private HttpMethodType method;

    @NotBlank
    private String path;

    @Min(100)
    @Max(599)
    private int statusCode;

    @NotBlank
    private String contentType;

    @NotBlank
    private String responseBody;

    private Map<String, String> headers = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HttpMethodType getMethod() {
        return method;
    }

    public void setMethod(HttpMethodType method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
