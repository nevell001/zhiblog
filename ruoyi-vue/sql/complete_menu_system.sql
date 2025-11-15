
-- ========================================
-- 完整的后台管理菜单体系
-- 创建时间：2025-11-15
-- 描述：建立结构清晰、层次分明的三级菜单架构
-- ========================================

USE newblog;

-- 清理现有菜单数据（谨慎使用）
DELETE FROM sys_role_menu WHERE menu_id >= 2000;
DELETE FROM sys_menu WHERE menu_id >= 2000;

-- ========================================
-- 一级菜单：博客管理（2000-2999）
-- ========================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '博客管理', 0, 5, 'blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '博客内容管理目录');

-- ========================================
-- 二级菜单：博客管理子模块
-- ========================================

-- 2001: 文章管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2001, '文章管理', 2000, 1, 'article', 'admin/blog/article/article/index', '', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', NOW(), '', NULL, '博客文章管理菜单');

-- 2002: 分类管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2002, '分类管理', 2000, 2, 'category', 'admin/blog/category/category/index', '', '', 1, 0, 'C', '0', '0', 'blog:category:list', 'list', 'admin', NOW(), '', NULL, '博客分类管理菜单');

-- 2003: 标签管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '标签管理', 2000, 3, 'tag', 'system/tag/index', '', '', 1, 0, 'C', '0', '0', 'blog:tag:list', 'tag', 'admin', NOW(), '', NULL, '博客标签管理菜单');

-- 2004: 评论管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2004, '评论管理', 2000, 4, 'comment', 'admin/blog/comment/comment/index', '', '', 1, 0, 'C', '0', '0', 'blog:comment:list', 'message', 'admin', NOW(), '', NULL, '博客评论管理菜单');

-- 2005: 友链管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2005, '友链管理', 2000, 5, 'friendLink', 'admin/blog/friendLink/friendLink/index', '', '', 1, 0, 'C', '0', '0', 'blog:friendLink:list', 'link', 'admin', NOW(), '', NULL, '友情链接管理菜单');

-- 2006: 博客设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '博客设置', 2000, 6, 'setting', 'admin/blog/setting/setting/index', '', '', 1, 0, 'C', '0', '0', 'blog:setting:list', 'setting', 'admin', NOW(), '', NULL, '博客系统设置菜单');

-- 2007: 关于作者
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2007, '关于作者', 2000, 7, 'about', 'admin/system/about/index', '', '', 1, 0, 'C', '0', '0', 'blog:about:list', 'user', 'admin', NOW(), '', NULL, '关于作者管理菜单');

-- ========================================
-- 三级菜单：按钮权限（20000-20999）
-- ========================================

-- 文章管理按钮权限（20010-20019）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20010, '文章查询', 2001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', NOW(), '', NULL, ''),
(20011, '文章新增', 2001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', NOW(), '', NULL, ''),
(20012, '文章修改', 2001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', NOW(), '', NULL, ''),
(20013, '文章删除', 2001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', NOW(), '', NULL, ''),
(20014, '文章导出', 2001, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:export', '#', 'admin', NOW(), '', NULL, ''),
(20015, '文章发布', 2001, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:publish', '#', 'admin', NOW(), '', NULL, '');

-- 分类管理按钮权限（20020-20029）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20020, '分类查询', 2002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:query', '#', 'admin', NOW(), '', NULL, ''),
(20021, '分类新增', 2002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:add', '#', 'admin', NOW(), '', NULL, ''),
(20022, '分类修改', 2002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:edit', '#', 'admin', NOW(), '', NULL, ''),
(20023, '分类删除', 2002, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:remove', '#', 'admin', NOW(), '', NULL, ''),
(20024, '分类导出', 2002, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:export', '#', 'admin', NOW(), '', NULL, '');

-- 标签管理按钮权限（20030-20039）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20030, '标签查询', 2003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:query', '#', 'admin', NOW(), '', NULL, ''),
(20031, '标签新增', 2003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:add', '#', 'admin', NOW(), '', NULL, ''),
(20032, '标签修改', 2003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:edit', '#', 'admin', NOW(), '', NULL, ''),
(20033, '标签删除', 2003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:remove', '#', 'admin', NOW(), '', NULL, ''),
(20034, '标签导出', 2003, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:export', '#', 'admin', NOW(), '', NULL, '');

-- 评论管理按钮权限（20040-20049）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20040, '评论查询', 2004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:query', '#', 'admin', NOW(), '', NULL, ''),
(20041, '评论审核', 2004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:approve', '#', 'admin', NOW(), '', NULL, ''),
(20042, '评论回复', 2004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:reply', '#', 'admin', NOW(), '', NULL, ''),
(20043, '评论删除', 2004, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:remove', '#', 'admin', NOW(), '', NULL, ''),
(20044, '评论导出', 2004, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:comment:export', '#', 'admin', NOW(), '', NULL, '');

-- 友链管理按钮权限（20050-20059）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20050, '友链查询', 2005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:query', '#', 'admin', NOW(), '', NULL, ''),
(20051, '友链新增', 2005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:add', '#', 'admin', NOW(), '', NULL, ''),
(20052, '友链修改', 2005, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:edit', '#', 'admin', NOW(), '', NULL, ''),
(20053, '友链删除', 2005, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:remove', '#', 'admin', NOW(), '', NULL, ''),
(20054, '友链导出', 2005, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:friendLink:export', '#', 'admin', NOW(), '', NULL, '');

-- 博客设置按钮权限（20060-20069）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20060, '设置查询', 2006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:query', '#', 'admin', NOW(), '', NULL, ''),
(20061, '设置修改', 2006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:edit', '#', 'admin', NOW(), '', NULL, ''),
(20062, '设置导出', 2006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:setting:export', '#', 'admin', NOW(), '', NULL, '');

-- 关于作者按钮权限（20070-20079）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(20070, '关于查询', 2007, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:about:query', '#', 'admin', NOW(), '', NULL, ''),
(20071, '关于修改', 2007, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'blog:about:edit', '#', 'admin', NOW(), '', NULL, '');

-- ========================================
-- 一级菜单：数据统计（3000-3999）
-- ========================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3000, '数据统计', 0, 6, 'statistics', NULL, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', NOW(), '', NULL, '数据统计分析目录');

-- ========================================
-- 二级菜单：数据统计子模块
-- ========================================

-- 3001: 访问统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3001, '访问统计', 3000, 1, 'visit', 'admin/statistics/visit/index', '', '', 1, 0, 'C', '0', '0', 'statistics:visit:list', 'eye', 'admin', NOW(), '', NULL, '网站访问统计菜单');

-- 3002: 文章统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3002, '文章统计', 3000, 2, 'article', 'admin/statistics/article/index', '', '', 1, 0, 'C', '0', '0', 'statistics:article:list', 'data-analysis', 'admin', NOW(), '', NULL, '文章数据统计菜单');

-- 3003: 用户统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3003, '用户统计', 3000, 3, 'user', 'admin/statistics/user/index', '', '', 1, 0, 'C', '0', '0', 'statistics:user:list', 'peoples', 'admin', NOW(), '', NULL, '用户行为统计菜单');

-- ========================================
-- 三级菜单：数据统计按钮权限（30000-30999）
-- ========================================

-- 访问统计按钮权限（30010-30019）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30010, '访问查询', 3001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:visit:query', '#', 'admin', NOW(), '', NULL, ''),
(30011, '访问导出', 3001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:visit:export', '#', 'admin', NOW(), '', NULL, '');

-- 文章统计按钮权限（30020-30029）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30020, '文章统计查询', 3002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:query', '#', 'admin', NOW(), '', NULL, ''),
(30021, '文章统计导出', 3002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:export', '#', 'admin', NOW(), '', NULL, '');

-- 用户统计按钮权限（30030-30039）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30030, '用户统计查询', 3003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:query', '#', 'admin', NOW(), '', NULL, ''),
(30031, '用户统计导出', 3003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:export', '#', 'admin', NOW(), '', NULL, '');

-- ========================================
-- 角色权限分配：为超级管理员分配所有菜单权限
-- ========================================

-- 分配博客管理模块权限（2000-2999, 20000-20999）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE (menu_id >= 2000 AND menu_id < 3000) OR (menu_id >= 20000 AND menu_id < 21000);

-- 分配数据统计模块权限（3000-3999, 30000-30999）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE (menu_id >= 3000 AND menu_id < 4000) OR (menu_id >= 30000 AND menu_id < 31000);

-- ========================================
-- 验证脚本
-- ========================================

SELECT '========================================' AS '';
SELECT '菜单体系创建完成！' AS '结果';
SELECT '========================================' AS '';

SELECT '一级菜单统计:' AS '类别';
SELECT menu_id, menu_name, path, icon, order_num
FROM sys_menu
WHERE parent_id = 0 AND menu_id >= 2000
ORDER BY order_num;

SELECT '' AS '';
SELECT '博客管理模块:' AS '类别';
SELECT menu_id, menu_name, menu_type, perms,
       CASE visible WHEN '0' THEN '显示' ELSE '隐藏' END AS visible_status,
       CASE status WHEN '0' THEN '正常' ELSE '停用' END AS menu_status
FROM sys_menu
WHERE parent_id = 2000
ORDER BY order_num;

SELECT '' AS '';
SELECT '数据统计模块:' AS '类别';
SELECT menu_id, menu_name, menu_type, perms,
       CASE visible WHEN '0' THEN '显示' ELSE '隐藏' END AS visible_status,
       CASE status WHEN '0' THEN '正常' ELSE '停用' END AS menu_status
FROM sys_menu
WHERE parent_id = 3000
ORDER BY order_num;

SELECT '' AS '';
SELECT '菜单数量统计:' AS '类别';
SELECT
    '博客管理菜单' AS module,
    COUNT(*) AS count
FROM sys_menu
WHERE menu_id >= 2000 AND menu_id < 3000
UNION ALL
SELECT
    '博客管理按钮' AS module,
    COUNT(*) AS count
FROM sys_menu
WHERE menu_id >= 20000 AND menu_id < 21000
UNION ALL
SELECT
    '数据统计菜单' AS module,
    COUNT(*) AS count
FROM sys_menu
WHERE menu_id >= 3000 AND menu_id < 4000
UNION ALL
SELECT
    '数据统计按钮' AS module,
    COUNT(*) AS count
FROM sys_menu
WHERE menu_id >= 30000 AND menu_id < 31000
UNION ALL
SELECT
    '总计' AS module,
    COUNT(*) AS count
FROM sys_menu
WHERE menu_id >= 2000;

SELECT '' AS '';
SELECT '管理员权限统计:' AS '类别';
SELECT COUNT(*) AS '已分配权限数量'
FROM sys_role_menu
WHERE role_id = 1 AND menu_id >= 2000;

SELECT '========================================' AS '';
SELECT '请刷新后台页面查看新菜单！' AS '提示';
SELECT '========================================' AS '';