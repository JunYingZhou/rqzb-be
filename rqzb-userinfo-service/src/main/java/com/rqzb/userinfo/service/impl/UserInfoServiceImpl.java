package com.rqzb.userinfo.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.rqzb.userinfo.dto.UserInfoResponse;
import com.rqzb.userinfo.entity.SysUser;
import com.rqzb.userinfo.mapper.SysUserMapper;
import com.rqzb.userinfo.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final SysUserMapper sysUserMapper;

    public UserInfoServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public UserInfoResponse getCurrentUserInfo() {
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        return getUserInfoById(userId);
    }

    @Override
    public UserInfoResponse getUserInfoById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user id must not be null");
        }

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            throw new IllegalArgumentException("user not found or disabled");
        }

        return new UserInfoResponse(
                user.getId(),
                user.getDeptId(),
                user.getUsername(),
                user.getNickname(),
                user.getRealName(),
                user.getEmail(),
                user.getPhone(),
                user.getSex(),
                user.getAvatar(),
                user.getStatus()
        );
    }
}
