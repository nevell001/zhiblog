-- 系统管理和数据统计模块按钮权限配置脚本
-- 为所有菜单项添加细粒度的按钮权限控制

USE newblog;

-- ========================================
-- 系统管理模块按钮权限
-- ========================================

-- 1. 用户管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10011, '用户查询', 1001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', NOW(), '', NULL, ''),
(10012, '用户新增', 1001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', NOW(), '', NULL, ''),
(10013, '用户修改', 1001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', NOW(), '', NULL, ''),
(10014, '用户删除', 1001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', NOW(), '', NULL, ''),
(10015, '用户导出', 1001, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', NOW(), '', NULL, ''),
(10016, '用户导入', 1001, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', NOW(), '', NULL, ''),
(10017, '重置密码', 1001, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', NOW(), '', NULL, '');

-- 2. 角色管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10021, '角色查询', 1002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', NOW(), '', NULL, ''),
(10022, '角色新增', 1002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', NOW(), '', NULL, ''),
(10023, '角色修改', 1002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', NOW(), '', NULL, ''),
(10024, '角色删除', 1002, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW(), '', NULL, ''),
(10025, '角色导出', 1002, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', NOW(), '', NULL, '');

-- 3. 菜单管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10031, '菜单查询', 1003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', NOW(), '', NULL, ''),
(10032, '菜单新增', 1003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', NOW(), '', NULL, ''),
(10033, '菜单修改', 1003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', NOW(), '', NULL, ''),
(10034, '菜单删除', 1003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW(), '', NULL, '');

-- 4. 部门管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10041, '部门查询', 1004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', NOW(), '', NULL, ''),
(10042, '部门新增', 1004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', NOW(), '', NULL, ''),
(10043, '部门修改', 1004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', NOW(), '', NULL, ''),
(10044, '部门删除', 1004, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', NOW(), '', NULL, '');

-- 5. 岗位管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10051, '岗位查询', 1005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', NOW(), '', NULL, ''),
(10052, '岗位新增', 1005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', NOW(), '', NULL, ''),
(10053, '岗位修改', 1005, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', NOW(), '', NULL, ''),
(10054, '岗位删除', 1005, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', NOW(), '', NULL, ''),
(10055, '岗位导出', 1005, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', NOW(), '', NULL, '');

-- 6. 字典管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10061, '字典查询', 1006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', NOW(), '', NULL, ''),
(10062, '字典新增', 1006, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', NOW(), '', NULL, ''),
(10063, '字典修改', 1006, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', NOW(), '', NULL, ''),
(10064, '字典删除', 1006, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', NOW(), '', NULL, ''),
(10065, '字典导出', 1006, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', NOW(), '', NULL, '');

-- 7. 参数设置按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10071, '参数查询', 1007, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', NOW(), '', NULL, ''),
(10072, '参数新增', 1007, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', NOW(), '', NULL, ''),
(10073, '参数修改', 1007, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', NOW(), '', NULL, ''),
(10074, '参数删除', 1007, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', NOW(), '', NULL, ''),
(10075, '参数导出', 1007, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', NOW(), '', NULL, '');

-- 8. 通知公告按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10081, '公告查询', 1008, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', NOW(), '', NULL, ''),
(10082, '公告新增', 1008, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', NOW(), '', NULL, ''),
(10083, '公告修改', 1008, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', NOW(), '', NULL, ''),
(10084, '公告删除', 1008, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', NOW(), '', NULL, '');

-- 9. 操作日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10091, '操作查询', 10091, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', NOW(), '', NULL, ''),
(10092, '操作删除', 10091, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', NOW(), '', NULL, ''),
(10093, '日志导出', 10091, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', NOW(), '', NULL, '');

-- 10. 登录日志按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(10094, '登录查询', 10092, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', NOW(), '', NULL, ''),
(10095, '登录删除', 10092, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', NOW(), '', NULL, ''),
(10096, '账户解锁', 10092, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', NOW(), '', NULL, ''),
(10097, '日志导出', 10092, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', NOW(), '', NULL, '');

-- ========================================
-- 数据统计模块按钮权限
-- ========================================

-- 1. 访问统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30011, '访问查询', 3001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:visit:query', '#', 'admin', NOW(), '', NULL, ''),
(30012, '访问导出', 3001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:visit:export', '#', 'admin', NOW(), '', NULL, ''),
(30013, '访问统计', 3001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:visit:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 2. 文章统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30021, '文章查询', 3002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:query', '#', 'admin', NOW(), '', NULL, ''),
(30022, '文章导出', 3002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:export', '#', 'admin', NOW(), '', NULL, ''),
(30023, '文章统计', 3002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:article:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 3. 用户统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30031, '用户查询', 3003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:query', '#', 'admin', NOW(), '', NULL, ''),
(30032, '用户导出', 3003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:export', '#', 'admin', NOW(), '', NULL, ''),
(30033, '用户统计', 3003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:user:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 4. 评论统计按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30041, '评论查询', 3004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:comment:query', '#', 'admin', NOW(), '', NULL, ''),
(30042, '评论导出', 3004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:comment:export', '#', 'admin', NOW(), '', NULL, ''),
(30043, '评论统计', 3004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:comment:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 5. 流量分析按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(30051, '流量查询', 3005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:traffic:query', '#', 'admin', NOW(), '', NULL, ''),
(30052, '流量导出', 3005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:traffic:export', '#', 'admin', NOW(), '', NULL, ''),
(30053, '流量分析', 3005, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'statistics:traffic:analysis', '#', 'admin', NOW(), '', NULL, '');

-- ========================================
-- 系统监控模块按钮权限
-- ========================================

-- 1. 在线用户按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40011, '在线查询', 4001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', NOW(), '', NULL, ''),
(40012, '强退用户', 4001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', NOW(), '', NULL, '');

-- 2. 定时任务按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40021, '任务查询', 4002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', NOW(), '', NULL, ''),
(40022, '任务新增', 4002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', NOW(), '', NULL, ''),
(40023, '任务修改', 4002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', NOW(), '', NULL, ''),
(40024, '任务删除', 4002, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', NOW(), '', NULL, ''),
(40025, '任务导出', 4002, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', NOW(), '', NULL, ''),
(40026, '任务执行', 4002, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', NOW(), '', NULL, ''),
(40027, '任务详情', 4002, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:detail', '#', 'admin', NOW(), '', NULL, '');

-- 3. 数据监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40031, '数据查询', 4003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:druid:query', '#', 'admin', NOW(), '', NULL, '');

-- 4. 服务监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40041, '服务查询', 4004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:server:query', '#', 'admin', NOW(), '', NULL, '');

-- 5. 缓存监控按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40051, '缓存查询', 4005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:query', '#', 'admin', NOW(), '', NULL, ''),
(40052, '缓存清理', 4005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:clean', '#', 'admin', NOW(), '', NULL, ''),
(40053, '缓存详情', 4005, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'monitor:cache:detail', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配所有按钮权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
-- 系统管理按钮权限
(1, 10011), (1, 10012), (1, 10013), (1, 10014), (1, 10015), (1, 10016), (1, 10017),
(1, 10021), (1, 10022), (1, 10023), (1, 10024), (1, 10025),
(1, 10031), (1, 10032), (1, 10033), (1, 10034),
(1, 10041), (1, 10042), (1, 10043), (1, 10044),
(1, 10051), (1, 10052), (1, 10053), (1, 10054), (1, 10055),
(1, 10061), (1, 10062), (1, 10063), (1, 10064), (1, 10065),
(1, 10071), (1, 10072), (1, 10073), (1, 10074), (1, 10075),
(1, 10081), (1, 10082), (1, 10083), (1, 10084),
(1, 10091), (1, 10092), (1, 10093),
(1, 10094), (1, 10095), (1, 10096), (1, 10097),
-- 数据统计按钮权限
(1, 30011), (1, 30012), (1, 30013),
(1, 30021), (1, 30022), (1, 30023),
(1, 30031), (1, 30032), (1, 30033),
(1, 30041), (1, 30042), (1, 30043),
(1, 30051), (1, 30052), (1, 30053),
-- 系统监控按钮权限
(1, 40011), (1, 40012),
(1, 40021), (1, 40022), (1, 40023), (1, 40024), (1, 40025), (1, 40026), (1, 40027),
(1, 40031),
(1, 40041),
(1, 40051), (1, 40052), (1, 40053);

SELECT '系统管理和数据统计模块按钮权限配置完成！' AS result;