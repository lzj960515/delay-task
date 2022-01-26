package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.github.lzj960515.delaytask.thread.NameThreadFactory;
import com.github.lzj960515.delaytask.thread.ThreadPool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Component
public class DelayTaskExecutor {

    @Resource
    private DelayTaskRepository delayTaskRepository;

    public void start() {
        this.queryTask();
        this.executeTask();
    }

    public void queryTask() {
        // 查询数据库中的任务放入时间轮中

    }
    private void executeTask() {
        // 处理时间轮中的任务
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new NameThreadFactory("delay-task-schedule"));
        scheduledThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 1.从时间轮中取出该秒所有任务id
                List<Long> taskIds = TimeRing.pull();
                // 2.执行任务
                ThreadPool.DELAY_TASK_EXECUTOR.execute(new DelayTaskRunner(delayTaskRepository, taskIds));
                long time = System.currentTimeMillis();
                // 让时间到下一秒
                long delay = 1000 - time % 1000;
                scheduledThreadPoolExecutor.schedule(this, delay, TimeUnit.MICROSECONDS);
            }
        });
    }


}
