package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.renqing.entity.RenqingYearlySummary;
import com.rqzb.renqing.service.RenqingYearlySummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/summary")
public class RenqingSummaryController {

    private final RenqingYearlySummaryService yearlySummaryService;

    public RenqingSummaryController(RenqingYearlySummaryService yearlySummaryService) {
        this.yearlySummaryService = yearlySummaryService;
    }

    @GetMapping("/yearly")
    public ApiResponse<List<RenqingYearlySummary>> yearly() {
        return ApiResponse.ok(yearlySummaryService.list(Wrappers.<RenqingYearlySummary>lambdaQuery()
                .orderByDesc(RenqingYearlySummary::getRecordYear)));
    }

    @GetMapping("/yearly/{year}")
    public ApiResponse<RenqingYearlySummary> yearlyDetail(@PathVariable Integer year) {
        RenqingYearlySummary summary = yearlySummaryService.getById(year);
        if (summary == null) {
            return ApiResponse.fail(404, "summary not found");
        }
        return ApiResponse.ok(summary);
    }
}
