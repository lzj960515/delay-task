package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
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
        // 处理时间轮中的任务
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new NameThreadFactory("delay-task-query"));
        scheduledThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 1.查询5s内的任务放入时间轮中
                List<DelayTaskInfo> taskInfoList = delayTaskRepository.findByExecuteTime(TimeRing.getAfterFiveSecond());
                for (DelayTaskInfo delayTaskInfo : taskInfoList) {
                    // 2.判断时间
                    // 2.1如果时间在这之前，直接执行任务
                    long now = System.currentTimeMillis();
                    long executeTime = delayTaskInfo.getExecuteTime();
                    if(now >= executeTime){
                        // TODO 改为线程执行
                        DelayTaskInvoker.invoke(delayTaskInfo);
                    }else {
                        // 2.1否则将任务放入时间轮
                        TimeRing.put(executeTime, delayTaskInfo.getId());
                    }
                }
                long time = System.currentTimeMillis();
                // 让时间到下一个5秒
                long delay = 5000 - time % 1000;
                scheduledThreadPoolExecutor.schedule(this, delay, TimeUnit.MICROSECONDS);
            }
        });
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