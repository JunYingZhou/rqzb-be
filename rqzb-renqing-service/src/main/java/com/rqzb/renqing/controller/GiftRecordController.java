package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.renqing.entity.GiftRecord;
import com.rqzb.renqing.service.GiftRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/gift-records")
public class GiftRecordController extends BaseCrudController<GiftRecord> {

    private final GiftRecordService giftRecordService;

    public GiftRecordController(GiftRecordService giftRecordService) {
        super(giftRecordService);
        this.giftRecordService = giftRecordService;
    }

    @GetMapping("/person/{personId}")
    public ApiResponse<List<GiftRecord>> listByPerson(@PathVariable Long personId) {
        return ApiResponse.ok(giftRecordService.list(Wrappers.<GiftRecord>lambdaQuery()
                .eq(GiftRecord::getPersonId, personId)
                .orderByDesc(GiftRecord::getEventDate)));
    }

    @GetMapping("/direction/{direction}")
    public ApiResponse<List<GiftRecord>> listByDirection(@PathVariable String direction) {
        return ApiResponse.ok(giftRecordService.list(Wrappers.<GiftRecord>lambdaQuery()
                .eq(GiftRecord::getDirection, direction)
                .orderByDesc(GiftRecord::getEventDate)));
    }
}
