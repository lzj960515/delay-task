package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.constant.ExecuteResult;
import com.github.lzj960515.delaytask.constant.ExecuteStatus;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;

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
            DelayTaskInfo delayTaskInfo = delayTaskRepository.getOne(taskId);
            // TODO 以下方法抽出
            // 2.调用延迟任务调度器
            ExecuteResult result = DelayTaskInvoker.invoke(delayTaskInfo);
            // 3.更新任务状态
            if(result.equals(ExecuteResult.SUCCESS)){
                delayTaskInfo.setExecuteStatus(ExecuteStatus.SUCCESS.status());
            }
            else {
                delayTaskInfo.setExecuteStatus(ExecuteStatus.FAIL.status());
            }
            delayTaskRepository.save(delayTaskInfo);
        }

    }
}
