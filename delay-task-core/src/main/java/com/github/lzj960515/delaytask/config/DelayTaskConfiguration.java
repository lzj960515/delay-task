package com.github.lzj960515.delaytask.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com.github.lzj960515.delaytask")
@EnableConfigurationProperties(DelayTaskProperties.class)
public class DelayTaskConfiguration {
}
