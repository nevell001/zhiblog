-- =============================================
-- 博客用户注册登录系统 - 数据库迁移脚本
-- Version: 1.0.0
-- Date: 2025-01-26
-- Description: 创建博客用户认证相关表结构
-- =============================================

-- 1. 创建博客用户角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES(3, '博客用户', 'blog_user', 3, 1, 1, 1, '0', '0', 'admin', NOW(), '', NULL, '博客普通用户，用于前台注册登录');

-- 2. 创建邮箱验证码表
DROP TABLE IF EXISTS blog_email_code;
CREATE TABLE blog_email_code (
  id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  email VARCHAR(50) NOT NULL COMMENT '邮箱地址',
  code VARCHAR(6) NOT NULL COMMENT '验证码',
  code_type VARCHAR(20) NOT NULL COMMENT '验证码类型：register=注册, reset=重置密码, bind=绑定邮箱',
  expire_time DATETIME NOT NULL COMMENT '过期时间',
  used TINYINT(1) DEFAULT 0 COMMENT '是否已使用：0=未使用, 1=已使用',
  ip_address VARCHAR(50) DEFAULT NULL COMMENT '请求IP地址',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  use_time DATETIME DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (id),
  INDEX idx_email_code (email, code_type),
  INDEX idx_expire_time (expire_time),
  INDEX idx_code (code)
) ENGINE=INNODB COMMENT='博客邮箱验证码表';

-- 3. 扩展 sys_user 表字段（可选增强功能）
-- 如果需要邮箱验证状态字段，取消以下注释
-- ALTER TABLE sys_user ADD COLUMN email_verified TINYINT(1) DEFAULT 0 COMMENT '邮箱是否验证：0=未验证, 1=已验证' AFTER email;

-- 如果需要博客昵称字段，取消以下注释
-- ALTER TABLE sys_user ADD COLUMN blog_nickname VARCHAR(50) DEFAULT NULL COMMENT '博客昵称' AFTER nick_name;

-- =============================================
-- 说明
-- =============================================
-- 1. role_id=3 为博客用户角色，用于前台注册用户
-- 2. blog_email_code 表用于存储各类邮箱验证码
-- 3. 验证码有效期默认为 5 分钟，使用后立即标记为已使用
-- 4. 代码中的 user_type='01' 表示博客用户，'00' 表示系统管理员
-- =============================================
