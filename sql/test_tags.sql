-- 测试数据：为文章添加标签
-- 执行前请备份数据库

-- 1. 插入测试标签（如果不存在）
INSERT IGNORE INTO `blog_tag` (`id`, `name`, `description`, `color`, `icon`, `create_time`, `update_time`, `del_flag`) VALUES
(1, 'Java', 'Java编程语言相关文章', '#f89820', 'el-icon-cpu', NOW(), NOW(), 0),
(2, 'Vue.js', 'Vue.js前端框架相关文章', '#4fc08d', 'el-icon-monitor', NOW(), NOW(), 0),
(3, 'Spring Boot', 'Spring Boot框架相关文章', '#6db33f', 'el-icon-setting', NOW(), NOW(), 0),
(4, 'MySQL', 'MySQL数据库相关文章', '#00758f', 'el-icon-coin', NOW(), NOW(), 0),
(5, '前端开发', '前端开发技术相关文章', '#e34c26', 'el-icon-brush', NOW(), NOW(), 0),
(6, '后端开发', '后端开发技术相关文章', '#337ecc', 'el-icon-server', NOW(), NOW(), 0);

-- 2. 插入文章-标签关联关系
-- 为文章1 "Spring Boot + Vue.js 全栈开发实战" 添加标签
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(1, 1),  -- Java
(1, 2),  -- Vue.js
(1, 3);  -- Spring Boot

-- 为文章2 "MySQL数据库优化实践" 添加标签
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(2, 1),  -- Java
(2, 4),  -- MySQL
(2, 6);  -- 后端开发

-- 为文章4 "关于调整2025高校科技成果交易会举办时间等有关事项的通知" 添加标签
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(4, 6);  -- 后端开发

-- 3. 查询验证标签是否添加成功
SELECT '标签列表' as type, t.id, t.name, t.color, t.description
FROM blog_tag t WHERE t.del_flag = 0
ORDER BY t.id;

-- 4. 查询验证文章-标签关联是否成功
SELECT
    a.id as article_id,
    a.title as article_title,
    t.id as tag_id,
    t.name as tag_name,
    t.color as tag_color
FROM blog_article a
LEFT JOIN blog_article_tag bat ON a.id = bat.article_id
LEFT JOIN blog_tag t ON bat.tag_id = t.id AND t.del_flag = 0
WHERE a.id IN (1, 2, 4) AND a.del_flag = 0
ORDER BY a.id, t.name;

-- 5. 统计每篇文章的标签数量
SELECT
    a.id as article_id,
    a.title as article_title,
    COUNT(bat.tag_id) as tag_count
FROM blog_article a
LEFT JOIN blog_article_tag bat ON a.id = bat.article_id
LEFT JOIN blog_tag t ON bat.tag_id = t.id AND t.del_flag = 0
WHERE a.id IN (1, 2, 4) AND a.del_flag = 0
GROUP BY a.id, a.title
ORDER BY a.id;