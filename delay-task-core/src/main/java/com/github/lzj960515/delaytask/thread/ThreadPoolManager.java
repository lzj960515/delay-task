package com.github.lzj960515.delaytask.thread;


import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池管理器，维护从异步组件中获取的所有线程，并在系统停止时对所有线程进行shutdown
 * @author Zijian Liao
 * @since 1.14.0
 */
public final class ThreadPoolManager {

    private static final Map<String, ExecutorService> RESOURCES_MANAGER = new ConcurrentHashMap<>(8);

    static {
        ThreadUtil.addShutdownHook(ThreadPoolManager::shutdown);
    }

    public static ExecutorService getExecutor(String threadName) {
        ExecutorService executorService = RESOURCES_MANAGER.get(threadName);
        if(executorService == null){
            synchronized (ThreadPoolManager.class) {
                executorService = RESOURCES_MANAGER.get(threadName);
                if(executorService == null){
                    final int count = ThreadUtil.getSuitableThreadCount();
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(count, count, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(200), new NameThreadFactory(threadName));
                    ThreadPoolManager.register(threadName, executor);
                }
            }
        }
        return executorService;
    }

    public static void register(String name, ExecutorService executor) {
        RESOURCES_MANAGER.put(name, executor);
    }

    public static void shutdown(){
        for (Map.Entry<String, ExecutorService> executorEntry : RESOURCES_MANAGER.entrySet()) {
            ThreadUtil.shutdownThreadPool(executorEntry.getValue());
        }
    }
}
