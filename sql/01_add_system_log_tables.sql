-- ===============================================================
-- 系统日志表补充脚本
-- ===============================================================
-- 📅 创建时间：2025-12-22
-- 📝 描述：添加 sys_logininfor 和 sys_oper_log 系统日志表
-- ⚠️ 注意：在执行 init_database.sql 之后执行此脚本
-- ===============================================================

USE newblog;

-- 1、登录日志表
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` VARCHAR(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` VARCHAR(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` VARCHAR(255) DEFAULT '' COMMENT '登录地点',
  `browser` VARCHAR(50) DEFAULT '' COMMENT '浏览器类型',
  `os` VARCHAR(50) DEFAULT '' COMMENT '操作系统',
  `status` CHAR(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` VARCHAR(255) DEFAULT '' COMMENT '提示消息',
  `login_time` DATETIME DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';

-- 2、操作日志表
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` VARCHAR(50) DEFAULT '' COMMENT '模块标题',
  `business_type` INT(2) DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` VARCHAR(100) DEFAULT '' COMMENT '方法名称',
  `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` INT(1) DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` VARCHAR(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` VARCHAR(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` VARCHAR(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` VARCHAR(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` VARCHAR(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` VARCHAR(2000) DEFAULT '' COMMENT '返回参数',
  `status` INT(1) DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` VARCHAR(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` DATETIME DEFAULT NULL COMMENT '操作时间',
  `cost_time` BIGINT(20) DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_oper_time` (`oper_time`),
  KEY `idx_oper_name` (`oper_name`),
  KEY `idx_business_type` (`business_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';

-- ===============================================================
-- 执行完成提示
-- ===============================================================
SELECT '系统日志表创建完成！' AS '状态';
SELECT 'sys_logininfor - 登录日志表' AS '表1';
SELECT 'sys_oper_log - 操作日志表' AS '表2';