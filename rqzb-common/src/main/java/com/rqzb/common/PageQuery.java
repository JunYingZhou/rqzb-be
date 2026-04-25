package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询参数")
public class PageQuery {

    @Schema(description = "当前页码", example = "1", minimum = "1")
    private Long current = 1L;

    @Schema(description = "每页条数", example = "10", minimum = "1", maximum = "100")
    private Long size = 10L;

    public Long getCurrent() {
        if (current == null || current < 1) {
            return 1L;
        }
        return current;
    }

    public Long getSize() {
        if (size == null || size < 1) {
            return 10L;
        }
        return Math.min(size, 100L);
    }
}
