package com.mockforge.mockforge.controller;

import com.mockforge.mockforge.model.HttpMethodType;
import com.mockforge.mockforge.model.MockEndpoint;
import com.mockforge.mockforge.service.MockEndpointService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicMockController {

    private final MockEndpointService mockEndpointService;

    public DynamicMockController(MockEndpointService mockEndpointService) {
        this.mockEndpointService = mockEndpointService;
    }

    @RequestMapping("/mock/**")
    public ResponseEntity<String> serveMock(HttpServletRequest request) {
        HttpMethodType method = HttpMethodType.valueOf(request.getMethod());
        String requestPath = request.getRequestURI().substring("/mock".length());
        MockEndpoint endpoint = mockEndpointService.findByMethodAndPath(method, requestPath);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, endpoint.getContentType());
        endpoint.getHeaders().forEach(headers::set);

        return ResponseEntity.status(endpoint.getStatusCode())
                .headers(headers)
                .body(endpoint.getResponseBody());
    }
}
