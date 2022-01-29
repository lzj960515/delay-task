package com.github.lzj960515.delaytask.test;

import com.github.lzj960515.delaytask.util.TimeUtil;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 时间工具测试类
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class TimeUtilTest {

    @Test
    public void testLocalDateTime2MillSecond(){
        long l = TimeUtil.localDateTime2Millis(LocalDateTime.now());
        long now = System.currentTimeMillis();
        MatcherAssert.assertThat(Math.abs(l - now) < 10, CoreMatchers.is(true));
    }
}