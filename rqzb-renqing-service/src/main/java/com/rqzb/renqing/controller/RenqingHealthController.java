package com.rqzb.renqing.controller;

import com.rqzb.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/renqing")
public class RenqingHealthController {

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("renqing-service ok");
    }
}
