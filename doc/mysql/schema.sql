CREATE TABLE `delay_task` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                              `name` varchar(50) NOT NULL DEFAULT '' COMMENT '任务名：任务的名称，与执行任务的方法对应，用于执行任务时寻找执行任务的方法',
                              `description` varchar(128) DEFAULT '' COMMENT '任务描述',
                              `info` varchar(512) NOT NULL DEFAULT '' COMMENT '任务信息：放置执行任务所需的参数信息',
                              `execute_time` bigint(20) NOT NULL COMMENT '执行时间 时间戳',
                              `execute_status` tinyint(4) NOT NULL COMMENT '执行状态：1.创建 2.执行中 3.执行成功 4.执行失败',
                              `execute_message` varchar(256) DEFAULT '' COMMENT '执行结果信息',
                              PRIMARY KEY (`id`),
                              KEY `idx_etime` (`execute_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;