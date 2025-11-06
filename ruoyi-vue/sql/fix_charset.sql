-- 检查数据库字符集状态
SELECT '数据库字符集状态检查:' AS info;
SHOW VARIABLES LIKE 'character_set_%';
SHOW VARIABLES LIKE 'collation_%';

-- 检查sys_menu表的字符集
SELECT 'sys_menu表字符集检查:' AS info;
SELECT TABLE_NAME, TABLE_COLLATION 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'newblog' AND TABLE_NAME = 'sys_menu';

-- 检查sys_menu表列的字符集
SELECT 'sys_menu表列字符集检查:' AS info;
SELECT COLUMN_NAME, CHARACTER_SET_NAME, COLLATION_NAME
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'newblog' AND TABLE_NAME = 'sys_menu' 
AND CHARACTER_SET_NAME IS NOT NULL;

-- 检查当前菜单数据
SELECT '当前菜单数据检查:' AS info;
SELECT menu_id, menu_name, HEX(menu_name) as hex_name
FROM sys_menu 
WHERE menu_name LIKE '%博客%' OR menu_name LIKE '%管理%'
LIMIT 10;

-- 修复sys_menu表的字符集（如果需要）
SELECT '修复sys_menu表字符集:' AS info;
ALTER TABLE sys_menu CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 修复菜单数据（如果存储时字符集错误）
SELECT '修复菜单数据:' AS info;
-- 备份原始数据
CREATE TABLE IF NOT EXISTS sys_menu_backup_charset AS SELECT * FROM sys_menu;

-- 重新插入中文菜单数据（如果乱码）
UPDATE sys_menu SET menu_name = '博客管理' WHERE menu_id = 2000;
UPDATE sys_menu SET menu_name = '文章管理' WHERE menu_id = 2001;
UPDATE sys_menu SET menu_name = '分类管理' WHERE menu_id = 2002;
UPDATE sys_menu SET menu_name = '标签管理' WHERE menu_id = 2003;
UPDATE sys_menu SET menu_name = '评论管理' WHERE menu_id = 2004;

-- 验证修复结果
SELECT '修复后菜单数据验证:' AS info;
SELECT menu_id, menu_name, HEX(menu_name) as hex_name
FROM sys_menu 
WHERE menu_id IN (2000, 2001, 2002, 2003, 2004);