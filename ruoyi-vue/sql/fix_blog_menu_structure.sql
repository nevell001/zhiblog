-- 修复博客管理菜单结构
-- 创建时间：2025-10-27

-- 1. 删除错误的博客管理按钮权限配置
DELETE FROM sys_menu WHERE menu_id IN (2002, 2003, 2004, 2005, 2006);

-- 2. 删除错误的角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id IN (2002, 2003, 2004, 2005, 2006);

-- 3. 创建正确的博客管理子菜单
-- 分类管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2002, '分类管理', 2000, 2, 'category', 'blog/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '分类管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2002);

-- 标签管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2003, '标签管理', 2000, 3, 'tag', 'system/tag/index', '', '', 1, 0, 'C', '0', '0', 'system:tag:list', 'tag', 'admin', NOW(), '', NULL, '标签管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2003);

-- 评论管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2004, '评论管理', 2000, 4, 'comment', 'blog/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '评论管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2004);

-- 博客设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2005, '博客设置', 2000, 5, 'setting', 'blog/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'setting', 'admin', NOW(), '', NULL, '博客设置菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2005);

-- 友链管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2006, '友链管理', 2000, 6, 'friendLink', 'blog/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友链管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2006);

-- 4. 为管理员角色分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE parent_id = 2000
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 5. 创建正确的按钮权限（使用新的菜单ID范围）
-- 文章管理按钮权限（使用20010-20019）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20010, '文章查询', 2001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20010);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20011, '文章新增', 2001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20011);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20012, '文章修改', 2001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20012);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20013, '文章删除', 2001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20013);

-- 6. 为管理员角色分配按钮权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu 
WHERE menu_id IN (20010, 20011, 20012, 20013)
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 7. 验证修复结果
SELECT '修复后的博客管理菜单结构：' AS info;
SELECT menu_id, menu_name, parent_id, path, component 
FROM sys_menu 
WHERE menu_name = '博客管理' OR parent_id = 2000
ORDER BY menu_id, order_num;

SELECT '修复后的文章管理按钮权限：' AS info;
SELECT menu_id, menu_name, parent_id, perms 
FROM sys_menu 
WHERE parent_id = 2001
ORDER BY order_num;