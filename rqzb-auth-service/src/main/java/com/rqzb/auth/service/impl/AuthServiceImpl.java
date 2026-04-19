package com.rqzb.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rqzb.auth.dto.CurrentUserResponse;
import com.rqzb.auth.dto.LoginRequest;
import com.rqzb.auth.dto.LoginResponse;
import com.rqzb.auth.entity.SysLoginLog;
import com.rqzb.auth.entity.SysUser;
import com.rqzb.auth.mapper.AuthPermissionMapper;
import com.rqzb.auth.mapper.SysLoginLogMapper;
import com.rqzb.auth.mapper.SysUserMapper;
import com.rqzb.auth.service.AuthService;
import com.rqzb.common.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;

    private final AuthPermissionMapper permissionMapper;

    private final SysLoginLogMapper loginLogMapper;

    public AuthServiceImpl(SysUserMapper userMapper,
                           AuthPermissionMapper permissionMapper,
                           SysLoginLogMapper loginLogMapper) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.loginLogMapper = loginLogMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest servletRequest) {
        SysUser user = userMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, request.getUsername())
                .eq(SysUser::getDeleted, 0)
                .last("LIMIT 1"));

        if (user == null || !Integer.valueOf(1).equals(user.getStatus())
                || !PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            saveLoginLog(request.getUsername(), servletRequest, 0, "用户名或密码错误");
            throw new IllegalArgumentException("用户名或密码错误");
        }

        StpUtil.login(user.getId());
        String ip = getClientIp(servletRequest);
        userMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getLoginIp, ip)
                .set(SysUser::getLoginTime, LocalDateTime.now())
                .eq(SysUser::getId, user.getId()));
        saveLoginLog(user.getUsername(), servletRequest, 1, "登录成功");

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return new LoginResponse(tokenInfo.tokenName, tokenInfo.tokenValue,
                user.getId(), user.getUsername(), user.getNickname());
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public CurrentUserResponse currentUser() {
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        SysUser user = userMapper.selectById(userId);
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            StpUtil.logout();
            throw new IllegalArgumentException("当前用户不存在或已停用");
        }
        List<String> roles = permissionMapper.selectRoleKeysByUserId(userId);
        List<String> permissions = permissionMapper.selectPermsByUserId(userId);
        return new CurrentUserResponse(user.getId(), user.getUsername(), user.getNickname(),
                user.getAvatar(), roles, permissions);
    }

    private void saveLoginLog(String username, HttpServletRequest request, Integer status, String message) {
        SysLoginLog log = new SysLoginLog();
        log.setUsername(username);
        log.setIpaddr(getClientIp(request));
        log.setBrowser(truncate(request.getHeader("User-Agent"), 100));
        log.setOs(truncate(request.getHeader("sec-ch-ua-platform"), 100));
        log.setStatus(status);
        log.setMessage(message);
        log.setLoginTime(LocalDateTime.now());
        loginLogMapper.insert(log);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
