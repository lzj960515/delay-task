package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.dao.DelayTaskDao;
import com.github.lzj960515.delaytask.thread.ThreadPool;

import java.util.List;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
public class DelayTaskRunner implements Runnable {

    private final DelayTaskDao delayTaskDao;
    private final List<Long> taskIds;

    public DelayTaskRunner(DelayTaskDao delayTaskDao, List<Long> taskIds){
        this.delayTaskDao = delayTaskDao;
        this.taskIds = taskIds;
    }

    @Override
    public void run() {
        // 1.查询任务
        for (Long taskId : taskIds) {
            ThreadPool.DELAY_TASK_INVOKER.execute(new DelayTaskInvoker(delayTaskDao, taskId));
        }
    }
}
