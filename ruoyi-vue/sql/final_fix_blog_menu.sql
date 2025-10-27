-- 最终修复博客管理菜单问题
-- 执行时间：2025-10-27

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

-- 5. 为管理员角色分配博客管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE parent_id = 2000
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 6. 删除前端路由配置中的博客管理路由（让系统完全使用后端菜单）
-- 这个需要在代码中修改，这里只做标记

-- 7. 验证最终配置
SELECT '最终验证结果：' AS result;
SELECT COUNT(*) as '博客管理菜单数量' FROM sys_menu WHERE menu_name = '博客管理';
SELECT COUNT(*) as '博客管理子菜单数量' FROM sys_menu WHERE parent_id = 2000;

SELECT '博客管理菜单详情：' AS info;
SELECT menu_id, menu_name, parent_id, path, component 
FROM sys_menu 
WHERE menu_name = '博客管理' OR parent_id = 2000
ORDER BY menu_id, order_num;