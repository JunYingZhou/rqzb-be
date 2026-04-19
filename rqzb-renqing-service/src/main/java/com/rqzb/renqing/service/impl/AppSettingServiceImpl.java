package com.rqzb.renqing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.renqing.entity.AppSetting;
import com.rqzb.renqing.mapper.AppSettingMapper;
import com.rqzb.renqing.service.AppSettingService;
import org.springframework.stereotype.Service;

@Service
public class AppSettingServiceImpl extends ServiceImpl<AppSettingMapper, AppSetting> implements AppSettingService {
}
