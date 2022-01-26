package com.github.lzj960515.delaytask.thread;

import java.util.concurrent.ExecutorService;

/**
 * 线程池
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public final class ThreadPool {

    public static final ExecutorService DELAY_TASK_EXECUTOR = ThreadPoolManager.getExecutor("delay-task-executor");
}
