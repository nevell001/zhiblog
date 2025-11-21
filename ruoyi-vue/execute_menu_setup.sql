-- 完整菜单系统执行脚本
-- 按顺序执行以下所有脚本以完成菜单系统搭建

-- ========================================
-- 执行步骤说明
-- ========================================
/*
执行顺序：
1. complete_menu_system.sql - 创建主菜单结构
2. button_permissions_config.sql - 添加按钮权限
3. verify_menu_setup.sql - 验证菜单配置

使用方法：
mysql -u root -p newblog < complete_menu_system.sql
mysql -u root -p newblog < button_permissions_config.sql
mysql -u root -p newblog < verify_menu_setup.sql
*/

-- 开始执行菜单系统创建
USE newblog;

-- 首先清理可能存在的冲突数据
DELETE FROM sys_role_menu WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
);

DELETE FROM sys_menu WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
);

-- 重置自增ID，确保菜单ID连续性
ALTER TABLE sys_menu AUTO_INCREMENT = 5000;

-- 显示执行结果
SELECT '开始执行菜单系统创建...' AS status;

-- 最终菜单结构验证
SELECT 
    menu_id,
    menu_name,
    parent_id,
    order_num,
    menu_type,
    visible,
    status,
    perms,
    icon,
    CASE 
        WHEN parent_id = 0 THEN '主菜单'
        WHEN menu_type = 'M' THEN '目录'
        WHEN menu_type = 'C' THEN '菜单'
        WHEN menu_type = 'F' THEN '按钮'
        ELSE '其他'
    END as menu_level
FROM sys_menu 
WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
)
ORDER BY parent_id, order_num;

SELECT '菜单系统创建完成！请继续执行按钮权限配置脚本。' AS result;