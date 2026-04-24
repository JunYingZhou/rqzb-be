package com.rqzb.userinfo.controller;

import com.rqzb.common.ApiResponse;
import com.rqzb.userinfo.dto.UserInfoResponse;
import com.rqzb.userinfo.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> me() {
        return ApiResponse.ok(userInfoService.getCurrentUserInfo());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserInfoResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(userInfoService.getUserInfoById(id));
    }
}
