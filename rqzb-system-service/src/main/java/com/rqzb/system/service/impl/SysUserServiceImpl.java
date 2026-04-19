package com.rqzb.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rqzb.system.entity.SysUser;
import com.rqzb.system.entity.SysUserPost;
import com.rqzb.system.entity.SysUserRole;
import com.rqzb.system.mapper.SysUserMapper;
import com.rqzb.system.mapper.SysUserPostMapper;
import com.rqzb.system.mapper.SysUserRoleMapper;
import com.rqzb.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;

    private final SysUserPostMapper userPostMapper;

    public SysUserServiceImpl(SysUserRoleMapper userRoleMapper, SysUserPostMapper userPostMapper) {
        this.userRoleMapper = userRoleMapper;
        this.userPostMapper = userPostMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        normalizeIds(roleIds).forEach(roleId ->
                userRoleMapper.insert(new SysUserRole(userId, roleId, LocalDateTime.now())));
    }

    @Override
    public List<Long> listRoleIds(Long userId) {
        return userRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPosts(Long userId, List<Long> postIds) {
        userPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userId));
        normalizeIds(postIds).forEach(postId ->
                userPostMapper.insert(new SysUserPost(userId, postId, LocalDateTime.now())));
    }

    @Override
    public List<Long> listPostIds(Long userId) {
        return userPostMapper.selectList(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userId))
                .stream()
                .map(SysUserPost::getPostId)
                .toList();
    }

    private List<Long> normalizeIds(List<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();
    }
}
