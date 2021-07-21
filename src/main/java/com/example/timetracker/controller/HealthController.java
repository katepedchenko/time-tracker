package com.example.timetracker.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @ApiOperation(value = "Verify if service is up")
    @GetMapping("/health")
    public String checkHealth() {
        return "I am alive!";
    }
}
