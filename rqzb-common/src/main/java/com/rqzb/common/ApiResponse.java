package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Unified API response")
public class ApiResponse<T> {

    @Schema(description = "Business status code", example = "200")
    private Integer code;

    @Schema(description = "Response message", example = "success")
    private String message;

    @Schema(description = "Response payload")
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(200, "success", null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(500, message, null);
    }

    public static <T> ApiResponse<T> fail(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
