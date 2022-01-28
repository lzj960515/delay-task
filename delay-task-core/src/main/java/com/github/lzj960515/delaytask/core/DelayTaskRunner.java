package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.github.lzj960515.delaytask.thread.ThreadPool;

import java.util.List;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
public class DelayTaskRunner implements Runnable {

    private final DelayTaskRepository delayTaskRepository;
    private final List<Long> taskIds;

    public DelayTaskRunner(DelayTaskRepository delayTaskRepository, List<Long> taskIds){
        this.delayTaskRepository = delayTaskRepository;
        this.taskIds = taskIds;
    }

    @Override
    public void run() {
        // 1.查询任务
        for (Long taskId : taskIds) {
            ThreadPool.DELAY_TASK_INVOKER.execute(new DelayTaskInvoker(delayTaskRepository, taskId));
        }
    }
}
