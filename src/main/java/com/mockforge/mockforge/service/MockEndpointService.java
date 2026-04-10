package com.mockforge.mockforge.service;

import com.mockforge.mockforge.dto.MockEndpointRequest;
import com.mockforge.mockforge.model.HttpMethodType;
import com.mockforge.mockforge.model.MockEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockEndpointService {

    private final Map<String, MockEndpoint> endpoints = new ConcurrentHashMap<>();

    public List<MockEndpoint> findAll() {
        return endpoints.values().stream()
                .sorted(Comparator.comparing(MockEndpoint::getCreatedAt).reversed())
                .toList();
    }

    public MockEndpoint create(MockEndpointRequest request) {
        String normalizedPath = normalizePath(request.getPath());
        boolean duplicateExists = endpoints.values().stream()
                .anyMatch(endpoint -> endpoint.getMethod() == request.getMethod()
                        && endpoint.getPath().equals(normalizedPath));

        if (duplicateExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A mock endpoint already exists for this method and path.");
        }

        MockEndpoint endpoint = new MockEndpoint();
        endpoint.setId(UUID.randomUUID().toString());
        endpoint.setName(request.getName().trim());
        endpoint.setMethod(request.getMethod());
        endpoint.setPath(normalizedPath);
        endpoint.setStatusCode(request.getStatusCode());
        endpoint.setContentType(request.getContentType().trim());
        endpoint.setResponseBody(request.getResponseBody());
        endpoint.setHeaders(request.getHeaders());
        endpoint.setCreatedAt(Instant.now());

        endpoints.put(endpoint.getId(), endpoint);
        return endpoint;
    }

    public MockEndpoint update(String id, MockEndpointRequest request) {
        MockEndpoint existing = getById(id);
        String normalizedPath = normalizePath(request.getPath());

        boolean duplicateExists = endpoints.values().stream()
                .anyMatch(endpoint -> !endpoint.getId().equals(id)
                        && endpoint.getMethod() == request.getMethod()
                        && endpoint.getPath().equals(normalizedPath));

        if (duplicateExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A mock endpoint already exists for this method and path.");
        }

        existing.setName(request.getName().trim());
        existing.setMethod(request.getMethod());
        existing.setPath(normalizedPath);
        existing.setStatusCode(request.getStatusCode());
        existing.setContentType(request.getContentType().trim());
        existing.setResponseBody(request.getResponseBody());
        existing.setHeaders(request.getHeaders());
        return existing;
    }

    public MockEndpoint getById(String id) {
        MockEndpoint endpoint = endpoints.get(id);
        if (endpoint == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mock endpoint not found.");
        }
        return endpoint;
    }

    public void delete(String id) {
        MockEndpoint removed = endpoints.remove(id);
        if (removed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mock endpoint not found.");
        }
    }

    public MockEndpoint findByMethodAndPath(HttpMethodType method, String path) {
        String normalizedPath = normalizePath(path);
        return endpoints.values().stream()
                .filter(endpoint -> endpoint.getMethod() == method && endpoint.getPath().equals(normalizedPath))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No mock API exists for this request."));
    }

    private String normalizePath(String path) {
        String trimmed = path.trim();
        if (trimmed.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path cannot be empty.");
        }
        return trimmed.startsWith("/") ? trimmed : "/" + trimmed;
    }
}
