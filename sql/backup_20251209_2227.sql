-- MySQL dump 10.13  Distrib 8.4.7, for Linux (aarch64)
--
-- Host: localhost    Database: newblog
-- ------------------------------------------------------
-- Server version	8.4.7

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_BLOB_TRIGGERS`
--

LOCK TABLES `QRTZ_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CALENDARS`
--

LOCK TABLES `QRTZ_CALENDARS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CRON_TRIGGERS`
--

LOCK TABLES `QRTZ_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_FIRED_TRIGGERS`
--

LOCK TABLES `QRTZ_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_DETAILS`
--

LOCK TABLES `QRTZ_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_LOCKS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_LOCKS`
--

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `QRTZ_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SCHEDULER_STATE`
--

LOCK TABLES `QRTZ_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPLE_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPROP_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPROP_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `sched_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `QRTZ_JOB_DETAILS` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGERS`
--

LOCK TABLES `QRTZ_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_menu_missing_perms_before_semantic`
--

DROP TABLE IF EXISTS `backup_sys_menu_missing_perms_before_semantic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_menu_missing_perms_before_semantic` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_menu_missing_perms_before_semantic`
--

LOCK TABLES `backup_sys_menu_missing_perms_before_semantic` WRITE;
/*!40000 ALTER TABLE `backup_sys_menu_missing_perms_before_semantic` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup_sys_menu_missing_perms_before_semantic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_menu_missing_perms_path_before_semantic`
--

DROP TABLE IF EXISTS `backup_sys_menu_missing_perms_path_before_semantic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_menu_missing_perms_path_before_semantic` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_menu_missing_perms_path_before_semantic`
--

LOCK TABLES `backup_sys_menu_missing_perms_path_before_semantic` WRITE;
/*!40000 ALTER TABLE `backup_sys_menu_missing_perms_path_before_semantic` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup_sys_menu_missing_perms_path_before_semantic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_menu_perms_before_normalize`
--

DROP TABLE IF EXISTS `backup_sys_menu_perms_before_normalize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_menu_perms_before_normalize` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_menu_perms_before_normalize`
--

LOCK TABLES `backup_sys_menu_perms_before_normalize` WRITE;
/*!40000 ALTER TABLE `backup_sys_menu_perms_before_normalize` DISABLE KEYS */;
INSERT INTO `backup_sys_menu_perms_before_normalize` VALUES (20010,'文章查询',2001,1,'','','','',1,0,'F','1','1','blog:article:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20011,'文章新增',2001,2,'','','','',1,0,'F','1','1','blog:article:add','#','admin','2025-11-12 09:54:08','',NULL,''),(20012,'文章修改',2001,3,'','','','',1,0,'F','1','1','blog:article:edit','#','admin','2025-11-12 09:54:08','',NULL,''),(20013,'文章删除',2001,4,'','','','',1,0,'F','1','1','blog:article:remove','#','admin','2025-11-12 09:54:08','',NULL,''),(20020,'分类查询',2002,1,'','','','',1,0,'F','1','1','blog:category:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20021,'分类新增',2002,2,'','','','',1,0,'F','1','1','blog:category:add','#','admin','2025-11-12 09:54:08','',NULL,''),(20022,'分类修改',2002,3,'','','','',1,0,'F','1','1','blog:category:edit','#','admin','2025-11-12 09:54:08','',NULL,''),(20023,'分类删除',2002,4,'','','','',1,0,'F','1','1','blog:category:remove','#','admin','2025-11-12 09:54:08','',NULL,''),(20030,'标签查询',2003,1,'','','','',1,0,'F','1','1','blog:tag:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20031,'标签新增',2003,2,'','','','',1,0,'F','1','1','blog:tag:add','#','admin','2025-11-12 09:54:08','',NULL,''),(20032,'标签修改',2003,3,'','','','',1,0,'F','1','1','blog:tag:edit','#','admin','2025-11-12 09:54:08','',NULL,''),(20033,'标签删除',2003,4,'','','','',1,0,'F','1','1','blog:tag:remove','#','admin','2025-11-12 09:54:08','',NULL,''),(20034,'标签导出',2003,5,'','','','',1,0,'F','1','1','blog:tag:export','#','admin','2025-11-12 09:54:08','',NULL,''),(20040,'评论查询',2004,1,'','','','',1,0,'F','1','1','blog:comment:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20041,'评论审核',2004,2,'','','','',1,0,'F','1','1','blog:comment:approve','#','admin','2025-11-12 09:54:08','',NULL,''),(20042,'评论删除',2004,3,'','','','',1,0,'F','1','1','blog:comment:remove','#','admin','2025-11-12 09:54:08','',NULL,''),(20050,'设置查询',2005,1,'','','','',1,0,'F','1','1','blog:setting:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20051,'设置修改',2005,2,'','','','',1,0,'F','1','1','blog:setting:edit','#','admin','2025-11-12 09:54:08','',NULL,''),(20060,'友链查询',2006,1,'','','','',1,0,'F','1','1','blog:friendLink:query','#','admin','2025-11-12 09:54:08','',NULL,''),(20061,'友链新增',2006,2,'','','','',1,0,'F','1','1','blog:friendLink:add','#','admin','2025-11-12 09:54:08','',NULL,''),(20062,'友链修改',2006,3,'','','','',1,0,'F','1','1','blog:friendLink:edit','#','admin','2025-11-12 09:54:08','',NULL,''),(20063,'友链删除',2006,4,'','','','',1,0,'F','1','1','blog:friendLink:remove','#','admin','2025-11-12 09:54:08','',NULL,'');
/*!40000 ALTER TABLE `backup_sys_menu_perms_before_normalize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_menu_perms_blog_prefix_before_normalize`
--

DROP TABLE IF EXISTS `backup_sys_menu_perms_blog_prefix_before_normalize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_menu_perms_blog_prefix_before_normalize` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_menu_perms_blog_prefix_before_normalize`
--

LOCK TABLES `backup_sys_menu_perms_blog_prefix_before_normalize` WRITE;
/*!40000 ALTER TABLE `backup_sys_menu_perms_blog_prefix_before_normalize` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup_sys_menu_perms_blog_prefix_before_normalize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_menu_perms_friendlink_before_normalize`
--

DROP TABLE IF EXISTS `backup_sys_menu_perms_friendlink_before_normalize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_menu_perms_friendlink_before_normalize` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_menu_perms_friendlink_before_normalize`
--

LOCK TABLES `backup_sys_menu_perms_friendlink_before_normalize` WRITE;
/*!40000 ALTER TABLE `backup_sys_menu_perms_friendlink_before_normalize` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup_sys_menu_perms_friendlink_before_normalize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup_sys_role_menu_before_assign`
--

DROP TABLE IF EXISTS `backup_sys_role_menu_before_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_sys_role_menu_before_assign` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_sys_role_menu_before_assign`
--

LOCK TABLES `backup_sys_role_menu_before_assign` WRITE;
/*!40000 ALTER TABLE `backup_sys_role_menu_before_assign` DISABLE KEYS */;
INSERT INTO `backup_sys_role_menu_before_assign` VALUES (1,2000),(1,2001),(1,2002),(1,2003),(1,2004),(1,2005),(1,2006),(1,20010),(1,20011),(1,20012),(1,20013),(1,20020),(1,20021),(1,20022),(1,20023),(1,20030),(1,20031),(1,20032),(1,20033),(1,20034),(1,20040),(1,20041),(1,20042),(1,20050),(1,20051),(1,20060),(1,20061),(1,20062),(1,20063);
/*!40000 ALTER TABLE `backup_sys_role_menu_before_assign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_article`
--

DROP TABLE IF EXISTS `blog_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(200) NOT NULL COMMENT '文章标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `content` longtext NOT NULL COMMENT '文章内容',
  `cover_url` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `author_id` bigint DEFAULT NULL COMMENT '作者ID',
  `author_name` varchar(50) DEFAULT NULL COMMENT '作者名称',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶：0否 1是',
  `is_recommend` tinyint DEFAULT '0' COMMENT '是否推荐：0否 1是',
  `status` tinyint DEFAULT '0' COMMENT '文章状态：0草稿 1已发布',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_article`
--

LOCK TABLES `blog_article` WRITE;
/*!40000 ALTER TABLE `blog_article` DISABLE KEYS */;
INSERT INTO `blog_article` VALUES (1,'Spring Boot + Vue.js 全栈开发实战','本文介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用','<p># Spring Boot + Vue.js 全栈开发实战\n\n## 项目介绍\n本项目是基于Spring Boot 2.5.15和Vue.js 3.x构建的现代化博客系统。\n\n## 技术栈\n- 后端：Spring Boot + MyBatis + MySQL + Redis\n- 前端：Vue.js + Element Plus + Vite\n\n## 功能特性\n1. 用户管理\n2. 文章管理\n3. 分类标签\n4. 评论系统\n5. 权限控制\n\n## 总结\n通过本项目的实践，可以深入理解前后端分离开发的完整流程。</p>','',1,1,'admin',0,0,1,0,0,0,'0','','2025-12-02 13:20:37','','2025-12-03 22:56:31'),(2,'MySQL数据库优化实践','分享MySQL数据库性能优化的实用技巧和经验','# MySQL数据库优化实践\n\n## 索引优化\n合理使用索引可以大幅提升查询性能。\n\n## 查询优化\n避免全表扫描，优化SQL语句结构。\n\n## 配置优化\n调整MySQL配置参数，提升整体性能。','',1,1,'admin',0,0,1,0,0,0,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37'),(3,'Vue.js 3.0 新特性详解','深入了解Vue.js 3.0的Composition API等新特性','# Vue.js 3.0 新特性详解\n\n## Composition API\n新的组合式API提供了更好的逻辑复用能力。\n\n## 性能提升\nVue 3在性能方面有显著提升。\n\n## TypeScript支持\n更好的TypeScript集成。','',1,1,'admin',0,0,1,0,0,0,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37'),(5,'微软终于出手了！统一托管Windows 11所有应用更新','','<p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">快科技12月9日消息，</span><strong style=\"background-color: rgb(255, 255, 255); color: rgb(255, 0, 0);\">微软目前正在测试一项名为 “更新编排平台”（UOP）的新 API，旨在解决Windows 11上应用更新管理混乱的问题。</strong></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">这项新功能可能允许Windows 11根据用户对应用程序的使用情况，自动扫描、下载甚至安装应用程序的更新，使应用更新体验变得更加流畅。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">目前Windows 11拥有不少更新产品的途径，比如Windows Update负责处理每月的安全更新，Microsoft Store负责商店应用。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">同时还有Windows组件、开发者工具和大量第三方软件，它们都以自己的方式扫描、下载、安装和提示更新。</span></p><p><strong style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">为了终结这种混乱局面，微软构建一个新的Windows更新系统，允许应用或开发者通过UOP API接入这个平台。</strong></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">甚至未未在微软应用商店上架的第三方软件，或者第三方驱动程序，也可以使用此平台，届时，Windows将根据开发者提供的更新程序，决定何时更新应用或驱动程序。</span></p><p class=\"ql-align-center\"><u style=\"background-color: rgb(255, 255, 255); color: rgb(0, 153, 255);\"><a href=\"https://img1.mydrivers.com/img/20251209/277b1a8a-1c49-4dcd-9451-eb240e722239.jpg\" rel=\"noopener noreferrer\" target=\"_blank\"><img src=\"https://img1.mydrivers.com/img/20251209/S277b1a8a-1c49-4dcd-9451-eb240e722239.jpg\" alt=\"微软终于出手了！统一托管Windows 11所有应用更新\"></a></u></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">在Windows Latest的测试中，系统设置中已出现新的“应用更新”页面，位于“设置 &gt; 应用”之下，所有使用新UOP API的应用都将在此页面集中显示。</span></p><p><strong style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">这意味着Windows将能够自动下载或安装未在Microsoft Store上架的第三方应用的更新，并且用户可以在这一个页面上管理所有的应用更新。</strong></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">不过UOP将是一个可选功能，开发者需要注册为“更新提供者”，并需要向Windows提供一个可执行文件用于扫描更新，Windows随后会定期运行该扫描程序。</span></p><p class=\"ql-align-center\"><u style=\"background-color: rgb(255, 255, 255); color: rgb(0, 153, 255);\"><a href=\"https://img1.mydrivers.com/img/20251209/fcec26282bfa4727913d76a3dca286aa.jpg\" rel=\"noopener noreferrer\" target=\"_blank\"><img src=\"https://img1.mydrivers.com/img/20251209/s_fcec26282bfa4727913d76a3dca286aa.jpg\" alt=\"微软终于出手了！统一托管Windows 11所有应用更新\"></a></u></p><p><br></p>','',1,1,NULL,0,0,1,NULL,NULL,NULL,'0','','2025-12-09 20:31:21','',NULL);
/*!40000 ALTER TABLE `blog_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_article_tag`
--

DROP TABLE IF EXISTS `blog_article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_article_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_article_tag`
--

LOCK TABLES `blog_article_tag` WRITE;
/*!40000 ALTER TABLE `blog_article_tag` DISABLE KEYS */;
INSERT INTO `blog_article_tag` VALUES (19,1,1),(20,1,2),(21,1,3),(5,2,1),(6,2,4),(22,2,6),(7,2,8),(8,3,3),(9,3,6),(10,3,9),(29,5,11);
/*!40000 ALTER TABLE `blog_article_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_category`
--

DROP TABLE IF EXISTS `blog_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `alias` varchar(100) DEFAULT NULL COMMENT '分类别名',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID，0表示顶级分类',
  `sort_order` int DEFAULT '0' COMMENT '排序序号',
  `article_count` int DEFAULT '0' COMMENT '文章数量',
  `status` tinyint DEFAULT '1' COMMENT '状态：0禁用 1启用',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_category`
--

LOCK TABLES `blog_category` WRITE;
/*!40000 ALTER TABLE `blog_category` DISABLE KEYS */;
INSERT INTO `blog_category` VALUES (1,'技术分享',NULL,'技术相关文章',0,1,0,1,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37',1),(2,'生活随笔',NULL,'生活记录和感悟',0,2,0,1,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37',2),(3,'学习笔记',NULL,'学习过程中的笔记',0,3,0,1,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37',3),(4,'项目实战',NULL,'项目开发经验',0,4,0,1,'0','','2025-12-02 13:20:37','','2025-12-02 13:20:37',4);
/*!40000 ALTER TABLE `blog_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_comment`
--

DROP TABLE IF EXISTS `blog_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（可为空，匿名评论）',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称（匿名评论用）',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱（匿名评论用）',
  `content` text NOT NULL COMMENT '评论内容',
  `parent_id` bigint DEFAULT '0' COMMENT '父评论ID',
  `status` tinyint DEFAULT '1' COMMENT '状态 0待审核 1正常 2已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论IP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_comment`
--

LOCK TABLES `blog_comment` WRITE;
/*!40000 ALTER TABLE `blog_comment` DISABLE KEYS */;
INSERT INTO `blog_comment` VALUES (1,1,NULL,NULL,NULL,'hello',0,1,'2025-12-02 14:14:24',NULL),(2,4,NULL,NULL,NULL,'你好',0,1,'2025-12-05 22:07:08',NULL);
/*!40000 ALTER TABLE `blog_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_friend_link`
--

DROP TABLE IF EXISTS `blog_friend_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_friend_link` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '友链名称',
  `url` varchar(255) NOT NULL COMMENT '友链地址',
  `logo` varchar(255) DEFAULT NULL COMMENT '友链Logo',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态 0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客友链表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_friend_link`
--

LOCK TABLES `blog_friend_link` WRITE;
/*!40000 ALTER TABLE `blog_friend_link` DISABLE KEYS */;
INSERT INTO `blog_friend_link` VALUES (1,'Spring官网','https://spring.io/',NULL,'Spring框架官方网站',1,'2025-12-02 13:20:37',0,'2025-12-02 13:20:37',0,'',''),(2,'Vue.js官网','https://vuejs.org/',NULL,'Vue.js框架官方网站',1,'2025-12-02 13:20:37',0,'2025-12-02 13:20:37',0,'',''),(3,'Element Plus','https://element-plus.org/',NULL,'Vue 3组件库',1,'2025-12-02 13:20:37',0,'2025-12-02 13:20:37',0,'',''),(4,'GitHub','https://github.com/',NULL,'全球最大的代码托管平台',1,'2025-12-02 13:20:37',0,'2025-12-02 13:20:37',0,'','');
/*!40000 ALTER TABLE `blog_friend_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_setting`
--

DROP TABLE IF EXISTS `blog_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置项Key',
  `config_value` longtext COMMENT '配置项Value',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客系统设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_setting`
--

LOCK TABLES `blog_setting` WRITE;
/*!40000 ALTER TABLE `blog_setting` DISABLE KEYS */;
INSERT INTO `blog_setting` VALUES (1,'blog_name','我的博客','博客名称','2025-12-02 13:20:37','2025-12-02 13:20:37'),(2,'blog_desc','这是一个基于RuoYi-Vue的博客系统','博客描述','2025-12-02 13:20:37','2025-12-02 13:20:37'),(3,'blog_author','nevell','博客作者','2025-12-02 13:20:37','2025-12-02 13:20:37'),(4,'blog_keywords','博客,RuoYi,Vue,Spring Boot','博客关键词','2025-12-02 13:20:37','2025-12-02 13:20:37'),(5,'blog_copyright','Copyright © 2025 nevell','版权信息','2025-12-02 13:20:37','2025-12-02 13:20:37'),(6,'blog_beian','ICP备56789号','备案信息','2025-12-02 13:20:37','2025-12-09 22:23:25'),(7,'footer_enabled','true','是否显示底部','2025-12-03 13:45:32','2025-12-09 22:23:42'),(8,'copyright_enabled','true','是否显示版权信息','2025-12-03 13:45:32','2025-12-09 22:23:42'),(9,'comment_enabled','true','是否开启评论功能','2025-12-03 13:45:32','2025-12-09 22:23:42'),(10,'comment_review','true','评论是否需要审核','2025-12-03 13:45:32','2025-12-09 22:23:42'),(11,'like_enabled','true','是否开启点赞功能','2025-12-03 13:45:32','2025-12-09 22:23:42'),(12,'view_count_enabled','true','是否开启浏览统计','2025-12-03 13:45:32','2025-12-09 22:23:42'),(13,'share_enabled','true','是否开启分享功能','2025-12-03 13:45:32','2025-12-09 22:23:42'),(14,'search_enabled','true','是否开启搜索功能','2025-12-03 13:45:32','2025-12-09 22:23:42'),(15,'sidebar_enabled','true','是否显示侧边栏','2025-12-03 13:45:32','2025-12-09 22:23:42'),(16,'theme_color','#409EFF','主题颜色','2025-12-03 13:45:32','2025-12-03 13:45:32'),(17,'header_background','#304156','头部背景色','2025-12-03 13:45:32','2025-12-03 13:45:32'),(18,'sidebar_style','dark','侧边栏样式','2025-12-03 13:45:32','2025-12-03 13:45:32'),(19,'page_size','10','每页文章数','2025-12-03 13:45:32','2025-12-03 13:45:32'),(20,'hot_article_count','5','热门文章数','2025-12-03 13:45:32','2025-12-03 13:45:32'),(21,'recent_comment_count','5','最新评论数','2025-12-03 13:45:32','2025-12-03 13:45:32'),(22,'greeting_message','欢迎来到我的博客！','欢迎信息','2025-12-03 13:45:32','2025-12-03 13:45:32'),(23,'about_content','关于我','关于页面内容','2025-12-03 13:45:32','2025-12-09 22:23:42'),(24,'seo_title','','SEO标题','2025-12-03 13:45:32','2025-12-03 13:45:32'),(25,'seo_description','','SEO描述','2025-12-03 13:45:32','2025-12-03 13:45:32'),(26,'seo_canonical_url','','规范URL','2025-12-03 13:45:32','2025-12-03 13:45:32'),(27,'seo_robots','index,follow','Robots规则','2025-12-03 13:45:32','2025-12-03 13:45:32'),(28,'seo_favicon','tag','网站图标','2025-12-03 13:45:32','2025-12-09 22:23:09'),(29,'blog_avatar','','博主头像','2025-12-03 13:45:32','2025-12-03 13:45:32'),(30,'blog_signature','Hello','博主签名','2025-12-03 13:45:32','2025-12-09 22:22:58'),(31,'blog_start_time','2025-01-01','博客创建时间','2025-12-03 13:45:32','2025-12-03 13:45:32');
/*!40000 ALTER TABLE `blog_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_tag`
--

DROP TABLE IF EXISTS `blog_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '标签名称',
  `description` varchar(255) DEFAULT NULL COMMENT '标签描述',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '标签颜色',
  `icon` varchar(100) DEFAULT NULL COMMENT '标签图标',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志 0正常 1删除',
  `article_count` int DEFAULT '0' COMMENT '文章数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_tag`
--

LOCK TABLES `blog_tag` WRITE;
/*!40000 ALTER TABLE `blog_tag` DISABLE KEYS */;
INSERT INTO `blog_tag` VALUES (1,'Java','Java编程语言相关标签','#409EFF','el-icon-cpu','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(2,'Spring Boot','Spring Boot框架相关标签','#67C23A','el-icon-coin','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(3,'Vue.js','Vue.js前端框架相关标签','#E6A23C','el-icon-monitor','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(4,'MySQL','MySQL数据库相关标签','#F56C6C','el-icon-data-base','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(5,'前端开发','前端开发相关标签','#909399','el-icon-monitor','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(6,'后端开发','后端开发相关标签','#409EFF','el-icon-cpu','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(7,'数据库','数据库相关标签','#67C23A','el-icon-coin','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(8,'前端框架','前端框架相关标签','#E6A23C','el-icon-monitor','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(9,'开发工具','开发工具相关标签','#F56C6C','el-icon-tools','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(10,'算法','算法相关标签','#909399','el-icon-data-analysis','2025-12-02 13:20:37','2025-12-02 13:20:37',0,0),(11,'数码科技','自动创建的标签：数码科技','#FF6B6B',NULL,'2025-12-05 22:12:40','2025-12-05 22:12:40',0,0);
/*!40000 ALTER TABLE `blog_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2025-12-02 13:20:37','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2025-12-02 13:20:37','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2025-12-02 13:20:37','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2025-12-02 13:20:37','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2025-12-02 13:20:37','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2025-12-02 13:20:37','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）'),(7,'用户管理-初始密码修改策略','sys.account.initPasswordModify','1','Y','admin','2025-12-02 13:20:37','',NULL,'0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框'),(8,'用户管理-账号密码更新周期','sys.account.passwordValidateDays','0','Y','admin','2025-12-02 13:20:37','',NULL,'密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-12-02 13:20:37','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2025-12-02 13:20:37','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2025-12-02 13:20:37','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2025-12-02 13:20:37','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2025-12-02 13:20:37','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2025-12-02 13:20:37','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2025-12-02 13:20:37','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2025-12-02 13:20:37','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2025-12-02 13:20:37','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2025-12-02 13:20:37','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2025-12-02 13:20:37','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2025-12-02 13:20:37','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2025-12-02 13:20:37','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2025-12-02 13:20:37','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2025-12-02 13:20:37','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2025-12-02 13:20:37','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2025-12-02 13:20:37','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2025-12-02 13:20:37','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2025-12-02 13:20:37','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2025-12-02 13:20:37','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2025-12-02 13:20:37','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2025-12-02 13:20:37','',NULL,'停用状态');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '字典类型',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2025-12-02 13:20:37','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2025-12-02 13:20:37','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2025-12-02 13:20:37','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2025-12-02 13:20:37','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2025-12-02 13:20:37','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2025-12-02 13:20:37','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2025-12-02 13:20:37','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2025-12-02 13:20:37','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2025-12-02 13:20:37','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2025-12-02 13:20:37','',NULL,'登录状态列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2025-12-02 13:20:37','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2025-12-02 13:20:37','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2025-12-02 13:20:37','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日志信息',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作系统',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (100,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-02 13:34:27'),(101,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-02 14:13:54'),(102,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 13:06:38'),(103,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 13:34:07'),(104,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 14:00:18'),(105,'admin','192.168.65.1','内网IP','Downloading Tool','Unknown','1','验证码已失效','2025-12-03 14:02:48'),(106,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 22:06:55'),(107,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 22:29:10'),(108,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 22:37:38'),(109,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-03 23:33:42'),(110,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-04 23:14:08'),(111,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-05 22:04:00'),(112,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-05 22:21:29'),(113,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-05 22:54:19'),(114,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-05 23:20:54'),(115,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-09 20:29:14'),(116,'admin','172.18.0.5','内网IP','Chrome 14','Mac OS X','0','登录成功','2025-12-09 22:22:15');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2025-12-02 13:20:37','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2025-12-02 13:20:37','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2025-12-02 13:20:37','',NULL,'系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.vip',NULL,'','',0,0,'M','1','1','','guide','admin','2025-12-02 13:20:37','admin','2025-12-05 22:05:19','若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2025-12-02 13:20:37','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2025-12-02 13:20:37','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-12-02 13:20:37','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2025-12-02 13:20:37','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2025-12-02 13:20:37','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2025-12-02 13:20:37','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2025-12-02 13:20:37','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2025-12-02 13:20:37','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2025-12-02 13:20:37','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2025-12-02 13:20:37','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2025-12-02 13:20:37','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2025-12-02 13:20:37','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2025-12-02 13:20:37','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2025-12-02 13:20:37','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2025-12-02 13:20:37','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2025-12-02 13:20:37','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2025-12-02 13:20:37','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2025-12-02 13:20:37','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2025-12-02 13:20:37','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2025-12-02 13:20:37','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2025-12-02 13:20:37','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2025-12-02 13:20:37','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2025-12-02 13:20:37','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2025-12-02 13:20:37','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2025-12-02 13:20:37','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2025-12-02 13:20:37','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2025-12-02 13:20:37','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2025-12-02 13:20:37','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2025-12-02 13:20:37','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2025-12-02 13:20:37','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2025-12-02 13:20:37','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2025-12-02 13:20:37','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2025-12-02 13:20:37','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2025-12-02 13:20:37','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu_backup_20251027`
--

DROP TABLE IF EXISTS `sys_menu_backup_20251027`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu_backup_20251027` (
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu_backup_20251027`
--

LOCK TABLES `sys_menu_backup_20251027` WRITE;
/*!40000 ALTER TABLE `sys_menu_backup_20251027` DISABLE KEYS */;
INSERT INTO `sys_menu_backup_20251027` VALUES (1,'系统管理',0,50,'system',NULL,'','',1,0,'M','0','0','','system','admin','2025-07-18 18:58:46','admin','2025-08-13 16:04:27','系统管理目录'),(2,'系统监控',0,40,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2025-07-18 18:58:46','admin','2025-08-13 16:04:34','系统监控目录'),(3,'系统工具',0,20,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2025-07-18 18:58:46','admin','2025-08-13 16:04:09','系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.vip',NULL,'','',0,0,'F','0','1','','guide','admin','2025-07-18 18:58:46','admin','2025-08-13 16:06:36','若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2025-07-18 18:58:46','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2025-07-18 18:58:46','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-07-18 18:58:46','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2025-07-18 18:58:46','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2025-07-18 18:58:46','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2025-07-18 18:58:46','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2025-07-18 18:58:46','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2025-07-18 18:58:46','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2025-07-18 18:58:46','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2025-07-18 18:58:46','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2025-07-18 18:58:46','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2025-07-18 18:58:46','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2025-07-18 18:58:46','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2025-07-18 18:58:46','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2025-07-18 18:58:46','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2025-07-18 18:58:46','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2025-07-18 18:58:46','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2025-07-18 18:58:46','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2025-07-18 18:58:46','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2025-07-18 18:58:46','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2025-07-18 18:58:46','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2025-07-18 18:58:46','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2025-07-18 18:58:46','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2025-07-18 18:58:46','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2025-07-18 18:58:46','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2025-07-18 18:58:46','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2025-07-18 18:58:46','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2025-07-18 18:58:46','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2025-07-18 18:58:46','',NULL,''),(2000,'博客管理',0,10,'blog',NULL,'','',1,0,'M','0','0','','documentation','admin','2025-10-24 17:19:20','',NULL,'博客管理目录'),(2001,'文章管理',2007,1,'article','blog/article/index',NULL,'',1,0,'C','0','0','system:article:list','edit','admin','2025-07-18 20:11:29','admin','2025-08-26 15:33:03','博客文章菜单'),(2002,'博客文章查询',2001,1,'#','',NULL,'',1,0,'F','0','0','system:article:query','#','admin','2025-07-18 20:11:29','',NULL,''),(2003,'博客文章新增',2001,2,'#','',NULL,'',1,0,'F','0','0','system:article:add','#','admin','2025-07-18 20:11:29','',NULL,''),(2004,'博客文章修改',2001,3,'#','',NULL,'',1,0,'F','0','0','system:article:edit','#','admin','2025-07-18 20:11:29','',NULL,''),(2005,'博客文章删除',2001,4,'#','',NULL,'',1,0,'F','0','0','system:article:remove','#','admin','2025-07-18 20:11:29','',NULL,''),(2006,'博客文章导出',2001,5,'#','',NULL,'',1,0,'F','0','0','system:article:export','#','admin','2025-07-18 20:11:29','',NULL,''),(2007,'博客管理',0,1,'blog',NULL,NULL,'',1,0,'M','0','0','','documentation','admin','2025-08-26 15:33:03','',NULL,'博客管理目录'),(2008,'分类管理',2007,2,'category','blog/category/index',NULL,'',1,0,'C','0','0','system:category:list','list','admin','2025-08-26 15:33:03','',NULL,'博客分类管理菜单'),(2009,'标签管理',2007,3,'tag','blog/tag/index',NULL,'',1,0,'C','0','0','system:tag:list','tab','admin','2025-08-26 15:33:03','admin','2025-08-26 15:36:12','博客标签管理菜单'),(2010,'分类查询',2008,1,'#','',NULL,'',1,0,'F','0','0','system:category:query','#','admin','2025-08-26 15:33:03','',NULL,''),(2011,'分类新增',2008,2,'#','',NULL,'',1,0,'F','0','0','system:category:add','#','admin','2025-08-26 15:33:03','',NULL,''),(2012,'分类修改',2008,3,'#','',NULL,'',1,0,'F','0','0','system:category:edit','#','admin','2025-08-26 15:33:03','',NULL,''),(2013,'分类删除',2008,4,'#','',NULL,'',1,0,'F','0','0','system:category:remove','#','admin','2025-08-26 15:33:03','',NULL,''),(2014,'分类导出',2008,5,'#','',NULL,'',1,0,'F','0','0','system:category:export','#','admin','2025-08-26 15:33:03','',NULL,''),(2015,'标签查询',2009,1,'#','',NULL,'',1,0,'F','0','0','system:tag:query','#','admin','2025-08-26 15:33:03','',NULL,''),(2016,'标签新增',2009,2,'#','',NULL,'',1,0,'F','0','0','system:tag:add','#','admin','2025-08-26 15:33:03','',NULL,''),(2017,'标签修改',2009,3,'#','',NULL,'',1,0,'F','0','0','system:tag:edit','#','admin','2025-08-26 15:33:03','',NULL,''),(2018,'标签删除',2009,4,'#','',NULL,'',1,0,'F','0','0','system:tag:remove','#','admin','2025-08-26 15:33:03','',NULL,''),(2019,'标签导出',2009,5,'#','',NULL,'',1,0,'F','0','0','system:tag:export','#','admin','2025-08-26 15:33:03','',NULL,''),(2039,'评论管理',2020,4,'comment','blog/comment/index',NULL,'',1,0,'C','0','0','system:comment:list','comment','admin','2025-10-14 11:50:56','',NULL,'评论管理菜单'),(2040,'评论查询',2038,1,'#','',NULL,'',1,0,'F','0','0','system:comment:query','#','admin','2025-10-14 11:50:56','',NULL,''),(2041,'评论新增',2038,2,'#','',NULL,'',1,0,'F','0','0','system:comment:add','#','admin','2025-10-14 11:50:56','',NULL,''),(2042,'评论修改',2038,3,'#','',NULL,'',1,0,'F','0','0','system:comment:edit','#','admin','2025-10-14 11:50:56','',NULL,''),(2043,'评论删除',2038,4,'#','',NULL,'',1,0,'F','0','0','system:comment:remove','#','admin','2025-10-14 11:50:56','',NULL,''),(2044,'评论导出',2038,5,'#','',NULL,'',1,0,'F','0','0','system:comment:export','#','admin','2025-10-14 11:50:56','',NULL,''),(2045,'友链管理',2020,5,'friendLink','blog/friendLink/index',NULL,'',1,0,'C','0','0','system:friendLink:list','link','admin','2025-10-14 11:50:56','',NULL,'友链管理菜单'),(2046,'友链查询',2044,1,'#','',NULL,'',1,0,'F','0','0','system:friendLink:query','#','admin','2025-10-14 11:50:56','',NULL,''),(2047,'友链新增',2044,2,'#','',NULL,'',1,0,'F','0','0','system:friendLink:add','#','admin','2025-10-14 11:50:56','',NULL,''),(2048,'友链修改',2044,3,'#','',NULL,'',1,0,'F','0','0','system:friendLink:edit','#','admin','2025-10-14 11:50:56','',NULL,''),(2049,'友链删除',2044,4,'#','',NULL,'',1,0,'F','0','0','system:friendLink:remove','#','admin','2025-10-14 11:50:56','',NULL,''),(2050,'友链导出',2044,5,'#','',NULL,'',1,0,'F','0','0','system:friendLink:export','#','admin','2025-10-14 11:50:56','',NULL,''),(2051,'博客设置',2020,6,'setting','blog/setting/index',NULL,'',1,0,'C','0','0','system:setting:list','setting','admin','2025-10-14 11:50:56','',NULL,'博客设置菜单'),(2052,'设置查询',2050,1,'#','',NULL,'',1,0,'F','0','0','system:setting:query','#','admin','2025-10-14 11:50:56','',NULL,''),(2053,'设置新增',2050,2,'#','',NULL,'',1,0,'F','0','0','system:setting:add','#','admin','2025-10-14 11:50:56','',NULL,''),(2054,'设置修改',2050,3,'#','',NULL,'',1,0,'F','0','0','system:setting:edit','#','admin','2025-10-14 11:50:57','',NULL,''),(2055,'设置删除',2050,4,'#','',NULL,'',1,0,'F','0','0','system:setting:remove','#','admin','2025-10-14 11:50:57','',NULL,''),(20030,'标签查询',2003,1,'','','','',1,0,'F','0','0','system:tag:query','#','admin','2025-10-24 17:19:20','',NULL,''),(20031,'标签新增',2003,2,'','','','',1,0,'F','0','0','system:tag:add','#','admin','2025-10-24 17:19:20','',NULL,''),(20032,'标签修改',2003,3,'','','','',1,0,'F','0','0','system:tag:edit','#','admin','2025-10-24 17:19:20','',NULL,''),(20033,'标签删除',2003,4,'','','','',1,0,'F','0','0','system:tag:remove','#','admin','2025-10-24 17:19:20','',NULL,''),(20034,'标签导出',2003,5,'','','','',1,0,'F','0','0','system:tag:export','#','admin','2025-10-24 17:19:20','',NULL,'');
/*!40000 ALTER TABLE `sys_menu_backup_20251027` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2025-12-02 13:20:37','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2025-12-02 13:20:37','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (100,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"false\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:37:48',23),(101,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:37:48',23),(102,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:37:52',13),(103,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:37:55',17),(104,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:37:58',20),(105,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:05',13),(106,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:05',13),(107,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:10',12),(108,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:10',14),(109,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"blog_beian\",\"settingValue\":\"ICP备123456号\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:41',23),(110,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"false\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:41',23),(111,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:47',10),(112,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:47',10),(113,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:52',22),(114,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:48:52',22),(115,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:49:24',20),(116,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:49:24',19),(117,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:49:24',19),(118,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:49:24',19),(119,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(120,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(121,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(122,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(123,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(124,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',22),(125,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',6),(126,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',5),(127,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:55:29',6),(128,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":1,\"title\":\"Spring Boot + Vue.js 全栈开发实战\",\"summary\":\"本文介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用\",\"content\":\"<p># Spring Boot + Vue.js 全栈开发实战\\n\\n## 项目介绍\\n本项目是基于Spring Boot 2.5.15和Vue.js 3.x构建的现代化博客系统。\\n\\n## 技术栈\\n- 后端：Spring Boot + MyBatis + MySQL + Redis\\n- 前端：Vue.js + Element Plus + Vite\\n\\n## 功能特性\\n1. 用户管理\\n2. 文章管理\\n3. 分类标签\\n4. 评论系统\\n5. 权限控制\\n\\n## 总结\\n通过本项目的实践，可以深入理解前后端分离开发的完整流程。</p>\",\"coverUrl\":\"\",\"categoryId\":1,\"authorId\":1,\"author\":\"admin\",\"authorName\":\"admin\",\"isTop\":1,\"isRecommend\":1,\"status\":1,\"viewCount\":0,\"likeCount\":0,\"commentCount\":0,\"tagIds\":[1,2,3,7],\"tags\":[{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":1,\"name\":\"Java\",\"delFlag\":0,\"description\":\"Java编程语言相关标签\",\"color\":\"#409EFF\",\"icon\":\"el-icon-cpu\",\"tagName\":\"Java\",\"tagId\":1},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":2,\"name\":\"Spring Boot\",\"delFlag\":0,\"description\":\"Spring Boot框架相关标签\",\"color\":\"#67C23A\",\"icon\":\"el-icon-coin\",\"tagName\":\"Spring Boot\",\"tagId\":2},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":3,\"name\":\"Vue.js\",\"delFlag\":0,\"description\":\"Vue.js前端框架相关标签\",\"color\":\"#E6A23C\",\"icon\":\"el-icon-monitor\",\"tagName\":\"Vue.js\",\"tagId\":3},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":7,\"name\":\"数据库\",\"delFlag\":0,\"description\":\"数据库相关标签\",\"color\":\"#67C23A\",\"icon\":\"el-icon-coin\",\"tagName\":\"数据库\",\"tagId\":7}],\"coverImage\":\"\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-03 22:56:08',66),(129,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":1,\"title\":\"Spring Boot + Vue.js 全栈开发实战\",\"summary\":\"本文介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用\",\"content\":\"<p># Spring Boot + Vue.js 全栈开发实战\\n\\n## 项目介绍\\n本项目是基于Spring Boot 2.5.15和Vue.js 3.x构建的现代化博客系统。\\n\\n## 技术栈\\n- 后端：Spring Boot + MyBatis + MySQL + Redis\\n- 前端：Vue.js + Element Plus + Vite\\n\\n## 功能特性\\n1. 用户管理\\n2. 文章管理\\n3. 分类标签\\n4. 评论系统\\n5. 权限控制\\n\\n## 总结\\n通过本项目的实践，可以深入理解前后端分离开发的完整流程。</p>\",\"coverUrl\":\"\",\"categoryId\":1,\"authorId\":1,\"author\":\"admin\",\"authorName\":\"admin\",\"isTop\":0,\"isRecommend\":0,\"status\":1,\"viewCount\":0,\"likeCount\":0,\"commentCount\":0,\"tagIds\":[1,2,3,7],\"tags\":[{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":1,\"name\":\"Java\",\"delFlag\":0,\"description\":\"Java编程语言相关标签\",\"color\":\"#409EFF\",\"icon\":\"el-icon-cpu\",\"tagName\":\"Java\",\"tagId\":1},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":2,\"name\":\"Spring Boot\",\"delFlag\":0,\"description\":\"Spring Boot框架相关标签\",\"color\":\"#67C23A\",\"icon\":\"el-icon-coin\",\"tagName\":\"Spring Boot\",\"tagId\":2},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":3,\"name\":\"Vue.js\",\"delFlag\":0,\"description\":\"Vue.js前端框架相关标签\",\"color\":\"#E6A23C\",\"icon\":\"el-icon-monitor\",\"tagName\":\"Vue.js\",\"tagId\":3},{\"createTime\":\"2025-12-02 13:20:37\",\"updateTime\":\"2025-12-02 13:20:37\",\"id\":7,\"name\":\"数据库\",\"delFlag\":0,\"description\":\"数据库相关标签\",\"color\":\"#67C23A\",\"icon\":\"el-icon-coin\",\"tagName\":\"数据库\",\"tagId\":7}],\"coverImage\":\"\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-03 22:56:23',33),(130,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":1,\"status\":0,\"title\":\"Spring Boot + Vue.js 全栈开发实战\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-03 22:56:27',31),(131,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":1,\"status\":1,\"title\":\"Spring Boot + Vue.js 全栈开发实战\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-03 22:56:30',19),(132,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',15),(133,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',12),(134,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',13),(135,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',11),(136,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',15),(137,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',13),(138,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',6),(139,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',7),(140,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 22:57:12',5),(141,'博客评论',2,'com.ruoyi.system.controller.BlogCommentController.audit()','PUT',1,'admin','研发部门','/system/comment/audit/1','172.18.0.5','内网IP','[1]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 23:18:13',26),(142,'博客评论',2,'com.ruoyi.system.controller.BlogCommentController.audit()','PUT',1,'admin','研发部门','/system/comment/audit/1','172.18.0.5','内网IP','[1]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-03 23:18:58',10),(143,'博客评论',2,'com.ruoyi.system.controller.BlogCommentController.audit()','PUT',1,'admin','研发部门','/system/comment/audit/1','172.18.0.5','内网IP','[1]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-04 23:15:00',16),(144,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/4','172.18.0.5','内网IP','4','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',0,NULL,'2025-12-05 22:04:13',41),(145,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','172.18.0.5','内网IP','{\"children\":[],\"createTime\":\"2025-12-02 13:20:37\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"1\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-05 22:04:23',12),(146,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/4','172.18.0.5','内网IP','4','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',0,NULL,'2025-12-05 22:04:27',18),(147,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/4','172.18.0.5','内网IP','4','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',0,NULL,'2025-12-05 22:05:13',15),(148,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','172.18.0.5','内网IP','{\"children\":[],\"createTime\":\"2025-12-02 13:20:37\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"1\",\"updateBy\":\"admin\",\"visible\":\"1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-05 22:05:19',11),(149,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/4','172.18.0.5','内网IP','4','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',0,NULL,'2025-12-05 22:05:23',11),(150,'文章管理',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p class=\\\"q','{\"msg\":\"文章创建成功\",\"code\":200}',0,NULL,'2025-12-05 22:06:49',53),(151,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:08:04',33),(152,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/4','172.18.0.5','内网IP','4','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',0,NULL,'2025-12-05 22:08:27',5),(153,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"status\":0,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:11:11',26),(154,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"status\":1,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:11:43',24),(155,'博客标签',1,'com.ruoyi.system.controller.BlogTagController.add()','POST',1,'admin','研发部门','/system/tag','172.18.0.5','内网IP','{\"color\":\"#FF6B6B\",\"createBy\":\"admin\",\"delFlag\":0,\"description\":\"自动创建的标签：数码科技\",\"id\":11,\"name\":\"数码科技\",\"params\":{},\"tagId\":11,\"tagName\":\"数码科技\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-05 22:12:40',34),(156,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:12:44',21),(157,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:22:01',42),(158,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:54:54',56),(159,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:55:29',43),(160,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"status\":0,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:55:59',32),(161,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:56:24',42),(162,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"status\":1,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\"}','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:56:36',21),(163,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:57:39',27),(164,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 22:58:58',51),(165,'文章管理',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"id\":4,\"title\":\"关于调整2025高校科技成果交易会举办时间等有关事项的通知\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">各省、自治区、直辖市教育厅（教委），新疆生产建设兵团教育局，部属各高等学校、部省合建各高等学校：</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">为确保2025高校科技成果交易会（以下简称2025科交会）举办成效，经综合考虑，现将调整大会举办时间等事项通知如下。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 一、大会时间</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办时间：由2025年12月10—12日变更为12月15—17日，报到时间由12月9日变更为12月14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会场馆布置时间：12月13—14日。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">3.大会开幕活动时间：12月15日上午。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">4.高校技术经理人研修实践活动时间：12月13—17日，12日报到。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\"> 二、大会地点</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.大会举办地点：广州市琶洲广交会展馆D区（广东省广州市海珠区阅江中路380号）。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.大会报到地点：具体报到地点由会务接待组另行通知。不住宿人员于12月15日上午8:00-9:00在大会现场签到。</span></p><p class=\\\"ql-align-justify\\\"><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">\\t三、参会报名</strong></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">1.请已报名的参会单位于12月5日前，按照调整后的大会时间安排，登录2025科交会线上平台https://kjh.csrd.edu.cn，修改并确认参会人员及住宿需求。</span></p><p class=\\\"ql-align-justify\\\"><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);\\\">2.请未报名的参会单位于12月5日前，登录2025科交会线上平台报名并填写有关信息。</span></p><p cl','{\"msg\":\"文章更新成功\",\"code\":200}',0,NULL,'2025-12-05 23:21:14',34),(166,'文章管理',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/4','172.18.0.5','内网IP','[4]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 20:29:25',42),(167,'文章管理',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','172.18.0.5','内网IP','{\"title\":\"微软终于出手了！统一托管Windows 11所有应用更新\",\"summary\":\"\",\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">快科技12月9日消息，</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(255, 0, 0);\\\">微软目前正在测试一项名为 “更新编排平台”（UOP）的新 API，旨在解决Windows 11上应用更新管理混乱的问题。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">这项新功能可能允许Windows 11根据用户对应用程序的使用情况，自动扫描、下载甚至安装应用程序的更新，使应用更新体验变得更加流畅。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">目前Windows 11拥有不少更新产品的途径，比如Windows Update负责处理每月的安全更新，Microsoft Store负责商店应用。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">同时还有Windows组件、开发者工具和大量第三方软件，它们都以自己的方式扫描、下载、安装和提示更新。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">为了终结这种混乱局面，微软构建一个新的Windows更新系统，允许应用或开发者通过UOP API接入这个平台。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">甚至未未在微软应用商店上架的第三方软件，或者第三方驱动程序，也可以使用此平台，届时，Windows将根据开发者提供的更新程序，决定何时更新应用或驱动程序。</span></p><p class=\\\"ql-align-center\\\"><u style=\\\"background-color: rgb(255, 255, 255); color: rgb(0, 153, 255);\\\"><a href=\\\"https://img1.mydrivers.com/img/20251209/277b1a8a-1c49-4dcd-9451-eb240e722239.jpg\\\" rel=\\\"noopener noreferrer\\\" target=\\\"_blank\\\"><img src=\\\"https://img1.mydrivers.com/img/20251209/S277b1a8a-1c49-4dcd-9451-eb240e722239.jpg\\\" alt=\\\"微软终于出手了！统一托管Windows 11所有应用更新\\\"></a></u></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">在Windows Latest的测试中，系统设置中已出现新的“应用更新”页面，位于“设置 &gt; 应用”之下，所有使用新UOP API的应用都将在此页面集中显示。</span></p><p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">这意味着Windows将能够自动下载或安装未在Microsoft Store上架的第三方应用的更新，并且用户可以在这一个页面上管理所有的应用更新。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">不过UOP将是一个可选功能，开发者需要注册为“更新提供者”，并需要向Windows提供一个可执行文件用于扫描更新，Windows随后会定期运行该扫描程序。</span><','{\"msg\":\"文章创建成功\",\"code\":200}',0,NULL,'2025-12-09 20:31:21',38),(168,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"blog_signature\",\"settingValue\":\"Hello\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',33),(169,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',29),(170,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',29),(171,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',30),(172,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',11),(173,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',30),(174,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',32),(175,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',13),(176,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',50),(177,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:22:58',48),(178,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',10),(179,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',10),(180,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',12),(181,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"seo_favicon\",\"settingValue\":\"tag\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',14),(182,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',17),(183,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',10),(184,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',6),(185,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',5),(186,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',7),(187,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:09',5),(188,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',20),(189,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',19),(190,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',20),(191,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',20),(192,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',20),(193,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"blog_beian\",\"settingValue\":\"ICP备56789号\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',20),(194,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',6),(195,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',8),(196,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',9),(197,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:25',6),(198,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_review\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',22),(199,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"share_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',22),(200,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"view_count_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',22),(201,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"like_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',23),(202,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"search_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',23),(203,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"comment_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',23),(204,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"copyright_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',6),(205,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"footer_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',4),(206,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"about_content\",\"settingValue\":\"关于我\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',8),(207,'博客设置',2,'com.ruoyi.system.controller.BlogSettingController.updateByKeyPost()','POST',1,'admin','研发部门','/system/setting/updateByKey','172.18.0.5','内网IP','{\"params\":{},\"settingKey\":\"sidebar_enabled\",\"settingValue\":\"true\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-12-09 22:23:42',7);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,'0','admin','2025-12-02 13:20:37','',NULL,''),(2,'se','项目经理',2,'0','admin','2025-12-02 13:20:37','',NULL,''),(3,'hr','人力资源',3,'0','admin','2025-12-02 13:20:37','',NULL,''),(4,'user','普通员工',4,'0','admin','2025-12-02 13:20:37','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2025-12-02 13:20:37','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2025-12-02 13:20:37','',NULL,'普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '密码',
  `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','172.18.0.5','2025-12-09 22:22:16','2025-12-02 13:20:37','admin','2025-12-02 13:20:37','','2025-12-09 22:22:15','管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-12-02 13:20:37','2025-12-02 13:20:37','admin','2025-12-02 13:20:37','',NULL,'测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-09 22:28:30
