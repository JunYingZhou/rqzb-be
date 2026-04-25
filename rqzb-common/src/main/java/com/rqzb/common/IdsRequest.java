package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量 ID 请求体")
public class IdsRequest {

    @Schema(description = "ID 列表", example = "[1,2,3]")
    private List<Long> ids;
}
