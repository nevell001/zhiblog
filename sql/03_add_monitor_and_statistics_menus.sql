-- ===============================================================
-- 系统监控和数据统计菜单补充脚本
-- ===============================================================
-- 📅 创建时间：2026-01-13
-- 📝 描述：添加系统监控和数据统计相关的菜单和权限配置
-- ⚠️ 注意：在执行 02_add_system_menu.sql 之后执行此脚本
-- ===============================================================

USE newblog;

-- ========== 系统监控补充菜单 ==========

-- 1、Actuator监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5000, 'Actuator监控', 2, 10, 'actuator', 'monitor/actuator/index', '', '', 1, 0, 'C', '0', '0', 'monitor:actuator:list', 'monitor', 'admin', NOW(), '', NULL, 'Actuator监控菜单');

-- Actuator监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5001, 'Actuator查询', 5000, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:actuator:query', '#', 'admin', NOW(), '', NULL, '');

-- 2、Prometheus监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5002, 'Prometheus监控', 2, 11, 'prometheus', 'monitor/prometheus/index', '', '', 1, 0, 'C', '0', '0', 'monitor:prometheus:list', 'chart', 'admin', NOW(), '', NULL, 'Prometheus监控菜单');

-- Prometheus监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5003, 'Prometheus查询', 5002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:prometheus:query', '#', 'admin', NOW(), '', NULL, '');

-- 3、Grafana监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5004, 'Grafana监控', 2, 12, 'grafana', 'monitor/grafana/index', '', '', 1, 0, 'C', '0', '0', 'monitor:grafana:list', 'dashboard', 'admin', NOW(), '', NULL, 'Grafana监控菜单');

-- Grafana监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5005, 'Grafana查询', 5004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:grafana:query', '#', 'admin', NOW(), '', NULL, '');

-- 4、登录日志菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5006, '登录日志', 2, 7, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', NOW(), '', NULL, '登录日志菜单');

-- 登录日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5007, '登录日志查询', 5006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', NOW(), '', NULL, ''),
(5008, '登录日志删除', 5006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', NOW(), '', NULL, ''),
(5009, '登录日志导出', 5006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', NOW(), '', NULL, '');

-- 5、操作日志菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (5010, '操作日志', 2, 8, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', NOW(), '', NULL, '操作日志菜单');

-- 操作日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(5011, '操作日志查询', 5010, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', NOW(), '', NULL, ''),
(5012, '操作日志删除', 5010, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', NOW(), '', NULL, ''),
(5013, '操作日志导出', 5010, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', NOW(), '', NULL, '');

-- ========== 数据统计菜单配置 ==========

-- 1、数据统计一级菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6000, '数据统计', 0, 4, 'statistics', NULL, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', NOW(), '', NULL, '数据统计目录');

-- 2、数据概览菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6001, '数据概览', 6000, 1, 'overview', 'statistics/overview/index', '', '', 1, 0, 'C', '0', '0', 'statistics:overview:list', 'overview', 'admin', NOW(), '', NULL, '数据概览菜单');

-- 数据概览按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6002, '数据概览查询', 6001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:overview:query', '#', 'admin', NOW(), '', NULL, '');

-- 3、文章统计菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6003, '文章统计', 6000, 2, 'article', 'statistics/article/index', '', '', 1, 0, 'C', '0', '0', 'statistics:article:list', 'documentation', 'admin', NOW(), '', NULL, '文章统计菜单');

-- 文章统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6004, '文章统计查询', 6003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:query', '#', 'admin', NOW(), '', NULL, ''),
(6005, '文章统计导出', 6003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:export', '#', 'admin', NOW(), '', NULL, '');

-- 4、用户统计菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (6006, '用户统计', 6000, 3, 'user', 'statistics/user/index', '', '', 1, 0, 'C', '0', '0', 'statistics:user:list', 'user', 'admin', NOW(), '', NULL, '用户统计菜单');

-- 用户统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(6007, '用户统计查询', 6006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:query', '#', 'admin', NOW(), '', NULL, ''),
(6008, '用户统计导出', 6006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:export', '#', 'admin', NOW(), '', NULL, '');

-- ========== 为管理员角色分配新增菜单权限 ==========

-- 系统监控补充菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 5000), (1, 5001),
(1, 5002), (1, 5003),
(1, 5004), (1, 5005),
(1, 5006), (1, 5007), (1, 5008), (1, 5009),
(1, 5010), (1, 5011), (1, 5012), (1, 5013);

-- 数据统计菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 6000),
(1, 6001), (1, 6002),
(1, 6003), (1, 6004), (1, 6005),
(1, 6006), (1, 6007), (1, 6008);

-- ========== 执行完成提示 ==========

SELECT '✅ 系统监控和数据统计菜单补充完成！' AS '状态';
SELECT '📋 添加的菜单数量：' AS '信息', COUNT(*) AS '数量' FROM sys_menu WHERE menu_id >= 5000;
SELECT '🔑 为管理员角色分配的新增菜单权限数量：' AS '信息', COUNT(*) AS '数量' FROM sys_role_menu WHERE role_id = 1 AND menu_id >= 5000;

-- 显示添加的系统监控补充菜单
SELECT '📁 系统监控补充菜单列表：' AS '类型', menu_id AS '菜单ID', menu_name AS '菜单名称', perms AS '权限标识'
FROM sys_menu
WHERE parent_id = 2 AND menu_id >= 5000 AND menu_id < 6000
ORDER BY order_num;

-- 显示添加的数据统计菜单
SELECT '📁 数据统计菜单列表：' AS '类型', menu_id AS '菜单ID', menu_name AS '菜单名称', perms AS '权限标识'
FROM sys_menu
WHERE parent_id = 6000 OR menu_id = 6000
ORDER BY order_num;