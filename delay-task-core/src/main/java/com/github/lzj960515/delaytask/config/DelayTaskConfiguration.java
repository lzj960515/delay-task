package com.github.lzj960515.delaytask.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
@Configuration
@EntityScan("com.github.lzj960515.delaytask.core.domain")
@ComponentScan(basePackages = "com.github.lzj960515.delaytask")
@EnableJpaRepositories(basePackages = "com.github.lzj960515.delaytask.dao")
public class DelayTaskConfiguration {
}
