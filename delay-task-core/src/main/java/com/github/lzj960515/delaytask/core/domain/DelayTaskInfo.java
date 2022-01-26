package com.github.lzj960515.delaytask.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 延时任务信息
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Table(name = "delay_task")
@Entity
public class DelayTaskInfo {

    /**
     * 任务id：主键
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 任务名：任务的名称，与执行任务的方法对应，用于执行任务时寻找执行任务的方法
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务信息：放置执行任务所需的参数信息
     */
    private String info;

    /**
     * 执行时间 时间戳
     */
    private Long executeTime;

    /**
     * 执行状态：1.创建 2.执行中 3.执行成功 4.执行失败
     */
    private Integer executeStatus;

    /**
     * 执行结果信息
     */
    private String executeMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getExecuteMessage() {
        return executeMessage;
    }

    public void setExecuteMessage(String executeMessage) {
        this.executeMessage = executeMessage;
    }
}
