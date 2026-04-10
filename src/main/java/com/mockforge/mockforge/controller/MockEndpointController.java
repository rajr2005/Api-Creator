package com.mockforge.mockforge.controller;

import com.mockforge.mockforge.dto.MockEndpointRequest;
import com.mockforge.mockforge.model.MockEndpoint;
import com.mockforge.mockforge.service.MockEndpointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mock-endpoints")
public class MockEndpointController {

    private final MockEndpointService mockEndpointService;

    public MockEndpointController(MockEndpointService mockEndpointService) {
        this.mockEndpointService = mockEndpointService;
    }

    @GetMapping
    public List<MockEndpoint> findAll() {
        return mockEndpointService.findAll();
    }

    @GetMapping("/{id}")
    public MockEndpoint findById(@PathVariable String id) {
        return mockEndpointService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MockEndpoint create(@Valid @RequestBody MockEndpointRequest request) {
        return mockEndpointService.create(request);
    }

    @PutMapping("/{id}")
    public MockEndpoint update(@PathVariable String id, @Valid @RequestBody MockEndpointRequest request) {
        return mockEndpointService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        mockEndpointService.delete(id);
    }
}
