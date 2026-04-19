package com.rqzb.system.controller;

import com.rqzb.system.entity.SysOperLog;
import com.rqzb.system.service.SysOperLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/oper-logs")
public class SysOperLogController extends BaseCrudController<SysOperLog> {

    public SysOperLogController(SysOperLogService operLogService) {
        super(operLogService);
    }
}
