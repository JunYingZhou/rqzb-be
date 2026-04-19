package com.rqzb.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.entity.SysDictData;
import com.rqzb.system.service.SysDictDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system/dict-data")
public class SysDictDataController extends BaseCrudController<SysDictData> {

    private final SysDictDataService dictDataService;

    public SysDictDataController(SysDictDataService dictDataService) {
        super(dictDataService);
        this.dictDataService = dictDataService;
    }

    @GetMapping("/type/{dictType}")
    public ApiResponse<List<SysDictData>> listByType(@PathVariable String dictType) {
        return ApiResponse.ok(dictDataService.list(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 1)
                .orderByAsc(SysDictData::getDictSort)));
    }
}
