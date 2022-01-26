package com.github.lzj960515.delaytask.annotation;

import com.github.lzj960515.delaytask.config.DelayTaskConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(DelayTaskConfiguration.class)
public @interface EnableDelayTask {
}
