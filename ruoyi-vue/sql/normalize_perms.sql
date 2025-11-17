-- normalize_perms.sql
-- 目的：规范化已有的 perms 命名风格（将驼峰或大写片段转换为下划线风格）
-- 注意：在执行任何写操作之前请先备份数据库或本脚本提供的备份表。
-- 使用示例（dry-run + apply）：
--   mysql -u root -p newblog < ruoyi-vue/sql/normalize_perms.sql

USE newblog;

-- 1) 备份当前所有按钮的 perms（仅备份非空 perms）
DROP TABLE IF EXISTS backup_sys_menu_perms_before_normalize;
CREATE TABLE backup_sys_menu_perms_before_normalize AS
SELECT * FROM sys_menu WHERE menu_type='F' AND perms IS NOT NULL;

-- 2) Dry-run：显示将被修改的行及建议的新 perms
DROP TABLE IF EXISTS proposed_perms_normalize;
CREATE TEMPORARY TABLE proposed_perms_normalize AS
SELECT
  menu_id,
  menu_name,
  perms AS old_perms,
  LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) AS new_perms
FROM sys_menu
WHERE menu_type='F' AND perms IS NOT NULL
  AND LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) != perms;

SELECT '=== PROPOSED NORMALIZATIONS ===' AS header;
SELECT * FROM proposed_perms_normalize ORDER BY menu_id;
SELECT '=== END PROPOSED NORMALIZATIONS ===' AS footer;

-- 3) 若确认无误，请取消下面注释以应用更改（在事务中执行）
-- START TRANSACTION;
--
-- UPDATE sys_menu m
-- JOIN proposed_perms_normalize p ON m.menu_id = p.menu_id
-- SET m.perms = p.new_perms;
--
-- COMMIT;
--
-- 4) 应用后检查结果：
-- SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type='F' ORDER BY menu_id;

-- 5) 回滚示例（如果需要恢复为备份值）
-- UPDATE sys_menu m
-- JOIN backup_sys_menu_perms_before_normalize b ON m.menu_id = b.menu_id
-- SET m.perms = b.perms;

-- 说明：
-- - 该脚本会把 perms 中的每个大写字母前插入下划线并整体转小写，示例：
--     'blog:friendLink:query' -> 'blog:friend_link:query'
-- - 如果你希望使用不同的规则（例如把驼峰拆分规则更严格或只针对某一段），请告诉我需求，我会生成更精细的版本。