package com.harness.demo.controller;

import com.harness.demo.model.AppConfig;
import com.harness.demo.service.AppConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {
    private final AppConfigService service;

    public AppConfigController(AppConfigService service) {
        this.service = service;
    }

    @GetMapping
    public AppConfig get() {
        return service.getConfig();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateRequest request) {
        try {
            return ResponseEntity.ok(service.update(request.heading(), request.subtitle()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        }
    }

    public record UpdateRequest(String heading, String subtitle) {}
    public record ErrorResponse(String error) {}
}
