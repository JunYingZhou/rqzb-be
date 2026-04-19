package com.rqzb.common;

import lombok.Data;

@Data
public class PageQuery {

    private Long current = 1L;

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
