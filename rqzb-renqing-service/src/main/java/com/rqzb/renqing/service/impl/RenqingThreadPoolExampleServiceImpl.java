package com.rqzb.renqing.service.impl;

import com.rqzb.common.service.GlobalThreadPoolService;
import com.rqzb.renqing.dto.ThreadPoolExampleResponse;
import com.rqzb.renqing.service.GiftRecordService;
import com.rqzb.renqing.service.PersonService;
import com.rqzb.renqing.service.RenqingRecordService;
import com.rqzb.renqing.service.RenqingThreadPoolExampleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class RenqingThreadPoolExampleServiceImpl implements RenqingThreadPoolExampleService {

    private final GlobalThreadPoolService threadPoolService;

    private final PersonService personService;

    private final RenqingRecordService renqingRecordService;

    private final GiftRecordService giftRecordService;

    public RenqingThreadPoolExampleServiceImpl(GlobalThreadPoolService threadPoolService,
                                              PersonService personService,
                                              RenqingRecordService renqingRecordService,
                                              GiftRecordService giftRecordService) {
        this.threadPoolService = threadPoolService;
        this.personService = personService;
        this.renqingRecordService = renqingRecordService;
        this.giftRecordService = giftRecordService;
    }

    @Override
    public ThreadPoolExampleResponse buildOverview() {
        long startMillis = System.currentTimeMillis();
        CompletableFuture<TaskResult<Long>> personCountFuture = threadPoolService.supplyAsync(
                () -> runTask("personCount", personService::count));
        CompletableFuture<TaskResult<Long>> renqingRecordCountFuture = threadPoolService.supplyAsync(
                () -> runTask("renqingRecordCount", renqingRecordService::count));
        CompletableFuture<TaskResult<Long>> giftRecordCountFuture = threadPoolService.supplyAsync(
                () -> runTask("giftRecordCount", giftRecordService::count));

        CompletableFuture.allOf(personCountFuture, renqingRecordCountFuture, giftRecordCountFuture).join();

        TaskResult<Long> personCount = personCountFuture.join();
        TaskResult<Long> renqingRecordCount = renqingRecordCountFuture.join();
        TaskResult<Long> giftRecordCount = giftRecordCountFuture.join();

        ThreadPoolExampleResponse response = new ThreadPoolExampleResponse();
        response.setPersonCount(personCount.value());
        response.setRenqingRecordCount(renqingRecordCount.value());
        response.setGiftRecordCount(giftRecordCount.value());
        response.setTotalCostMillis(System.currentTimeMillis() - startMillis);
        response.setTasks(List.of(personCount.taskInfo(), renqingRecordCount.taskInfo(), giftRecordCount.taskInfo()));
        response.setPool(buildPoolInfo());
        return response;
    }

    private <T> TaskResult<T> runTask(String taskName, Supplier<T> supplier) {
        long startMillis = System.currentTimeMillis();
        T value = supplier.get();

        ThreadPoolExampleResponse.TaskInfo taskInfo = new ThreadPoolExampleResponse.TaskInfo();
        taskInfo.setTaskName(taskName);
        taskInfo.setThreadName(Thread.currentThread().getName());
        taskInfo.setCostMillis(System.currentTimeMillis() - startMillis);
        return new TaskResult<>(value, taskInfo);
    }

    private ThreadPoolExampleResponse.PoolInfo buildPoolInfo() {
        ThreadPoolExampleResponse.PoolInfo poolInfo = new ThreadPoolExampleResponse.PoolInfo();
        poolInfo.setActiveCount(threadPoolService.getActiveCount());
        poolInfo.setPoolSize(threadPoolService.getPoolSize());
        poolInfo.setQueueSize(threadPoolService.getQueueSize());
        poolInfo.setCompletedTaskCount(threadPoolService.getCompletedTaskCount());
        return poolInfo;
    }

    private record TaskResult<T>(T value, ThreadPoolExampleResponse.TaskInfo taskInfo) {
    }
}
