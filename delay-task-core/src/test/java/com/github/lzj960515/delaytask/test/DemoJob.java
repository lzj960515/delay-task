package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.annotation.DelayTask;
import com.github.lzj960515.delaytask.helper.DelayTaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 用于测试的类
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Component
public class DemoJob {

    private static final Logger log = LoggerFactory.getLogger(DemoJob.class);

    @DelayTask(name = "demoJob")
    public void job(String info) {
        log.info("延迟任务「job」被调用了, id:{} 当前时间：{}", info, LocalDateTime.now());
        DelayTaskHelper.handleSuccess();
    }

    @DelayTask(name = "demoJob0")
    public void job0(String info) {
        log.info("延迟任务「job0」被调用了, id:{} 当前时间：{}", info, LocalDateTime.now());
        DelayTaskHelper.handleSuccess();
    }

    @DelayTask(name = "demoJob1")
    public void job1(String info) {
        log.info("延迟任务「job1」被调用了, id:{} 当前时间：{}", info, LocalDateTime.now());
        DelayTaskHelper.handleSuccess();
    }
}
