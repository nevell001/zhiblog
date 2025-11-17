-- normalize_perms_apply_blog_prefix.sql
-- 目的：仅对 perms 以 'blog:' 前缀的按钮进行规范化（驼峰 -> 下划线），并提供备份、dry-run、apply 与回滚。
-- 强烈建议先在测试库运行 dry-run 并备份数据库。
-- 运行示例（dry-run 与 apply 使用相同脚本；apply 会执行事务并写入）：
--   mysql -u root -p newblog < ruoyi-vue/sql/normalize_perms_apply_blog_prefix.sql

USE newblog;

-- 1) 备份当前匹配行到表（覆盖旧备份）
DROP TABLE IF EXISTS backup_sys_menu_perms_blog_prefix_before_normalize;
CREATE TABLE backup_sys_menu_perms_blog_prefix_before_normalize AS
SELECT * FROM sys_menu WHERE menu_type='F' AND perms LIKE 'blog:%';

-- 2) 生成 proposed 规范化值（仅针对以 blog: 前缀的 perms）
DROP TABLE IF EXISTS proposed_perms_normalize_blog_prefix;
CREATE TEMPORARY TABLE proposed_perms_normalize_blog_prefix AS
SELECT
  menu_id,
  menu_name,
  perms AS old_perms,
  -- 在小写/数字 与 大写之间插入下划线，然后整体转小写
  LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) AS new_perms
FROM sys_menu
WHERE menu_type='F' AND perms LIKE 'blog:%'
  AND LOWER(REGEXP_REPLACE(perms, '([a-z0-9])([A-Z])', '\\1_\\2')) != perms;

-- 3) Dry-run 输出（查看 proposed 改动）
SELECT '=== PROPOSED NORMALIZATIONS FOR blog: PREFIX ===' AS header;
SELECT * FROM proposed_perms_normalize_blog_prefix ORDER BY menu_id;
SELECT '=== END PROPOSED ===' AS footer;

-- 4) 如果 dry-run 看起来正确，下面的部分会在事务中应用更改（已打开为默认执行）
START TRANSACTION;

UPDATE sys_menu m
JOIN proposed_perms_normalize_blog_prefix p ON m.menu_id = p.menu_id
SET m.perms = p.new_perms;

COMMIT;

-- 5) 结果统计与示例
SELECT '=== SUMMARY ===' AS header2;
SELECT COUNT(*) AS changed_count FROM proposed_perms_normalize_blog_prefix;
SELECT '=== SAMPLE AFTER APPLY ===' AS header3;
SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type='F' AND perms LIKE 'blog:%' ORDER BY menu_id LIMIT 200;

-- 6) 回滚示例（如需恢复到备份表）
-- 注意：回滚会把 perms 恢复为备份表中的原始 perms
-- 使用以下语句进行恢复（在 mysql 提示符中运行或作为单条命令）
-- mysql -u root -p -D newblog -e "UPDATE sys_menu m JOIN backup_sys_menu_perms_blog_prefix_before_normalize b ON m.menu_id = b.menu_id SET m.perms = b.perms;"

-- 说明：
-- - 本脚本只影响以 'blog:' 开头的 perms，降低误改范围。
-- - 若你希望先做 dry-run 并手动应用，请把 START/COMMIT 段注释掉，然后手动查看 proposed 表后执行 UPDATE/COMMIT。
-- - 若你希望我生成一个仅打印但不执行写操作的 dry-run 版本，我可以把 apply 段注释并保存为另一文件。