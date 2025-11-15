-- ========== 菜单诊断脚本 ==========
-- 用于检查后台管理菜单的配置状态

USE newblog;

-- 1. 查看所有一级菜单
SELECT '========== 一级菜单 ==========' AS info;
SELECT menu_id, menu_name, parent_id, order_num, path, visible, status, perms, icon
FROM sys_menu 
WHERE parent_id = 0 
ORDER BY order_num;

-- 2. 查看博客管理相关菜单
SELECT '========== 博客管理菜单 ==========' AS info;
SELECT menu_id, menu_name, parent_id, order_num, path, component, visible, status, perms, icon
FROM sys_menu 
WHERE menu_id >= 2000 OR parent_id >= 2000
ORDER BY menu_id;

-- 3. 查看管理员角色的菜单权限
SELECT '========== 管理员角色菜单权限 ==========' AS info;
SELECT rm.role_id, r.role_name, rm.menu_id, m.menu_name, m.menu_type, m.visible, m.status
FROM sys_role_menu rm
JOIN sys_role r ON rm.role_id = r.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.menu_id >= 2000 OR m.parent_id >= 2000)
ORDER BY rm.menu_id;

-- 4. 检查菜单可见性和状态
SELECT '========== 菜单状态检查 ==========' AS info;
SELECT 
    menu_id,
    menu_name,
    parent_id,
    CASE visible WHEN '0' THEN '显示' WHEN '1' THEN '隐藏' ELSE visible END AS visible_status,
    CASE status WHEN '0' THEN '正常' WHEN '1' THEN '停用' ELSE status END AS menu_status,
    menu_type,
    perms
FROM sys_menu 
WHERE menu_id >= 2000 OR parent_id >= 2000
ORDER BY menu_id;

-- 5. 统计信息
SELECT '========== 统计信息 ==========' AS info;
SELECT 
    '博客菜单总数' AS item,
    COUNT(*) AS count
FROM sys_menu 
WHERE menu_id >= 2000 OR parent_id >= 2000
UNION ALL
SELECT 
    '管理员博客权限数' AS item,
    COUNT(*) AS count
FROM sys_role_menu 
WHERE role_id = 1 AND menu_id >= 2000;