-- ========================================
-- 系统状态验证脚本
-- ========================================
-- 用途：验证菜单配置、权限分配和系统状态
-- 执行：mysql -u root -p newblog < ruoyi-vue/sql/verify_system.sql
-- ========================================

USE newblog;

SELECT '========================================'  AS '';
SELECT '系统状态验证报告' AS '报告类型';
SELECT '========================================'  AS '';

-- 1. 菜单统计
SELECT '========================================'  AS '';
SELECT '1. 菜单配置统计' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    '总菜单数' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
UNION ALL
SELECT 
    '一级菜单（目录）' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
WHERE parent_id = 0 AND menu_type = 'M'
UNION ALL
SELECT 
    '二级菜单（页面）' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
WHERE parent_id != 0 AND menu_type = 'C'
UNION ALL
SELECT 
    '三级菜单（按钮）' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
WHERE menu_type = 'F'
UNION ALL
SELECT 
    '可见菜单' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
WHERE visible = '0'
UNION ALL
SELECT 
    '启用菜单' AS '类别',
    COUNT(*) AS '数量'
FROM sys_menu
WHERE status = '0';

-- 2. 博客管理模块检查
SELECT '========================================'  AS '';
SELECT '2. 博客管理模块（2000-2999）' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    menu_id AS 'ID',
    menu_name AS '菜单名称',
    CASE menu_type
        WHEN 'M' THEN '目录'
        WHEN 'C' THEN '菜单'
        WHEN 'F' THEN '按钮'
    END AS '类型',
    CASE visible
        WHEN '0' THEN '显示'
        WHEN '1' THEN '隐藏'
    END AS '可见性',
    CASE status
        WHEN '0' THEN '正常'
        WHEN '1' THEN '停用'
    END AS '状态',
    perms AS '权限标识'
FROM sys_menu
WHERE menu_id BETWEEN 2000 AND 2999
ORDER BY menu_id;

-- 3. 数据统计模块检查
SELECT '========================================'  AS '';
SELECT '3. 数据统计模块（3000-3999）' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    menu_id AS 'ID',
    menu_name AS '菜单名称',
    CASE menu_type
        WHEN 'M' THEN '目录'
        WHEN 'C' THEN '菜单'
        WHEN 'F' THEN '按钮'
    END AS '类型',
    CASE visible
        WHEN '0' THEN '显示'
        WHEN '1' THEN '隐藏'
    END AS '可见性',
    CASE status
        WHEN '0' THEN '正常'
        WHEN '1' THEN '停用'
    END AS '状态',
    perms AS '权限标识'
FROM sys_menu
WHERE menu_id BETWEEN 3000 AND 3999
ORDER BY menu_id;

-- 4. 管理员权限检查
SELECT '========================================'  AS '';
SELECT '4. 管理员角色权限分配' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    '管理员拥有的菜单权限数' AS '类别',
    COUNT(*) AS '数量'
FROM sys_role_menu
WHERE role_id = 1;

-- 5. 问题检查
SELECT '========================================'  AS '';
SELECT '5. 潜在问题检查' AS '检查项';
SELECT '========================================'  AS '';

-- 检查隐藏的菜单
SELECT 
    '隐藏的菜单' AS '问题类型',
    COUNT(*) AS '数量',
    GROUP_CONCAT(menu_name SEPARATOR ', ') AS '详情'
FROM sys_menu
WHERE visible = '1'
HAVING COUNT(*) > 0
UNION ALL
-- 检查停用的菜单
SELECT 
    '停用的菜单' AS '问题类型',
    COUNT(*) AS '数量',
    GROUP_CONCAT(menu_name SEPARATOR ', ') AS '详情'
FROM sys_menu
WHERE status = '1'
HAVING COUNT(*) > 0
UNION ALL
-- 检查没有权限标识的菜单（按钮类型必须有）
SELECT 
    '缺少权限标识的按钮' AS '问题类型',
    COUNT(*) AS '数量',
    GROUP_CONCAT(menu_name SEPARATOR ', ') AS '详情'
FROM sys_menu
WHERE menu_type = 'F' AND (perms IS NULL OR perms = '')
HAVING COUNT(*) > 0;

-- 6. 路由配置检查
SELECT '========================================'  AS '';
SELECT '6. 路由配置检查' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    menu_name AS '菜单名称',
    path AS '路由路径',
    component AS '组件路径',
    CASE is_frame
        WHEN 1 THEN '否'
        WHEN 0 THEN '是'
    END AS '外链'
FROM sys_menu
WHERE menu_type = 'C' 
  AND menu_id BETWEEN 2000 AND 3999
ORDER BY menu_id;

-- 7. 博客数据统计
SELECT '========================================'  AS '';
SELECT '7. 博客内容统计' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    '文章总数' AS '类别',
    COUNT(*) AS '数量'
FROM blog_article
UNION ALL
SELECT 
    '已发布文章' AS '类别',
    COUNT(*) AS '数量'
FROM blog_article
WHERE status = 1
UNION ALL
SELECT 
    '分类总数' AS '类别',
    COUNT(*) AS '数量'
FROM blog_category
UNION ALL
SELECT 
    '标签总数' AS '类别',
    COUNT(*) AS '数量'
FROM blog_tag
UNION ALL
SELECT 
    '评论总数' AS '类别',
    COUNT(*) AS '数量'
FROM blog_comment
WHERE EXISTS (SELECT 1 FROM blog_comment LIMIT 1)
UNION ALL
SELECT 
    '友链总数' AS '类别',
    COUNT(*) AS '数量'
FROM blog_friend_link
WHERE EXISTS (SELECT 1 FROM blog_friend_link LIMIT 1);

-- 8. 系统建议
SELECT '========================================'  AS '';
SELECT '8. 系统建议' AS '检查项';
SELECT '========================================'  AS '';

SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM sys_menu WHERE visible = '1' OR status = '1') > 0 
        THEN '⚠️  发现隐藏或停用的菜单，请检查是否需要启用'
        ELSE '✅ 所有菜单均已启用且可见'
    END AS '建议1'
UNION ALL
SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM sys_role_menu WHERE role_id = 1) < 40 
        THEN '⚠️  管理员权限数量较少，可能需要重新分配权限'
        ELSE '✅ 管理员权限配置正常'
    END AS '建议2'
UNION ALL
SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM blog_article WHERE status = 1) = 0 
        THEN '💡 建议：创建一些文章以测试博客功能'
        ELSE '✅ 已有发布的文章'
    END AS '建议3';

SELECT '========================================'  AS '';
SELECT '验证完成！' AS '状态';
SELECT '========================================'  AS '';

-- 显示下一步操作建议
SELECT '下一步操作建议：' AS '';
SELECT '1. 如果菜单不显示，请清除浏览器缓存并重新登录' AS '';
SELECT '2. 访问 http://localhost:80/admin 登录后台管理' AS '';
SELECT '3. 检查左侧菜单栏是否显示"博客管理"和"数据统计"' AS '';
SELECT '4. 如有问题，请查看 FRONTEND_FIXES.md 文档' AS '';