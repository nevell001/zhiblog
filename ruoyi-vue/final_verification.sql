USE newblog;

SELECT '========================================' AS '';
SELECT '系统模块修复最终验证' AS '标题';
SELECT '========================================' AS '';

SELECT '' AS '';
SELECT '一级菜单:' AS '类别';
SELECT menu_id, menu_name, path, icon
FROM sys_menu
WHERE parent_id = 0 AND menu_id IN (1, 2, 3)
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统管理模块子菜单:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 1
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统监控模块子菜单:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 2
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统工具模块子菜单:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 3
ORDER BY order_num;

SELECT '' AS '';
SELECT '管理员角色权限统计:' AS '类别';
SELECT 
    '系统管理' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 1 OR m.menu_id = 1)
UNION ALL
SELECT 
    '系统监控' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 2 OR m.menu_id = 2)
UNION ALL
SELECT 
    '系统工具' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 3 OR m.menu_id = 3);

SELECT '========================================' AS '';
SELECT '修复完成！请刷新后台页面查看效果。' AS '提示';
SELECT '========================================' AS '';