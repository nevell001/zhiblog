-- 菜单系统验证脚本
-- 用于验证菜单创建和权限分配是否正确

USE newblog;

-- ========================================
-- 1. 验证菜单结构完整性
-- ========================================
SELECT '=== 菜单结构验证 ===' AS verification_type;

-- 主菜单验证
SELECT 
    '主菜单' as menu_type,
    COUNT(*) as total_count,
    GROUP_CONCAT(menu_name ORDER BY order_num) as menu_list
FROM sys_menu 
WHERE parent_id = 0 AND menu_type = 'M';

-- 各模块子菜单验证
SELECT 
    CASE parent_id 
        WHEN 1000 THEN '系统管理'
        WHEN 2000 THEN '博客管理'
        WHEN 3000 THEN '数据统计'
        WHEN 4000 THEN '系统监控'
        ELSE '其他'
    END as module_name,
    COUNT(*) as submenu_count,
    GROUP_CONCAT(menu_name ORDER BY order_num) as submenu_list
FROM sys_menu 
WHERE parent_id IN (1000, 2000, 3000, 4000) AND menu_type = 'C'
GROUP BY parent_id
ORDER BY parent_id;

-- ========================================
-- 2. 验证权限配置
-- ========================================
SELECT '=== 权限配置验证 ===' AS verification_type;

-- 按钮权限统计
SELECT 
    CASE m.parent_id 
        WHEN 1001 THEN '用户管理'
        WHEN 1002 THEN '角色管理'
        WHEN 1003 THEN '菜单管理'
        WHEN 1004 THEN '部门管理'
        WHEN 1005 THEN '岗位管理'
        WHEN 1006 THEN '字典管理'
        WHEN 1007 THEN '参数设置'
        WHEN 1008 THEN '通知公告'
        WHEN 10091 THEN '操作日志'
        WHEN 10092 THEN '登录日志'
        WHEN 2101 THEN '文章管理'
        WHEN 2102 THEN '分类管理'
        WHEN 2103 THEN '标签管理'
        WHEN 2104 THEN '评论管理'
        WHEN 2105 THEN '博客设置'
        WHEN 2106 THEN '友链管理'
        WHEN 3001 THEN '访问统计'
        WHEN 3002 THEN '文章统计'
        WHEN 3003 THEN '用户统计'
        WHEN 3004 THEN '评论统计'
        WHEN 3005 THEN '流量分析'
        WHEN 4001 THEN '在线用户'
        WHEN 4002 THEN '定时任务'
        WHEN 4003 THEN '数据监控'
        WHEN 4004 THEN '服务监控'
        WHEN 4005 THEN '缓存监控'
        ELSE '其他模块'
    END as module_name,
    COUNT(*) as button_count,
    GROUP_CONCAT(SUBSTRING(perms FROM POSITION(':' IN perms) + 2) ORDER BY order_num) as button_list
FROM sys_menu m
WHERE m.menu_type = 'F' AND m.parent_id IN (
    SELECT menu_id FROM sys_menu WHERE menu_type = 'C'
)
GROUP BY m.parent_id
ORDER BY m.parent_id;

-- ========================================
-- 3. 验证角色权限分配
-- ========================================
SELECT '=== 角色权限分配验证 ===' AS verification_type;

-- 管理员角色权限统计
SELECT 
    r.role_name,
    COUNT(rm.menu_id) as assigned_permissions,
    GROUP_CONCAT(DISTINCT CASE 
        WHEN m.parent_id = 0 THEN m.menu_name
        WHEN m.menu_type = 'M' THEN m.menu_name
        ELSE NULL
    END ORDER BY m.menu_id) as main_modules
FROM sys_role r
LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE r.role_id = 1
GROUP BY r.role_id, r.role_name;

-- ========================================
-- 4. 菜单层级结构展示
-- ========================================
SELECT '=== 完整菜单层级结构 ===' AS verification_type;

-- 递归查询显示菜单层级
SELECT 
    CONCAT(
        CASE 
            WHEN parent_id = 0 THEN ''
            WHEN parent_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 0) THEN '├── '
            ELSE '│   ├── '
        END,
        CASE 
            WHEN menu_type = 'M' THEN CONCAT('📁 ', menu_name)
            WHEN menu_type = 'C' THEN CONCAT('📄 ', menu_name)
            WHEN menu_type = 'F' THEN CONCAT('🔘 ', menu_name)
            ELSE menu_name
        END
    ) as menu_tree,
    menu_id,
    parent_id,
    menu_type,
    perms,
    path
FROM (
    SELECT * FROM sys_menu 
    WHERE menu_id IN (
        1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
        2000, 2101, 2102, 2103, 2104, 2105, 2106,
        3000, 3001, 3002, 3003, 3004, 3005,
        4000, 4001, 4002, 4003, 4004, 4005
    )
    ORDER BY 
        CASE WHEN parent_id = 0 THEN menu_id ELSE parent_id END,
        order_num
) m;

-- ========================================
-- 5. 权限前缀验证
-- ========================================
SELECT '=== 权限前缀验证 ===' AS verification_type;

SELECT 
    SUBSTRING_INDEX(perms, ':', 1) as permission_prefix,
    COUNT(*) as count,
    GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(perms, ':', 2)) as modules
FROM sys_menu 
WHERE perms IS NOT NULL AND perms != ''
GROUP BY SUBSTRING_INDEX(perms, ':', 1)
ORDER BY permission_prefix;

-- ========================================
-- 6. 数据完整性检查
-- ========================================
SELECT '=== 数据完整性检查 ===' AS verification_type;

-- 检查必填字段完整性
SELECT 
    'menu_name' as field_name,
    COUNT(*) as total_records,
    COUNT(CASE WHEN menu_name IS NULL OR menu_name = '' THEN 1 END) as null_count
FROM sys_menu 
WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
)

UNION ALL

SELECT 
    'path' as field_name,
    COUNT(*) as total_records,
    COUNT(CASE WHEN menu_type = 'C' AND (path IS NULL OR path = '') THEN 1 END) as null_count
FROM sys_menu 
WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
) AND menu_type = 'C'

UNION ALL

SELECT 
    'perms' as field_name,
    COUNT(*) as total_records,
    COUNT(CASE WHEN perms IS NULL OR perms = '' THEN 1 END) as null_count
FROM sys_menu 
WHERE menu_id IN (
    1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
    2000, 2101, 2102, 2103, 2104, 2105, 2106,
    3000, 3001, 3002, 3003, 3004, 3005,
    4000, 4001, 4002, 4003, 4004, 4005
) AND menu_type IN ('C', 'F');

-- 验证完成
SELECT '菜单系统验证完成！' AS verification_result;