package com.rqzb.system.controller;

import com.rqzb.system.entity.SysLoginLog;
import com.rqzb.system.service.SysLoginLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/login-logs")
public class SysLoginLogController extends BaseCrudController<SysLoginLog> {

    public SysLoginLogController(SysLoginLogService loginLogService) {
        super(loginLogService);
    }
}
