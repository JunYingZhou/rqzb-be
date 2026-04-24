package com.rqzb.userinfo.service;

import com.rqzb.userinfo.dto.UserInfoResponse;

public interface UserInfoService {

    UserInfoResponse getCurrentUserInfo();

    UserInfoResponse getUserInfoById(Long userId);
}
