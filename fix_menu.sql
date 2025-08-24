-- ========================================
-- 博客管理菜单修复脚本 - 方案1（推荐）
-- ========================================
-- 此脚本将创建标准的博客管理菜单结构
-- 执行前请备份数据库！

-- 1. 创建博客管理父菜单
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component, 
    is_frame, is_cache, menu_type, visible, status, 
    perms, icon, create_by, create_time, remark
) VALUES (
    '博客管理', 0, 1, 'blog', NULL, 
    1, 0, 'M', '0', '0', 
    '', 'documentation', 'admin', NOW(), '博客管理目录'
);

-- 获取刚插入的博客管理菜单ID
SET @blog_parent_id = LAST_INSERT_ID();

-- 2. 更新现有的文章管理菜单，使其成为博客管理的子菜单
UPDATE sys_menu SET 
    parent_id = @blog_parent_id,
    order_num = 1,
    path = 'article',
    component = 'system/article/index',
    menu_name = '文章管理',
    update_by = 'admin',
    update_time = NOW()
WHERE menu_id = 2001;

-- 3. 添加分类管理菜单
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component, 
    is_frame, is_cache, menu_type, visible, status, 
    perms, icon, create_by, create_time, remark
) VALUES (
    '分类管理', @blog_parent_id, 2, 'category', 'system/category/index', 
    1, 0, 'C', '0', '0', 
    'system:category:list', 'list', 'admin', NOW(), '博客分类管理菜单'
);

SET @category_menu_id = LAST_INSERT_ID();

-- 4. 添加标签管理菜单
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component, 
    is_frame, is_cache, menu_type, visible, status, 
    perms, icon, create_by, create_time, remark
) VALUES (
    '标签管理', @blog_parent_id, 3, 'tag', 'system/tag/index', 
    1, 0, 'C', '0', '0', 
    'system:tag:list', 'tag', 'admin', NOW(), '博客标签管理菜单'
);

SET @tag_menu_id = LAST_INSERT_ID();

-- 5. 为博客管理及其子菜单添加权限（给超级管理员角色）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES 
(1, @blog_parent_id),    -- 博客管理父菜单
(1, 2001),               -- 文章管理（更新后的）
(1, @category_menu_id),  -- 分类管理
(1, @tag_menu_id);       -- 标签管理

-- 6. 为普通角色也添加相应权限（可选）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES 
(2, @blog_parent_id),    -- 博客管理父菜单
(2, 2001),               -- 文章管理
(2, @category_menu_id),  -- 分类管理
(2, @tag_menu_id);       -- 标签管理

-- 7. 添加分类管理的按钮权限
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component, 
    is_frame, is_cache, menu_type, visible, status, 
    perms, icon, create_by, create_time, remark
) VALUES 
('分类查询', @category_menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'system:category:query', '#', 'admin', NOW(), ''),
('分类新增', @category_menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'system:category:add', '#', 'admin', NOW(), ''),
('分类修改', @category_menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'system:category:edit', '#', 'admin', NOW(), ''),
('分类删除', @category_menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'system:category:remove', '#', 'admin', NOW(), ''),
('分类导出', @category_menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'system:category:export', '#', 'admin', NOW(), '');

-- 8. 添加标签管理的按钮权限
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component, 
    is_frame, is_cache, menu_type, visible, status, 
    perms, icon, create_by, create_time, remark
) VALUES 
('标签查询', @tag_menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'system:tag:query', '#', 'admin', NOW(), ''),
('标签新增', @tag_menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'system:tag:add', '#', 'admin', NOW(), ''),
('标签修改', @tag_menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'system:tag:edit', '#', 'admin', NOW(), ''),
('标签删除', @tag_menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'system:tag:remove', '#', 'admin', NOW(), ''),
('标签导出', @tag_menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'system:tag:export', '#', 'admin', NOW(), '');

-- ========================================
-- 执行完成提示
-- ========================================
-- 1. 重新登录后台管理系统
-- 2. 清除浏览器缓存
-- 3. 菜单结构将变为：
--    📁 博客管理
--      ├── 📄 文章管理
--      ├── 📁 分类管理
--      └── 🏷️ 标签管理