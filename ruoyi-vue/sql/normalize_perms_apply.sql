-- normalize_perms_apply.sql
-- 目的：对所有已有的 perms（menu_type='F' 且 perms 非空）进行规范化，
-- 把驼峰/大写片段转换为下划线风格（例：blog:friendLink:query -> blog:friend_link:query）。
-- 此脚本会：
--  1) 备份当前数据到 `backup_sys_menu_perms_before_normalize`（全表备份针对按钮行）
--  2) 生成 proposed 规范化 perms
--  3) 在事务中应用 UPDATE
--  4) 显示变更统计
-- 使用说明：
--  - 强烈建议先在测试库运行 `ruoyi-vue/sql/normalize_perms.sql` 的 dry-run，确认 proposed 结果。
--  - 仅在确认无误后再运行本脚本以实际应用变更。
-- 运行示例：
--   mysql -u root -p newblog < ruoyi-vue/sql/normalize_perms_apply.sql

USE newblog;

-- 1) 备份（覆盖旧备份）
DROP TABLE IF EXISTS backup_sys_menu_perms_before_normalize;
CREATE TABLE backup_sys_menu_perms_before_normalize AS
SELECT * FROM sys_menu WHERE menu_type='F' AND perms IS NOT NULL;

-- 2) 生成规范化值（临时表）
DROP TABLE IF EXISTS proposed_perms_normalize_apply;
CREATE TEMPORARY TABLE proposed_perms_normalize_apply AS
SELECT
  menu_id,
  perms AS old_perms,
  -- 在小写/数字 与 大写之间插入下划线，然后全部转小写
  LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) AS new_perms
FROM sys_menu
WHERE menu_type='F' AND perms IS NOT NULL
  AND LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) != perms;

-- 3) 显示将要被修改的行
SELECT '=== WILL APPLY THESE CHANGES ===' AS header;
SELECT * FROM proposed_perms_normalize_apply ORDER BY menu_id;
SELECT '=== END ===' AS footer;

-- 4) 应用变更（在事务中）
START TRANSACTION;

UPDATE sys_menu m
JOIN proposed_perms_normalize_apply p ON m.menu_id = p.menu_id
SET m.perms = p.new_perms;

COMMIT;

-- 5) 结果统计
SELECT '=== SUMMARY ===' AS header;
SELECT COUNT(*) AS changed_count FROM proposed_perms_normalize_apply;
SELECT '=== SAMPLE AFTER APPLY ===' AS header2;
SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type='F' ORDER BY menu_id LIMIT 200;

-- 6) 回滚说明（如需恢复）
-- 恢复示例：
-- UPDATE sys_menu m
-- JOIN backup_sys_menu_perms_before_normalize b ON m.menu_id = b.menu_id
-- SET m.perms = b.perms;

-- 注意：
-- - 本脚本会直接修改 perms 字段，请确保已备份并在测试环境验证过结果。
-- - 如果你只想对 perms 包含驼峰的特定前缀（如 blog:friendLink）做替换，请告知，我会生成更窄范围的脚本。
