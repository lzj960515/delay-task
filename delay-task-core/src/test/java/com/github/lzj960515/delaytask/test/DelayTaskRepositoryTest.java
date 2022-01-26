package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import com.github.lzj960515.delaytask.dao.DelayTaskRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class DelayTaskRepositoryTest {

    @Resource
    private DelayTaskRepository delayTaskRepository;

    @Test
    public void testOk(){
        DelayTaskInfo delayTaskInfo = new DelayTaskInfo();
        delayTaskInfo.setName("测试");
        delayTaskInfo.setDescription("描述");
        delayTaskInfo.setInfo("信息");
        delayTaskInfo.setExecuteTime(System.currentTimeMillis());
        delayTaskInfo.setExecuteStatus(1);
        delayTaskInfo.setExecuteMessage("message");
        DelayTaskInfo save = delayTaskRepository.save(delayTaskInfo);
        MatcherAssert.assertThat(delayTaskInfo.getId(), CoreMatchers.notNullValue());
        Optional<DelayTaskInfo> optionalDelayTaskInfo = delayTaskRepository.findById(save.getId());
        MatcherAssert.assertThat(optionalDelayTaskInfo.isPresent(), CoreMatchers.is(true));
        MatcherAssert.assertThat(optionalDelayTaskInfo.get().getName(), CoreMatchers.is("测试"));
    }
}