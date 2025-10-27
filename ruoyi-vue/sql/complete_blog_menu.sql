-- 完整的博客管理菜单配置
-- 创建时间：2025-10-27

-- 1. 检查当前博客管理菜单配置
SELECT '当前博客管理菜单配置：' AS info;
SELECT menu_id, menu_name, parent_id, path, component, visible 
FROM sys_menu 
WHERE menu_name = '博客管理' OR path = 'blog' OR component LIKE '%blog%'
ORDER BY menu_id;

-- 2. 检查博客管理子菜单配置
SELECT '博客管理子菜单配置：' AS info;
SELECT menu_id, menu_name, parent_id, path, component 
FROM sys_menu 
WHERE parent_id = 2000
ORDER BY order_num;

-- 3. 如果博客管理菜单不存在，创建它
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2000, '博客管理', 0, 10, 'blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客管理目录'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2000);

-- 4. 创建博客管理子菜单（如果不存在）
-- 文章管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2001, '文章管理', 2000, 1, 'article', 'blog/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '文章管理菜单'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2001);

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

-- 5. 为管理员角色分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE parent_id = 2000
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 6. 创建博客管理按钮权限（如果不存在）
-- 文章管理按钮权限
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

-- 分类管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20020, '分类查询', 2002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20020);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20021, '分类新增', 2002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:add', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20021);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20022, '分类修改', 2002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20022);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20023, '分类删除', 2002, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20023);

-- 标签管理按钮权限（已存在，确保完整）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20030, '标签查询', 2003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20030);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20031, '标签新增', 2003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:add', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20031);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20032, '标签修改', 2003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20032);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20033, '标签删除', 2003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20033);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20034, '标签导出', 2003, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:tag:export', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20034);

-- 评论管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20040, '评论查询', 2004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20040);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20041, '评论审核', 2004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:approve', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20041);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20042, '评论删除', 2004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20042);

-- 博客设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20050, '设置查询', 2005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20050);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20051, '设置修改', 2005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20051);

-- 友链管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20060, '友链查询', 2006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20060);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20061, '友链新增', 2006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20061);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20062, '友链修改', 2006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20062);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 20063, '友链删除', 2006, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20063);

-- 7. 为管理员角色分配所有博客管理按钮权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu 
WHERE menu_id IN (
  20010, 20011, 20012, 20013,  -- 文章管理按钮
  20020, 20021, 20022, 20023,  -- 分类管理按钮
  20030, 20031, 20032, 20033, 20034,  -- 标签管理按钮
  20040, 20041, 20042,  -- 评论管理按钮
  20050, 20051,  -- 博客设置按钮
  20060, 20061, 20062, 20063  -- 友链管理按钮
)
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 8. 验证最终配置
SELECT '最终验证结果：' AS result;
SELECT COUNT(*) as '博客管理菜单数量' FROM sys_menu WHERE menu_name = '博客管理';
SELECT COUNT(*) as '博客管理子菜单数量' FROM sys_menu WHERE parent_id = 2000;

SELECT '博客管理菜单详情：' AS info;
SELECT menu_id, menu_name, parent_id, path, component 
FROM sys_menu 
WHERE menu_name = '博客管理' OR parent_id = 2000
ORDER BY menu_id, order_num;

SELECT '博客管理按钮权限详情：' AS info;
SELECT menu_id, menu_name, parent_id, perms 
FROM sys_menu 
WHERE parent_id IN (2001, 2002, 2003, 2004, 2005, 2006)
ORDER BY parent_id, order_num;