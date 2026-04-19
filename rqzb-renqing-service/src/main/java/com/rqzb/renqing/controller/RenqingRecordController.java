package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.renqing.entity.RenqingRecord;
import com.rqzb.renqing.service.RenqingRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/records")
public class RenqingRecordController extends BaseCrudController<RenqingRecord> {

    private final RenqingRecordService renqingRecordService;

    public RenqingRecordController(RenqingRecordService renqingRecordService) {
        super(renqingRecordService);
        this.renqingRecordService = renqingRecordService;
    }

    @GetMapping("/name/{name}")
    public ApiResponse<List<RenqingRecord>> listByName(@PathVariable String name) {
        return ApiResponse.ok(renqingRecordService.list(Wrappers.<RenqingRecord>lambdaQuery()
                .like(RenqingRecord::getName, name)
                .orderByDesc(RenqingRecord::getRecordDate)));
    }

    @GetMapping("/type/{type}")
    public ApiResponse<List<RenqingRecord>> listByType(@PathVariable String type) {
        return ApiResponse.ok(renqingRecordService.list(Wrappers.<RenqingRecord>lambdaQuery()
                .eq(RenqingRecord::getType, type)
                .orderByDesc(RenqingRecord::getRecordDate)));
    }

    @GetMapping("/occasion/{occasion}")
    public ApiResponse<List<RenqingRecord>> listByOccasion(@PathVariable String occasion) {
        return ApiResponse.ok(renqingRecordService.list(Wrappers.<RenqingRecord>lambdaQuery()
                .eq(RenqingRecord::getOccasion, occasion)
                .orderByDesc(RenqingRecord::getRecordDate)));
    }
}
