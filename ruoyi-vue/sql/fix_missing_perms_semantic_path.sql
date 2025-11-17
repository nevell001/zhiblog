-- fix_missing_perms_semantic_path.sql
-- 目的：为缺少 perms（menu_type='F'）的按钮生成语义化 perms，优先基于 `path` 字段生成，path 为空时回退到 `menu_name`。
-- 说明：脚本包含备份、dry-run、应用（注释）、以及回滚示例。请先在测试库或备份上执行 dry-run 并确认结果。
-- 运行示例（dry-run）：
--   mysql -u root -p newblog < ruoyi-vue/sql/fix_missing_perms_semantic_path.sql

USE newblog;

-- 1) 备份：先把将被修改的记录备份到表 `backup_sys_menu_missing_perms_path_before_semantic`（仅当存在缺失 perms 时创建）
DROP TABLE IF EXISTS backup_sys_menu_missing_perms_path_before_semantic;
CREATE TABLE backup_sys_menu_missing_perms_path_before_semantic AS
SELECT * FROM sys_menu WHERE menu_type='F' AND (perms IS NULL OR TRIM(perms) = '');

-- 2) 生成建议的语义化 perms（dry-run）
--    规则：
--      - 如果 `path` 不为空，去除首尾斜杠，将 '/' 替换为 ':'，对路径中的驼峰段插入下划线并全部转小写；最终前缀为 'sys:menu:'。
--      - 如果 `path` 为空，则回退到基于 `menu_name` 的规范化（把非字母数字替换为下划线并转小写）。
-- 注意：本脚本使用 MySQL 8 的 REGEXP_REPLACE。

DROP TABLE IF EXISTS proposed_semantic_perms_path;
CREATE TEMPORARY TABLE proposed_semantic_perms_path AS
SELECT
  menu_id,
  menu_name,
  path,
  perms AS old_perms,
  CASE
    WHEN path IS NOT NULL AND TRIM(path) <> '' THEN
      CONCAT('sys:menu:', LOWER(REGEXP_REPLACE(REPLACE(TRIM(BOTH '/' FROM path), '/', ':'), '([A-Z])', '_\\1')))
    ELSE
      CONCAT('sys:menu:', LOWER(REGEXP_REPLACE(menu_name, '[^A-Za-z0-9]+', '_')))
  END AS new_perms
FROM sys_menu
WHERE menu_type='F' AND (perms IS NULL OR TRIM(perms) = '');

SELECT '=== PROPOSED CHANGES (PATH-BASED DRY RUN) ===' AS header;
SELECT * FROM proposed_semantic_perms_path ORDER BY menu_id;
SELECT '=== END PROPOSED CHANGES ===' AS footer;

-- 3) 如确认无误，取消下面注释段落以应用更改（并在事务中执行）
-- START TRANSACTION;
--
-- UPDATE sys_menu m
-- JOIN proposed_semantic_perms_path p ON m.menu_id = p.menu_id
-- SET m.perms = p.new_perms;
--
-- COMMIT;
--
-- 4) 应用后可查看已更新记录：
-- SELECT menu_id, menu_name, path, perms FROM sys_menu WHERE menu_type='F' AND perms LIKE 'sys:menu:%' ORDER BY menu_id;

-- 5) 可选：把这些新 perms 分配给管理员（role_id=1），下面为示例语句（请在确认后执行）
-- INSERT INTO sys_role_menu(role_id, menu_id)
-- SELECT 1, p.menu_id FROM proposed_semantic_perms_path p
-- LEFT JOIN sys_role_menu rm ON rm.role_id = 1 AND rm.menu_id = p.menu_id
-- WHERE rm.menu_id IS NULL;

-- 6) 回滚提示：如果需要恢复到备份，可使用 backup_sys_menu_missing_perms_path_before_semantic 表
-- UPDATE sys_menu m
-- JOIN backup_sys_menu_missing_perms_path_before_semantic b ON m.menu_id = b.menu_id
-- SET m.perms = b.perms;

-- 说明：
-- - 本脚本不会覆盖已有的 perms，仅对 perms 为空或仅包含空白的按钮记录生成 perms。
-- - 如果你希望也规范化已有但格式不一致的 perms（如驼峰混合），我可以生成一个 "normalize_perms" 的改版来覆盖所有符合条件的记录。