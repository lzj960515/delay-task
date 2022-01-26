package com.github.lzj960515.delaytask.core;

import com.github.lzj960515.delaytask.annotation.DelayTask;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * RedisListener注解 扫描器
 *
 * @author Zijian Liao
 * @since 1.4.0
 */
public class DelayTaskScanner {

    public static Map<String, DelayTaskMethod> scan(ApplicationContext applicationContext){
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        Map<String, DelayTaskMethod> map = new HashMap<>(4);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Map<Method, DelayTask> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                    (MethodIntrospector.MetadataLookup<DelayTask>) method -> AnnotatedElementUtils.findMergedAnnotation(method, DelayTask.class));
            annotatedMethods.forEach((method, delayTask) -> {
                if(delayTask == null){
                    return;
                }
                map.put(delayTask.name(), new DelayTaskMethod(bean, method));
            });
        }
        return map;
    }
}
