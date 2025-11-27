-- 测试数据库连接和数据查询
-- 请在MySQL命令行中执行以下命令

-- 1. 检查数据库是否存在
SHOW DATABASES LIKE 'newblog';

-- 2. 使用数据库
USE newblog;

-- 3. 检查博客相关表是否存在
SHOW TABLES LIKE 'blog_%';

-- 4. 检查blog_article表结构
DESCRIBE blog_article;

-- 5. 查看blog_article表中的数据
SELECT id, title, author_name, status, create_time FROM blog_article LIMIT 5;

-- 6. 检查是否有数据
SELECT COUNT(*) as total_articles FROM blog_article;

-- 7. 检查已发布的文章
SELECT id, title, author_name FROM blog_article WHERE status = 1 LIMIT 3;

-- 8. 检查标签表
SELECT COUNT(*) as total_tags FROM blog_tag;

-- 9. 检查文章标签关联表
SELECT COUNT(*) as total_relations FROM blog_article_tag;

-- 10. 检查具体的文章标签关联
SELECT
    ba.id as article_id,
    ba.title,
    GROUP_CONCAT(bt.name) as tags
FROM blog_article ba
LEFT JOIN blog_article_tag bat ON ba.id = bat.article_id
LEFT JOIN blog_tag bt ON bat.tag_id = bt.id
WHERE ba.status = 1
GROUP BY ba.id, ba.title
LIMIT 5;