cd /home/nevell/code/newblog/ruoyi-vue
docker compose down
docker compose up --build
-- 清理冗余菜单数据脚本
-- 删除乱码和重复的菜单项

-- 1. 首先备份当前菜单数据
CREATE TABLE IF NOT EXISTS sys_menu_backup_cleanup AS SELECT * FROM sys_menu;

-- 2. 查看所有乱码的菜单项（包含特殊字符的）
SELECT '乱码菜单项列表:' AS info;
SELECT menu_id, menu_name, HEX(menu_name) as hex_name, parent_id, menu_type 
FROM sys_menu 
WHERE menu_name REGEXP '[^\x00-\x7F]' 
AND menu_name NOT LIKE '%博客%' 
AND menu_name NOT LIKE '%管理%' 
AND menu_name NOT LIKE '%文章%' 
AND menu_name NOT LIKE '%分类%' 
AND menu_name NOT LIKE '%标签%' 
AND menu_name NOT LIKE '%评论%' 
AND menu_name NOT LIKE '%设置%'
ORDER BY menu_id;

-- 3. 查看重复的菜单路径和组件
SELECT '重复菜单路径:' AS info;
SELECT path, component, COUNT(*) as count
FROM sys_menu 
WHERE path != '#' AND path IS NOT NULL
GROUP BY path, component
HAVING COUNT(*) > 1;

-- 4. 删除乱码的菜单项（保留menu_id在2000-2091范围内的正确中文菜单）
SELECT '删除乱码菜单项:' AS info;
DELETE FROM sys_menu 
WHERE menu_id >= 2000 
AND menu_name REGEXP '[^\x00-\x7F]' 
AND menu_name NOT LIKE '%博客%' 
AND menu_name NOT LIKE '%管理%' 
AND menu_name NOT LIKE '%文章%' 
AND menu_name NOT LIKE '%分类%' 
AND menu_name NOT LIKE '%标签%' 
AND menu_name NOT LIKE '%评论%' 
AND menu_name NOT LIKE '%设置%';

-- 5. 修复乱码但需要保留的菜单项
SELECT '修复乱码菜单名称:' AS info;
UPDATE sys_menu SET menu_name = '博客管理' WHERE menu_id = 2056 AND menu_name LIKE '%åšå®¢%';
UPDATE sys_menu SET menu_name = '文章管理' WHERE menu_id = 2057 AND menu_name LIKE '%æ–‡ç«%';
UPDATE sys_menu SET menu_name = '文章查询' WHERE menu_id = 2058 AND menu_name LIKE '%æ–‡ç«%';
UPDATE sys_menu SET menu_name = '文章新增' WHERE menu_id = 2059 AND menu_name LIKE '%æ–‡ç«%';
UPDATE sys_menu SET menu_name = '文章修改' WHERE menu_id = 2060 AND menu_name LIKE '%æ–‡ç«%';
UPDATE sys_menu SET menu_name = '文章删除' WHERE menu_id = 2061 AND menu_name LIKE '%æ–‡ç«%';
UPDATE sys_menu SET menu_name = '分类管理' WHERE menu_id = 2062 AND menu_name LIKE '%åˆ†ç±»%';
UPDATE sys_menu SET menu_name = '分类查询' WHERE menu_id = 2063 AND menu_name LIKE '%åˆ†ç±»%';
UPDATE sys_menu SET menu_name = '分类新增' WHERE menu_id = 2064 AND menu_name LIKE '%åˆ†ç±»%';
UPDATE sys_menu SET menu_name = '分类修改' WHERE menu_id = 2065 AND menu_name LIKE '%åˆ†ç±»%';
UPDATE sys_menu SET menu_name = '分类删除' WHERE menu_id = 2066 AND menu_name LIKE '%åˆ†ç±»%';
UPDATE sys_menu SET menu_name = '标签管理' WHERE menu_id = 2067 AND menu_name LIKE '%æ ‡ç¾%';
UPDATE sys_menu SET menu_name = '标签查询' WHERE menu_id = 2068 AND menu_name LIKE '%æ ‡ç¾%';
UPDATE sys_menu SET menu_name = '标签新增' WHERE menu_id = 2069 AND menu_name LIKE '%æ ‡ç¾%';
UPDATE sys_menu SET menu_name = '标签修改' WHERE menu_id = 2070 AND menu_name LIKE '%æ ‡ç¾%';
UPDATE sys_menu SET menu_name = '标签删除' WHERE menu_id = 2071 AND menu_name LIKE '%æ ‡ç¾%';
UPDATE sys_menu SET menu_name = '评论管理' WHERE menu_id = 2072 AND menu_name LIKE '%è¯„è®º%';
UPDATE sys_menu SET menu_name = '评论查询' WHERE menu_id = 2073 AND menu_name LIKE '%è¯„è®º%';
UPDATE sys_menu SET menu_name = '评论新增' WHERE menu_id = 2074 AND menu_name LIKE '%è¯„è®º%';
UPDATE sys_menu SET menu_name = '评论修改' WHERE menu_id = 2075 AND menu_name LIKE '%è¯„è®º%';
UPDATE sys_menu SET menu_name = '评论删除' WHERE menu_id = 2076 AND menu_name LIKE '%è¯„è®º%';
UPDATE sys_menu SET menu_name = '友链管理' WHERE menu_id = 2080 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '友链查询' WHERE menu_id = 2081 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '友链新增' WHERE menu_id = 2082 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '友链修改' WHERE menu_id = 2083 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '友链删除' WHERE menu_id = 2084 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '友链导出' WHERE menu_id = 2085 AND menu_name LIKE '%å‹é“¾%';
UPDATE sys_menu SET menu_name = '博客设置' WHERE menu_id = 2087 AND menu_name LIKE '%åšå®¢è®¾ç½®%';
UPDATE sys_menu SET menu_name = '设置查询' WHERE menu_id = 2088 AND menu_name LIKE '%è®¾ç½®%';
UPDATE sys_menu SET menu_name = '设置新增' WHERE menu_id = 2089 AND menu_name LIKE '%è®¾ç½®%';
UPDATE sys_menu SET menu_name = '设置修改' WHERE menu_id = 2090 AND menu_name LIKE '%è®¾ç½®%';
UPDATE sys_menu SET menu_name = '设置删除' WHERE menu_id = 2091 AND menu_name LIKE '%è®¾ç½®%';

-- 6. 删除重复的菜单项（保留menu_id较小的）
SELECT '删除重复菜单项:' AS info;
DELETE m1 FROM sys_menu m1
INNER JOIN sys_menu m2 
WHERE m1.menu_id > m2.menu_id 
AND m1.path = m2.path 
AND m1.component = m2.component 
AND m1.path != '#' 
AND m1.path IS NOT NULL;

-- 7. 验证清理结果
SELECT '清理后菜单总数:' AS info;
SELECT COUNT(*) as total_menus FROM sys_menu;

SELECT '清理后博客相关菜单:' AS info;
SELECT menu_id, menu_name, parent_id, order_num, path, component, menu_type 
FROM sys_menu 
WHERE menu_id >= 2000 
ORDER BY menu_id;

-- 8. 检查菜单层级结构
SELECT '菜单层级结构:' AS info;
SELECT m1.menu_id, m1.menu_name, m1.parent_id, m2.menu_name as parent_name
FROM sys_menu m1
LEFT JOIN sys_menu m2 ON m1.parent_id = m2.menu_id
WHERE m1.menu_id >= 2000
ORDER BY m1.parent_id, m1.order_num;