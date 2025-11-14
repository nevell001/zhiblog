-- 更新和添加博客系统设置项

-- 更新现有设置项
UPDATE `blog_setting` SET `config_value` = '我的博客 - 记录技术与生活' WHERE `config_key` = 'blog_name';
UPDATE `blog_setting` SET `config_value` = '分享技术经验，记录生活点滴，成为更好的自己' WHERE `config_key` = 'blog_desc';
UPDATE `blog_setting` SET `config_value` = '前端开发,后端开发,全栈开发,Spring Boot,Vue.js,MySQL,算法,面试' WHERE `config_key` = 'blog_keywords';

-- 添加新的基本设置项
INSERT IGNORE INTO `blog_setting` (`config_key`, `config_value`, `description`) VALUES
('blog_email', 'admin@example.com', '博客联系邮箱'),
('blog_url', 'http://localhost:3000', '博客访问地址'),
('blog_start_time', '2025-01-01', '博客创建时间'),
('blog_avatar', 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主', '博主头像'),
('blog_signature', 'Stay hungry, Stay foolish', '博主签名'),

-- SEO相关设置
('seo_title', '我的博客 - 分享技术与生活', 'SEO标题'),
('seo_description', '专注于前后端技术分享，包含Spring Boot、Vue.js、MySQL等技术内容', 'SEO描述'),
('seo_canonical_url', 'http://localhost:3000', '规范URL'),
('seo_robots', 'index,follow', 'Robots规则'),
('seo_favicon', '/favicon.ico', '网站图标'),

-- 个性化设置
('theme_color', '#409EFF', '主题颜色'),
('header_background', '#ffffff', '头部背景色'),
('sidebar_style', 'dark', '侧边栏样式'),
('footer_enabled', 'true', '是否显示底部'),
('copyright_enabled', 'true', '是否显示版权信息'),

-- 功能开关
('comment_enabled', 'true', '是否启用评论功能'),
('comment_review', 'true', '评论是否需要审核'),
('like_enabled', 'true', '是否启用点赞功能'),
('view_count_enabled', 'true', '是否统计浏览量'),
('share_enabled', 'true', '是否启用分享功能'),
('search_enabled', 'true', '是否启用搜索功能'),

-- 其他设置
('page_size', '10', '每页显示文章数量'),
('hot_article_count', '5', '热门文章显示数量'),
('recent_comment_count', '5', '最新评论显示数量'),
('sidebar_enabled', 'true', '是否显示侧边栏'),
('about_content', '我是一名全栈开发者，热爱技术，热爱生活。', '关于页面内容'),
('greeting_message', '欢迎来到我的博客！', '欢迎信息');

-- 查询所有设置项确认
SELECT '博客设置更新完成！' AS result;
SELECT COUNT(*) AS '设置项总数' FROM `blog_setting`;
SELECT * FROM `blog_setting` ORDER BY `config_key`;