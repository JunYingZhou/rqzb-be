package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Pagination query parameters")
public class PageQuery {

    @Schema(description = "Current page number", example = "1", minimum = "1")
    private Long current = 1L;

    @Schema(description = "Page size", example = "10", minimum = "1", maximum = "100")
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
