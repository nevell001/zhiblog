-- 修复blog_article表中del_flag字段为null的问题
UPDATE blog_article SET del_flag = 0 WHERE del_flag IS NULL;

-- 修复blog_category表中del_flag字段为null的问题  
UPDATE blog_category SET del_flag = 0 WHERE del_flag IS NULL;

-- 修复blog_tag表中del_flag字段为null的问题
UPDATE blog_tag SET del_flag = 0 WHERE del_flag IS NULL;

-- 验证修复结果
SELECT 'blog_article' as table_name, COUNT(*) as null_count FROM blog_article WHERE del_flag IS NULL
UNION ALL
SELECT 'blog_category' as table_name, COUNT(*) as null_count FROM blog_category WHERE del_flag IS NULL  
UNION ALL
SELECT 'blog_tag' as table_name, COUNT(*) as null_count FROM blog_tag WHERE del_flag IS NULL;

-- 显示修复后的数据统计
SELECT 'blog_article' as table_name, COUNT(*) as total_count, 
       SUM(CASE WHEN del_flag = 0 THEN 1 ELSE 0 END) as normal_count,
       SUM(CASE WHEN del_flag = 1 THEN 1 ELSE 0 END) as deleted_count
FROM blog_article
UNION ALL
SELECT 'blog_category' as table_name, COUNT(*) as total_count,
       SUM(CASE WHEN del_flag = 0 THEN 1 ELSE 0 END) as normal_count, 
       SUM(CASE WHEN del_flag = 1 THEN 1 ELSE 0 END) as deleted_count
FROM blog_category
UNION ALL
SELECT 'blog_tag' as table_name, COUNT(*) as total_count,
       SUM(CASE WHEN del_flag = 0 THEN 1 ELSE 0 END) as normal_count,
       SUM(CASE WHEN del_flag = 1 THEN 1 ELSE 0 END) as deleted_count
FROM blog_tag;