package com.rqzb.userinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;

    private Long deptId;

    private String username;

    private String nickname;

    private String realName;

    private String email;

    private String phone;

    private Integer sex;

    private String avatar;

    private Integer status;
}
