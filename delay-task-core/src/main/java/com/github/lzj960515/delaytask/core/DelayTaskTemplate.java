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
        boolean innerFiveSecond = TimeRing.isInnerFiveSecond(executeTime);
        // 1.设置任务状态
        this.setStatus(delayTaskInfo, innerFiveSecond);
        // 2.将任务存入数据库
        delayTaskRepository.save(delayTaskInfo);
        // 3.如果任务执行时间在5s内，放入时间轮中
        if(innerFiveSecond){
            TimeRing.put(executeTime, delayTaskInfo.getId());
        }
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
