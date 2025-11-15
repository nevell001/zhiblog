-- 博客标签管理菜单权限配置脚本
-- 执行前请确保已连接到数据库：mysql -u root -p ry-vue

-- 删除可能存在的旧菜单（如果需要重新创建）
DELETE FROM sys_menu WHERE menu_id IN (2000, 2003, 20030, 20031, 20032, 20033, 20034);

-- 插入博客管理主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 10, 'blog', NULL, NULL, 0, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), 'admin', NOW(), '博客管理系统');

-- 插入标签管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '标签管理', 2000, 3, 'tag', 'system/tag/index', NULL, 0, 0, 'C', '0', '0', 'system:tag:list', 'tag', 'admin', NOW(), 'admin', NOW(), '博客标签管理');

-- 插入标签管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
(20030, '标签查询', 2003, 1, '', NULL, NULL, 0, 0, 'F', '0', '0', 'system:tag:query', '#', 'admin', NOW(), 'admin', NOW(), ''),
(20031, '标签新增', 2003, 2, '', NULL, NULL, 0, 0, 'F', '0', '0', 'system:tag:add', '#', 'admin', NOW(), 'admin', NOW(), ''),
(20032, '标签修改', 2003, 3, '', NULL, NULL, 0, 0, 'F', '0', '0', 'system:tag:edit', '#', 'admin', NOW(), 'admin', NOW(), ''),
(20033, '标签删除', 2003, 4, '', NULL, NULL, 0, 0, 'F', '0', '0', 'system:tag:remove', '#', 'admin', NOW(), 'admin', NOW(), ''),
(20034, '标签导出', 2003, 5, '', NULL, NULL, 0, 0, 'F', '0', '0', 'system:tag:export', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 为管理员角色分配标签管理权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id IN (2000, 2003, 20030, 20031, 20032, 20033, 20034);

-- 验证配置结果
SELECT '菜单配置完成' as result;
SELECT menu_id, menu_name, parent_id, perms FROM sys_menu WHERE menu_id IN (2000, 2003, 20030, 20031, 20032, 20033, 20034);

SELECT '角色权限分配完成' as result;
SELECT rm.role_id, r.role_name, rm.menu_id, m.menu_name, m.perms
FROM sys_role_menu rm
JOIN sys_role r ON rm.role_id = r.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.menu_id IN (2003, 20030, 20031, 20032, 20033, 20034);