package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.util.TimeUtil;
import com.github.lzj960515.delaytask.constant.ExecuteStatus;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 存储任务
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Component
public class DelayTaskTemplate {

    @Resource
    private DelayTaskRepository delayTaskRepository;

    /**
     * 存储任务
     * @param delayTaskInfo 任务信息
     */
    private void save(DelayTaskInfo delayTaskInfo) {
        Long executeTime = delayTaskInfo.getExecuteTime();
        boolean innerFiveSecond = TimeUtil.isInnerFiveSecond(executeTime);
        // 1.设置任务状态
        this.setStatus(delayTaskInfo, innerFiveSecond);
        // 2.将任务存入数据库
        delayTaskRepository.save(delayTaskInfo);
        // 3.如果任务执行时间在5s内，放入时间轮中
        if(innerFiveSecond){
            TimeRing.put(executeTime, delayTaskInfo.getId());
        }
    }

    /**
     * 指定时间执行延迟任务
     * @param taskName 任务名称
     * @param info 任务信息
     * @param executeTime 执行时间
     * @param description 描述
     */
    public void save(@NotNull String taskName, String info, @NotNull LocalDateTime executeTime, String description){
        Assert.hasText(taskName, "taskName must not be null");
        Assert.notNull(executeTime, "executeTime must not be null");
        DelayTaskInfo delayTaskInfo = new DelayTaskInfo();
        delayTaskInfo.setName(taskName);
        delayTaskInfo.setInfo(info);
        delayTaskInfo.setExecuteTime(TimeUtil.localDateTime2Millis(executeTime));
        delayTaskInfo.setDescription(description);
        this.save(delayTaskInfo);
    }

    public void save(@NotNull String taskName, String info, @NotNull long time, TimeUnit unit, String description){
        long executeTime = TimeUtil.convert2Millis(time, unit);
        DelayTaskInfo delayTaskInfo = new DelayTaskInfo();
        delayTaskInfo.setName(taskName);
        delayTaskInfo.setInfo(info);
        delayTaskInfo.setExecuteTime(executeTime);
        delayTaskInfo.setDescription(description);
        this.save(delayTaskInfo);
    }

    private void setStatus(DelayTaskInfo delayTaskInfo, boolean innerFiveSecond){
        // 判断任务执行时间
        if(innerFiveSecond){
            delayTaskInfo.setExecuteStatus(ExecuteStatus.EXECUTE.status());
        }
        else {
            delayTaskInfo.setExecuteStatus(ExecuteStatus.NEW.status());
        }
    }


}
