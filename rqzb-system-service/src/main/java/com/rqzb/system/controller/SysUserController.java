package com.rqzb.system.controller;

import com.rqzb.common.ApiResponse;
import com.rqzb.common.IdsRequest;
import com.rqzb.common.PasswordUtils;
import com.rqzb.system.entity.SysUser;
import com.rqzb.system.service.SysUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/system/users")
public class SysUserController extends BaseCrudController<SysUser> {

    private final SysUserService userService;

    public SysUserController(SysUserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    protected void beforeCreate(SysUser entity) {
        if (!StringUtils.hasText(entity.getUsername())) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (!StringUtils.hasText(entity.getNickname())) {
            entity.setNickname(entity.getUsername());
        }
        if (!StringUtils.hasText(entity.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }
        entity.setPassword(PasswordUtils.hash(entity.getPassword()));
    }

    @Override
    protected void beforeUpdate(SysUser entity) {
        if (StringUtils.hasText(entity.getPassword())) {
            entity.setPassword(PasswordUtils.hash(entity.getPassword()));
        } else {
            entity.setPassword(null);
        }
    }

    @PutMapping("/{id}/roles")
    public ApiResponse<Void> assignRoles(@PathVariable Long id, @RequestBody(required = false) IdsRequest request) {
        userService.assignRoles(id, request == null ? Collections.emptyList() : request.getIds());
        return ApiResponse.ok();
    }

    @GetMapping("/{id}/roles")
    public ApiResponse<List<Long>> roleIds(@PathVariable Long id) {
        return ApiResponse.ok(userService.listRoleIds(id));
    }

    @PutMapping("/{id}/posts")
    public ApiResponse<Void> assignPosts(@PathVariable Long id, @RequestBody(required = false) IdsRequest request) {
        userService.assignPosts(id, request == null ? Collections.emptyList() : request.getIds());
        return ApiResponse.ok();
    }

    @GetMapping("/{id}/posts")
    public ApiResponse<List<Long>> postIds(@PathVariable Long id) {
        return ApiResponse.ok(userService.listPostIds(id));
    }
}
