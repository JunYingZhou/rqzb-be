package com.rqzb.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class LoginRequest {

    @NotBlank(message = "username must not be blank")
    @Schema(description = "Username", example = "admin")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Schema(description = "Password", example = "admin123")
    private String password;
}
