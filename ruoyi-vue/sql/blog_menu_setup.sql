-- ========== 博客系统菜单权限配置 ==========
-- 创建时间：2025-11-07
-- 描述：为博客系统创建完整的菜单权限配置

USE newblog;

-- 先删除已存在的博客菜单记录
DELETE FROM sys_role_menu WHERE menu_id >= 2000;
DELETE FROM sys_menu WHERE menu_id >= 2000;

-- ========== 博客管理主菜单 ==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 10, 'blog', NULL, '', '', 1, 0, 'M', '1', '1', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录');

-- ========== 博客管理子菜单 ==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2001, '文章管理', 2000, 1, 'article', 'admin/blog/article/article/index', '', '', 1, 0, 'C', '1', '1', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '文章管理菜单'),
(2002, '分类管理', 2000, 2, 'category', 'admin/blog/category/category/index', '', '', 1, 0, 'C', '1', '1', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '分类管理菜单'),
(2003, '标签管理', 2000, 3, 'tag', 'blog/tag/index', '', '', 1, 0, 'C', '1', '1', 'blog:tag:list', 'tag', 'admin', NOW(), '', NULL, '标签管理菜单'),
(2004, '评论管理', 2000, 4, 'comment', 'blog/comment/index', '', '', 1, 0, 'C', '1', '1', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '评论管理菜单'),
(2005, '博客设置', 2000, 5, 'setting', 'blog/setting/index', '', '', 1, 0, 'C', '1', '1', 'blog:setting:list', 'setting', 'admin', NOW(), '', NULL, '博客设置菜单'),
(2006, '友链管理', 2000, 6, 'friendLink', 'blog/friendLink/index', '', '', 1, 0, 'C', '1', '1', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友链管理菜单');

-- ========== 按钮权限配置 ==========

-- 文章管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20010, '文章查询', 2001, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:article:query', '#', 'admin', NOW(), '', NULL, ''),
(20011, '文章新增', 2001, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:article:add', '#', 'admin', NOW(), '', NULL, ''),
(20012, '文章修改', 2001, 3, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:article:edit', '#', 'admin', NOW(), '', NULL, ''),
(20013, '文章删除', 2001, 4, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:article:remove', '#', 'admin', NOW(), '', NULL, '');

-- 分类管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20020, '分类查询', 2002, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:category:query', '#', 'admin', NOW(), '', NULL, ''),
(20021, '分类新增', 2002, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:category:add', '#', 'admin', NOW(), '', NULL, ''),
(20022, '分类修改', 2002, 3, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:category:edit', '#', 'admin', NOW(), '', NULL, ''),
(20023, '分类删除', 2002, 4, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:category:remove', '#', 'admin', NOW(), '', NULL, '');

-- 标签管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20030, '标签查询', 2003, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:tag:query', '#', 'admin', NOW(), '', NULL, ''),
(20031, '标签新增', 2003, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:tag:add', '#', 'admin', NOW(), '', NULL, ''),
(20032, '标签修改', 2003, 3, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:tag:edit', '#', 'admin', NOW(), '', NULL, ''),
(20033, '标签删除', 2003, 4, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:tag:remove', '#', 'admin', NOW(), '', NULL, ''),
(20034, '标签导出', 2003, 5, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:tag:export', '#', 'admin', NOW(), '', NULL, '');

-- 评论管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20040, '评论查询', 2004, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:comment:query', '#', 'admin', NOW(), '', NULL, ''),
(20041, '评论审核', 2004, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:comment:approve', '#', 'admin', NOW(), '', NULL, ''),
(20042, '评论删除', 2004, 3, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:comment:remove', '#', 'admin', NOW(), '', NULL, '');

-- 博客设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20050, '设置查询', 2005, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''),
(20051, '设置修改', 2005, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, '');

-- 友链管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20060, '友链查询', 2006, 1, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''),
(20061, '友链新增', 2006, 2, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''),
(20062, '友链修改', 2006, 3, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''),
(20063, '友链删除', 2006, 4, '', '', '', '', 1, 0, 'F', '1', '1', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, '');

-- ========== 为管理员角色分配博客管理菜单权限 ==========
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2000), (1, 2001), (1, 2002), (1, 2003), (1, 2004), (1, 2005), (1, 2006),
(1, 20010), (1, 20011), (1, 20012), (1, 20013),
(1, 20020), (1, 20021), (1, 20022), (1, 20023),
(1, 20030), (1, 20031), (1, 20032), (1, 20033), (1, 20034),
(1, 20040), (1, 20041), (1, 20042),
(1, 20050), (1, 20051),
(1, 20060), (1, 20061), (1, 20062), (1, 20063);

-- ========== 验证脚本 ==========
SELECT '博客菜单配置完成！' AS result;
SELECT COUNT(*) AS '博客菜单数量' FROM sys_menu WHERE menu_id >= 2000;
SELECT COUNT(*) AS '博客权限数量' FROM sys_role_menu WHERE menu_id >= 2000;
SELECT '管理员博客权限数量:' AS info, COUNT(*) AS count FROM sys_role_menu WHERE role_id = 1 AND menu_id >= 2000;