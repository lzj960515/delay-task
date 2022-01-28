package com.github.lzj960515.delaytask.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public final class ThreadPool {

    // 查询任务线程池，负责查询任务，将任务放入时间轮：单线程，定时任务线程池
    public static final ScheduledThreadPoolExecutor DELAY_TASK_QUERY = ThreadPoolManager.getSingleScheduledExecutor("delay-task-query");
    // 执行任务线程池，负责将任务从时间轮取出：单线程, 定时任务线程池
    public static final ScheduledThreadPoolExecutor DELAY_TASK_EXECUTOR = ThreadPoolManager.getSingleScheduledExecutor("delay-task-executor");
    // 查询任务线程池，负责遍历任务，将任务传递给调度器：单线程
    public static final ExecutorService DELAY_TASK_WORKER = ThreadPoolManager.getSingleExecutor("delay-task-worker");
    // 调度任务线程池，负责调度任务：多线程
    public static final ExecutorService DELAY_TASK_INVOKER = ThreadPoolManager.getExecutor("delay-task-invoker");

}
