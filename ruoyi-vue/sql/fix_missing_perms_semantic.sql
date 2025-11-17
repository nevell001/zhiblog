-- fix_missing_perms_semantic.sql
-- 目的：为缺少 perms（menu_type='F'）的按钮生成语义化 perms，基于 `menu_name` 规范化生成。
-- 注意（重要）：
--  - 如果 `menu_name` 包含中文或非 ASCII 字符，脚本会将这些字符替换为下划线；这会导致生成的 perms 可能不可读。
--  - 推荐优先使用 `path` 字段或手工提供英文映射以获得更可读的 perms。如果你希望基于 `path`，请取消注释相应部分并通知我。
-- 使用方式（推荐先 dry-run 查看 proposed_perms 再决定是否 APPLY）：
--   mysql -u root -p newblog < ruoyi-vue/sql/fix_missing_perms_semantic.sql

USE newblog;

-- 1) 备份：先把将被修改的记录备份到表 `backup_sys_menu_missing_perms_before_semantic`（仅当存在缺失 perms 时创建）
DROP TABLE IF EXISTS backup_sys_menu_missing_perms_before_semantic;
CREATE TABLE backup_sys_menu_missing_perms_before_semantic AS
SELECT * FROM sys_menu WHERE menu_type='F' AND (perms IS NULL OR TRIM(perms)='');

-- 2) 生成建议的语义化 perms（dry-run）
--    这里我们用 MySQL 8+ 的 REGEXP_REPLACE 来把非字母数字字符替换为下划线，并转小写。
DROP TABLE IF EXISTS proposed_semantic_perms;
CREATE TEMPORARY TABLE proposed_semantic_perms AS
SELECT
  menu_id,
  menu_name,
  perms AS old_perms,
  LOWER(REGEXP_REPLACE(menu_name, '[^A-Za-z0-9]+', '_')) AS norm_name,
  CONCAT('sys:menu:', LOWER(REGEXP_REPLACE(menu_name, '[^A-Za-z0-9]+', '_'))) AS new_perms
FROM sys_menu
WHERE menu_type='F' AND (perms IS NULL OR TRIM(perms) = '');

SELECT '=== PROPOSED CHANGES (DRY RUN) ===' AS header;
SELECT * FROM proposed_semantic_perms ORDER BY menu_id;
SELECT '=== END PROPOSED CHANGES ===' AS footer;

-- 3) 如确认无误，取消下面注释段落以应用更改（并在事务中执行）
-- START TRANSACTION;
--
-- UPDATE sys_menu m
-- JOIN proposed_semantic_perms p ON m.menu_id = p.menu_id
-- SET m.perms = p.new_perms;
--
-- COMMIT;
--
-- 4) 应用后可查看已更新记录：
-- SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type='F' AND perms LIKE 'sys:menu:%' ORDER BY menu_id;

-- 5) 可选：把这些新 perms 分配给管理员（role_id=1），下面为示例语句（请在确认后执行）
-- INSERT INTO sys_role_menu(role_id, menu_id)
-- SELECT 1, p.menu_id FROM proposed_semantic_perms p
-- LEFT JOIN sys_role_menu rm ON rm.role_id = 1 AND rm.menu_id = p.menu_id
-- WHERE rm.menu_id IS NULL;

-- 6) 回滚提示：如果需要恢复到备份，可使用 backup_sys_menu_missing_perms_before_semantic 表
-- UPDATE sys_menu m
-- JOIN backup_sys_menu_missing_perms_before_semantic b ON m.menu_id = b.menu_id
-- SET m.perms = b.perms;

-- 说明：
--  - 本脚本默认只对 perms 为空或仅包含空白的按钮记录生成 perms；不会覆盖已有 perms。
--  - 若你希望把所有非规范化（例如包含大写字母或驼峰的 perms）统一替换，也可以把 WHERE 条件调整为更宽松的匹配条件，我可以为你生成改版脚本。
-- 如需把规范规则改为使用 `path`（更推荐），回复“semantic path”，我会生成对应脚本。