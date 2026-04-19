package com.rqzb.renqing.controller;

import com.rqzb.common.ApiResponse;
import com.rqzb.renqing.dto.ThreadPoolExampleResponse;
import com.rqzb.renqing.service.RenqingThreadPoolExampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/renqing/thread-pool")
public class RenqingThreadPoolExampleController {

    private final RenqingThreadPoolExampleService threadPoolExampleService;

    public RenqingThreadPoolExampleController(RenqingThreadPoolExampleService threadPoolExampleService) {
        this.threadPoolExampleService = threadPoolExampleService;
    }

    @GetMapping("/example")
    public ApiResponse<ThreadPoolExampleResponse> example() {
        return ApiResponse.ok(threadPoolExampleService.buildOverview());
    }
}
