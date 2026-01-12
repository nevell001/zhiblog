-- ===============================================================
-- 系统管理菜单补充脚本
-- ===============================================================
-- 📅 创建时间：2026-01-12
-- 📝 描述：添加系统管理下的完整菜单和权限配置
-- ⚠️ 注意：在执行 init_database.sql 之后执行此脚本
-- ===============================================================

USE newblog;

-- ========== 系统管理菜单配置 ==========

-- 1、用户管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', NOW(), '', NULL, '用户管理菜单');

-- 用户管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1001, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', NOW(), '', NULL, ''),
(1002, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', NOW(), '', NULL, ''),
(1003, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', NOW(), '', NULL, ''),
(1004, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', NOW(), '', NULL, ''),
(1005, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', NOW(), '', NULL, ''),
(1006, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', NOW(), '', NULL, ''),
(1007, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', NOW(), '', NULL, '');

-- 2、角色管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', NOW(), '', NULL, '角色管理菜单');

-- 角色管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1011, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', NOW(), '', NULL, ''),
(1012, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', NOW(), '', NULL, ''),
(1013, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', NOW(), '', NULL, ''),
(1014, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW(), '', NULL, ''),
(1015, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', NOW(), '', NULL, '');

-- 3、菜单管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单');

-- 菜单管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1021, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', NOW(), '', NULL, ''),
(1022, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', NOW(), '', NULL, ''),
(1023, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', NOW(), '', NULL, ''),
(1024, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW(), '', NULL, '');

-- 4、部门管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', NOW(), '', NULL, '部门管理菜单');

-- 部门管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1031, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', NOW(), '', NULL, ''),
(1032, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', NOW(), '', NULL, ''),
(1033, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', NOW(), '', NULL, ''),
(1034, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', NOW(), '', NULL, '');

-- 5、岗位管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', NOW(), '', NULL, '岗位管理菜单');

-- 岗位管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1041, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', NOW(), '', NULL, ''),
(1042, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', NOW(), '', NULL, ''),
(1043, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', NOW(), '', NULL, ''),
(1044, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', NOW(), '', NULL, ''),
(1045, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', NOW(), '', NULL, '');

-- 6、字典管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', NOW(), '', NULL, '字典管理菜单');

-- 字典管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1051, '字典查询', 105, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', NOW(), '', NULL, ''),
(1052, '字典新增', 105, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', NOW(), '', NULL, ''),
(1053, '字典修改', 105, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', NOW(), '', NULL, ''),
(1054, '字典删除', 105, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', NOW(), '', NULL, ''),
(1055, '字典导出', 105, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', NOW(), '', NULL, '');

-- 7、参数设置菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', NOW(), '', NULL, '参数设置菜单');

-- 参数设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1061, '参数查询', 106, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', NOW(), '', NULL, ''),
(1062, '参数新增', 106, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', NOW(), '', NULL, ''),
(1063, '参数修改', 106, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', NOW(), '', NULL, ''),
(1064, '参数删除', 106, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', NOW(), '', NULL, ''),
(1065, '参数导出', 106, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', NOW(), '', NULL, '');

-- 8、通知公告菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', NOW(), '', NULL, '通知公告菜单');

-- 通知公告按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1071, '公告查询', 107, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', NOW(), '', NULL, ''),
(1072, '公告新增', 107, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', NOW(), '', NULL, ''),
(1073, '公告修改', 107, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', NOW(), '', NULL, ''),
(1074, '公告删除', 107, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', NOW(), '', NULL, '');

-- ========== 系统监控菜单配置 ==========

-- 1、在线用户菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', NOW(), '', NULL, '在线用户菜单');

-- 在线用户按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1091, '在线用户查询', 109, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', NOW(), '', NULL, ''),
(1092, '批量强退', 109, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', NOW(), '', NULL, ''),
(1093, '单条强退', 109, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', NOW(), '', NULL, '');

-- 2、定时任务菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', NOW(), '', NULL, '定时任务菜单');

-- 定时任务按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1101, '任务查询', 110, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', NOW(), '', NULL, ''),
(1102, '任务新增', 110, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', NOW(), '', NULL, ''),
(1103, '任务修改', 110, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', NOW(), '', NULL, ''),
(1104, '任务删除', 110, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', NOW(), '', NULL, ''),
(1105, '任务导出', 110, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', NOW(), '', NULL, ''),
(1106, '状态修改', 110, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', NOW(), '', NULL, ''),
(1107, '任务详细', 110, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:detail', '#', 'admin', NOW(), '', NULL, ''),
(1108, '任务执行', 110, 8, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:execute', '#', 'admin', NOW(), '', NULL, '');

-- 3、数据监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', NOW(), '', NULL, '数据监控菜单');

-- 4、服务监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', NOW(), '', NULL, '服务监控菜单');

-- 5、缓存监控菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', NOW(), '', NULL, '缓存监控菜单');

-- 6、缓存列表菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', NOW(), '', NULL, '缓存列表菜单');

-- 缓存列表按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1141, '缓存查询', 114, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:query', '#', 'admin', NOW(), '', NULL, ''),
(1142, '缓存新增', 114, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:add', '#', 'admin', NOW(), '', NULL, ''),
(1143, '缓存修改', 114, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:edit', '#', 'admin', NOW(), '', NULL, ''),
(1144, '缓存删除', 114, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:remove', '#', 'admin', NOW(), '', NULL, ''),
(1145, '缓存清理', 114, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:clean', '#', 'admin', NOW(), '', NULL, '');

-- ========== 系统工具菜单配置 ==========

-- 1、表单构建菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', NOW(), '', NULL, '表单构建菜单');

-- 2、代码生成菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', NOW(), '', NULL, '代码生成菜单');

-- 代码生成按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(1161, '生成查询', 116, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', NOW(), '', NULL, ''),
(1162, '生成新增', 116, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:add', '#', 'admin', NOW(), '', NULL, ''),
(1163, '生成编辑', 116, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', NOW(), '', NULL, ''),
(1164, '生成删除', 116, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', NOW(), '', NULL, ''),
(1165, '导入代码', 116, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', NOW(), '', NULL, ''),
(1166, '预览代码', 116, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', NOW(), '', NULL, ''),
(1167, '生成代码', 116, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', NOW(), '', NULL, '');

-- 3、系统接口菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', NOW(), '', NULL, '系统接口菜单');

-- ========== 为管理员角色分配所有菜单权限 ==========

-- 系统管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 100), (1, 1001), (1, 1002), (1, 1003), (1, 1004), (1, 1005), (1, 1006), (1, 1007),
(1, 101), (1, 1011), (1, 1012), (1, 1013), (1, 1014), (1, 1015),
(1, 102), (1, 1021), (1, 1022), (1, 1023), (1, 1024),
(1, 103), (1, 1031), (1, 1032), (1, 1033), (1, 1034),
(1, 104), (1, 1041), (1, 1042), (1, 1043), (1, 1044), (1, 1045),
(1, 105), (1, 1051), (1, 1052), (1, 1053), (1, 1054), (1, 1055),
(1, 106), (1, 1061), (1, 1062), (1, 1063), (1, 1064), (1, 1065),
(1, 107), (1, 1071), (1, 1072), (1, 1073), (1, 1074);

-- 系统监控菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 109), (1, 1091), (1, 1092), (1, 1093),
(1, 110), (1, 1101), (1, 1102), (1, 1103), (1, 1104), (1, 1105), (1, 1106), (1, 1107), (1, 1108),
(1, 111), (1, 112), (1, 113),
(1, 114), (1, 1141), (1, 1142), (1, 1143), (1, 1144), (1, 1145);

-- 系统工具菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 115), (1, 116), (1, 1161), (1, 1162), (1, 1163), (1, 1164), (1, 1165), (1, 1166), (1, 1167), (1, 117);

-- ========== 执行完成提示 ==========

SELECT '✅ 系统管理菜单补充完成！' AS '状态';
SELECT '📋 添加的菜单数量：' AS '信息', COUNT(*) AS '数量' FROM sys_menu WHERE menu_id >= 100 AND menu_id < 2000;
SELECT '🔑 为管理员角色分配的菜单权限数量：' AS '信息', COUNT(*) AS '数量' FROM sys_role_menu WHERE role_id = 1 AND menu_id >= 100 AND menu_id < 2000;

-- 显示添加的系统管理菜单
SELECT '📁 系统管理菜单列表：' AS '类型', menu_id AS '菜单ID', menu_name AS '菜单名称', perms AS '权限标识'
FROM sys_menu
WHERE parent_id = 1 AND menu_id >= 100
ORDER BY order_num;

-- 显示添加的角色管理菜单和权限
SELECT '🔐 角色管理菜单和权限：' AS '类型', menu_id AS '菜单ID', menu_name AS '菜单名称', perms AS '权限标识'
FROM sys_menu
WHERE parent_id = 101 OR menu_id = 101
ORDER BY menu_id;