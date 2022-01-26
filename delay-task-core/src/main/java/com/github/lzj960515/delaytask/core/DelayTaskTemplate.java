package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.constant.ExecuteStatus;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public void save(DelayTaskInfo delayTaskInfo) {
        Long executeTime = delayTaskInfo.getExecuteTime();
        // 1.设置任务状态
        this.setStatus(delayTaskInfo);
        // 2.将任务存入数据库
        delayTaskRepository.save(delayTaskInfo);
        // 3.如果任务执行时间在5s内，放入时间轮中
        if(isInnerFiveSecond(executeTime)){
            TimeRing.put(executeTime, delayTaskInfo.getId());
        }
    }

    private void setStatus(DelayTaskInfo delayTaskInfo){
        // 判断任务执行时间
        if(isInnerFiveSecond(delayTaskInfo.getExecuteTime())){
            delayTaskInfo.setExecuteStatus(ExecuteStatus.EXECUTE.status());
        }
        else {
            delayTaskInfo.setExecuteStatus(ExecuteStatus.NEW.status());
        }
    }

    private static final long FIVE_SECOND = 5000;

    private boolean isInnerFiveSecond(long time) {
        long now = System.currentTimeMillis();
        return now + FIVE_SECOND > time;
    }
}
