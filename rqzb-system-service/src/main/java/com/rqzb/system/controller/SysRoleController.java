package com.rqzb.system.controller;

import com.rqzb.common.ApiResponse;
import com.rqzb.common.IdsRequest;
import com.rqzb.system.entity.SysRole;
import com.rqzb.system.service.SysRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/system/roles")
public class SysRoleController extends BaseCrudController<SysRole> {

    private final SysRoleService roleService;

    public SysRoleController(SysRoleService roleService) {
        super(roleService);
        this.roleService = roleService;
    }

    @PutMapping("/{id}/menus")
    public ApiResponse<Void> assignMenus(@PathVariable Long id, @RequestBody(required = false) IdsRequest request) {
        roleService.assignMenus(id, request == null ? Collections.emptyList() : request.getIds());
        return ApiResponse.ok();
    }

    @GetMapping("/{id}/menus")
    public ApiResponse<List<Long>> menuIds(@PathVariable Long id) {
        return ApiResponse.ok(roleService.listMenuIds(id));
    }
}
