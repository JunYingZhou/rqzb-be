package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rqzb.common.ApiResponse;
import com.rqzb.common.PageQuery;
import com.rqzb.common.PageResult;
import com.rqzb.renqing.entity.AppSetting;
import com.rqzb.renqing.service.AppSettingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renqing/settings")
public class AppSettingController {

    private final AppSettingService appSettingService;

    public AppSettingController(AppSettingService appSettingService) {
        this.appSettingService = appSettingService;
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<AppSetting>> page(PageQuery query) {
        Page<AppSetting> page = appSettingService.page(new Page<>(query.getCurrent(), query.getSize()));
        return ApiResponse.ok(PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords()));
    }

    @GetMapping("/list")
    public ApiResponse<List<AppSetting>> list() {
        return ApiResponse.ok(appSettingService.list());
    }

    @GetMapping("/{settingKey}")
    public ApiResponse<AppSetting> detail(@PathVariable String settingKey) {
        AppSetting setting = appSettingService.getById(settingKey);
        if (setting == null) {
            return ApiResponse.fail(404, "setting not found");
        }
        return ApiResponse.ok(setting);
    }

    @PostMapping
    public ApiResponse<AppSetting> create(@RequestBody AppSetting setting) {
        appSettingService.save(setting);
        return ApiResponse.ok(setting);
    }

    @PutMapping("/{settingKey}")
    public ApiResponse<AppSetting> update(@PathVariable String settingKey, @RequestBody AppSetting setting) {
        setting.setSettingKey(settingKey);
        boolean updated = appSettingService.saveOrUpdate(setting);
        if (!updated) {
            return ApiResponse.fail(404, "setting not found");
        }
        return ApiResponse.ok(setting);
    }

    @DeleteMapping("/{settingKey}")
    public ApiResponse<Void> delete(@PathVariable String settingKey) {
        boolean removed = appSettingService.removeById(settingKey);
        if (!removed) {
            return ApiResponse.fail(404, "setting not found");
        }
        return ApiResponse.ok();
    }
}
