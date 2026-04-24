package com.rqzb.userinfo.controller;

import com.rqzb.common.ApiResponse;
import com.rqzb.userinfo.dto.UserInfoResponse;
import com.rqzb.userinfo.service.UserInfoService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInfoControllerTest {

    @Test
    void shouldReturnCurrentUserInfo() {
        UserInfoService userInfoService = mock(UserInfoService.class);
        UserInfoResponse userInfo = new UserInfoResponse(1L, 2L, "admin", "Admin",
                "Administrator", "admin@example.com", "13800000000", 1, "/avatar.png", 1);
        when(userInfoService.getCurrentUserInfo()).thenReturn(userInfo);

        UserInfoController controller = new UserInfoController(userInfoService);
        ApiResponse<UserInfoResponse> response = controller.me();

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getData()).isEqualTo(userInfo);
    }

    @Test
    void shouldReturnUserInfoById() {
        UserInfoService userInfoService = mock(UserInfoService.class);
        UserInfoResponse userInfo = new UserInfoResponse(2L, 3L, "demo", "Demo",
                "Demo User", "demo@example.com", "13900000000", 2, null, 1);
        when(userInfoService.getUserInfoById(2L)).thenReturn(userInfo);

        UserInfoController controller = new UserInfoController(userInfoService);
        ApiResponse<UserInfoResponse> response = controller.getById(2L);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getData()).isEqualTo(userInfo);
    }
}
