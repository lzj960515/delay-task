package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.core.DelayTaskTemplate;
import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
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
        DelayTaskInfo delayTaskInfo = new DelayTaskInfo();
        delayTaskInfo.setName("demoJob");
        delayTaskInfo.setDescription("demo任务测试");
        delayTaskInfo.setInfo("id=1");
        System.out.println("保存延时任务，时间："+ LocalDateTime.now());
        // TODO 这里不够人性化，需支持指定时间，或者指定多长时间之后 使用 number + timeUnit方式 或者 LocalDateTime方式
        delayTaskInfo.setExecuteTime(System.currentTimeMillis() + 4000);
        delayTaskTemplate.save(delayTaskInfo);
        System.in.read();
    }
}
