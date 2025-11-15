-- ========================================
-- 强制刷新菜单权限
-- 创建时间：2025-11-15
-- 描述：确保管理员能看到所有菜单
-- ========================================

USE newblog;

-- 1. 清除管理员的所有菜单权限
DELETE FROM sys_role_menu WHERE role_id = 1;

-- 2. 重新分配所有菜单权限给管理员
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id > 0;

-- 3. 验证权限分配
SELECT '========================================' AS '';
SELECT '权限刷新完成！' AS '结果';
SELECT '========================================' AS '';

SELECT '管理员拥有的菜单权限:' AS '类别';
SELECT COUNT(*) AS '总权限数' FROM sys_role_menu WHERE role_id = 1;

SELECT '' AS '';
SELECT '一级菜单列表:' AS '类别';
SELECT menu_id, menu_name, path, icon, visible, status
FROM sys_menu 
WHERE parent_id = 0
ORDER BY order_num;

SELECT '' AS '';
SELECT '博客管理子菜单:' AS '类别';
SELECT menu_id, menu_name, visible, status, perms
FROM sys_menu 
WHERE parent_id = 2000
ORDER BY order_num;

SELECT '' AS '';
SELECT '数据统计子菜单:' AS '类别';
SELECT menu_id, menu_name, visible, status, perms
FROM sys_menu 
WHERE parent_id = 3000
ORDER BY order_num;

SELECT '========================================' AS '';
SELECT '请执行以下操作：' AS '提示';
SELECT '1. 退出后台管理系统' AS '步骤';
SELECT '2. 清除浏览器缓存（Ctrl+Shift+Delete）' AS '步骤';
SELECT '3. 重新登录系统' AS '步骤';
SELECT '========================================' AS '';