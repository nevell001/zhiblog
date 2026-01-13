-- ===============================================================
-- 检查菜单数据脚本
-- ===============================================================
-- 📅 创建时间：2026-01-13
-- 📝 描述：检查数据库中的菜单数据，用于调试菜单显示问题
-- ===============================================================

USE newblog;

-- 1. 查询所有一级菜单（parent_id = 0）
SELECT 
    menu_id,
    menu_name,
    parent_id,
    order_num,
    path,
    component,
    menu_type,
    visible,
    status,
    icon
FROM sys_menu 
WHERE parent_id = 0 AND status = '0'
ORDER BY order_num;

-- 2. 查询系统管理（menu_id = 1）下的所有子菜单
SELECT 
    menu_id,
    menu_name,
    parent_id,
    order_num,
    path,
    component,
    menu_type,
    visible,
    status,
    icon
FROM sys_menu 
WHERE parent_id = 1 AND status = '0'
ORDER BY order_num;

-- 3. 查询所有菜单的 visible 状态统计
SELECT 
    visible,
    COUNT(*) as count,
    CASE 
        WHEN visible = '0' THEN '显示'
        WHEN visible = '1' THEN '隐藏'
        ELSE '未知'
    END as visible_name
FROM sys_menu 
WHERE status = '0' AND menu_type IN ('M', 'C')
GROUP BY visible;

-- 4. 查询所有菜单的 status 状态统计
SELECT 
    status,
    COUNT(*) as count,
    CASE 
        WHEN status = '0' THEN '正常'
        WHEN status = '1' THEN '停用'
        ELSE '未知'
    END as status_name
FROM sys_menu 
WHERE menu_type IN ('M', 'C')
GROUP BY status;

-- 5. 查询菜单类型分布
SELECT 
    menu_type,
    COUNT(*) as count,
    CASE 
        WHEN menu_type = 'M' THEN '目录'
        WHEN menu_type = 'C' THEN '菜单'
        WHEN menu_type = 'F' THEN '按钮'
        ELSE '未知'
    END as menu_type_name
FROM sys_menu 
WHERE status = '0'
GROUP BY menu_type;

-- 6. 查询所有菜单的层级结构
SELECT 
    m1.menu_id,
    m1.menu_name as level1_name,
    m2.menu_id as level2_id,
    m2.menu_name as level2_name,
    m2.visible,
    m2.status
FROM sys_menu m1
LEFT JOIN sys_menu m2 ON m1.menu_id = m2.parent_id
WHERE m1.parent_id = 0 AND m1.status = '0' AND (m2.status = '0' OR m2.status IS NULL)
ORDER BY m1.order_num, m2.order_num;

-- 7. 检查是否有菜单的 visible 字段为 NULL
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status
FROM sys_menu 
WHERE visible IS NULL;

-- 8. 检查是否有菜单的 status 字段为 NULL
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status
FROM sys_menu 
WHERE status IS NULL;

-- 9. 检查角色菜单关联（查询 admin 角色有权访问的菜单）
SELECT 
    m.menu_id,
    m.menu_name,
    m.parent_id,
    m.visible,
    m.status
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
INNER JOIN sys_role r ON rm.role_id = r.role_id
WHERE r.role_key = 'admin' AND m.status = '0' AND m.menu_type IN ('M', 'C')
ORDER BY m.parent_id, m.order_num;

-- 10. 检查用户角色关联（查询 admin 用户有权访问的菜单）
SELECT 
    m.menu_id,
    m.menu_name,
    m.parent_id,
    m.visible,
    m.status
FROM sys_menu m
INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id
INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id
INNER JOIN sys_user u ON ur.user_id = u.user_id
WHERE u.user_name = 'admin' AND m.status = '0' AND m.menu_type IN ('M', 'C')
ORDER BY m.parent_id, m.order_num;