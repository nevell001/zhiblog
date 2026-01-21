-- ===============================================================
-- 🌟 博客系统完整数据库初始化脚本
-- ===============================================================
-- 📅 创建时间：2025-11-07
-- 🔧 最后更新：2026-01-13
-- 📝 描述：整合所有SQL文件，创建完整的博客系统数据库
-- 🚀 版本：v2.1.0 (完整版)
--
-- 📋 包含内容：
-- ✅ 若依系统基础表结构和数据 (21个表，包含sys_config、sys_user_role、sys_role_dept、sys_user_post、sys_dict_type、sys_dict_data、sys_notice、sys_logininfor、sys_oper_log)
-- ✅ Quartz定时任务表结构 (11个表)
-- ✅ 博客系统表结构和数据 (7个表)
-- ✅ 代码生成器表结构 (2个表：gen_table、gen_table_column)
-- ✅ 性能优化索引 (20+个索引)
-- ✅ 数据完整性约束和触发器
-- ✅ 完整的示例数据 (文章6篇、分类14个、标签19个、友链10个)
-- ✅ 博客管理菜单和权限配置
-- ✅ 系统管理菜单和权限配置
-- ✅ 系统监控菜单和权限配置（包含Actuator、Prometheus、Grafana、登录日志、操作日志）
-- ✅ 数据统计菜单和权限配置
-- ✅ 系统工具菜单和权限配置
--
-- 🔧 技术栈：
-- - MySQL 8.0+
-- - Spring Boot 3.3.0
-- - Vue.js 3.x
-- - MyBatis
-- - Redis
--
-- 📊 数据库统计：
-- - 总表数：41个（包含完整的若依系统表、Quartz表、博客系统表和代码生成器表）
-- - 总设置项：38个
-- - 示例文章：6篇
-- - 示例分类：14个 (含层级结构)
-- - 示例标签：26个
-- - 友情链接：10个
--
-- ⚠️ 重要提示：
-- 本脚本已整合所有必要的表结构和数据，完整的数据库初始化只需执行此脚本即可
-- 所有相关SQL文件已整合，包括系统管理、系统监控、数据统计、代码生成器等模块
--
-- ✅ 正确的初始化步骤：
-- 1. 创建数据库：CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 2. 导入本脚本：mysql -u root -p newblog < init_database.sql
-- 3. （可选）添加性能索引：mysql -u root -p newblog < performance_indexes.sql
--
-- 🎯 使用方法：
-- 1. 创建数据库：CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 2. 选择数据库：USE newblog;
-- 3. 执行脚本：source /path/to/init_database.sql;
-- 4. 验证结果：查看最后的统计信息
--
-- ⚠️  注意事项：
-- - 请确保MySQL版本 >= 8.0
-- - 执行前请备份现有数据
-- - 脚本使用了INSERT IGNORE，重复执行不会报错
-- - 包含触发器和索引，执行时间约2-3分钟
-- ===============================================================

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

-- 4、参数配置表（已修复config_value字段长度）
DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config (
  config_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '参数主键',
  config_name       VARCHAR(100)    DEFAULT ''                 COMMENT '参数名称',
  config_key        VARCHAR(100)    DEFAULT ''                 COMMENT '参数键名',
  config_value      VARCHAR(1000)   DEFAULT ''                 COMMENT '参数键值（已扩展到1000字符）',
  config_type       CHAR(1)         DEFAULT 'N'                COMMENT '系统内置（Y是 N否）',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (config_id),
  INDEX idx_config_key (config_key)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '参数配置表';

-- 初始化-参数配置表数据
INSERT INTO sys_config VALUES(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',               'skin-blue',     'Y', 'admin', NOW(), '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
INSERT INTO sys_config VALUES(2, '用户管理-账号初始密码',         'sys.user.initPassword',            '123456',        'Y', 'admin', NOW(), '', NULL, '初始化密码 123456' );
INSERT INTO sys_config VALUES(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',              'theme-dark',    'Y', 'admin', NOW(), '', NULL, '深色主题theme-dark，浅色主题theme-light' );
INSERT INTO sys_config VALUES(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',       'true',          'Y', 'admin', NOW(), '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO sys_config VALUES(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser',         'false',         'Y', 'admin', NOW(), '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO sys_config VALUES(6, '用户登录-黑名单列表',           'sys.login.blackIPList',            '',              'Y', 'admin', NOW(), '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO sys_config VALUES(7, '用户管理-初始密码修改策略',     'sys.account.initPasswordModify',   '1',             'Y', 'admin', NOW(), '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO sys_config VALUES(8, '用户管理-账号密码更新周期',     'sys.account.passwordValidateDays', '0',             'Y', 'admin', NOW(), '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
INSERT INTO sys_config VALUES(9, '博客头像',                       'blog_avatar',                       '',              'Y', 'admin', NOW(), '', NULL, '博主头像URL，用于前台博客页面显示。与blog_setting表中的blog_avatar保持同步');

-- 5、角色信息表
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

-- 6、菜单权限表
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
INSERT INTO sys_menu VALUES('1', '系统管理', '0', '5', 'admin/system',    NULL, '', '', 1, 0, 'M', '0', '0', '', 'system',   'admin', NOW(), '', NULL, '系统管理目录');
INSERT INTO sys_menu VALUES('2', '系统监控', '0', '3', 'admin/monitor',   NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', NOW(), '', NULL, '系统监控目录');
INSERT INTO sys_menu VALUES('3', '系统工具', '0', '4', 'admin/tool',      NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', NOW(), '', NULL, '系统工具目录');

-- 7、用户和角色关联表（用户N-1角色）
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
  user_id   BIGINT(20) NOT NULL COMMENT '用户ID',
  role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id)
) ENGINE=INNODB COMMENT = '用户和角色关联表';

-- 初始化-用户和角色关联表数据（admin用户分配超级管理员角色，普通用户分配普通角色）
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- 8、角色和菜单关联表（角色1-N菜单）
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
  role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
  menu_id   BIGINT(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (role_id, menu_id)
) ENGINE=INNODB COMMENT = '角色和菜单关联表';

-- 9、角色和部门关联表（角色1-N部门）
DROP TABLE IF EXISTS sys_role_dept;
CREATE TABLE sys_role_dept (
  role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
  dept_id   BIGINT(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (role_id, dept_id)
) ENGINE=INNODB COMMENT = '角色和部门关联表';

-- 10、用户与岗位关联表（用户1-N岗位）
DROP TABLE IF EXISTS sys_user_post;
CREATE TABLE sys_user_post (
  user_id   BIGINT(20) NOT NULL COMMENT '用户ID',
  post_id   BIGINT(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (user_id, post_id)
) ENGINE=INNODB COMMENT = '用户与岗位关联表';

-- 11、字典类型表
DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
  dict_id          BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '字典主键',
  dict_name        VARCHAR(100)   DEFAULT ''                 COMMENT '字典名称',
  dict_type        VARCHAR(100)   DEFAULT ''                 COMMENT '字典类型',
  status           CHAR(1)        DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  create_by        VARCHAR(64)    DEFAULT ''                 COMMENT '创建者',
  create_time      DATETIME                                  COMMENT '创建时间',
  update_by        VARCHAR(64)    DEFAULT ''                 COMMENT '更新者',
  update_time      DATETIME                                  COMMENT '更新时间',
  remark           VARCHAR(500)   DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (dict_id),
  UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '字典类型表';

-- 12、字典数据表
DROP TABLE IF EXISTS sys_dict_data;
CREATE TABLE sys_dict_data (
  dict_code        BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '字典编码',
  dict_sort        INT(4)         DEFAULT 0                  COMMENT '字典排序',
  dict_label       VARCHAR(100)   DEFAULT ''                 COMMENT '字典标签',
  dict_value       VARCHAR(100)   DEFAULT ''                 COMMENT '字典键值',
  dict_type        VARCHAR(100)   DEFAULT ''                 COMMENT '字典类型',
  css_class        VARCHAR(100)   DEFAULT NULL               COMMENT '样式属性（其他样式扩展）',
  list_class       VARCHAR(100)   DEFAULT NULL               COMMENT '表格回显样式',
  is_default       CHAR(1)        DEFAULT 'N'                COMMENT '是否默认（Y是 N否）',
  status           CHAR(1)        DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  create_by        VARCHAR(64)    DEFAULT ''                 COMMENT '创建者',
  create_time      DATETIME                                  COMMENT '创建时间',
  update_by        VARCHAR(64)    DEFAULT ''                 COMMENT '更新者',
  update_time      DATETIME                                  COMMENT '更新时间',
  remark           VARCHAR(500)   DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (dict_code)
) ENGINE=INNODB AUTO_INCREMENT=100 COMMENT = '字典数据表';

-- 初始化字典数据
INSERT INTO sys_dict_type VALUES(1,  '用户性别', 'sys_user_sex',        '0', 'admin', NOW(), '', NULL, '用户性别列表');
INSERT INTO sys_dict_type VALUES(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', NOW(), '', NULL, '菜单状态列表');
INSERT INTO sys_dict_type VALUES(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', NOW(), '', NULL, '系统开关列表');
INSERT INTO sys_dict_type VALUES(4,  '评论状态', 'comment_status',     '0', 'admin', NOW(), '', NULL, '评论状态列表');

INSERT INTO sys_dict_data VALUES(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', NOW(), '', NULL, '性别男');
INSERT INTO sys_dict_data VALUES(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', NOW(), '', NULL, '性别女');
INSERT INTO sys_dict_data VALUES(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', NOW(), '', NULL, '性别未知');

INSERT INTO sys_dict_data VALUES(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', NOW(), '', NULL, '显示菜单');
INSERT INTO sys_dict_data VALUES(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', NOW(), '', NULL, '隐藏菜单');

INSERT INTO sys_dict_data VALUES(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', NOW(), '', NULL, '正常状态');
INSERT INTO sys_dict_data VALUES(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', NOW(), '', NULL, '停用状态');

INSERT INTO sys_dict_data VALUES(8,  1,  '待审核',   '0',       'comment_status',      '',   'warning',  'N', '0', 'admin', NOW(), '', NULL, '待审核状态');
INSERT INTO sys_dict_data VALUES(9,  2,  '已审核',   '1',       'comment_status',      '',   'success', 'N', '0', 'admin', NOW(), '', NULL, '已审核状态');
INSERT INTO sys_dict_data VALUES(10, 3,  '已删除',   '2',       'comment_status',      '',   'danger',  'N', '0', 'admin', NOW(), '', NULL, '已删除状态');

-- 13、通知公告表
DROP TABLE IF EXISTS sys_notice;
CREATE TABLE sys_notice (
  notice_id         BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '公告ID',
  notice_title      VARCHAR(50)    NOT NULL                   COMMENT '公告标题',
  notice_type       CHAR(1)        DEFAULT ''                 COMMENT '公告类型（1通知 2公告）',
  notice_content    LONGTEXT       DEFAULT NULL               COMMENT '公告内容',
  status            CHAR(1)        DEFAULT '0'                COMMENT '公告状态（0正常 1关闭）',
  create_by         VARCHAR(64)    DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)    DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(255)   DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (notice_id)
) ENGINE=INNODB AUTO_INCREMENT=10 COMMENT = '通知公告表';

-- 9、系统访问记录表
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
) ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';

-- 10、操作日志记录表
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
) ENGINE=INNODB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';

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

-- 定时任务调度表
DROP TABLE IF EXISTS sys_job;
CREATE TABLE sys_job (
    job_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    job_name varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
    job_group varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
    invoke_target varchar(500) NOT NULL COMMENT '调用目标字符串',
    cron_expression varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
    misfire_policy varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
    status char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT NULL COMMENT '更新时间',
    remark varchar(500) DEFAULT '' COMMENT '备注信息',
    PRIMARY KEY (job_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务调度表';

-- 定时任务执行日志表
DROP TABLE IF EXISTS sys_job_log;
CREATE TABLE sys_job_log (
    job_log_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    job_name varchar(64) NOT NULL COMMENT '任务名称',
    job_group varchar(64) NOT NULL COMMENT '任务组名',
    invoke_target varchar(500) NOT NULL COMMENT '调用目标字符串',
    job_message varchar(500) DEFAULT NULL COMMENT '日志信息',
    status char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
    exception_info varchar(2000) DEFAULT '' COMMENT '异常信息',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (job_log_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务执行日志表';

-- 博客文章表
DROP TABLE IF EXISTS `blog_article`;
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
  `view_count` int DEFAULT '0' COMMENT '浏览量（已使用IFNULL处理NULL值，默认从0开始计数）',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章表';

-- 博客分类表
DROP TABLE IF EXISTS `blog_category`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客分类表';

-- 博客标签表
DROP TABLE IF EXISTS `blog_tag`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客标签表';

-- 文章标签关联表
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章标签关联表';

-- 博客评论表
DROP TABLE IF EXISTS `blog_comment`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客评论表';

-- 博客友情链接表
DROP TABLE IF EXISTS `blog_friend_link`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客友链表';

-- 博客系统设置表
DROP TABLE IF EXISTS `blog_setting`;
CREATE TABLE `blog_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置项Key',
  `config_value` longtext COMMENT '配置项Value',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客系统设置表';

-- ========== 代码生成器表 ==========

-- 代码生成业务表
DROP TABLE IF EXISTS gen_table;
CREATE TABLE gen_table (
  table_id          BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT 'id',
  table_name        VARCHAR(200)    DEFAULT ''                 COMMENT '表名称',
  table_comment     VARCHAR(500)    DEFAULT ''                 COMMENT '表描述',
  sub_table_name    VARCHAR(64)     DEFAULT NULL               COMMENT '关联子表的表名',
  sub_table_fk_name VARCHAR(64)     DEFAULT NULL               COMMENT '子表关联的外键名',
  class_name        VARCHAR(100)    DEFAULT ''                 COMMENT '实体类名称',
  tpl_category      VARCHAR(200)    DEFAULT 'crud'              COMMENT '使用的模板（crud单表 tree树表）',
  tpl_web_type      VARCHAR(30)     DEFAULT 'element-plus'      COMMENT '前端模板类型（element-plus element-ui）',
  package_name      VARCHAR(100)    DEFAULT NULL               COMMENT '生成包路径',
  module_name       VARCHAR(30)     DEFAULT NULL               COMMENT '生成模块名',
  business_name     VARCHAR(30)     DEFAULT NULL               COMMENT '生成业务名',
  function_name     VARCHAR(50)     DEFAULT NULL               COMMENT '生成功能名',
  function_author   VARCHAR(50)     DEFAULT NULL               COMMENT '生成功能作者',
  gen_type          CHAR(1)         DEFAULT '0'                COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          VARCHAR(200)    DEFAULT '/'                 COMMENT '生成路径（不填默认项目路径）',
  options           VARCHAR(1000)   DEFAULT NULL               COMMENT '其它生成选项',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (table_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT = '代码生成业务表';

-- 代码生成字段表
DROP TABLE IF EXISTS gen_table_column;
CREATE TABLE gen_table_column (
  column_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '列id',
  table_id          BIGINT(20)      DEFAULT NULL               COMMENT '归属表id',
  column_name       VARCHAR(200)    DEFAULT NULL               COMMENT '列名称',
  column_comment    VARCHAR(500)    DEFAULT NULL               COMMENT '列描述',
  column_type       VARCHAR(100)    DEFAULT NULL               COMMENT '列类型',
  java_type         VARCHAR(500)    DEFAULT NULL               COMMENT 'Java类型',
  java_field        VARCHAR(200)    DEFAULT NULL               COMMENT 'Java字段名',
  is_pk             CHAR(1)         DEFAULT NULL               COMMENT '是否主键（1是）',
  is_increment      CHAR(1)         DEFAULT NULL               COMMENT '是否自增（1是）',
  is_required       CHAR(1)         DEFAULT NULL               COMMENT '是否必填（1是）',
  is_insert         CHAR(1)         DEFAULT NULL               COMMENT '是否为插入字段（1是）',
  is_edit           CHAR(1)         DEFAULT NULL               COMMENT '是否编辑字段（1是）',
  is_list           CHAR(1)         DEFAULT NULL               COMMENT '是否列表字段（1是）',
  is_query          CHAR(1)         DEFAULT NULL               COMMENT '是否查询字段（1是）',
  query_type        VARCHAR(200)    DEFAULT 'EQ'               COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  html_type         VARCHAR(200)    DEFAULT 'input'             COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         VARCHAR(200)    DEFAULT ''                 COMMENT '字典类型',
  sort              INT(4)          DEFAULT NULL               COMMENT '排序',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  PRIMARY KEY (column_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT = '代码生成字段表';

-- ========== 初始化博客系统数据 ==========

-- 初始化完整的博客设置数据（38项配置）
INSERT INTO `blog_setting` (`config_key`, `config_value`, `description`, `create_time`, `update_time`) VALUES
-- 基础信息设置
('blog_name', '我的博客', '博客名称', NOW(), NOW()),
('blog_desc', '这是一个基于RuoYi-Vue的博客系统', '博客描述', NOW(), NOW()),
('blog_author', 'admin', '博客作者', NOW(), NOW()),
('blog_keywords', '博客,RuoYi,Vue,Spring Boot,MySQL,前端开发,后端开发', '博客关键词', NOW(), NOW()),
('blog_copyright', 'Copyright © 2025-2026 我的博客. All rights reserved.', '版权信息', NOW(), NOW()),
('blog_beian', 'ICP备12345678号', '备案信息', NOW(), NOW()),

-- 联系方式设置
('blog_email', 'admin@example.com', '博客联系邮箱', NOW(), NOW()),
('blog_url', 'http://localhost:8080', '博客访问地址', NOW(), NOW()),
('blog_avatar', '', '博主头像', NOW(), NOW()),
('blog_signature', 'Stay hungry, Stay foolish', '博主签名', NOW(), NOW()),
('blog_start_time', '2025-01-01', '博客创建时间', NOW(), NOW()),

-- 社交媒体设置
('author_title', '全栈开发工程师', '博主头衔', NOW(), NOW()),
('author_description', '这个人很懒，什么都没留下', '博主描述', NOW(), NOW()),
('github_url', 'https://github.com/nevell', 'GitHub链接', NOW(), NOW()),
('weibo_url', '', '微博链接', NOW(), NOW()),
('zhihu_url', '', '知乎链接', NOW(), NOW()),

-- 功能开关设置
('footer_enabled', 'true', '是否显示底部', NOW(), NOW()),
('copyright_enabled', 'true', '是否显示版权信息', NOW(), NOW()),
('comment_enabled', 'true', '是否开启评论功能', NOW(), NOW()),
('comment_review', 'true', '评论是否需要审核', NOW(), NOW()),
('like_enabled', 'true', '是否开启点赞功能', NOW(), NOW()),
('view_count_enabled', 'true', '是否开启浏览统计', NOW(), NOW()),
('share_enabled', 'true', '是否开启分享功能', NOW(), NOW()),
('search_enabled', 'true', '是否开启搜索功能', NOW(), NOW()),
('sidebar_enabled', 'true', '是否显示侧边栏', NOW(), NOW()),

-- 界面样式设置
('theme_color', '#409EFF', '主题颜色', NOW(), NOW()),
('header_background', '#ffffff', '头部背景色', NOW(), NOW()),
('sidebar_style', 'dark', '侧边栏样式', NOW(), NOW()),

-- 显示数量设置
('page_size', '10', '每页显示文章数量', NOW(), NOW()),
('hot_article_count', '5', '热门文章显示数量', NOW(), NOW()),
('recent_comment_count', '5', '最新评论显示数量', NOW(), NOW()),

-- 内容设置
('greeting_message', '欢迎来到我的博客！', '欢迎信息', NOW(), NOW()),
('about_content', '<p>这是一个基于Spring Boot + Vue.js构建的现代化博客系统。</p><p>主要功能包括：</p><ul><li>文章管理</li><li>分类标签</li><li>评论系统</li><li>搜索功能</li></ul>', '关于页面内容', NOW(), NOW()),

-- SEO优化设置
('seo_title', '我的博客 - 分享技术与生活', 'SEO标题', NOW(), NOW()),
('seo_description', '专注于前后端技术分享，包含Spring Boot、Vue.js、MySQL等技术内容', 'SEO描述', NOW(), NOW()),
('seo_canonical_url', 'http://localhost:8080', '规范URL', NOW(), NOW()),
('seo_robots', 'index,follow', 'Robots规则', NOW(), NOW()),
('seo_favicon', '/favicon.ico', '网站图标', NOW(), NOW());

-- 插入完整的博客分类数据（包含层级结构）
INSERT INTO `blog_category` (`name`, `alias`, `description`, `parent_id`, `sort_order`, `sort`, `status`) VALUES
-- 一级分类
('技术分享', 'tech', '技术相关文章和教程', 0, 1, 1, 1),
('生活随笔', 'life', '生活记录和感悟分享', 0, 2, 2, 1),
('学习笔记', 'study', '学习过程中的知识整理', 0, 4, 4, 1),
('资源分享', 'resource', '优质工具和资源推荐', 0, 5, 5, 1),

-- 二级分类（技术分享下的子分类）
('前端开发', 'frontend', '前端技术相关内容', 1, 1, 1, 1),
('后端开发', 'backend', '后端技术相关内容', 1, 2, 2, 1),
('数据库', 'database', '数据库相关技术', 1, 3, 3, 1),
('运维部署', 'devops', '系统运维和部署相关', 1, 4, 4, 1),

-- 二级分类（生活随笔下的子分类）
('日常记录', 'daily', '日常生活记录', 2, 1, 1, 1),
('读书笔记', 'reading', '读书心得和笔记', 2, 2, 2, 1),
('旅行见闻', 'travel', '旅行经历和见闻', 2, 3, 3, 1);

-- 插入完整的博客标签数据（18个常用标签）
INSERT INTO `blog_tag` (`id`, `name`, `description`, `color`, `icon`, `article_count`) VALUES
-- 核心标签（确保与test_tags.sql一致）
(1, 'Java', 'Java编程语言相关文章', '#f89820', 'el-icon-cpu', 0),
(2, 'Vue.js', 'Vue.js前端框架相关文章', '#4fc08d', 'el-icon-monitor', 0),
(3, 'Spring Boot', 'Spring Boot框架相关文章', '#6db33f', 'el-icon-setting', 0),
(4, 'MySQL', 'MySQL数据库相关文章', '#00758f', 'el-icon-coin', 0),
(5, '前端开发', '前端开发技术相关文章', '#e34c26', 'el-icon-brush', 0),
(6, '后端开发', '后端开发技术相关文章', '#337ecc', 'el-icon-server', 0),

-- 编程语言
(7, 'JavaScript', 'JavaScript编程语言相关', '#F7DF1E', 'el-icon-link', 0),
(8, 'TypeScript', 'TypeScript编程语言相关', '#3178C6', 'el-icon-document', 0),

-- 后端框架
(9, 'Spring Cloud', 'Spring Cloud微服务框架', '#6DB33F', 'el-icon-cloudy', 0),
(10, 'MyBatis', 'MyBatis持久层框架', '#000000', 'el-icon-database', 0),
(11, 'Node.js', 'Node.js运行时环境', '#339933', 'el-icon-node', 0),

-- 前端框架
(12, 'Element Plus', 'Element Plus组件库', '#409EFF', 'el-icon-menu', 0),
(13, 'React', 'React前端框架相关', '#61DAFB', 'el-icon-cpu', 0),

-- 数据库
(14, 'Redis', 'Redis缓存数据库', '#DC382D', 'el-icon-connection', 0),
(15, 'MongoDB', 'MongoDB文档数据库', '#47A248', 'el-icon-folder-opened', 0),

-- 工具和其他
(16, 'Docker', 'Docker容器技术', '#2496ED', 'el-icon-box', 0),
(17, 'Git', 'Git版本控制工具', '#F05032', 'el-icon-branch', 0),
(18, 'Linux', 'Linux操作系统', '#FCC624', 'el-icon-monitor', 0);

-- 插入完整的博客文章示例数据
INSERT INTO `blog_article` (`title`, `summary`, `content`, `cover_url`, `category_id`, `author_id`, `author_name`, `is_top`, `is_recommend`, `status`, `view_count`, `like_count`) VALUES
-- 置顶推荐文章
('Spring Boot + Vue.js 全栈开发实战', '本文详细介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用，包含完整的项目搭建和部署流程。',
'# Spring Boot + Vue.js 全栈开发实战

## 📋 项目介绍
本项目是基于Spring Boot 3.3.0和Vue.js 3.x构建的现代化博客系统，采用前后端分离架构，提供完整的内容管理功能。

## 🛠️ 技术栈详解
### 后端技术栈
- **Spring Boot 3.3.0** - 核心框架
- **MyBatis** - 持久层框架
- **MySQL 8.0** - 关系型数据库
- **Redis** - 缓存和会话存储
- **Spring Security** - 安全认证框架

### 前端技术栈
- **Vue.js 3.x** - 前端框架
- **Element Plus** - UI组件库
- **Vite** - 构建工具
- **Axios** - HTTP客户端
- **Vue Router** - 路由管理

## 🚀 核心功能特性
1. **用户管理** - 注册、登录、权限控制
2. **文章管理** - 增删改查、富文本编辑
3. **分类标签** - 分类管理、标签关联
4. **评论系统** - 评论发布、回复、审核
5. **搜索功能** - 全文搜索、关键词高亮
6. **统计分析** - 访问统计、热度排行

## 💡 开发经验总结
通过本项目的实践，深入理解了：
- 前后端分离架构的设计思想
- RESTful API设计规范
- 数据库性能优化技巧
- 前端组件化开发模式
- 安全防护和权限控制',
'', 1, 1, 'admin', 1, 1, 1, 156, 23),

('MySQL数据库优化实战指南', '分享MySQL数据库在生产环境中的性能优化经验，包含索引设计、查询优化、配置调优等实用技巧。',
'# MySQL数据库优化实战指南

## 🔍 索引优化策略
### 合理创建索引
```sql
-- 单列索引
CREATE INDEX idx_user_email ON user(email);

-- 复合索引
CREATE INDEX idx_article_status_create_time ON article(status, create_time);

-- 覆盖索引
CREATE INDEX idx_article_cover ON article(id, title, status);
```

### 避免索引失效
- 避免在索引列上使用函数
- 避免隐式类型转换
- 避免左模糊查询（LIKE "%value"）

## ⚡ 查询优化技巧
### SQL语句优化
- 使用EXPLAIN分析执行计划
- 避免SELECT * 查询
- 合理使用JOIN和子查询

### 分页优化
```sql
-- 传统分页（深度分页性能差）
SELECT * FROM article ORDER BY id LIMIT 100000, 10;

-- 优化分页（使用书签模式）
SELECT * FROM article WHERE id > 100000 ORDER BY id LIMIT 10;
```

## 🛠️ 配置参数调优
```ini
# 内存相关配置
innodb_buffer_pool_size = 4G
innodb_log_file_size = 256M

# 连接数配置
max_connections = 500
max_connect_errors = 1000

# 查询缓存
query_cache_type = 1
query_cache_size = 256M
```

通过以上优化措施，数据库性能可提升3-5倍。',
'', 6, 1, 'admin', 1, 1, 1, 89, 15),

-- 普通文章
('Vue.js 3.0 Composition API 深度解析', '深入了解Vue.js 3.0的Composition API，通过实际案例学习如何使用组合式API构建复杂应用。',
'# Vue.js 3.0 Composition API 深度解析

## 🎯 Composition API 核心概念
### 基本语法
```javascript
import { ref, reactive, computed, watch } from ''vue''

export default {
  setup() {
    const count = ref(0)
    const state = reactive({ name: ''Vue'', version: 3 })

    const doubled = computed(() => count.value * 2)

    watch(count, (newVal, oldVal) => {
      console.log(`Count changed from ${oldVal} to ${newVal}`)
    })

    return { count, state, doubled }
  }
}
```

### 逻辑复用
通过自定义Hook实现逻辑复用：
```javascript
// useCounter.js
export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  const increment = () => count.value++
  const decrement = () => count.value--

  return { count, increment, decrement }
}
```

## 🚀 性能提升
Vue 3相比Vue 2在以下方面有显著提升：
- **打包体积** 减少41%
- **初始渲染** 快55%
- **更新渲染** 快133%
- **内存占用** 减少54%

## 🔧 TypeScript支持
Vue 3提供更好的TypeScript集成：
```typescript
interface User {
  id: number
  name: string
  email: string
}

const user = ref<User>({ id: 1, name: ''Admin'', email: ''admin@example.com'' })
```

Composition API让代码更加模块化和可维护，特别适合大型项目开发。',
'', 6, 1, 'admin', 0, 1, 1, 67, 12),

('Docker容器化部署最佳实践', '详细介绍如何使用Docker对Spring Boot应用进行容器化部署，包含Dockerfile编写、镜像优化、多阶段构建等技巧。',
'# Docker容器化部署最佳实践

## 🐳 Dockerfile编写技巧
### 多阶段构建优化
```dockerfile
# 构建阶段
FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 镜像体积优化
- 使用合适的基础镜像（alpine、slim）
- 清理不必要的包和缓存
- 合并RUN指令减少层数

## 🚀 部署配置
### Docker Compose配置
```yaml
version: ''3.8''
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - mysql
      - redis

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: blog
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:alpine
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

## 📊 监控和日志
### 健康检查
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
```

通过容器化部署，大大简化了应用的环境管理和部署流程。',
'', 3, 1, 'admin', 0, 1, 1, 45, 8),

('Redis缓存设计与实战', '深入讲解Redis在Web应用中的缓存设计模式，包含缓存更新策略、数据一致性、分布式锁等高级话题。',
'# Redis缓存设计与实战

## 🎯 缓存设计模式
### Cache-Aside模式
```java
public User getUser(Long id) {
    String key = "user:" + id;
    User user = redisTemplate.opsForValue().get(key);

    if (user == null) {
        user = userMapper.selectById(id);
        if (user != null) {
            redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
        }
    }
    return user;
}
```

### Write-Through模式
在写入数据库的同时更新缓存，保证数据一致性。

## 🔄 缓存更新策略
### 延迟双删策略
```java
public void updateUser(User user) {
    // 第一次删除
    redisTemplate.delete("user:" + user.getId());

    // 更新数据库
    userMapper.updateById(user);

    // 延迟删除（避免脏数据）
    Thread.sleep(500);
    redisTemplate.delete("user:" + user.getId());
}
```

## ⚡ 性能优化技巧
### Pipeline批量操作
```java
List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
    for (Long id : userIds) {
        connection.get(("user:" + id).getBytes());
    }
    return null;
});
```

### 合理设置过期时间
- 热点数据：短过期时间
- 静态数据：长过期时间
- 活动数据：动态过期时间

通过合理使用Redis缓存，可以将系统性能提升10倍以上。',
'', 7, 1, 'admin', 0, 0, 1, 78, 11),

('Git工作流程与团队协作规范', '介绍高效的Git工作流程，包括分支策略、提交规范、代码审查等团队协作最佳实践。',
'# Git工作流程与团队协作规范

## 🌲 分支管理策略
### Git Flow工作流
```
master (生产分支)
  ↑
develop (开发分支)
  ↑
feature/* (功能分支)
hotfix/* (热修复分支)
release/* (发布分支)
```

### 分支命名规范
- `feature/用户登录功能`
- `bugfix/修复支付接口异常`
- `hotfix/紧急修复内存泄漏`
- `release/v1.2.0`

## 📝 提交信息规范
### Conventional Commits规范
```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

### 提交类型
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构代码
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

## 🔍 代码审查清单
### 功能性检查
- [ ] 功能是否按需求实现
- [ ] 边界条件是否处理
- [ ] 异常情况是否有处理

### 代码质量检查
- [ ] 代码是否遵循规范
- [ ] 是否有重复代码
- [ ] 注释是否充分

### 安全性检查
- [ ] 是否有SQL注入风险
- [ ] 敏感信息是否脱敏
- [ ] 权限控制是否合理

良好的Git工作流程可以显著提升团队的协作效率和代码质量。',
'', 5, 1, 'admin', 0, 0, 1, 92, 16);

-- 插入文章标签关联数据（建立文章与标签的多对多关系）
INSERT INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
-- 文章1：Spring Boot + Vue.js 全栈开发实战
(1, 1), (1, 2), (1, 3),

-- 文章2：MySQL数据库优化实战指南
(2, 1), (2, 4), (2, 6),

-- 文章3：Vue.js 3.0 Composition API 深度解析
(3, 3), (3, 4), (3, 10), (3, 11),

-- 文章4：Docker容器化部署最佳实践
(4, 6),

-- 文章5：Redis缓存设计与实战
(5, 1), (5, 7), (5, 15),

-- 文章6：Git工作流程与团队协作规范
(6, 18), (6, 19);

-- ========== 更新分类文章数量 ==========

-- 禁用触发器以避免冲突
SET @DISABLE_TRIGGERS = TRUE;

-- 批量更新所有分类的文章数量
UPDATE blog_category c
SET c.article_count = (
    SELECT COUNT(*)
    FROM blog_article a
    WHERE a.category_id = c.id
    AND a.del_flag = '0'
    AND a.status = 1
),
c.update_time = NOW()
WHERE c.del_flag = '0';

-- 重新启用触发器
SET @DISABLE_TRIGGERS = NULL;

-- ========== 验证分类文章数量统计 ==========

SELECT '📊 验证分类文章数量统计：' AS info;
SELECT
    c.id,
    c.name,
    c.status,
    c.del_flag,
    IFNULL(COUNT(a.id), 0) as actual_article_count,
    c.article_count as cached_article_count,
    CASE
        WHEN IFNULL(COUNT(a.id), 0) != c.article_count THEN '❌ 不匹配'
        ELSE '✅ 匹配'
    END as status
FROM blog_category c
LEFT JOIN blog_article a ON c.id = a.category_id AND a.del_flag = '0' AND a.status = 1
WHERE c.del_flag = '0'
GROUP BY c.id, c.name, c.status, c.del_flag, c.article_count
ORDER BY c.sort_order ASC;

-- 插入完整的友情链接数据
INSERT INTO `blog_friend_link` (`name`, `url`, `logo`, `description`, `status`, `sort`) VALUES
('Spring官网', 'https://spring.io/', 'https://spring.io/images/spring-logo.png', 'Spring框架官方网站，提供最新的Spring生态系统资讯和文档', 0, 1),
('Vue.js官网', 'https://vuejs.org/', 'https://vuejs.org/logo.svg', 'Vue.js渐进式JavaScript框架官方网站', 1, 2),
('Element Plus', 'https://element-plus.org/', 'https://element-plus.org/images/element-plus-logo.svg', 'Vue 3桌面端组件库，提供丰富的UI组件', 1, 3),
('GitHub', 'https://github.com/', 'https://github.com/github.png', '全球最大的代码托管平台和开源社区', 0, 4),
('MDN Web Docs', 'https://developer.mozilla.org/', 'https://developer.mozilla.org/mdn-social-share.cd6c89a5a1a.png', 'Web开发者资源中心，提供权威的前端技术文档', 0, 5),
('Stack Overflow', 'https://stackoverflow.com/', 'https://cdn.sstatic.net/Sites/stackoverflow/Img/apple-touch-icon.png', '程序员问答社区，解决编程问题的首选平台', 0, 6),
('掘金', 'https://juejin.cn/', 'https://lf3-cdn-tos.bytegoofy.com/obj/iconpark/icons_19361_1.svg', '技术社区，分享技术文章和经验', 0, 7),
('CSDN', 'https://www.csdn.net/', 'https://www.csdn.net/images/logo.png', '中国最大的IT技术社区和开发者服务平台', 0, 8),
('Redis官网', 'https://redis.io/', 'https://redis.io/images/redis-white.svg', 'Redis内存数据库官方网站', 0, 9),
('MySQL官网', 'https://www.mysql.com/', 'https://www.mysql.com/common/logos/logo-mysql-170x115.png', 'MySQL数据库官方网站，提供数据库软件和文档', 0, 10);

-- ========== 配置博客管理菜单 ==========

-- 博客管理主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 1, 'admin/blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录');

-- 文章管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2001, '文章管理', 2000, 1, 'article', 'blog/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '文章管理菜单');

-- 分类管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2002, '分类管理', 2000, 2, 'category', 'blog/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '分类管理菜单');

-- 标签管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '标签管理', 2000, 3, 'tag', 'blog/tag/index', '', '', 1, 0, 'C', '0', '0', 'blog:tag:list', 'tab', 'admin', NOW(), '', NULL, '标签管理菜单');

-- 评论管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2004, '评论管理', 2000, 4, 'comment', 'blog/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '评论管理菜单');

-- 友链管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '友链管理', 2000, 5, 'friendLink', 'blog/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友链管理菜单');

-- 博客设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2005, '博客设置', 2000, 6, 'setting', 'blog/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'swagger', 'admin', NOW(), '', NULL, '博客设置菜单');

-- 为管理员角色分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2000), (1, 2001), (1, 2002), (1, 2003), (1, 2004), (1, 2006), (1, 2005);

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

-- 友链管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20060, '友链查询', 2006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''),
(20061, '友链新增', 2006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''),
(20062, '友链修改', 2006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''),
(20063, '友链删除', 2006, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, '');

-- 博客设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20050, '设置查询', 2005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''),
(20051, '设置修改', 2005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配所有博客管理按钮权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 20010), (1, 20011), (1, 20012), (1, 20013),
(1, 20020), (1, 20021), (1, 20022), (1, 20023),
(1, 20030), (1, 20031), (1, 20032), (1, 20033), (1, 20034),
(1, 20040), (1, 20041), (1, 20042),
(1, 20060), (1, 20061), (1, 20062), (1, 20063),
(1, 20050), (1, 20051);

-- ========== 配置系统管理菜单 ==========

-- 1、用户管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (100, '用户管理', 1, 1, 'user', 'system/user/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', NOW(), '', NULL, '用户管理菜单');

-- 用户管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1001, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', NOW(), '', NULL, ''),
(1002, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', NOW(), '', NULL, ''),
(1003, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', NOW(), '', NULL, ''),
(1004, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', NOW(), '', NULL, ''),
(1005, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', NOW(), '', NULL, ''),
(1006, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', NOW(), '', NULL, ''),
(1007, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', NOW(), '', NULL, '');

-- 2、角色管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', NOW(), '', NULL, '角色管理菜单');

-- 角色管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1011, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', NOW(), '', NULL, ''),
(1012, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', NOW(), '', NULL, ''),
(1013, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', NOW(), '', NULL, ''),
(1014, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW(), '', NULL, ''),
(1015, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', NOW(), '', NULL, '');

-- 3、菜单管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单');

-- 菜单管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1021, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', NOW(), '', NULL, ''),
(1022, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', NOW(), '', NULL, ''),
(1023, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', NOW(), '', NULL, ''),
(1024, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW(), '', NULL, '');

-- 4、部门管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', NOW(), '', NULL, '部门管理菜单');

-- 部门管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1031, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', NOW(), '', NULL, ''),
(1032, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', NOW(), '', NULL, ''),
(1033, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', NOW(), '', NULL, ''),
(1034, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', NOW(), '', NULL, '');

-- 5、岗位管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', NOW(), '', NULL, '岗位管理菜单');

-- 岗位管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1041, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', NOW(), '', NULL, ''),
(1042, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', NOW(), '', NULL, ''),
(1043, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', NOW(), '', NULL, ''),
(1044, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', NOW(), '', NULL, ''),
(1045, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', NOW(), '', NULL, '');

-- 6、字典管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', NOW(), '', NULL, '字典管理菜单');

-- 字典管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1051, '字典查询', 105, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', NOW(), '', NULL, ''),
(1052, '字典新增', 105, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', NOW(), '', NULL, ''),
(1053, '字典修改', 105, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', NOW(), '', NULL, ''),
(1054, '字典删除', 105, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', NOW(), '', NULL, ''),
(1055, '字典导出', 105, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', NOW(), '', NULL, '');

-- 7、参数设置菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (106, '参数设置', 1, 7, 'config', 'system/config/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', NOW(), '', NULL, '参数设置菜单');

-- 参数设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1061, '参数查询', 106, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', NOW(), '', NULL, ''),
(1062, '参数新增', 106, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', NOW(), '', NULL, ''),
(1063, '参数修改', 106, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', NOW(), '', NULL, ''),
(1064, '参数删除', 106, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', NOW(), '', NULL, ''),
(1065, '参数导出', 106, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', NOW(), '', NULL, '');

-- 8、通知公告菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', NOW(), '', NULL, '通知公告菜单');

-- 通知公告按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1071, '公告查询', 107, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', NOW(), '', NULL, ''),
(1072, '公告新增', 107, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', NOW(), '', NULL, ''),
(1073, '公告修改', 107, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', NOW(), '', NULL, ''),
(1074, '公告删除', 107, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', NOW(), '', NULL, '');

-- ========== 配置系统监控菜单 ==========

-- 1、在线用户菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', NOW(), '', NULL, '在线用户菜单');

-- 在线用户按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1091, '在线用户查询', 109, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', NOW(), '', NULL, ''),
(1092, '批量强退', 109, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', NOW(), '', NULL, ''),
(1093, '单条强退', 109, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', NOW(), '', NULL, '');

-- 2、定时任务菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', NOW(), '', NULL, '定时任务菜单');

-- 定时任务按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1101, '任务查询', 110, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', NOW(), '', NULL, ''),
(1102, '任务新增', 110, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', NOW(), '', NULL, ''),
(1103, '任务修改', 110, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', NOW(), '', NULL, ''),
(1104, '任务删除', 110, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', NOW(), '', NULL, ''),
(1105, '任务导出', 110, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', NOW(), '', NULL, ''),
(1106, '状态修改', 110, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', NOW(), '', NULL, ''),
(1107, '任务详细', 110, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:detail', '#', 'admin', NOW(), '', NULL, ''),
(1108, '任务执行', 110, 8, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:execute', '#', 'admin', NOW(), '', NULL, '');

-- 3、数据监控菜单
-- INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', NOW(), '', NULL, '数据监控菜单');

-- 4、服务监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', NOW(), '', NULL, '服务监控菜单');

-- 5、缓存监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', NOW(), '', NULL, '缓存监控菜单');

-- 6、缓存列表菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', NOW(), '', NULL, '缓存列表菜单');

-- 缓存列表按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1141, '缓存查询', 114, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:query', '#', 'admin', NOW(), '', NULL, ''),
(1142, '缓存新增', 114, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:add', '#', 'admin', NOW(), '', NULL, ''),
(1143, '缓存修改', 114, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:edit', '#', 'admin', NOW(), '', NULL, ''),
(1144, '缓存删除', 114, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:remove', '#', 'admin', NOW(), '', NULL, ''),
(1145, '缓存清理', 114, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:clean', '#', 'admin', NOW(), '', NULL, '');

-- ========== 配置系统工具菜单 ==========

-- 1、表单构建菜单
-- INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', NOW(), '', NULL, '表单构建菜单');

-- 2、代码生成菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', NOW(), '', NULL, '代码生成菜单');

-- 代码生成按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1161, '生成查询', 116, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', NOW(), '', NULL, ''),
(1162, '生成新增', 116, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:add', '#', 'admin', NOW(), '', NULL, ''),
(1163, '生成编辑', 116, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', NOW(), '', NULL, ''),
(1164, '生成删除', 116, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', NOW(), '', NULL, ''),
(1165, '导入代码', 116, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', NOW(), '', NULL, ''),
(1166, '预览代码', 116, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', NOW(), '', NULL, ''),
(1167, '生成代码', 116, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', NOW(), '', NULL, '');

-- 3、系统接口菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', NOW(), '', NULL, '系统接口菜单');

-- ========== 系统监控补充菜单 ==========

-- 1、Actuator监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5000, 'Actuator监控', 2, 10, 'actuator', 'monitor/actuator/index', '', '', 1, 0, 'C', '0', '0', 'monitor:actuator:list', 'monitor', 'admin', NOW(), '', NULL, 'Actuator监控菜单');

-- Actuator监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5001, 'Actuator查询', 5000, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:actuator:query', '#', 'admin', NOW(), '', NULL, '');

-- 2、Prometheus监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5002, 'Prometheus监控', 2, 11, 'prometheus', 'monitor/prometheus/index', '', '', 1, 0, 'C', '0', '0', 'monitor:prometheus:list', 'chart', 'admin', NOW(), '', NULL, 'Prometheus监控菜单');

-- Prometheus监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5003, 'Prometheus查询', 5002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:prometheus:query', '#', 'admin', NOW(), '', NULL, '');

-- 3、Grafana监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5004, 'Grafana监控', 2, 12, 'grafana', 'monitor/grafana/index', '', '', 1, 0, 'C', '0', '0', 'monitor:grafana:list', 'dashboard', 'admin', NOW(), '', NULL, 'Grafana监控菜单');

-- Grafana监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5005, 'Grafana查询', 5004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:grafana:query', '#', 'admin', NOW(), '', NULL, '');

-- 4、登录日志菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5006, '登录日志', 2, 7, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', NOW(), '', NULL, '登录日志菜单');

-- 登录日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5007, '登录日志查询', 5006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', NOW(), '', NULL, ''),
(5008, '登录日志删除', 5006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', NOW(), '', NULL, ''),
(5009, '登录日志导出', 5006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', NOW(), '', NULL, '');

-- 5、操作日志菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5010, '操作日志', 2, 8, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', NOW(), '', NULL, '操作日志菜单');

-- 操作日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5011, '操作日志查询', 5010, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', NOW(), '', NULL, ''),
(5012, '操作日志删除', 5010, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', NOW(), '', NULL, ''),
(5013, '操作日志导出', 5010, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', NOW(), '', NULL, '');

-- ========== 数据统计菜单配置 ==========

-- 1、数据统计一级菜单（注意：visible='0' 表示显示，'1' 表示隐藏）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5, '数据统计', 0, 2, 'admin/statistics', NULL, '', '', 1, 0, 'M', '0', '0', '', 'nested', 'admin', NOW(), '', NULL, '数据统计目录');

-- 2、数据概览菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6001, '数据概览', 5, 1, 'overview', 'statistics/overview/index', '', '', 1, 0, 'C', '0', '0', 'statistics:overview:list', 'chart', 'admin', NOW(), '', NULL, '数据概览菜单');

-- 数据概览按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6002, '数据概览查询', 6001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:overview:query', '#', 'admin', NOW(), '', NULL, '');

-- 3、文章统计菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6003, '文章统计', 5, 2, 'article', 'statistics/article/index', '', '', 1, 0, 'C', '0', '0', 'statistics:article:list', 'documentation', 'admin', NOW(), '', NULL, '文章统计菜单');

-- 文章统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6004, '文章统计查询', 6003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:query', '#', 'admin', NOW(), '', NULL, ''),
(6005, '文章统计导出', 6003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:export', '#', 'admin', NOW(), '', NULL, '');

-- 4、用户统计菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6006, '用户统计', 5, 3, 'user', 'statistics/user/index', '', '', 1, 0, 'C', '0', '0', 'statistics:user:list', 'user', 'admin', NOW(), '', NULL, '用户统计菜单');

-- 用户统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6007, '用户统计查询', 6006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:query', '#', 'admin', NOW(), '', NULL, ''),
(6008, '用户统计导出', 6006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:export', '#', 'admin', NOW(), '', NULL, '');

-- ========== 为管理员角色分配系统管理、监控和工具菜单权限 ==========

-- 系统管理菜单权限（包括菜单和按钮权限）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
-- 系统管理主菜单
(1, 1),
-- 用户管理
(1, 100), (1, 1001), (1, 1002), (1, 1003), (1, 1004), (1, 1005), (1, 1006), (1, 1007),
-- 角色管理
(1, 101), (1, 1011), (1, 1012), (1, 1013), (1, 1014), (1, 1015),
-- 菜单管理
(1, 102), (1, 1021), (1, 1022), (1, 1023), (1, 1024),
-- 部门管理
(1, 103), (1, 1031), (1, 1032), (1, 1033), (1, 1034),
-- 岗位管理
(1, 104), (1, 1041), (1, 1042), (1, 1043), (1, 1044), (1, 1045),
-- 字典管理
(1, 105), (1, 1051), (1, 1052), (1, 1053), (1, 1054), (1, 1055),
-- 参数设置
(1, 106), (1, 1061), (1, 1062), (1, 1063), (1, 1064), (1, 1065),
-- 通知公告
(1, 107), (1, 1071), (1, 1072), (1, 1073), (1, 1074);

-- 系统监控菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 109), (1, 1091), (1, 1092), (1, 1093),
(1, 110), (1, 1101), (1, 1102), (1, 1103), (1, 1104), (1, 1105), (1, 1106), (1, 1107), (1, 1108),
(1, 111), (1, 112), (1, 113),
(1, 114), (1, 1141), (1, 1142), (1, 1143), (1, 1144), (1, 1145),
(1, 5000), (1, 5001),
(1, 5002), (1, 5003),
(1, 5004), (1, 5005),
(1, 5006), (1, 5007), (1, 5008), (1, 5009),
(1, 5010), (1, 5011), (1, 5012), (1, 5013);

-- 系统工具菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 115), (1, 116), (1, 1161), (1, 1162), (1, 1163), (1, 1164), (1, 1165), (1, 1166), (1, 1167), (1, 117);

-- 数据统计菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 5),
(1, 6001), (1, 6002),
(1, 6003), (1, 6004), (1, 6005),
(1, 6006), (1, 6007), (1, 6008);

-- ========== 创建性能优化索引 ==========

-- 博客评论表索引优化
ALTER TABLE `blog_comment` ADD INDEX `idx_article_id` (`article_id`);
ALTER TABLE `blog_comment` ADD INDEX `idx_parent_id` (`parent_id`);
ALTER TABLE `blog_comment` ADD INDEX `idx_status` (`status`);
ALTER TABLE `blog_comment` ADD INDEX `idx_create_time` (`create_time`);
ALTER TABLE `blog_comment` ADD INDEX `idx_article_status` (`article_id`, `status`);

-- 博客标签表索引优化
ALTER TABLE `blog_tag` ADD INDEX `idx_del_flag` (`del_flag`);
ALTER TABLE `blog_tag` ADD INDEX `idx_name` (`name`);
ALTER TABLE `blog_tag` ADD INDEX `idx_article_count` (`article_count`);

-- 博客文章表复合索引优化
ALTER TABLE `blog_article` ADD INDEX `idx_status_del_time` (`status`, `del_flag`, `create_time`);
ALTER TABLE `blog_article` ADD INDEX `idx_category_status` (`category_id`, `status`);
ALTER TABLE `blog_article` ADD INDEX `idx_author_status` (`author_id`, `status`);
ALTER TABLE `blog_article` ADD INDEX `idx_top_status_time` (`is_top`, `status`, `create_time`);

-- 博客设置表索引优化
ALTER TABLE `blog_setting` ADD INDEX `idx_config_key` (`config_key`);

-- 文章标签关联表索引优化
ALTER TABLE `blog_article_tag` ADD INDEX `idx_tag_id` (`tag_id`);

-- ========== 添加数据完整性约束 ==========

-- 博客文章表约束检查和触发器
DELIMITER $$

-- 创建分类文章数量更新函数
CREATE FUNCTION `update_category_article_count`(
    category_id BIGINT,
    count_change INT
) RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE category_exists BOOLEAN;

    -- 检查分类是否存在且未删除
    SELECT COUNT(*) > 0 INTO category_exists
    FROM blog_category
    WHERE id = category_id AND del_flag = '0';

    -- 如果分类存在，更新文章数量
    IF category_exists THEN
        UPDATE blog_category
        SET article_count = GREATEST(0, article_count + count_change),
            update_time = NOW()
        WHERE id = category_id;
        RETURN TRUE;
    END IF;

    RETURN FALSE;
END$$

-- 文章更新前触发器
CREATE TRIGGER `tr_article_update_time`
BEFORE UPDATE ON `blog_article`
FOR EACH ROW
BEGIN
    -- 更新时间总是设置
    SET NEW.update_time = NOW();

    -- 如果禁用了触发器（用于批量更新），直接返回（不执行后续逻辑）
    IF @DISABLE_TRIGGERS IS NULL THEN
        -- 只有在未禁用触发器时才执行分类统计更新

    -- 检查旧文章状态是否有效（未删除且已发布）
    IF OLD.del_flag = '0' AND OLD.status = 1 AND OLD.category_id IS NOT NULL THEN
        SET old_valid_articles = 1;
    END IF;

    -- 检查新文章状态是否有效
    IF NEW.del_flag = '0' AND NEW.status = 1 AND NEW.category_id IS NOT NULL THEN
        SET new_valid_articles = 1;
    END IF;

    -- 处理分类变更
    IF OLD.category_id != NEW.category_ID THEN
        -- 从旧分类中减去
        IF old_valid_articles = 1 THEN
            CALL update_category_article_count(OLD.category_id, -1);
        END IF;

        -- 向新分类中添加
        IF new_valid_articles = 1 THEN
            CALL update_category_article_count(NEW.category_id, 1);
        END IF;
    ELSE
        -- 同一分类内的状态变更
        IF OLD.category_id IS NOT NULL THEN
            CALL update_category_article_count(OLD.category_id, new_valid_articles - old_valid_articles);
        END IF;
    END IF;
    END IF;
END$$

-- 文章插入前触发器
CREATE TRIGGER `tr_article_insert`
BEFORE INSERT ON `blog_article`
FOR EACH ROW
BEGIN
    -- 设置创建时间
    IF NEW.create_time IS NULL OR NEW.create_time = '0000-00-00 00:00:00' THEN
        SET NEW.create_time = NOW();
    END IF;
    SET NEW.update_time = NOW();

    -- 如果禁用了触发器（用于批量更新），直接返回（不执行后续逻辑）
    IF @DISABLE_TRIGGERS IS NULL THEN
        -- 自动增加分类文章数（仅在文章有效时）
        IF NEW.category_id IS NOT NULL AND NEW.del_flag = '0' AND NEW.status = 1 THEN
            CALL update_category_article_count(NEW.category_id, 1);
        END IF;
    END IF;
END$$

-- 文章删除后触发器（物理删除）
CREATE TRIGGER `tr_article_delete_physical`
AFTER DELETE ON `blog_article`
FOR EACH ROW
BEGIN
    -- 如果禁用了触发器，直接返回（不执行后续逻辑）
    IF @DISABLE_TRIGGERS IS NULL THEN
        -- 物理删除时减少分类文章数（仅在文章有效时）
        IF OLD.category_id IS NOT NULL AND OLD.del_flag = '0' AND OLD.status = 1 THEN
            CALL update_category_article_count(OLD.category_id, -1);
        END IF;
    END IF;
END$$

DELIMITER ;

-- ========== 创建分类数量修复存储过程 ==========

DELIMITER $$

-- 修复分类文章数量的存储过程
CREATE PROCEDURE `sp_fix_category_article_count`(
    IN category_id_param BIGINT DEFAULT NULL
)
BEGIN
    DECLARE fix_count INT DEFAULT 0;
    DECLARE total_categories INT DEFAULT 0;
    DECLARE mismatch_count INT DEFAULT 0;

    -- 禁用触发器以避免冲突
    SET @DISABLE_TRIGGERS = TRUE;

    -- 如果指定了分类ID，只修复该分类
    IF category_id_param IS NOT NULL THEN
        UPDATE blog_category c
        SET c.article_count = (
            SELECT COUNT(*)
            FROM blog_article a
            WHERE a.category_id = c.id
            AND a.del_flag = '0'
            AND a.status = 1
        ),
        c.update_time = NOW()
        WHERE c.id = category_id_param AND c.del_flag = '0';

        SET fix_count = ROW_COUNT();
    ELSE
        -- 修复所有分类
        UPDATE blog_category c
        SET c.article_count = (
            SELECT COUNT(*)
            FROM blog_article a
            WHERE a.category_id = c.id
            AND a.del_flag = '0'
            AND a.status = 1
        ),
        c.update_time = NOW()
        WHERE c.del_flag = '0';

        SET fix_count = ROW_COUNT();
    END IF;

    -- 重新启用触发器
    SET @DISABLE_TRIGGERS = NULL;

    -- 统计信息
    SELECT COUNT(*) INTO total_categories
    FROM blog_category
    WHERE del_flag = '0';

    SELECT COUNT(*) INTO mismatch_count
    FROM blog_category c
    LEFT JOIN (
        SELECT category_id, COUNT(*) as real_count
        FROM blog_article
        WHERE del_flag = '0' AND status = 1
        GROUP BY category_id
    ) a ON c.id = a.category_id
    WHERE c.del_flag = '0'
    AND (IFNULL(a.real_count, 0) != c.article_count OR a.real_count IS NULL);

    -- 返回修复结果
    SELECT
        fix_count as fixed_categories,
        total_categories as total_categories,
        mismatch_count as remaining_mismatches,
        CASE
            WHEN mismatch_count = 0 THEN '✅ 所有分类数量已同步'
            ELSE '⚠️ 仍有分类数量不匹配'
        END as status;
END$$

-- 验证分类文章数量的存储过程
CREATE PROCEDURE `sp_verify_category_article_count`()
BEGIN
    -- 显示详细的验证结果
    SELECT
        c.id,
        c.name,
        c.status as category_status,
        c.del_flag as category_del_flag,
        IFNULL(COUNT(a.id), 0) as actual_article_count,
        c.article_count as cached_article_count,
        CASE
            WHEN IFNULL(COUNT(a.id), 0) != c.article_count THEN '❌ 不匹配'
            ELSE '✅ 匹配'
        END as status,
        CASE
            WHEN c.status = 1 AND c.del_flag = '0' THEN '✅ 启用'
            ELSE '❌ 禁用/删除'
        END as visibility
    FROM blog_category c
    LEFT JOIN blog_article a ON c.id = a.category_id AND a.del_flag = '0' AND a.status = 1
    WHERE c.del_flag = '0'
    GROUP BY c.id, c.name, c.status, c.del_flag, c.article_count
    ORDER BY c.sort_order ASC;

    -- 统计前台可见的分类
    SELECT
        '🔍 前台可见分类统计（启用且未删除）：' AS type,
        COUNT(*) as total_categories,
        SUM(article_count) as total_articles
    FROM blog_category
    WHERE del_flag = '0' AND status = 1;

    -- 查找不匹配的分类
    SELECT
        '⚠️ 不匹配的分类：' AS type,
        COUNT(*) as mismatch_count
    FROM blog_category c
    LEFT JOIN (
        SELECT category_id, COUNT(*) as real_count
        FROM blog_article
        WHERE del_flag = '0' AND status = 1
        GROUP BY category_id
    ) a ON c.id = a.category_id
    WHERE c.del_flag = '0'
    AND (IFNULL(a.real_count, 0) != c.article_count OR a.real_count IS NULL);
END$$

DELIMITER ;

-- ========== 数据完整性验证 ==========

-- 修复可能的NULL值（确保view_count、like_count、comment_count字段不为NULL）
UPDATE blog_article SET view_count = 0 WHERE view_count IS NULL;
UPDATE blog_article SET like_count = 0 WHERE like_count IS NULL;
UPDATE blog_article SET comment_count = 0 WHERE comment_count IS NULL;

-- 验证关键字段完整性
SELECT '验证博客文章数据完整性...' AS status;
SELECT COUNT(*) AS empty_title_articles FROM blog_article WHERE title = '' OR title IS NULL;
SELECT COUNT(*) AS empty_content_articles FROM blog_article WHERE content = '' OR content IS NULL;
SELECT COUNT(*) AS invalid_status_articles FROM blog_article WHERE status NOT IN (0, 1);
SELECT COUNT(*) AS invalid_del_flag_articles FROM blog_article WHERE del_flag NOT IN ('0', '1');

SELECT '验证博客分类数据完整性...' AS status;
SELECT COUNT(*) AS empty_name_categories FROM blog_category WHERE name = '' OR name IS NULL;
SELECT COUNT(*) AS invalid_parent_categories FROM blog_category WHERE parent_id NOT IN (SELECT id FROM blog_category) AND parent_id != 0;

SELECT '验证博客标签数据完整性...' AS status;
SELECT COUNT(*) AS empty_name_tags FROM blog_tag WHERE name = '' OR name IS NULL;
SELECT COUNT(*) AS duplicate_tags FROM name, COUNT(*) as count FROM blog_tag GROUP BY name HAVING count > 1;

SELECT '验证博客设置数据完整性...' AS status;
SELECT COUNT(*) AS total_settings FROM blog_setting;
SELECT COUNT(*) AS duplicate_settings FROM config_key, COUNT(*) as count FROM blog_setting GROUP BY config_key HAVING count > 1;

-- ========== 性能统计 ==========

-- 统计各表数据量
SELECT 'blog_article' AS table_name, COUNT(*) AS record_count FROM blog_article
UNION ALL
SELECT 'blog_category' AS table_name, COUNT(*) AS record_count FROM blog_category
UNION ALL
SELECT 'blog_tag' AS table_name, COUNT(*) AS record_count FROM blog_tag
UNION ALL
SELECT 'blog_article_tag' AS table_name, COUNT(*) AS record_count FROM blog_article_tag
UNION ALL
SELECT 'blog_comment' AS table_name, COUNT(*) AS record_count FROM blog_comment
UNION ALL
SELECT 'blog_friend_link' AS table_name, COUNT(*) AS record_count FROM blog_friend_link
UNION ALL
SELECT 'blog_setting' AS table_name, COUNT(*) AS record_count FROM blog_setting;

-- ========== 数据库初始化完成 ==========

SELECT '🎉 数据库初始化完成！' AS result;
SELECT '✅ 若依系统基础表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'sys_%';
SELECT '✅ 博客系统表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'blog_%';
SELECT '✅ Quartz定时任务表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'QRTZ_%';
SELECT '✅ 代码生成器表数量：' AS info, COUNT(*) as count FROM information_schema.tables WHERE table_schema = 'newblog' AND table_name LIKE 'gen_%';
SELECT '✅ 博客管理菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_name = '博客管理' OR parent_id = 2000;
SELECT '✅ 系统管理菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_id BETWEEN 100 AND 107;
SELECT '✅ 系统监控菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_id BETWEEN 109 AND 114 OR menu_id BETWEEN 5000 AND 5013;
SELECT '✅ 数据统计菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_id = 5 OR parent_id = 5;
SELECT '✅ 系统工具菜单数量：' AS info, COUNT(*) as count FROM sys_menu WHERE menu_id BETWEEN 115 AND 117;

-- ========== 执行分类数量验证和修复 ==========

-- 调用修复存储过程确保分类数量正确
SELECT '🔧 正在执行分类数量修复...' AS status;
CALL sp_fix_category_article_count();

-- 调用验证存储过程显示修复结果
SELECT '✅ 分类数量修复完成，验证结果如下：' AS status;
CALL sp_verify_category_article_count();

-- 显示初始化的数据统计
SELECT '📊 数据初始化统计：' AS status;
SELECT CONCAT('📝 文章数量: ', COUNT(*)) AS info FROM blog_article WHERE del_flag = '0'
UNION ALL
SELECT CONCAT('📁 分类数量: ', COUNT(*)) AS info FROM blog_category WHERE del_flag = '0'
UNION ALL
SELECT CONCAT('🏷️  标签数量: ', COUNT(*)) AS info FROM blog_tag WHERE del_flag = '0'
UNION ALL
SELECT CONCAT('💬 评论数量: ', COUNT(*)) AS info FROM blog_comment
UNION ALL
SELECT CONCAT('🔗 友链数量: ', COUNT(*)) AS info FROM blog_friend_link WHERE del_flag = '0'
UNION ALL
SELECT CONCAT('⚙️  设置数量: ', COUNT(*)) AS info FROM blog_setting;

-- 验证标签和文章关联数据
SELECT '📋 标签列表验证' as type, t.id, t.name, t.color, t.description
FROM blog_tag t WHERE t.del_flag = 0
ORDER BY t.id;

-- 验证文章-标签关联
SELECT
    '📎 文章标签关联' as type,
    a.id as article_id,
    a.title as article_title,
    t.id as tag_id,
    t.name as tag_name,
    t.color as tag_color
FROM blog_article a
LEFT JOIN blog_article_tag bat ON a.id = bat.article_id
LEFT JOIN blog_tag t ON bat.tag_id = t.id AND t.del_flag = 0
WHERE a.id IN (1, 2, 4) AND a.del_flag = 0
ORDER BY a.id, t.name;

-- 统计每篇文章的标签数量
SELECT
    '📊 标签数量统计' as type,
    a.id as article_id,
    a.title as article_title,
    COUNT(bat.tag_id) as tag_count
FROM blog_article a
LEFT JOIN blog_article_tag bat ON a.id = bat.article_id
LEFT JOIN blog_tag t ON bat.tag_id = t.id AND t.del_flag = 0
WHERE a.id IN (1, 2, 4) AND a.del_flag = 0
GROUP BY a.id, a.title
ORDER BY a.id;

SELECT '🚀 博客系统已准备就绪，可以开始使用了！' AS final_message;

-- ===============================================================
-- 📖 使用指南和故障排除
-- ===============================================================

SELECT '📚 使用指南：' AS guide_title;
SELECT '1. 启动后端服务：mvn spring-boot:run' AS step1;
SELECT '2. 启动前端服务：cd ruoyi-ui && npm run dev' AS step2;
SELECT '3. 访问管理后台：http://localhost:8080' AS step3;
SELECT '4. 默认账号：admin / admin123' AS step4;
SELECT '5. 访问前台首页：http://localhost:3000' AS step5;

-- 验证关键配置
SELECT '🔍 关键配置验证：' AS verify_title;
SELECT config_key, config_value, description
FROM blog_setting
WHERE config_key IN ('blog_name', 'footer_enabled', 'copyright_enabled', 'comment_enabled')
ORDER BY config_key;

-- 常见问题解决方案
SELECT '🛠️  常见问题：' AS trouble_title;
SELECT 'Q: 忘记密码怎么办？' AS q1, 'A: 删除sys_user表中admin记录，重新运行脚本' AS a1;
SELECT 'Q: 前端页面空白？' AS q2, 'A: 检查API接口地址是否正确，端口是否被占用' AS a2;
SELECT 'Q: 评论功能不工作？' AS q3, 'A: 检查comment_enabled设置是否为true' AS a3;
SELECT 'Q: 图片上传失败？' AS q4, 'A: 检查文件路径权限和磁盘空间' AS a4;
SELECT 'Q: 分类文章数量不准确？' AS q5, 'A: 执行 CALL sp_fix_category_article_count(); 修复' AS a5;
SELECT 'Q: 文章阅读数不增加？' AS q6, 'A: 1. BlogArticleMapper.xml使用IFNULL处理NULL; 2. 确保view_count默认值为0; 3. 执行UPDATE修复现有数据; 4. 重启后端服务' AS a6;

-- 分类数量管理指南
SELECT '📊 分类数量管理指南：' AS category_guide;
SELECT '1. 查看验证结果：CALL sp_verify_category_article_count();' AS guide1;
SELECT '2. 修复单个分类：CALL sp_fix_category_article_count(分类ID);' AS guide2;
SELECT '3. 修复所有分类：CALL sp_fix_category_article_count();' AS guide3;
SELECT '4. 手动修复语句：UPDATE blog_category SET article_count = (SELECT COUNT(*) FROM blog_article WHERE category_id = 目标ID AND del_flag = ''0'' AND status = 1);' AS guide4;

-- 性能优化建议
SELECT '⚡ 性能优化建议：' AS performance_title;
SELECT '1. 定期清理软删除的数据：DELETE FROM blog_article WHERE del_flag = 1 AND update_time < DATE_SUB(NOW(), INTERVAL 30 DAY)' AS tip1;
SELECT '2. 优化大表查询：使用分页和索引' AS tip2;
SELECT '3. 启用Redis缓存：减轻数据库压力' AS tip3;
SELECT '4. 定期备份数据：mysqldump -u root -p newblog > backup.sql' AS tip4;

-- 联系信息
SELECT '📞 技术支持：' AS support_title;
SELECT 'Gitee: https://gitee.com/nevell/newblog' AS gitee;
SELECT 'Email: nevell@foxmail.com' AS email;
SELECT '✅ 初始化脚本执行完成！祝您使用愉快！' AS complete_message;

