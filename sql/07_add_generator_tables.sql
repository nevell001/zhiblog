-- ===============================================================
-- 代码生成器表初始化脚本
-- ===============================================================
-- 📅 创建时间：2026-01-13
-- 📝 描述：创建代码生成器所需的 gen_table 和 gen_table_column 表
-- ⚠️ 注意：在执行 init_database.sql 之后执行此脚本
-- ===============================================================

USE newblog;

-- ========== 代码生成业务表 ==========
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

-- ========== 代码生成字段表 ==========
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

-- ========== 执行完成提示 ==========

SELECT '✅ 代码生成器表创建完成！' AS '状态';
SELECT '📋 创建的表数量：' AS '信息', 2 AS '数量';

-- 显示创建的表
SELECT '📁 代码生成器表列表：' AS '类型', 
       table_name AS '表名', 
       table_comment AS '表描述',
       table_rows AS '记录数'
FROM information_schema.tables
WHERE table_schema = 'newblog' 
  AND table_name IN ('gen_table', 'gen_table_column')
ORDER BY table_name;