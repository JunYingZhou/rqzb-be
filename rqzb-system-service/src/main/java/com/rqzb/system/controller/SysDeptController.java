package com.rqzb.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.entity.SysDept;
import com.rqzb.system.service.SysDeptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system/depts")
public class SysDeptController extends BaseCrudController<SysDept> {

    private final SysDeptService deptService;

    public SysDeptController(SysDeptService deptService) {
        super(deptService);
        this.deptService = deptService;
    }

    @GetMapping("/tree")
    public ApiResponse<List<SysDept>> tree() {
        return ApiResponse.ok(deptService.list(Wrappers.<SysDept>lambdaQuery()
                .orderByAsc(SysDept::getParentId, SysDept::getOrderNum)));
    }
}
