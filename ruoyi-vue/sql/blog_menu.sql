-- 菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('博客管理', 0, 5, 'blog', NULL, 1, 0, 'M', '0', '0', '', 'blog', 'admin', sysdate(), '', NULL, '博客管理菜单');

-- 获取新插入的博客管理菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 文章管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章管理', @parentId, 1, 'article', 'blog/article/index', 1, 0, 'C', '0', '0', 'system:article:list', 'article', 'admin', sysdate(), '', NULL, '文章管理菜单');

-- 文章管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章查询', @parentId+1, 1, '#', '', 1, 0, 'F', '0', '0', 'system:article:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章新增', @parentId+1, 2, '#', '', 1, 0, 'F', '0', '0', 'system:article:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章修改', @parentId+1, 3, '#', '', 1, 0, 'F', '0', '0', 'system:article:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章删除', @parentId+1, 4, '#', '', 1, 0, 'F', '0', '0', 'system:article:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('文章导出', @parentId+1, 5, '#', '', 1, 0, 'F', '0', '0', 'system:article:export', '#', 'admin', sysdate(), '', NULL, '');

-- 分类管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类管理', @parentId, 2, 'category', 'blog/category/index', 1, 0, 'C', '0', '0', 'system:category:list', 'category', 'admin', sysdate(), '', NULL, '分类管理菜单');

-- 分类管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类查询', @parentId+6, 1, '#', '', 1, 0, 'F', '0', '0', 'system:category:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类新增', @parentId+6, 2, '#', '', 1, 0, 'F', '0', '0', 'system:category:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类修改', @parentId+6, 3, '#', '', 1, 0, 'F', '0', '0', 'system:category:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类删除', @parentId+6, 4, '#', '', 1, 0, 'F', '0', '0', 'system:category:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类导出', @parentId+6, 5, '#', '', 1, 0, 'F', '0', '0', 'system:category:export', '#', 'admin', sysdate(), '', NULL, '');

-- 标签管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签管理', @parentId, 3, 'tag', 'blog/tag/index', 1, 0, 'C', '0', '0', 'system:tag:list', 'tag', 'admin', sysdate(), '', NULL, '标签管理菜单');

-- 标签管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签查询', @parentId+12, 1, '#', '', 1, 0, 'F', '0', '0', 'system:tag:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签新增', @parentId+12, 2, '#', '', 1, 0, 'F', '0', '0', 'system:tag:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签修改', @parentId+12, 3, '#', '', 1, 0, 'F', '0', '0', 'system:tag:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签删除', @parentId+12, 4, '#', '', 1, 0, 'F', '0', '0', 'system:tag:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('标签导出', @parentId+12, 5, '#', '', 1, 0, 'F', '0', '0', 'system:tag:export', '#', 'admin', sysdate(), '', NULL, '');

-- 评论管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论管理', @parentId, 4, 'comment', 'blog/comment/index', 1, 0, 'C', '0', '0', 'system:comment:list', 'comment', 'admin', sysdate(), '', NULL, '评论管理菜单');

-- 评论管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论查询', @parentId+18, 1, '#', '', 1, 0, 'F', '0', '0', 'system:comment:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论新增', @parentId+18, 2, '#', '', 1, 0, 'F', '0', '0', 'system:comment:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论修改', @parentId+18, 3, '#', '', 1, 0, 'F', '0', '0', 'system:comment:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论删除', @parentId+18, 4, '#', '', 1, 0, 'F', '0', '0', 'system:comment:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('评论导出', @parentId+18, 5, '#', '', 1, 0, 'F', '0', '0', 'system:comment:export', '#', 'admin', sysdate(), '', NULL, '');

-- 友链管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链管理', @parentId, 5, 'friendLink', 'blog/friendLink/index', 1, 0, 'C', '0', '0', 'system:friendLink:list', 'link', 'admin', sysdate(), '', NULL, '友链管理菜单');

-- 友链管理按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链查询', @parentId+24, 1, '#', '', 1, 0, 'F', '0', '0', 'system:friendLink:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链新增', @parentId+24, 2, '#', '', 1, 0, 'F', '0', '0', 'system:friendLink:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链修改', @parentId+24, 3, '#', '', 1, 0, 'F', '0', '0', 'system:friendLink:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链删除', @parentId+24, 4, '#', '', 1, 0, 'F', '0', '0', 'system:friendLink:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('友链导出', @parentId+24, 5, '#', '', 1, 0, 'F', '0', '0', 'system:friendLink:export', '#', 'admin', sysdate(), '', NULL, '');

-- 博客设置菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('博客设置', @parentId, 6, 'setting', 'blog/setting/index', 1, 0, 'C', '0', '0', 'system:setting:list', 'setting', 'admin', sysdate(), '', NULL, '博客设置菜单');

-- 博客设置按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('设置查询', @parentId+30, 1, '#', '', 1, 0, 'F', '0', '0', 'system:setting:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('设置新增', @parentId+30, 2, '#', '', 1, 0, 'F', '0', '0', 'system:setting:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('设置修改', @parentId+30, 3, '#', '', 1, 0, 'F', '0', '0', 'system:setting:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('设置删除', @parentId+30, 4, '#', '', 1, 0, 'F', '0', '0', 'system:setting:remove', '#', 'admin', sysdate(), '', NULL, '');