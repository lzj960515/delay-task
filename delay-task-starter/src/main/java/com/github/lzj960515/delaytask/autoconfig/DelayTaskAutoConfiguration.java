package com.github.lzj960515.delaytask.autoconfig;

import com.github.lzj960515.delaytask.config.DelayTaskConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
@Configuration
@Import(DelayTaskConfiguration.class)
public class DelayTaskAutoConfiguration {
}
