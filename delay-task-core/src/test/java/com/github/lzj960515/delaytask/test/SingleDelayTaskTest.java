package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.core.DelayTaskTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 测试延迟任务
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@SpringBootTest
public class SingleDelayTaskTest {

    @Resource
    private DelayTaskTemplate delayTaskTemplate;

    @Test
    public void testDelayTask() throws IOException {
        for (int i = 0; i < 100; i++){
            delayTaskTemplate.save("demoJob", i + "", LocalDateTime.now().plusSeconds(10+i), "测试任务");
        }
        System.out.println("保存延时任务，时间："+ LocalDateTime.now());
        System.in.read();
    }
}
