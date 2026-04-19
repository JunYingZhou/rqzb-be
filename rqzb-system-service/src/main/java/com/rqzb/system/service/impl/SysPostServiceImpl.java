package com.rqzb.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.system.entity.SysPost;
import com.rqzb.system.mapper.SysPostMapper;
import com.rqzb.system.service.SysPostService;
import org.springframework.stereotype.Service;

@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {
}
