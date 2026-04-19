package com.rqzb.renqing.dto;

import lombok.Data;

import java.util.List;

@Data
public class ThreadPoolExampleResponse {

    private Long personCount;

    private Long renqingRecordCount;

    private Long giftRecordCount;

    private Long totalCostMillis;

    private List<TaskInfo> tasks;

    private PoolInfo pool;

    @Data
    public static class TaskInfo {

        private String taskName;

        private String threadName;

        private Long costMillis;
    }

    @Data
    public static class PoolInfo {

        private Integer activeCount;

        private Integer poolSize;

        private Integer queueSize;

        private Long completedTaskCount;
    }
}
