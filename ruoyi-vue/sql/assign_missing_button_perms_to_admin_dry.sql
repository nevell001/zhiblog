-- assign_missing_button_perms_to_admin_dry.sql
-- Dry-run only: 列出将被插入为管理员(role_id=1)的按钮映射，但不执行任何写操作。
-- 运行：
--   mysql -u root -p newblog < ruoyi-vue/sql/assign_missing_button_perms_to_admin_dry.sql

USE newblog;

-- Dry-run: 备份不会被创建于 dry-run 版本，若需要备份请先运行完整版脚本或做 mysqldump

-- 显示将要插入的 menu_id（没有映射给管理员的按钮）
DROP TABLE IF EXISTS proposed_assign_admin_buttons;
CREATE TEMPORARY TABLE proposed_assign_admin_buttons AS
SELECT m.menu_id, m.menu_name, m.perms
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.menu_id = m.menu_id AND rm.role_id = 1
WHERE m.menu_type = 'F' AND (rm.menu_id IS NULL OR rm.menu_id = 0);

SELECT '=== PROPOSED ASSIGNMENTS (DRY-RUN) ===' AS header;
SELECT * FROM proposed_assign_admin_buttons ORDER BY menu_id;
SELECT '=== END PROPOSED ===' AS footer;

-- 注意：该文件仅用于查看拟插入的行，不会修改数据库。若确认无误，请运行完整版脚本：
--   mysql -u root -p newblog < ruoyi-vue/sql/assign_missing_button_perms_to_admin.sql
