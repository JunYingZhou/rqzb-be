package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Batch ID request")
public class IdsRequest {

    @Schema(description = "List of IDs", example = "[1,2,3]")
    private List<Long> ids;
}
