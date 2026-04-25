package com.rqzb.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @NotBlank(message = "username must not be blank")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Schema(description = "密码", example = "admin123")
    private String password;
}
