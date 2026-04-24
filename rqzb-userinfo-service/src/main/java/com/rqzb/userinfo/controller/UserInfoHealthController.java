package com.rqzb.userinfo.controller;

import com.rqzb.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoHealthController {

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.ok("userinfo-service ok");
    }
}
