-- assign_missing_button_perms_to_admin.sql
-- 目的：为没有分配给管理员(role_id=1)的按钮(menu_type='F')创建映射。
-- 脚本包含：备份、dry-run（显示将要插入的行）、apply（插入）和回滚（从备份恢复或删除新增行）。
-- 使用：
--   mysql -u root -p newblog < ruoyi-vue/sql/assign_missing_button_perms_to_admin.sql

USE newblog;

-- 1) 备份当前的管理员映射（覆盖旧备份）
DROP TABLE IF EXISTS backup_sys_role_menu_before_assign;
CREATE TABLE backup_sys_role_menu_before_assign AS
SELECT * FROM sys_role_menu WHERE role_id = 1;

-- 2) Dry-run：显示将要插入的 menu_id（没有映射给管理员的按钮）
DROP TABLE IF EXISTS proposed_assign_admin_buttons;
CREATE TEMPORARY TABLE proposed_assign_admin_buttons AS
SELECT m.menu_id, m.menu_name, m.perms
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.menu_id = m.menu_id AND rm.role_id = 1
WHERE m.menu_type = 'F' AND (rm.menu_id IS NULL OR rm.menu_id = 0);

SELECT '=== PROPOSED ASSIGNMENTS (DRY-RUN) ===' AS header;
SELECT * FROM proposed_assign_admin_buttons ORDER BY menu_id;
SELECT '=== END PROPOSED ===' AS footer;

-- 3) Apply：在确认无误后，下面语句会把缺失的映射插入 sys_role_menu
-- 注意：插入时不检查重复（上面用 LEFT JOIN 过滤了已存在的），也不会修改现有映射
START TRANSACTION;

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, p.menu_id FROM proposed_assign_admin_buttons p;

COMMIT;

-- 4) 结果验证：显示管理员当前的按钮映射数量及样例
SELECT '=== ADMIN MAPPINGS AFTER APPLY ===' AS header2;
SELECT COUNT(*) AS admin_button_mappings FROM sys_role_menu WHERE role_id = 1 AND menu_id IN (SELECT menu_id FROM sys_menu WHERE menu_type='F');
SELECT role_id, menu_id FROM sys_role_menu WHERE role_id = 1 ORDER BY menu_id LIMIT 200;

-- 5) 回滚示例（如果要恢复为备份的原始 state）
-- 方案 A (恢复备份表)：
-- UPDATE sys_role_menu rm JOIN backup_sys_role_menu_before_assign b ON rm.role_id = b.role_id AND rm.menu_id = b.menu_id
-- SET rm.role_id = b.role_id; -- 这里通常不需要更新字段，本语句是占位符示例
-- 方案 B (删除自脚本插入的行)：
-- 注意：下面的 DELETE 会移除自本次运行后新增的映射（基于 backup 对比），请先在测试库验证
-- DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id NOT IN (SELECT menu_id FROM backup_sys_role_menu_before_assign);

-- 说明：
-- - 请先在测试环境运行 dry-run（脚本会打印 proposed rows）。
-- - 若你想只生成 dry-run 文件而不自动 apply，请在文件中把 `START TRANSACTION;` 到 `COMMIT;` 段注释掉，然后运行脚本查看输出。
-- - 插入后如需回滚，建议使用 backup 表或执行上面的 DELETE（谨慎）。
