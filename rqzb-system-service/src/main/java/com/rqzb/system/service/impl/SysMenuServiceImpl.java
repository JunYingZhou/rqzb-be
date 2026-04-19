package com.rqzb.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.system.entity.SysMenu;
import com.rqzb.system.mapper.SysMenuMapper;
import com.rqzb.system.service.SysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
}
