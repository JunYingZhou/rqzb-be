package com.rqzb.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rqzb.system.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    void assignRoles(Long userId, List<Long> roleIds);

    List<Long> listRoleIds(Long userId);

    void assignPosts(Long userId, List<Long> postIds);

    List<Long> listPostIds(Long userId);
}
