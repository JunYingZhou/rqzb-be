package com.rqzb.system.controller;

import com.rqzb.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemHealthController {

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("system-service ok");
    }
}
