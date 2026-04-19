package com.rqzb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String tokenName;

    private String tokenValue;

    private Long userId;

    private String username;

    private String nickname;
}
