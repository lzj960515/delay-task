package com.github.lzj960515.delaytask.core;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 延迟任务上下文，存放标识了DelayTask注解的方法
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Component
public class DelayTaskContext implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 执行器map, 任务名称:执行器
     */
    private static final Map<String, DelayTaskMethod> invokerRepository = new ConcurrentHashMap<>(4);

    @Resource
    private DelayTaskExecutor delayTaskExecutor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        invokerRepository.putAll(DelayTaskScanner.scan(event.getApplicationContext()));
        delayTaskExecutor.start();
    }

    public static DelayTaskMethod find(String taskName){
        return invokerRepository.get(taskName);
    }
}
