-- ========== 修复博客管理菜单 ==========
-- 创建时间：2025-11-15
-- 描述：修复博客管理菜单的显示和状态问题

USE newblog;

-- 1. 先检查并创建博客管理主菜单（如果不存在）
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 5, 'blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录');

-- 2. 修复所有博客子菜单的显示状态（visible='0'显示, status='0'正常）
UPDATE sys_menu 
SET visible = '0', status = '0'
WHERE menu_id IN (2001, 2002, 2004, 2005, 2006, 2007);

-- 3. 修复所有博客按钮权限的显示状态
UPDATE sys_menu 
SET visible = '0', status = '0'
WHERE menu_id >= 20010 AND menu_id <= 20071;

-- 4. 删除重复的关于作者菜单（menu_id=20064, 20065, 20066, 10090, 10091）
DELETE FROM sys_role_menu WHERE menu_id IN (20064, 20065, 20066, 10090, 10091, 1009);
DELETE FROM sys_menu WHERE menu_id IN (20064, 20065, 20066, 10090, 10091, 1009);

-- 5. 确保博客管理主菜单有权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 2000);

-- 6. 确保所有博客菜单都有管理员权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu 
WHERE (menu_id >= 2000 AND menu_id < 3000) OR (menu_id >= 20000 AND menu_id < 30000);

-- ========== 验证脚本 ==========
SELECT '========== 修复完成 ==========' AS info;

SELECT '博客管理主菜单:' AS info;
SELECT menu_id, menu_name, parent_id, visible, status, icon
FROM sys_menu 
WHERE menu_id = 2000;

SELECT '博客子菜单:' AS info;
SELECT menu_id, menu_name, parent_id, order_num, visible, status, perms
FROM sys_menu 
WHERE parent_id = 2000
ORDER BY order_num;

SELECT '管理员博客权限统计:' AS info;
SELECT COUNT(*) AS count
FROM sys_role_menu 
WHERE role_id = 1 AND menu_id >= 2000;