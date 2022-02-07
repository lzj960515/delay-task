package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.config.DelayTaskProperties;
import com.github.lzj960515.delaytask.util.TimeUtil;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.github.lzj960515.delaytask.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Resource
    private DelayTaskProperties delayTaskProperties;

    public void start() {
        long now = System.currentTimeMillis();
        try {
            // 时间归整
            Thread.sleep(1000 - now % 1000);
        } catch (InterruptedException ignore) {
        }
        this.queryTask();
        this.executeTask();
        this.clearSuccessTask();
    }

    public void queryTask() {
        // 处理时间轮中的任务
        ThreadPool.DELAY_TASK_QUERY.scheduleAtFixedRate(() -> {
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
        }, 0, 5, TimeUnit.SECONDS);
    }
    private void executeTask() {
        // 处理时间轮中的任务
        ThreadPool.DELAY_TASK_EXECUTOR.scheduleAtFixedRate(() -> {
            // 1.从时间轮中取出该秒所有任务id
            List<Long> taskIds = TimeRing.pull();
            // 2.执行任务
            ThreadPool.DELAY_TASK_WORKER.execute(new DelayTaskRunner(delayTaskRepository, taskIds));
        }, 100, 1000, TimeUnit.MILLISECONDS);
    }

    private void clearSuccessTask(){
        int taskRetentionDays = delayTaskProperties.getTaskRetentionDays();
        if(taskRetentionDays <= 0){
            return;
        }
        ThreadPool.DELAY_TASK_CLEANER.scheduleAtFixedRate(() -> {
            LocalDate clearDate = LocalDate.now().plusDays(-taskRetentionDays);
            delayTaskRepository.deleteByExecuteTime(TimeUtil.localDate2Millis(clearDate));
        }, 0, 1, TimeUnit.DAYS);
    }

}
