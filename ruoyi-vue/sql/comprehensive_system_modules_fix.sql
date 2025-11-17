-- ========================================
-- 综合修复系统管理、系统监控和系统工具三个核心模块
-- 创建时间：2025-11-13
-- 描述：全面修复数据库菜单数据，确保与前端路由配置一致
-- ========================================

USE newblog;

-- ========================================
-- 1. 修复系统管理模块菜单
-- ========================================

-- 更新系统管理模块的路径
UPDATE sys_menu SET path = 'system' WHERE menu_id = 1;

-- 更新用户管理组件路径
UPDATE sys_menu SET component = 'admin/system/user/user/index' WHERE menu_id = 100;

-- 更新角色管理组件路径
UPDATE sys_menu SET component = 'admin/system/role/role/index' WHERE menu_id = 101;

-- 更新菜单管理组件路径
UPDATE sys_menu SET component = 'admin/system/menu/menu/index' WHERE menu_id = 102;

-- 更新部门管理组件路径
UPDATE sys_menu SET component = 'admin/system/dept/dept/index' WHERE menu_id = 103;

-- 更新岗位管理组件路径
UPDATE sys_menu SET component = 'admin/system/post/post/index' WHERE menu_id = 104;

-- 更新字典管理组件路径
UPDATE sys_menu SET component = 'admin/system/dict/dict/index' WHERE menu_id = 105;

-- 更新参数设置组件路径
UPDATE sys_menu SET component = 'admin/system/config/config/index' WHERE menu_id = 106;

-- 更新通知公告组件路径
UPDATE sys_menu SET component = 'admin/system/notice/notice/index' WHERE menu_id = 107;

-- ========================================
-- 2. 修复系统监控模块菜单
-- ========================================

-- 更新系统监控模块的路径
UPDATE sys_menu SET path = 'monitor' WHERE menu_id = 2;

-- 更新在线用户组件路径
UPDATE sys_menu SET component = 'admin/monitor/online/index' WHERE menu_id = 109;

-- 更新定时任务组件路径
UPDATE sys_menu SET component = 'admin/monitor/job/index' WHERE menu_id = 110;

-- 更新数据监控组件路径
UPDATE sys_menu SET component = 'admin/monitor/druid/index' WHERE menu_id = 111;

-- 更新服务监控组件路径
UPDATE sys_menu SET component = 'admin/monitor/server/index' WHERE menu_id = 112;

-- 更新缓存监控组件路径
UPDATE sys_menu SET component = 'admin/monitor/cache/index' WHERE menu_id = 113;

-- 更新缓存列表组件路径
UPDATE sys_menu SET component = 'admin/monitor/cache/list' WHERE menu_id = 114;

-- 更新操作日志组件路径
UPDATE sys_menu SET component = 'admin/monitor/operlog/index' WHERE menu_id = 500;

-- 更新登录日志组件路径
UPDATE sys_menu SET component = 'admin/monitor/logininfor/index' WHERE menu_id = 501;

-- ========================================
-- 3. 修复系统工具模块菜单
-- ========================================

-- 更新系统工具模块的路径
UPDATE sys_menu SET path = 'tool' WHERE menu_id = 3;

-- 更新表单构建组件路径
UPDATE sys_menu SET component = 'admin/tool/build/index' WHERE menu_id = 115;

-- 更新代码生成组件路径
UPDATE sys_menu SET component = 'admin/tool/gen/index' WHERE menu_id = 116;

-- 更新系统接口组件路径
UPDATE sys_menu SET component = 'admin/tool/swagger/index' WHERE menu_id = 117;

-- ========================================
-- 4. 确保管理员拥有所有系统模块权限
-- ========================================

-- 为管理员角色分配系统管理模块权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE parent_id = 1 OR menu_id = 1;

-- 为管理员角色分配系统监控模块权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE parent_id = 2 OR menu_id = 2;

-- 为管理员角色分配系统工具模块权限
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE parent_id = 3 OR menu_id = 3;

-- ========================================
-- 5. 验证修复结果
-- ========================================

SELECT '========================================' AS '';
SELECT '系统模块菜单修复完成！' AS '结果';
SELECT '========================================' AS '';

SELECT '系统管理模块:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 1
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统监控模块:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 2
ORDER BY order_num;

SELECT '' AS '';
SELECT '系统工具模块:' AS '类别';
SELECT menu_id, menu_name, path, component, perms, icon
FROM sys_menu
WHERE parent_id = 3
ORDER BY order_num;

SELECT '' AS '';
SELECT '管理员权限统计:' AS '类别';
SELECT 
    '系统管理' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 1 OR m.menu_id = 1)
UNION ALL
SELECT 
    '系统监控' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 2 OR m.menu_id = 2)
UNION ALL
SELECT 
    '系统工具' AS module,
    COUNT(*) AS menu_count
FROM sys_role_menu rm
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1 AND (m.parent_id = 3 OR m.menu_id = 3);

SELECT '========================================' AS '';
SELECT '请刷新后台页面查看修复后的菜单！' AS '提示';
SELECT '========================================' AS '';