package com.github.lzj960515.delaytask.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间轮，用于存储任务id
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class TimeRing {

    /**
     * 时间轮 秒:任务id列表
     */
    private static final Map<Integer, ArrayList<Long>> RING = new ConcurrentHashMap<>(64, 1);


    public static void put(long time, Long taskId){
        int second = getSecond(time);
        List<Long> taskIds = RING.computeIfAbsent(second, k -> new ArrayList<>());
        taskIds.add(taskId);
    }

    public static List<Long> pull(){
        long time = System.currentTimeMillis();
        int second = getSecond(time);

        List<Long> taskIds = RING.getOrDefault(second, new ArrayList<>(0));
        List<Long> moreTaskIds = new ArrayList<>(taskIds.size());
        moreTaskIds.addAll(taskIds);
        // 为防止任务执行时间过长，跳过了前面几秒任务，将前面2秒的任务也取出
        // 当然，这种情况一般不会发生，任务一但取出就放入了线程池中。
        // 一般来说前面2秒都不会有任务，因为已经被取走了。只有卡住的时候才会发生这种情况
        for(int i = 1; i < 2; i++){
            // 当此时为1秒时，前面的2秒为0和59 如果只是减，将得到一个负数
            int beforeSecond = (second + 60 - i) % 60;
            moreTaskIds.addAll(RING.getOrDefault(beforeSecond, new ArrayList<>(0)));
        }
        return moreTaskIds;
    }

    private static int getSecond(long time){
        return (int) ((time / 1000) % 60);
    }

}
