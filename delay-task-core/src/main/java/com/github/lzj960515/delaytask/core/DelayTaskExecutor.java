package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.util.TimeUtil;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.github.lzj960515.delaytask.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 任务执行器
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Component
public class DelayTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(DelayTaskExecutor.class);

    @Resource
    private DelayTaskRepository delayTaskRepository;

    public void start() {
        this.queryTask();
        this.executeTask();
    }

    public void queryTask() {
        // 处理时间轮中的任务
        ThreadPool.DELAY_TASK_QUERY.execute(new Runnable() {
            @Override
            public void run() {
                // 1.查询5s内的任务放入时间轮中
                List<DelayTaskInfo> taskInfoList = delayTaskRepository.findByExecuteTime(TimeUtil.getAfterFiveSecond());
                for (DelayTaskInfo delayTaskInfo : taskInfoList) {
                    // 2.判断时间
                    long now = System.currentTimeMillis();
                    long executeTime = delayTaskInfo.getExecuteTime();
                    if(now >= executeTime){
                        // 2.1如果时间在这之前，直接执行任务
                        ThreadPool.DELAY_TASK_INVOKER.execute(new DelayTaskInvoker(delayTaskRepository, delayTaskInfo.getId()));
                    }else {
                        // 2.2否则将任务放入时间轮
                        TimeRing.put(executeTime, delayTaskInfo.getId());
                    }
                }
                long time = System.currentTimeMillis();
                // 3.让时间到下一个5秒
                long delay = 5000 - time % 1000;
                ThreadPool.DELAY_TASK_QUERY.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        });
    }
    private void executeTask() {
        // 处理时间轮中的任务
        ThreadPool.DELAY_TASK_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // 1.从时间轮中取出该秒所有任务id
                List<Long> taskIds = TimeRing.pull();
                log.info("时间轮pull出任务数：{}", taskIds.size());
                // 2.执行任务
                ThreadPool.DELAY_TASK_WORKER.execute(new DelayTaskRunner(delayTaskRepository, taskIds));
                long time = System.currentTimeMillis();
                // 3.让时间到下一秒
                long delay = 1000 - time % 1000;
                ThreadPool.DELAY_TASK_EXECUTOR.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        });
    }


}
