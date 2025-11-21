-- 完整的系统管理后台菜单结构创建脚本
-- 基于优化后的菜单设计

USE newblog;

-- 1. 创建系统管理主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1000, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', NULL, 'system', 'admin', NOW(), '', NULL, '系统管理主菜单');

-- 2. 系统管理子菜单
-- 用户管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1001, '用户管理', 1000, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', NOW(), '', NULL, '用户管理菜单');

-- 角色管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1002, '角色管理', 1000, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', NOW(), '', NULL, '角色管理菜单');

-- 菜单管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1003, '菜单管理', 1000, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单');

-- 部门管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1004, '部门管理', 1000, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', NOW(), '', NULL, '部门管理菜单');

-- 岗位管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1005, '岗位管理', 1000, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', NOW(), '', NULL, '岗位管理菜单');

-- 字典管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1006, '字典管理', 1000, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', NOW(), '', NULL, '字典管理菜单');

-- 参数设置
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1007, '参数设置', 1000, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', NOW(), '', NULL, '参数设置菜单');

-- 通知公告
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1008, '通知公告', 1000, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', NOW(), '', NULL, '通知公告菜单');

-- 日志管理 (作为父菜单)
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (1009, '日志管理', 1000, 9, 'log', NULL, '', '', 1, 0, 'M', '0', '0', NULL, 'log', 'admin', NOW(), '', NULL, '日志管理目录');

-- 操作日志
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (10091, '操作日志', 1009, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'system:operlog:list', 'form', 'admin', NOW(), '', NULL, '操作日志菜单');

-- 登录日志
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (10092, '登录日志', 1009, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'system:logininfor:list', 'logininfor', 'admin', NOW(), '', NULL, '登录日志菜单');

-- 3. 创建数据统计主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3000, '数据统计', 0, 2, 'statistics', NULL, '', '', 1, 0, 'M', '0', '0', NULL, 'chart', 'admin', NOW(), '', NULL, '数据统计主菜单');

-- 4. 数据统计子菜单
-- 访问统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3001, '访问统计', 3000, 1, 'visit', 'admin/statistics/visit/index', '', '', 1, 0, 'C', '0', '0', 'statistics:visit:list', 'eye', 'admin', NOW(), '', NULL, '访问统计菜单');

-- 文章统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3002, '文章统计', 3000, 2, 'article', 'admin/statistics/article/index', '', '', 1, 0, 'C', '0', '0', 'statistics:article:list', 'documentation', 'admin', NOW(), '', NULL, '文章统计菜单');

-- 用户统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3003, '用户统计', 3000, 3, 'user', 'admin/statistics/user/index', '', '', 1, 0, 'C', '0', '0', 'statistics:user:list', 'peoples', 'admin', NOW(), '', NULL, '用户统计菜单');

-- 评论统计
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3004, '评论统计', 3000, 4, 'comment', 'admin/statistics/comment/index', '', '', 1, 0, 'C', '0', '0', 'statistics:comment:list', 'message', 'admin', NOW(), '', NULL, '评论统计菜单');

-- 流量分析
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (3005, '流量分析', 3000, 5, 'traffic', 'admin/statistics/traffic/index', '', '', 1, 0, 'C', '0', '0', 'statistics:traffic:list', 'guide', 'admin', NOW(), '', NULL, '流量分析菜单');

-- 5. 创建系统监控主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4000, '系统监控', 0, 3, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', NULL, 'monitor', 'admin', NOW(), '', NULL, '系统监控主菜单');

-- 6. 系统监控子菜单
-- 在线用户
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4001, '在线用户', 4000, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', NOW(), '', NULL, '在线用户菜单');

-- 定时任务
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4002, '定时任务', 4000, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', NOW(), '', NULL, '定时任务菜单');

-- 数据监控
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4003, '数据监控', 4000, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', NOW(), '', NULL, '数据监控菜单');

-- 服务监控
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4004, '服务监控', 4000, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', NOW(), '', NULL, '服务监控菜单');

-- 缓存监控
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4005, '缓存监控', 4000, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', NOW(), '', NULL, '缓存监控菜单');

-- 更新博客管理主菜单的排序，确保正确顺序
UPDATE sys_menu SET order_num = 4 WHERE menu_id = 2000;

-- 为管理员角色分配所有新创建的菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
-- 系统管理菜单
(1, 1000), (1, 1001), (1, 1002), (1, 1003), (1, 1004), (1, 1005), (1, 1006), (1, 1007), (1, 1008), (1, 1009), (1, 10091), (1, 10092),
-- 数据统计菜单
(1, 3000), (1, 3001), (1, 3002), (1, 3003), (1, 3004), (1, 3005),
-- 系统监控菜单
(1, 4000), (1, 4001), (1, 4002), (1, 4003), (1, 4004), (1, 4005);

SELECT '系统管理后台菜单结构创建完成！' AS result;