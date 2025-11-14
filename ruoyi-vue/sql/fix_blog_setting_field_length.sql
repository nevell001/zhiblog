-- 修复博客设置表字段长度问题
-- 将 config_value 字段从 varchar(1024) 改为 LONGTEXT 以支持存储大量数据（如Base64编码的图片）

USE newblog;

-- 修改 blog_setting 表的 config_value 字段类型
ALTER TABLE `blog_setting` MODIFY COLUMN `config_value` LONGTEXT COMMENT '配置项Value';

-- 验证修改结果
DESCRIBE `blog_setting`;

SELECT '博客设置表字段修改完成！config_value 字段已改为 LONGTEXT 类型，可以存储大量数据。' AS result;