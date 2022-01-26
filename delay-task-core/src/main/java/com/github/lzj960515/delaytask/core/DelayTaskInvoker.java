package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.constant.ExecuteResult;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 延迟任务调度器
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public final class DelayTaskInvoker {

    private static final Logger log = LoggerFactory.getLogger(DelayTaskInvoker.class);

    public static ExecuteResult invoke(DelayTaskInfo delayTaskInfo){
        // 1.从上下文中取出任务对应的方法
        DelayTaskMethod delayTaskMethod = DelayTaskContext.find(delayTaskInfo.getName());
        // 2.调用
        try {
            return (ExecuteResult) delayTaskMethod.getMethod().invoke(delayTaskMethod.getBean(), delayTaskInfo.getInfo());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            return ExecuteResult.FAIL;
        }
    }
}
