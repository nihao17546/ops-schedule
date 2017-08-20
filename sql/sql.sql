CREATE TABLE `ops`.`tb_ops_schedule_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group` varchar(100) NOT NULL DEFAULT '',
  `key` varchar(100) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `type` tinyint(2) NOT NULL COMMENT '1:周期任务，2:每天定时任务',
  `period` bigint(22) DEFAULT NULL,
  `time` varchar(10) DEFAULT NULL,
  `create_uid` int(11) NOT NULL,
  `create_at` datetime NOT NULL,
  `update_uid` int(11) DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group` (`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ops`.`tb_ops_schedule_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '',
  `password` varchar(100) NOT NULL DEFAULT '',
  `create_at` datetime NOT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_ops_schedule_user2task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `create_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`user_id`,`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;