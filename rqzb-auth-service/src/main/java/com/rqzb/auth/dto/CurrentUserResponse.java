package com.rqzb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse {

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    private List<String> roles;

    private List<String> permissions;
}
