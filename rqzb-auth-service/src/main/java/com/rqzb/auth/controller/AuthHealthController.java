package com.rqzb.auth.controller;

import com.rqzb.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthHealthController {

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("auth-service ok");
    }
}
