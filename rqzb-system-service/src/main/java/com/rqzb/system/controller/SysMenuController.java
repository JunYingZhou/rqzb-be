package com.rqzb.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.entity.SysMenu;
import com.rqzb.system.service.SysMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/system/menus")
public class SysMenuController extends BaseCrudController<SysMenu> {

    private final SysMenuService menuService;

    public SysMenuController(SysMenuService menuService) {
        super(menuService);
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    public ApiResponse<List<SysMenu>> tree() {
        return ApiResponse.ok(menuService.list(Wrappers.<SysMenu>lambdaQuery()
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum)));
    }
}
