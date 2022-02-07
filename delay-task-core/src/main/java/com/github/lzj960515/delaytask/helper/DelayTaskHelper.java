package com.github.lzj960515.delaytask.helper;

import com.github.lzj960515.delaytask.constant.ExecuteStatus;

/**
 * 传递延时任务的执行情况
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public final class DelayTaskHelper {

    public static final ThreadLocal<DelayTaskExecuteInfo> CONTEXT = new ThreadLocal<>();

    public static void handleSuccess(){
        CONTEXT.set(new DelayTaskExecuteInfo(ExecuteStatus.SUCCESS.status()));
    }

    public static void handleFail(String message){
        CONTEXT.set(new DelayTaskExecuteInfo(ExecuteStatus.FAIL.status(), message));
    }

    public static DelayTaskExecuteInfo getExecuteInfo(){
        return CONTEXT.get();
    }

    public static void remove(){
        CONTEXT.remove();
    }
}
