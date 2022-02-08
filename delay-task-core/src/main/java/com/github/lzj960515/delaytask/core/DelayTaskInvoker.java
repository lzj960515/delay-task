package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.constant.ExecuteStatus;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import com.github.lzj960515.delaytask.helper.DelayTaskExecuteInfo;
import com.github.lzj960515.delaytask.helper.DelayTaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * 延迟任务调度器
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public final class DelayTaskInvoker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DelayTaskInvoker.class);

    private final DelayTaskRepository delayTaskRepository;
    private final Long taskId;

    public DelayTaskInvoker(DelayTaskRepository delayTaskRepository, Long taskId) {
        this.delayTaskRepository = delayTaskRepository;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        this.invoke();
    }

    private void invoke() {
        Optional<DelayTaskInfo> delayTaskInfoOptional = delayTaskRepository.findById(taskId);
        if (!delayTaskInfoOptional.isPresent()) {
            return;
        }
        DelayTaskInfo delayTaskInfo = delayTaskInfoOptional.get();
        // 2.调用延迟任务
        this.doInvoke(delayTaskInfo);
        // 3.更新任务状态
        DelayTaskExecuteInfo executeInfo = DelayTaskHelper.getExecuteInfo();
        if(executeInfo != null){
            delayTaskInfo.setExecuteStatus(executeInfo.getExecuteStatus());
            delayTaskInfo.setExecuteMessage(executeInfo.getExecuteMessage());
        }else {
            delayTaskInfo.setExecuteStatus(ExecuteStatus.SUCCESS.status());
        }
        delayTaskRepository.save(delayTaskInfo);
    }

    private void doInvoke(DelayTaskInfo delayTaskInfo) {
        // 1.从上下文中取出任务对应的方法
        DelayTaskMethod delayTaskMethod = DelayTaskContext.find(delayTaskInfo.getName());
        if(delayTaskMethod == null){
            log.error("not found delay task method for name: {}", delayTaskInfo.getName());
            DelayTaskHelper.handleFail("not found delay task method for name: " + delayTaskInfo.getName());
            return;
        }
        try {
            // 2.调用
            delayTaskMethod.getMethod().invoke(delayTaskMethod.getBean(), delayTaskInfo.getInfo());
        } catch (InvocationTargetException e){
            log.error(e.getTargetException().getMessage(), e.getTargetException());
            DelayTaskHelper.handleFail(e.getTargetException().getMessage());
        }
        catch (Throwable e) {
            log.error(e.getMessage(), e);
            DelayTaskHelper.handleFail(e.getMessage());
        }
    }
}
