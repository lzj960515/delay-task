package com.github.lzj960515.delaytask.constant;

/**
 * @author Zijian Liao
 * @since  1.0.0
 */
public enum  ExecuteStatus {

    NEW(1),

    EXECUTE(2),

    SUCCESS(3),

    FAIL(4);

    private final Integer status;

    ExecuteStatus(Integer status){
        this.status = status;
    }

    public Integer status() {
        return status;
    }
}
