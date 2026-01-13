-- 修复菜单显示状态问题
-- 确保"通知公告"菜单的 visible 字段为 '0'（显示）

-- 查询当前"通知公告"菜单的 visible 状态
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status,
    menu_type
FROM sys_menu 
WHERE menu_id = 107;

-- 更新"通知公告"菜单的 visible 状态为显示
UPDATE sys_menu 
SET visible = '0' 
WHERE menu_id = 107;

-- 验证更新结果
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status,
    menu_type
FROM sys_menu 
WHERE menu_id = 107;

-- 查询所有系统管理菜单及其 visible 状态
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status,
    menu_type,
    order_num
FROM sys_menu 
WHERE parent_id = 1 
ORDER BY order_num;

-- 确保"系统管理"父菜单的 visible 状态为显示
UPDATE sys_menu 
SET visible = '0' 
WHERE menu_id = 1;

-- 验证"系统管理"父菜单的 visible 状态
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    status,
    menu_type
FROM sys_menu 
WHERE menu_id = 1;