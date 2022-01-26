package com.github.lzj960515.delaytask.dao;

import com.github.lzj960515.delaytask.core.domain.DelayTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zijian Liao
 * @since 1.0.0
 */
public interface DelayTaskRepository extends JpaRepository<DelayTaskInfo, Long> {
}
