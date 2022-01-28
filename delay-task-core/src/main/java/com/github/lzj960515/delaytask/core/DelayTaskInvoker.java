package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.constant.ExecuteResult;
import com.github.lzj960515.delaytask.constant.ExecuteStatus;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
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
        ExecuteResult result = this.doInvoke(delayTaskInfo);
        // 3.更新任务状态
        if (result != null && result.equals(ExecuteResult.SUCCESS)) {
            delayTaskInfo.setExecuteStatus(ExecuteStatus.SUCCESS.status());
        } else {
            delayTaskInfo.setExecuteStatus(ExecuteStatus.FAIL.status());
        }
        delayTaskRepository.save(delayTaskInfo);
    }

    private ExecuteResult doInvoke(DelayTaskInfo delayTaskInfo) {
        // 1.从上下文中取出任务对应的方法
        DelayTaskMethod delayTaskMethod = DelayTaskContext.find(delayTaskInfo.getName());
        try {
            // 2.调用
            return (ExecuteResult) delayTaskMethod.getMethod().invoke(delayTaskMethod.getBean(), delayTaskInfo.getInfo());
        } catch (Throwable e) {
            // 反射异常需要取出实际中的异常信息
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ex = (InvocationTargetException) e;
                log.error(ex.getTargetException().getMessage(), ex.getTargetException());
            } else {
                log.error(e.getMessage(), e);
            }
            return ExecuteResult.FAIL;
        }
    }
}
