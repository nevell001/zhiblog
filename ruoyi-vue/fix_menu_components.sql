-- 修复菜单组件路径问题
USE newblog;

-- 修复标签管理的组件路径
UPDATE sys_menu 
SET component = 'admin/blog/tag/tag/index'
WHERE menu_id = 2003;

-- 修复关于作者的组件路径
UPDATE sys_menu 
SET component = 'admin/blog/about/about/index'
WHERE menu_id = 2007;

-- 验证修复结果
SELECT menu_id, menu_name, parent_id, path, component, perms 
FROM sys_menu 
WHERE menu_id IN (2003, 2007);