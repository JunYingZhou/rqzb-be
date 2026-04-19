package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rqzb.common.ApiResponse;
import com.rqzb.common.PageQuery;
import com.rqzb.common.PageResult;
import com.rqzb.renqing.entity.GiftRecordWithPerson;
import com.rqzb.renqing.service.GiftRecordWithPersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/gift-record-views")
public class GiftRecordViewController {

    private final GiftRecordWithPersonService giftRecordWithPersonService;

    public GiftRecordViewController(GiftRecordWithPersonService giftRecordWithPersonService) {
        this.giftRecordWithPersonService = giftRecordWithPersonService;
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<GiftRecordWithPerson>> page(PageQuery query) {
        Page<GiftRecordWithPerson> page = giftRecordWithPersonService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.<GiftRecordWithPerson>lambdaQuery().orderByDesc(GiftRecordWithPerson::getEventDate));
        return ApiResponse.ok(PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords()));
    }

    @GetMapping("/list")
    public ApiResponse<List<GiftRecordWithPerson>> list() {
        return ApiResponse.ok(giftRecordWithPersonService.list(Wrappers.<GiftRecordWithPerson>lambdaQuery()
                .orderByDesc(GiftRecordWithPerson::getEventDate)));
    }

    @GetMapping("/{id}")
    public ApiResponse<GiftRecordWithPerson> detail(@PathVariable Long id) {
        GiftRecordWithPerson record = giftRecordWithPersonService.getById(id);
        if (record == null) {
            return ApiResponse.fail(404, "record not found");
        }
        return ApiResponse.ok(record);
    }

    @GetMapping("/person/{personId}")
    public ApiResponse<List<GiftRecordWithPerson>> listByPerson(@PathVariable Long personId) {
        return ApiResponse.ok(giftRecordWithPersonService.list(Wrappers.<GiftRecordWithPerson>lambdaQuery()
                .eq(GiftRecordWithPerson::getPersonId, personId)
                .orderByDesc(GiftRecordWithPerson::getEventDate)));
    }
}
