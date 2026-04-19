package com.rqzb.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rqzb.system.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    void assignMenus(Long roleId, List<Long> menuIds);

    List<Long> listMenuIds(Long roleId);
}
