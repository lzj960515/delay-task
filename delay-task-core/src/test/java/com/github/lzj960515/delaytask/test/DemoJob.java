package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.annotation.DelayTask;
import com.github.lzj960515.delaytask.constant.ExecuteResult;
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
    public ExecuteResult job(String info) {
        log.error("延迟任务被调用了, id:{} 当前时间：{}", info, LocalDateTime.now());
        return ExecuteResult.SUCCESS;
    }
}
