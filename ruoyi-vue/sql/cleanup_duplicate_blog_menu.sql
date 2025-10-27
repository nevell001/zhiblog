-- 清理重复的博客管理菜单配置
-- 执行时间：2025-10-27

-- 1. 首先备份当前菜单数据
CREATE TABLE IF NOT EXISTS sys_menu_backup_20251027 AS SELECT * FROM sys_menu;

-- 2. 删除重复的博客管理菜单项（保留 menu_id=2000 的配置）
DELETE FROM sys_menu 
WHERE menu_name = '博客管理' 
AND menu_id != 2000 
AND parent_id = 0;

-- 3. 删除重复的博客管理子菜单项
DELETE FROM sys_menu 
WHERE parent_id IN (
    SELECT menu_id FROM sys_menu 
    WHERE menu_name = '博客管理' 
    AND menu_id != 2000
);

-- 4. 验证清理结果
SELECT '清理完成，当前博客管理菜单配置：' AS result;
SELECT menu_id, menu_name, parent_id, order_num, path, component 
FROM sys_menu 
WHERE menu_name = '博客管理' OR parent_id = 2000
ORDER BY menu_id, order_num;

-- 5. 清理角色菜单关联表中的重复数据
DELETE FROM sys_role_menu 
WHERE menu_id IN (
    SELECT menu_id FROM sys_menu_backup_20251027 
    WHERE menu_name = '博客管理' 
    AND menu_id != 2000
);

-- 6. 为管理员角色重新分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE parent_id = 2000
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 7. 最终验证
SELECT '最终验证结果：' AS result;
SELECT COUNT(*) as '博客管理菜单数量' FROM sys_menu WHERE menu_name = '博客管理';
SELECT COUNT(*) as '博客管理子菜单数量' FROM sys_menu WHERE parent_id = 2000;