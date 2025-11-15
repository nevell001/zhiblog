-- ========== 关于作者菜单权限配置 ==========
-- 创建时间：2025-11-15
-- 描述：为关于作者功能创建菜单权限配置

USE newblog;

-- 先删除已存在的关于作者菜单记录（如果存在）
DELETE FROM sys_role_menu WHERE menu_id IN (2007, 20070, 20071);
DELETE FROM sys_menu WHERE menu_id IN (2007, 20070, 20071);

-- ========== 关于作者菜单 ==========
-- 插入关于作者主菜单（放在博客管理下，parent_id=2000）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2007, '关于作者', 2000, 7, 'about', 'admin/system/about/index', '', '', 1, 0, 'C', '0', '0', 'blog:about:list', 'user', 'admin', NOW(), '', NULL, '关于作者管理菜单');

-- ========== 按钮权限配置 ==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20070, '关于查询', 2007, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:about:query', '#', 'admin', NOW(), '', NULL, ''),
(20071, '关于修改', 2007, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:about:edit', '#', 'admin', NOW(), '', NULL, '');

-- ========== 为管理员角色分配权限 ==========
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2007),
(1, 20070),
(1, 20071);

-- ========== 验证脚本 ==========
SELECT '关于作者菜单配置完成！' AS result;
SELECT menu_id, menu_name, parent_id, perms, visible, status FROM sys_menu WHERE menu_id IN (2007, 20070, 20071);

SELECT '角色权限分配完成' AS result;
SELECT rm.role_id, r.role_name, rm.menu_id, m.menu_name, m.perms
FROM sys_role_menu rm
JOIN sys_role r ON rm.role_id = r.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.menu_id IN (2007, 20070, 20071);