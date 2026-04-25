package com.rqzb.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pagination result")
public class PageResult<T> {

    @Schema(description = "Current page number", example = "1")
    private Long current;

    @Schema(description = "Page size", example = "10")
    private Long size;

    @Schema(description = "Total count", example = "128")
    private Long total;

    @Schema(description = "Current page records")
    private List<T> records;

    public static <T> PageResult<T> of(Long current, Long size, Long total, List<T> records) {
        return new PageResult<>(current, size, total, records);
    }
}
