package com.swygbro.housemate.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(originPatterns = "*")
@Api("HealthCheck 관련 API 입니다.")
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("/")
    public String hello() {
        return "Hello ZipHap Server!!!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Health ZipHap Server";
    }
}
