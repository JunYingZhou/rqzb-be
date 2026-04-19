package com.rqzb.system.controller;

import com.rqzb.system.entity.SysDictType;
import com.rqzb.system.service.SysDictTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/dict-types")
public class SysDictTypeController extends BaseCrudController<SysDictType> {

    public SysDictTypeController(SysDictTypeService dictTypeService) {
        super(dictTypeService);
    }
}
