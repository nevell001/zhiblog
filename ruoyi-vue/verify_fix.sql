USE newblog;

SELECT '系统管理模块菜单:' AS '类别';
SELECT menu_id, menu_name, path, component
FROM sys_menu
WHERE parent_id = 1
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统监控模块菜单:' AS '类别';
SELECT menu_id, menu_name, path, component
FROM sys_menu
WHERE parent_id = 2
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统工具模块菜单:' AS '类别';
SELECT menu_id, menu_name, path, component
FROM sys_menu
WHERE parent_id = 3
ORDER BY order_num;