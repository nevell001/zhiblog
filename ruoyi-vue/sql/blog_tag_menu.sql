-- 博客标签管理菜单权限配置
-- 创建时间：2025-10-24

-- 1. 博客管理主菜单（如果不存在）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2000, '博客管理', 0, 10, 'blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2000);

-- 2. 标签管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2003, '标签管理', 2000, 3, 'tag', 'system/tag/index', '', '', 1, 0, 'C', '0', '0', 'system:tag:list', 'tag', 'admin', NOW(), '', NULL, '标签管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2003);

-- 3. 标签管理按钮权限
-- 标签查询
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20030, '标签查询', 2003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20030);

-- 标签新增
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20031, '标签新增', 2003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:add', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20031);

-- 标签修改
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20032, '标签修改', 2003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20032);

-- 标签删除
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20033, '标签删除', 2003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20033);

-- 标签导出
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20034, '标签导出', 2003, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:export', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20034);

-- 4. 更新博客管理菜单的子菜单顺序（如果已存在其他博客相关菜单）
UPDATE sys_menu SET order_num = 1 WHERE menu_id = 2001 AND menu_name = '文章管理';
UPDATE sys_menu SET order_num = 2 WHERE menu_id = 2002 AND menu_name = '分类管理';
UPDATE sys_menu SET order_num = 3 WHERE menu_id = 2003 AND menu_name = '标签管理';
UPDATE sys_menu SET order_num = 4 WHERE menu_id = 2004 AND menu_name = '评论管理';

-- 5. 为管理员角色分配标签管理权限（如果角色ID为1）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2003
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2003);

-- 为管理员角色分配标签管理按钮权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id
FROM sys_menu 
WHERE menu_id IN (20030, 20031, 20032, 20033, 20034)
AND NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = sys_menu.menu_id);

-- 查询验证
SELECT '菜单权限配置完成' as result;
SELECT menu_id, menu_name, parent_id, order_num, perms FROM sys_menu WHERE menu_id IN (2000, 2003, 20030, 20031, 20032, 20033, 20034);