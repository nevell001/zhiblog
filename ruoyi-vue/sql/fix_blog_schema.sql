-- ========== 修复博客系统数据库结构 ==========
-- 创建时间：2025-11-07
-- 描述：修复博客系统数据库字段缺失问题

USE newblog;

-- 为blog_tag表添加缺失的字段
ALTER TABLE blog_tag
ADD COLUMN description varchar(255) DEFAULT NULL COMMENT '标签描述' AFTER name,
ADD COLUMN color varchar(20) DEFAULT '#409EFF' COMMENT '标签颜色' AFTER description,
ADD COLUMN icon varchar(100) DEFAULT NULL COMMENT '标签图标' AFTER color;

-- 验证修复结果
SELECT 'blog_tag表结构修复完成' AS result;
DESCRIBE blog_tag;