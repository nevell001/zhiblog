-- ===============================================================
-- 测试菜单 visible 字段
-- ===============================================================
-- 📅 创建时间：2026-01-13
-- 📝 描述：测试菜单的 visible 字段是否正确
-- ===============================================================

USE newblog;

-- 1. 查询所有一级菜单的 visible 字段（包括数据类型）
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    HEX(visible) as visible_hex,
    CHAR_LENGTH(visible) as visible_length,
    status,
    menu_type
FROM sys_menu 
WHERE parent_id = 0 AND status = '0'
ORDER BY order_num;

-- 2. 查询"数据统计"菜单的详细信息
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    HEX(visible) as visible_hex,
    CHAR_LENGTH(visible) as visible_length,
    status,
    menu_type,
    path,
    component
FROM sys_menu 
WHERE menu_id = 6000;

-- 3. 查询"系统工具"菜单的详细信息
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    HEX(visible) as visible_hex,
    CHAR_LENGTH(visible) as visible_length,
    status,
    menu_type,
    path,
    component
FROM sys_menu 
WHERE menu_id = 3;

-- 4. 测试：更新"数据统计"的 visible 字段为 '0'
UPDATE sys_menu 
SET visible = '0' 
WHERE menu_id = 6000;

-- 5. 验证更新结果
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    HEX(visible) as visible_hex,
    CHAR_LENGTH(visible) as visible_length,
    status
FROM sys_menu 
WHERE menu_id = 6000;

-- 6. 测试：更新"系统工具"的 visible 字段为 '0'
UPDATE sys_menu 
SET visible = '0' 
WHERE menu_id = 3;

-- 7. 验证更新结果
SELECT 
    menu_id,
    menu_name,
    parent_id,
    visible,
    HEX(visible) as visible_hex,
    CHAR_LENGTH(visible) as visible_length,
    status
FROM sys_menu 
WHERE menu_id = 3;