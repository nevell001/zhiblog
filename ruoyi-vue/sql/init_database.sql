-- ========== 完整的数据库初始化脚本 ==========
-- 创建时间：2025-11-07
-- 描述：整合所有SQL文件，创建完整的博客系统数据库

-- 1. 创建数据库和设置权限
CREATE DATABASE IF NOT EXISTS newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE newblog;

-- 设置权限，允许从Docker容器网络连接
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON newblog.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- ========== 导入若依系统基础表结构 ==========

-- 1、部门表
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
  dept_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '部门id',
  parent_id         BIGINT(20)      DEFAULT 0                  COMMENT '父部门id',
  ancestors         VARCHAR(50)     DEFAULT ''                 COMMENT '祖级列表',
  dept_name         VARCHAR(30)     DEFAULT ''                 COMMENT '部门名称',
  order_num         INT(4)          DEFAULT 0                  COMMENT '显示顺序',
  leader            VARCHAR(20)     DEFAULT NULL               COMMENT '负责人',
  phone             VARCHAR(11)     DEFAULT NULL               COMMENT '联系电话',
  email             VARCHAR(50)     DEFAULT NULL               COMMENT '邮箱',
  status            CHAR(1)         DEFAULT '0'                COMMENT '部门状态（0正常 1停用）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  PRIMARY KEY (dept_id)
) ENGINE=INNODB AUTO_INCREMENT=200 COMMENT = '部门表';

-- 初始化-部门表数据
INSERT INTO sys_dept VALUES(100,  0,   '0',          '若依科技',   0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(101,  100, '0,100',      '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(102,  100, '0,100',      '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(103,  101, '0,100,101',  '研发部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(104,  101, '0,100,101',  '市场部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(105,  101, '0,100,101',  '测试部门',   3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(106,  101, '0,100,101',  '财务部门',   4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(107,  101, '0,100,101',  '运维部门',   5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(108,  102, '0,100,102',  '市场部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept VALUES(109,  102, '0,100,102',  '财务部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', NOW(), '', NULL);

-- 2、用户信息表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  user_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '用户ID',
  dept_id           BIGINT(20)      DEFAULT NULL               COMMENT '部门ID',
  user_name         VARCHAR(30)     NOT NULL                   COMMENT '用户账号',
  nick_name         VARCHAR(30)     NOT NULL                   COMMENT '用户昵称',
  user_type         VARCHAR(2)      DEFAULT '00'               COMMENT '用户类型（00系统用户）',
  email             VARCHAR(50)     DEFAULT ''                 COMMENT '用户邮箱',
  phonenumber       VARCHAR(11)     DEFAULT ''                 COMMENT '手机号码',
  sex               CHAR(1)         DEFAULT '0'                COMMENT '用户性别（0男 1女 2未知）',
  avatar            VARCHAR(100)    DEFAULT ''                 COMMENT '头像地址',
  password          VARCHAR(100)    DEFAULT ''                 COMMENT '密码',
  status            CHAR(1)         DEFAULT '0'                COMMENT '账号状态（0正常 1停用）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  login_ip          VARCHAR(128)    DEFAULT ''                 COMMENT '最后登录IP',
  login_date        DATETIME                                   COMMENT '最后登录时间',
  pwd_update_date   DATETIME                                   COMMENT '密码最后更新时间',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (user_id)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '用户信息表';

-- 初始化-用户信息表数据
INSERT INTO sys_user VALUES(1,  103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), '', NULL, '管理员');
INSERT INTO sys_user VALUES(2,  105, 'ry',    '若依', '00', 'ry@qq.com',  '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), '', NULL, '测试员');

-- 3、岗位信息表
DROP TABLE IF EXISTS sys_post;
CREATE TABLE sys_post (
  post_id       BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '岗位ID',
  post_code     VARCHAR(64)     NOT NULL                   COMMENT '岗位编码',
  post_name     VARCHAR(50)     NOT NULL                   COMMENT '岗位名称',
  post_sort     INT(4)          NOT NULL                   COMMENT '显示顺序',
  status        CHAR(1)         NOT NULL                   COMMENT '状态（0正常 1停用）',
  create_by     VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time   DATETIME                                   COMMENT '创建时间',
  update_by     VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time   DATETIME                                   COMMENT '更新时间',
  remark        VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (post_id)
) ENGINE=INNODB COMMENT = '岗位信息表';

-- 初始化-岗位信息表数据
INSERT INTO sys_post VALUES(1, 'ceo',  '董事长',    1, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post VALUES(2, 'se',   '项目经理',  2, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post VALUES(3, 'hr',   '人力资源',  3, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post VALUES(4, 'user', '普通员工',  4, '0', 'admin', NOW(), '', NULL, '');

-- 4、角色信息表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
  role_id              BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '角色ID',
  role_name            VARCHAR(30)     NOT NULL                   COMMENT '角色名称',
  role_key             VARCHAR(100)    NOT NULL                   COMMENT '角色权限字符串',
  role_sort            INT(4)          NOT NULL                   COMMENT '显示顺序',
  data_scope           CHAR(1)         DEFAULT '1'                COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  menu_check_strictly  TINYINT(1)      DEFAULT 1                  COMMENT '菜单树选择项是否关联显示',
  dept_check_strictly  TINYINT(1)      DEFAULT 1                  COMMENT '部门树选择项是否关联显示',
  status               CHAR(1)         NOT NULL                   COMMENT '角色状态（0正常 1停用）',
  del_flag             CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by            VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time          DATETIME                                   COMMENT '创建时间',
  update_by            VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time          DATETIME                                   COMMENT '更新时间',
  remark               VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (role_id)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '角色信息表';

-- 初始化-角色信息表数据
INSERT INTO sys_role VALUES('1', '超级管理员',  'admin',  1, 1, 1, 1, '0', '0', 'admin', NOW(), '', NULL, '超级管理员');
INSERT INTO sys_role VALUES('2', '普通角色',    'common', 2, 2, 1, 1, '0', '0', 'admin', NOW(), '', NULL, '普通角色');

-- 5、菜单权限表
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
  menu_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '菜单ID',
  menu_name         VARCHAR(50)     NOT NULL                   COMMENT '菜单名称',
  parent_id         BIGINT(20)      DEFAULT 0                  COMMENT '父菜单ID',
  order_num         INT(4)          DEFAULT 0                  COMMENT '显示顺序',
  path              VARCHAR(200)    DEFAULT ''                 COMMENT '路由地址',
  component         VARCHAR(255)    DEFAULT NULL               COMMENT '组件路径',
  query             VARCHAR(255)    DEFAULT NULL               COMMENT '路由参数',
  route_name        VARCHAR(50)     DEFAULT ''                 COMMENT '路由名称',
  is_frame          INT(1)          DEFAULT 1                  COMMENT '是否为外链（0是 1否）',
  is_cache          INT(1)          DEFAULT 0                  COMMENT '是否缓存（0缓存 1不缓存）',
  menu_type         CHAR(1)         DEFAULT ''                 COMMENT '菜单类型（M目录 C菜单 F按钮）',
  visible           CHAR(1)         DEFAULT 0                  COMMENT '菜单状态（0显示 1隐藏）',
  status            CHAR(1)         DEFAULT 0                  COMMENT '菜单状态（0正常 1停用）',
  perms             VARCHAR(100)    DEFAULT NULL               COMMENT '权限标识',
  icon              VARCHAR(100)    DEFAULT '#'                COMMENT '菜单图标',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT ''                 COMMENT '备注',
  PRIMARY KEY (menu_id)
) ENGINE=INNODB AUTO_INCREMENT=2000 COMMENT = '菜单权限表';

-- 初始化-菜单信息表数据
INSERT INTO sys_menu VALUES('1', '系统管理', '0', '1', 'system',           NULL, '', '', 1, 0, 'M', '0', '0', '', 'system',   'admin', NOW(), '', NULL, '系统管理目录');
INSERT INTO sys_menu VALUES('2', '系统监控', '0', '2', 'monitor',          NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', NOW(), '', NULL, '系统监控目录');
INSERT INTO sys_menu VALUES('3', '系统工具', '0', '3', 'tool',             NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', NOW(), '', NULL, '系统工具目录');
INSERT INTO sys_menu VALUES('4', '若依官网', '0', '4', 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '0', '0', '', 'guide',    'admin', NOW(), '', NULL, '若依官网地址');

-- 6、角色和菜单关联表
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
  role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
  menu_id   BIGINT(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (role_id, menu_id)
) ENGINE=INNODB COMMENT = '角色和菜单关联表';

-- ========== 导入Quartz定时任务表结构 ==========

-- 按照正确的顺序删除表（先删除有外键约束的表）
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;

-- 1、存储每一个已配置的 jobDetail 的详细信息
CREATE TABLE QRTZ_JOB_DETAILS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    job_name             VARCHAR(200)    NOT NULL            COMMENT '任务名称',
    job_group            VARCHAR(200)    NOT NULL            COMMENT '任务组名',
    description          VARCHAR(250)    NULL                COMMENT '相关介绍',
    job_class_name       VARCHAR(250)    NOT NULL            COMMENT '执行任务类名称',
    is_durable           VARCHAR(1)      NOT NULL            COMMENT '是否持久化',
    is_nonconcurrent     VARCHAR(1)      NOT NULL            COMMENT '是否并发',
    is_update_data       VARCHAR(1)      NOT NULL            COMMENT '是否更新数据',
    requests_recovery    VARCHAR(1)      NOT NULL            COMMENT '是否接受恢复执行',
    job_data             BLOB            NULL                COMMENT '存放持久化job对象',
    PRIMARY KEY (sched_name, job_name, job_group)
) ENGINE=INNODB COMMENT = '任务详细信息表';

-- 2、存储已配置的 Trigger 的信息
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
CREATE TABLE QRTZ_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT '触发器的名字',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT '触发器所属组的名字',
    job_name             VARCHAR(200)    NOT NULL            COMMENT 'qrtz_job_details表job_name的外键',
    job_group            VARCHAR(200)    NOT NULL            COMMENT 'qrtz_job_details表job_group的外键',
    description          VARCHAR(250)    NULL                COMMENT '相关介绍',
    next_fire_time       BIGINT(13)      NULL                COMMENT '上一次触发时间（毫秒）',
    prev_fire_time       BIGINT(13)      NULL                COMMENT '下一次触发时间（默认为-1表示不触发）',
    priority             INTEGER         NULL                COMMENT '优先级',
    trigger_state        VARCHAR(16)     NOT NULL            COMMENT '触发器状态',
    trigger_type         VARCHAR(8)      NOT NULL            COMMENT '触发器的类型',
    start_time           BIGINT(13)      NOT NULL            COMMENT '开始时间',
    end_time             BIGINT(13)      NULL                COMMENT '结束时间',
    calendar_name        VARCHAR(200)    NULL                COMMENT '日程表名称',
    misfire_instr        SMALLINT(2)     NULL                COMMENT '补偿执行的策略',
    job_data             BLOB            NULL                COMMENT '存放持久化job对象',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, job_name, job_group) REFERENCES QRTZ_JOB_DETAILS(sched_name, job_name, job_group)
) ENGINE=INNODB COMMENT = '触发器详细信息表';

-- 3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_name的外键',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    repeat_count         BIGINT(7)       NOT NULL            COMMENT '重复的次数统计',
    repeat_interval      BIGINT(12)      NOT NULL            COMMENT '重复的间隔时间',
    times_triggered      BIGINT(10)      NOT NULL            COMMENT '已经触发的次数',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=INNODB COMMENT = '简单触发器的信息表';

-- 4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
CREATE TABLE QRTZ_CRON_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_name的外键',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    cron_expression      VARCHAR(200)    NOT NULL            COMMENT 'cron表达式',
    time_zone_id         VARCHAR(80)                         COMMENT '时区',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=INNODB COMMENT = 'Cron类型的触发器表';

-- 5、 Trigger 作为 Blob 类型存储
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
CREATE TABLE QRTZ_BLOB_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_name的外键',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    blob_data            BLOB            NULL                COMMENT '存放持久化Trigger对象',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=INNODB COMMENT = 'Blob类型的触发器表';

-- 6、 以 Blob 类型存储存放日历信息
DROP TABLE IF EXISTS QRTZ_CALENDARS;
CREATE TABLE QRTZ_CALENDARS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    calendar_name        VARCHAR(200)    NOT NULL            COMMENT '日历名称',
    calendar             BLOB            NOT NULL            COMMENT '存放持久化calendar对象',
    PRIMARY KEY (sched_name, calendar_name)
) ENGINE=INNODB COMMENT = '日历信息表';

-- 7、 存储已暂停的 Trigger 组的信息
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    PRIMARY KEY (sched_name, trigger_group)
) ENGINE=INNODB COMMENT = '暂停的触发器表';

-- 8、 存储与已触发的 Trigger 相关的状态信息
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
CREATE TABLE QRTZ_FIRED_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    entry_id             VARCHAR(95)     NOT NULL            COMMENT '调度器实例id',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_name的外键',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    instance_name        VARCHAR(200)    NOT NULL            COMMENT '调度器实例名',
    fired_time           BIGINT(13)      NOT NULL            COMMENT '触发的时间',
    sched_time           BIGINT(13)      NOT NULL            COMMENT '定时器制定的时间',
    priority             INTEGER         NOT NULL            COMMENT '优先级',
    state                VARCHAR(16)     NOT NULL            COMMENT '状态',
    job_name             VARCHAR(200)    NULL                COMMENT '任务名称',
    job_group            VARCHAR(200)    NULL                COMMENT '任务组名',
    is_nonconcurrent     VARCHAR(1)      NULL                COMMENT '是否并发',
    requests_recovery    VARCHAR(1)      NULL                COMMENT '是否接受恢复执行',
    PRIMARY KEY (sched_name, entry_id)
) ENGINE=INNODB COMMENT = '已触发的触发器表';

-- 9、 存储调度器的状态信息
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
CREATE TABLE QRTZ_SCHEDULER_STATE (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    instance_name        VARCHAR(200)    NOT NULL            COMMENT '实例名称',
    last_checkin_time    BIGINT(13)      NOT NULL            COMMENT '上次检查时间',
    checkin_interval     BIGINT(13)      NOT NULL            COMMENT '检查间隔时间',
    PRIMARY KEY (sched_name, instance_name)
) ENGINE=INNODB COMMENT = '调度器状态表';

-- 10、 存储程序的悲观锁的信息
DROP TABLE IF EXISTS QRTZ_LOCKS;
CREATE TABLE QRTZ_LOCKS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    lock_name            VARCHAR(40)     NOT NULL            COMMENT '悲观锁名称',
    PRIMARY KEY (sched_name, lock_name)
) ENGINE=INNODB COMMENT = '存储的悲观锁信息表';

-- 11、 Quartz集群实现同步机制的行锁表
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
CREATE TABLE QRTZ_SIMPROP_TRIGGERS (
    sched_name           VARCHAR(120)    NOT NULL            COMMENT '调度名称',
    trigger_name         VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_name的外键',
    trigger_group        VARCHAR(200)    NOT NULL            COMMENT 'qrtz_triggers表trigger_group的外键',
    str_prop_1           VARCHAR(512)    NULL                COMMENT 'String类型的trigger的第一个参数',
    str_prop_2           VARCHAR(512)    NULL                COMMENT 'String类型的trigger的第二个参数',
    str_prop_3           VARCHAR(512)    NULL                COMMENT 'String类型的trigger的第三个参数',
    int_prop_1           INT             NULL                COMMENT 'int类型的trigger的第一个参数',
    int_prop_2           INT             NULL                COMMENT 'int类型的trigger的第二个参数',
    long_prop_1          BIGINT          NULL                COMMENT 'long类型的trigger的第一个参数',
    long_prop_2          BIGINT          NULL                COMMENT 'long类型的trigger的第二个参数',
    dec_prop_1           NUMERIC(13,4)   NULL                COMMENT 'decimal类型的trigger的第一个参数',
    dec_prop_2           NUMERIC(13,4)   NULL                COMMENT 'decimal类型的trigger的第二个参数',
    bool_prop_1          VARCHAR(1)      NULL                COMMENT 'Boolean类型的trigger的第一个参数',
    bool_prop_2          VARCHAR(1)      NULL                COMMENT 'Boolean类型的trigger的第二个参数',
    PRIMARY KEY (sched_name, trigger_name, trigger_group),
    FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
) ENGINE=INNODB COMMENT = '同步机制的行锁表';

-- ========== 导入博客系统完整表结构和数据 ==========

-- 博客文章表
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `summary` varchar(512) DEFAULT NULL COMMENT '摘要',
  `content` longtext NOT NULL COMMENT '文章内容',
  `cover_url` varchar(512) DEFAULT NULL COMMENT '封面图片',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `author_id` bigint NOT NULL COMMENT '作者ID（关联sys_user）',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶 0否 1是',
  `is_recommend` tinyint DEFAULT '0' COMMENT '是否推荐 0否 1是',
  `status` tinyint DEFAULT '1' COMMENT '状态 0草稿 1发布',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志 0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章表';



-- ========== 初始化博客系统数据 ==========

-- 初始化博客设置数据
INSERT INTO `blog_setting` (`config_key`, `config_value`, `description`) VALUES
('blog_name', '我的博客', '博客名称'),
('blog_desc', '这是一个基于RuoYi-Vue的博客系统', '博客描述'),
('blog_author', 'nevell', '博客作者'),
('blog_keywords', '博客,RuoYi,Vue,Spring Boot', '博客关键词'),
('blog_copyright', 'Copyright © 2025 nevell', '版权信息'),
('blog_beian', 'ICP备XXXXXXXX号', '备案信息');

-- 插入博客分类示例数据
INSERT INTO `blog_category` (`name`, `sort`) VALUES
('技术分享', 1),
('生活随笔', 2),
('学习笔记', 3),
('项目实战', 4);

-- 插入博客标签示例数据
INSERT INTO `blog_tag` (`name`, `description`, `color`, `icon`) VALUES
('Java', 'Java编程语言相关标签', '#409EFF', 'el-icon-cpu'),
('Spring Boot', 'Spring Boot框架相关标签', '#67C23A', 'el-icon-coin'),
('Vue.js', 'Vue.js前端框架相关标签', '#E6A23C', 'el-icon-monitor'),
('MySQL', 'MySQL数据库相关标签', '#F56C6C', 'el-icon-data-base'),
('前端开发', '前端开发相关标签', '#909399', 'el-icon-monitor'),
('后端开发', '后端开发相关标签', '#409EFF', 'el-icon-cpu'),
('数据库', '数据库相关标签', '#67C23A', 'el-icon-coin'),
('前端框架', '前端框架相关标签', '#E6A23C', 'el-icon-monitor'),
('开发工具', '开发工具相关标签', '#F56C6C', 'el-icon-tools'),
('算法', '算法相关标签', '#909399', 'el-icon-data-analysis');

-- 插入博客文章示例数据
INSERT INTO `blog_article` (`title`, `summary`, `content`, `cover_url`, `category_id`, `author_id`, `is_top`, `is_recommend`, `status`) VALUES
('Spring Boot + Vue.js 全栈开发实战', '本文介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用', 
'# Spring Boot + Vue.js 全栈开发实战

## 项目介绍
本项目是基于Spring Boot 2.5.15和Vue.js 3.x构建的现代化博客系统。

## 技术栈
- 后端：Spring Boot + MyBatis + MySQL + Redis
- 前端：Vue.js + Element Plus + Vite

## 功能特性
1. 用户管理
2. 文章管理
3. 分类标签
4. 评论系统
5. 权限控制

## 总结
通过本项目的实践，可以深入理解前后端分离开发的完整流程。', 
'', 1, 1, 1, 1, 1),

('MySQL数据库优化实践', '分享MySQL数据库性能优化的实用技巧和经验', 
'# MySQL数据库优化实践

## 索引优化
合理使用索引可以大幅提升查询性能。

## 查询优化
避免全表扫描，优化SQL语句结构。

## 配置优化
调整MySQL配置参数，提升整体性能。', 
'', 1, 1, 0, 0, 1),

('Vue.js 3.0 新特性详解', '深入了解Vue.js 3.0的Composition API等新特性', 
'# Vue.js 3.0 新特性详解

## Composition API
新的组合式API提供了更好的逻辑复用能力。

## 性能提升
Vue 3在性能方面有显著提升。

## TypeScript支持
更好的TypeScript集成。', 
'', 1, 1, 0, 0, 1);

-- 插入文章标签关联数据
INSERT INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 7),
(2, 1), (2, 4), (2, 8),
(3, 3), (3, 6), (3, 9);

-- 插入友情链接示例数据
INSERT INTO `blog_friend_link` (`name`, `url`, `description`, `status`) VALUES
('Spring官网', 'https://spring.io/', 'Spring框架官方网站', 1),
('Vue.js官网', 'https://vuejs.org/', 'Vue.js框架官方网站', 1),
('Element Plus', 'https://element-plus.org/', 'Vue 3组件库', 1),
('GitHub', 'https://github.com/', '全球最大的代码托管平台', 1);

-- ========== 配置博客管理菜单 ==========

-- 博客管理主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 10, 'admin/blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录');

-- 文章管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2001, '文章管理', 2000, 1, 'article', 'blog/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '文章管理菜单');

-- 分类管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2002, '分类管理', 2000, 2, 'category', 'blog/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '分类管理菜单');

-- 标签管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '标签管理', 2000, 3, 'tag', 'blog/tag/index', '', '', 1, 0, 'C', '0', '0', 'blog:tag:list', 'tag', 'admin', NOW(), '', NULL, '标签管理菜单');

-- 评论管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2004, '评论管理', 2000, 4, 'comment', 'blog/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '评论管理菜单');

-- 博客设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2005, '博客设置', 2000, 5, 'setting', 'blog/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'setting', 'admin', NOW(), '', NULL, '博客设置菜单');

-- 友链管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '友链管理', 2000, 6, 'friendLink', 'blog/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友链管理菜单');

-- 为管理员角色分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2000), (1, 2001), (1, 2002), (1, 2003), (1, 2004), (1, 2005), (1, 2006);

-- ========== 配置博客管理按钮权限 ==========

-- 文章管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20010, '文章查询', 2001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', NOW(), '', NULL, ''),
(20011, '文章新增', 2001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', NOW(), '', NULL, ''),
(20012, '文章修改', 2001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', NOW(), '', NULL, ''),
(20013, '文章删除', 2001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', NOW(), '', NULL, '');

-- 分类管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20020, '分类查询', 2002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:query', '#', 'admin', NOW(), '', NULL, ''),
(20021, '分类新增', 2002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:add', '#', 'admin', NOW(), '', NULL, ''),
(20022, '分类修改', 2002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:edit', '#', 'admin', NOW(), '', NULL, ''),
(20023, '分类删除', 2002, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:remove', '#', 'admin', NOW(), '', NULL, '');

-- 标签管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20030, '标签查询', 2003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:query', '#', 'admin', NOW(), '', NULL, ''),
(20031, '标签新增', 2003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:add', '#', 'admin', NOW(), '', NULL, ''),
(20032, '标签修改', 2003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:edit', '#', 'admin', NOW(), '', NULL, ''),
(20033, '标签删除', 2003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:remove', '#', 'admin', NOW(), '', NULL, ''),
(20034, '标签导出', 2003, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:export', '#', 'admin', NOW(), '', NULL, '');

-- 评论管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20040, '评论查询', 2004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:query', '#', 'admin', NOW(), '', NULL, ''),
(20041, '评论审核', 2004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:approve', '#', 'admin', NOW(), '', NULL, ''),
(20042, '评论删除', 2004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:remove', '#', 'admin', NOW(), '', NULL, '');

-- 博客设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20050, '设置查询', 2005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''),
(20051, '设置修改', 2005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, '');

-- 友链管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20060, '友链查询', 2006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''),
(20061, '友链新增', 2006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''),
(20062, '友链修改', 2006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''),
(20063, '友链删除', 2006, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配所有博客管理按钮权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 20010), (1, 20011), (1, 20012), (1, 20013),
(1, 20020), (1, 20021), (1, 20022), (1, 20023),
(1, 20030), (1, 20031), (1, 20032), (1, 20033), (1, 20034),
(1, 20040), (1, 20041), (1, 20042),
(1, 20050), (1, 20051),
(1, 20060), (1, 20061), (1, 20062), (1, 20063);

-- ========== 数据库初始化完成 ==========

SELECT '数据库初始化完成！' AS result;
SELECT '若依系统基础表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'sys_%';
SELECT '博客系统表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'blog_%';
SELECT 'Quartz定时任务表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'QRTZ_%';
SELECT '博客管理菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_name = '博客管理' OR parent_id = 2000;



-- ========== 初始化博客数据 ==========

-- 初始化博客分类数据
INSERT IGNORE INTO `blog_category` (`name`, `sort`) VALUES
('Java技术', 1),
('Spring生态', 2),
('前端开发', 3),
('数据库', 4),
('项目实践', 5);

-- 初始化博客标签数据
INSERT IGNORE INTO `blog_tag` (`name`, `description`, `color`, `icon`) VALUES 
('Java', 'Java编程语言相关', '#FF6B6B', 'el-icon-coffee-cup'),
('Spring Boot', 'Spring Boot框架相关', '#4ECDC4', 'el-icon-leaf'),
('Vue.js', 'Vue.js前端框架相关', '#45B7D1', 'el-icon-monitor'),
('MySQL', 'MySQL数据库相关', '#96CEB4', 'el-icon-data-board'),
('Redis', 'Redis缓存相关', '#FECA57', 'el-icon-cloudy'),
('前端开发', '前端开发技术', '#FF9FF3', 'el-icon-brush'),
('后端开发', '后端开发技术', '#54A0FF', 'el-icon-cpu'),
('数据库', '数据库相关技术', '#5F27CD', 'el-icon-database'),
('框架学习', '各种框架学习', '#00D2D3', 'el-icon-reading'),
('项目经验', '项目开发经验', '#FF9F43', 'el-icon-trophy');

-- 初始化友情链接数据
INSERT IGNORE INTO `blog_friend_link` (`name`, `url`, `description`, `status`) VALUES
('Spring官网', 'https://spring.io/', 'Spring框架官方网站', 1),
('Vue.js官网', 'https://vuejs.org/', 'Vue.js框架官方网站', 1),
('Element Plus', 'https://element-plus.org/', 'Vue 3组件库', 1),
('GitHub', 'https://github.com/', '全球最大的代码托管平台', 1),
('MDN Web Docs', 'https://developer.mozilla.org/', 'Web开发者资源', 1);

-- 初始化博客设置数据
INSERT IGNORE INTO `blog_setting` (`config_key`, `config_value`, `description`) VALUES
('blog_name', '我的技术博客', '博客名称'),
('blog_desc', '分享技术，记录生活，成长路上的点点滴滴', '博客描述'),
('blog_author', 'Nevell', '博客作者'),
('blog_keywords', 'Java,Spring Boot,Vue.js,前端,后端,全栈开发', '博客关键词'),
('blog_copyright', 'Copyright © 2025 我的技术博客. All rights reserved.', '版权信息'),
('blog_beian', 'ICP备XXXXXXXX号', '备案信息'),
('blog_comment_enable', '1', '是否开启评论功能'),
('blog_comment_audit', '1', '评论是否需要审核');

-- 初始化示例文章数据
INSERT IGNORE INTO `blog_article` (`title`, `summary`, `content`, `category_id`, `author_id`, `is_top`, `is_recommend`, `status`) VALUES
('Spring Boot入门教程', '本文介绍Spring Boot的基本概念和快速入门方法', 'Spring Boot是一个基于Spring框架的快速开发脚手架...', 2, 1, 1, 1, 1),
('Vue.js组件化开发实践', '分享Vue.js组件化开发的最佳实践和经验', 'Vue.js作为现代前端框架，组件化是其核心特性...', 3, 1, 0, 1, 1),
('MySQL性能优化技巧', '介绍MySQL数据库性能优化的常用技巧和方法', 'MySQL作为最流行的关系型数据库，性能优化是DBA必备技能...', 4, 1, 0, 0, 1);

-- ========== 博客系统菜单权限配置 ==========

-- 创建博客管理主菜单（如果不存在）
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (2000, '博客管理', 0, 10, 'admin/blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '博客管理目录');

-- 创建博客管理子菜单
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES 
(2001, '文章管理', 2000, 1, 'article', 'blog/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '文章管理菜单'),
(2002, '分类管理', 2000, 2, 'category', 'blog/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '分类管理菜单'),
(2003, '标签管理', 2000, 3, 'tag', 'blog/tag/index', '', '', 1, 0, 'C', '0', '0', 'blog:tag:list', 'tag', 'admin', NOW(), '标签管理菜单'),
(2004, '评论管理', 2000, 4, 'comment', 'blog/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '评论管理菜单'),
(2005, '博客设置', 2000, 5, 'setting', 'blog/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'setting', 'admin', NOW(), '博客设置菜单'),
(2006, '友链管理', 2000, 6, 'friendLink', 'blog/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '友链管理菜单');

-- 为管理员角色分配博客管理菜单权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2000);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2001);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2002);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2003);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2004);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2005);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2006);

-- ========== 博客系统完成提示 ==========

SELECT '博客系统数据库初始化完成！' AS '系统提示';
SELECT '博客系统包含以下功能模块：' AS '功能模块';
SELECT '✓ 文章管理' AS '模块';
SELECT '✓ 分类管理' AS '模块';
SELECT '✓ 标签管理' AS '模块';
SELECT '✓ 评论管理' AS '模块';
SELECT '✓ 友链管理' AS '模块';
SELECT '✓ 系统设置' AS '模块';
SELECT '管理员账号：admin / 密码：admin123' AS '登录信息';
SELECT '博客前台地址：http://localhost:8080/blog' AS '前台地址';
SELECT '博客后台地址：http://localhost:8080' AS '后台地址';