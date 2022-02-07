package com.github.lzj960515.delaytask.dao;

import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
public interface DelayTaskRepository extends JpaRepository<DelayTaskInfo, Long> {

    /**
     * 查询执行时间小于某时间的所有任务
     * @param executeTime 执行时间
     * @return 任务列表
     */
    @Query(value = "select d.* from delay_task d where d.execute_time <= ?1 and d.execute_status = 1", nativeQuery = true)
    List<DelayTaskInfo> findByExecuteTime(Long executeTime);

    /**
     * 删除某个执行时间之前的已完成任务
     * @param executeTime 执行时间
     */
    @Query(value = "delete from delay_task where execute_status = 3 and execute_time <= ?1", nativeQuery = true)
    @Modifying
    void deleteByExecuteTime(Long executeTime);
}
