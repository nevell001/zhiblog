-- 创建博客管理子菜单
USE newblog;

-- 更新主菜单路径
UPDATE sys_menu SET path = 'admin/blog' WHERE menu_id = 2000;

-- 文章管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2101, '文章管理', 2000, 1, 'article', 'admin/blog/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '文章管理菜单');

-- 分类管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2102, '分类管理', 2000, 2, 'category', 'admin/blog/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '分类管理菜单');

-- 标签管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2103, '标签管理', 2000, 3, 'tag', 'admin/blog/tag/index', '', '', 1, 0, 'C', '0', '0', 'blog:tag:list', 'tag', 'admin', NOW(), '', NULL, '标签管理菜单');

-- 评论管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2104, '评论管理', 2000, 4, 'comment', 'admin/blog/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '评论管理菜单');

-- 博客设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2105, '博客设置', 2000, 5, 'setting', 'admin/blog/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'setting', 'admin', NOW(), '', NULL, '博客设置菜单');

-- 友链管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2106, '友链管理', 2000, 6, 'friendLink', 'admin/blog/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友链管理菜单');

-- 为管理员角色分配博客管理子菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2101), (1, 2102), (1, 2103), (1, 2104), (1, 2105), (1, 2106);

-- 创建按钮权限
-- 文章管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21010, '文章查询', 2101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', NOW(), '', NULL, ''),
(21011, '文章新增', 2101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', NOW(), '', NULL, ''),
(21012, '文章修改', 2101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', NOW(), '', NULL, ''),
(21013, '文章删除', 2101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', NOW(), '', NULL, '');

-- 分类管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21020, '分类查询', 2102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:query', '#', 'admin', NOW(), '', NULL, ''),
(21021, '分类新增', 2102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:add', '#', 'admin', NOW(), '', NULL, ''),
(21022, '分类修改', 2102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:edit', '#', 'admin', NOW(), '', NULL, ''),
(21023, '分类删除', 2102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:remove', '#', 'admin', NOW(), '', NULL, '');

-- 标签管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21030, '标签查询', 2103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:query', '#', 'admin', NOW(), '', NULL, ''),
(21031, '标签新增', 2103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:add', '#', 'admin', NOW(), '', NULL, ''),
(21032, '标签修改', 2103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:edit', '#', 'admin', NOW(), '', NULL, ''),
(21033, '标签删除', 2103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:remove', '#', 'admin', NOW(), '', NULL, ''),
(21034, '标签导出', 2103, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:export', '#', 'admin', NOW(), '', NULL, '');

-- 评论管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21040, '评论查询', 2104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:query', '#', 'admin', NOW(), '', NULL, ''),
(21041, '评论审核', 2104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:approve', '#', 'admin', NOW(), '', NULL, ''),
(21042, '评论删除', 2104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:remove', '#', 'admin', NOW(), '', NULL, '');

-- 博客设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21050, '设置查询', 2105, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''),
(21051, '设置修改', 2105, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, '');

-- 友链管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(21060, '友链查询', 2106, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''),
(21061, '友链新增', 2106, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''),
(21062, '友链修改', 2106, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''),
(21063, '友链删除', 2106, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配所有博客管理按钮权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 21010), (1, 21011), (1, 21012), (1, 21013),
(1, 21020), (1, 21021), (1, 21022), (1, 21023),
(1, 21030), (1, 21031), (1, 21032), (1, 21033), (1, 21034),
(1, 21040), (1, 21041), (1, 21042),
(1, 21050), (1, 21051),
(1, 21060), (1, 21061), (1, 21062), (1, 21063);

-- 完成
SELECT '博客管理子菜单创建完成！' AS result;