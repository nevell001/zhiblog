-- fix_missing_perms.sql
-- 目的：为所有 menu_type='F'（按钮）且缺少 perms 的记录生成唯一 perms 标识
-- 使用前请先在测试环境或备份数据库上验证。该脚本不会修改角色映射，若需要请在确认 perms 后同步分配给角色。
-- 运行示例：
--   mysql -u root -p your_database < ruoyi-vue/sql/fix_missing_perms.sql

USE newblog;

-- 1) 先查看将被修改的行（备份建议）
SELECT menu_id, menu_name, menu_type, perms
FROM sys_menu
WHERE menu_type = 'F' AND (perms IS NULL OR TRIM(perms) = '');

-- 2) 备份这些行到临时表（可选），如需保留历史请取消注释以下两行
-- DROP TABLE IF EXISTS backup_sys_menu_missing_perms;
-- CREATE TABLE backup_sys_menu_missing_perms AS SELECT * FROM sys_menu WHERE menu_type='F' AND (perms IS NULL OR TRIM(perms)='');

-- 3) 以 menu_id 生成唯一 perms（格式：sys:menu:action:<menu_id>）并更新
START TRANSACTION;

UPDATE sys_menu
SET perms = CONCAT('sys:menu:action:', menu_id)
WHERE menu_type = 'F' AND (perms IS NULL OR TRIM(perms) = '');

COMMIT;

-- 4) 显示已更新的行（确认）
SELECT menu_id, menu_name, perms
FROM sys_menu
WHERE menu_type = 'F' AND perms LIKE 'sys:menu:action:%'
ORDER BY menu_id;

-- 注意：
-- - 1) 请先在开发或备份库上执行，确认结果符合预期。
-- - 2) 若你希望 perms 采用更语义化的命名（如 sys:menu:blog:add），请在更新前生成对应的映射表并用 JOIN/CASE 做替换。
-- - 3) 更新 perms 以后，可能需要将这些 perms 分配给管理员角色（sys_role_menu / sys_role_permissions 等），否则对应按钮的权限检查仍可能阻止操作。
-- 示例：将新 perms 分配给 role_id=1（管理员），请先确认表结构再执行：
-- INSERT INTO sys_role_menu(role_id, menu_id) 
-- SELECT 1, menu_id FROM sys_menu WHERE menu_type='F' AND perms LIKE 'sys:menu:action:%' AND menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id=1);

-- 如需我生成把这些 perms 按语义命名的脚本（基于 menu_name 或 path），请告诉我你的偏好，我可以生成带映射的脚本。