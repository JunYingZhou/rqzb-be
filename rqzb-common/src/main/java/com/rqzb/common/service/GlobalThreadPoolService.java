package com.rqzb.common.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
public class GlobalThreadPoolService implements DisposableBean {

    private static final String PROPERTY_PREFIX = "rqzb.thread-pool.";

    private final ThreadPoolExecutor executor;

    private final long awaitTerminationSeconds;

    public GlobalThreadPoolService(Environment environment) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int coreSize = getInt(environment, "core-size", Math.max(2, availableProcessors));
        int maxSize = getInt(environment, "max-size", Math.max(coreSize, coreSize * 2));
        int queueCapacity = getInt(environment, "queue-capacity", 1024);
        long keepAliveSeconds = getLong(environment, "keep-alive-seconds", 60L);
        boolean allowCoreThreadTimeout = getBoolean(environment, "allow-core-thread-timeout", false);
        String threadNamePrefix = getString(environment, "thread-name-prefix", "rqzb-worker-");
        this.awaitTerminationSeconds = getLong(environment, "await-termination-seconds", 30L);

        validate(coreSize, maxSize, queueCapacity, keepAliveSeconds, allowCoreThreadTimeout, awaitTerminationSeconds);

        this.executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new NamedThreadFactory(threadNamePrefix),
                new ThreadPoolExecutor.CallerRunsPolicy());
        this.executor.allowCoreThreadTimeOut(allowCoreThreadTimeout);
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(task, result);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, executor);
    }

    public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public int getActiveCount() {
        return executor.getActiveCount();
    }

    public int getPoolSize() {
        return executor.getPoolSize();
    }

    public int getQueueSize() {
        return executor.getQueue().size();
    }

    public long getCompletedTaskCount() {
        return executor.getCompletedTaskCount();
    }

    @Override
    public void destroy() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(awaitTerminationSeconds, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static int getInt(Environment environment, String key, int defaultValue) {
        return environment.getProperty(PROPERTY_PREFIX + key, Integer.class, defaultValue);
    }

    private static long getLong(Environment environment, String key, long defaultValue) {
        return environment.getProperty(PROPERTY_PREFIX + key, Long.class, defaultValue);
    }

    private static boolean getBoolean(Environment environment, String key, boolean defaultValue) {
        return environment.getProperty(PROPERTY_PREFIX + key, Boolean.class, defaultValue);
    }

    private static String getString(Environment environment, String key, String defaultValue) {
        String value = environment.getProperty(PROPERTY_PREFIX + key, defaultValue);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    private static void validate(int coreSize,
                                 int maxSize,
                                 int queueCapacity,
                                 long keepAliveSeconds,
                                 boolean allowCoreThreadTimeout,
                                 long awaitTerminationSeconds) {
        if (coreSize < 1) {
            throw new IllegalArgumentException("rqzb.thread-pool.core-size must be greater than 0");
        }
        if (maxSize < coreSize) {
            throw new IllegalArgumentException("rqzb.thread-pool.max-size must be greater than or equal to core-size");
        }
        if (queueCapacity < 1) {
            throw new IllegalArgumentException("rqzb.thread-pool.queue-capacity must be greater than 0");
        }
        if (keepAliveSeconds < 0) {
            throw new IllegalArgumentException("rqzb.thread-pool.keep-alive-seconds must be greater than or equal to 0");
        }
        if (allowCoreThreadTimeout && keepAliveSeconds == 0) {
            throw new IllegalArgumentException("rqzb.thread-pool.keep-alive-seconds must be greater than 0 when allow-core-thread-timeout is true");
        }
        if (awaitTerminationSeconds < 0) {
            throw new IllegalArgumentException("rqzb.thread-pool.await-termination-seconds must be greater than or equal to 0");
        }
    }

    private static class NamedThreadFactory implements ThreadFactory {

        private final AtomicInteger index = new AtomicInteger(1);

        private final String threadNamePrefix;

        private NamedThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, threadNamePrefix + index.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        }
    }
}
