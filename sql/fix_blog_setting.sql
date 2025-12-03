-- 修复博客设置表 - 添加缺失的设置项
-- 执行前请备份数据库

-- 插入缺失的设置项（如果不存在）
INSERT IGNORE INTO `blog_setting` (`config_key`, `config_value`, `description`, `create_time`, `update_time`) VALUES
('footer_enabled', 'true', '是否显示底部', NOW(), NOW()),
('copyright_enabled', 'true', '是否显示版权信息', NOW(), NOW()),
('comment_enabled', 'true', '是否开启评论功能', NOW(), NOW()),
('comment_review', 'true', '评论是否需要审核', NOW(), NOW()),
('like_enabled', 'true', '是否开启点赞功能', NOW(), NOW()),
('view_count_enabled', 'true', '是否开启浏览统计', NOW(), NOW()),
('share_enabled', 'true', '是否开启分享功能', NOW(), NOW()),
('search_enabled', 'true', '是否开启搜索功能', NOW(), NOW()),
('sidebar_enabled', 'true', '是否显示侧边栏', NOW(), NOW()),
('theme_color', '#409EFF', '主题颜色', NOW(), NOW()),
('header_background', '#304156', '头部背景色', NOW(), NOW()),
('sidebar_style', 'dark', '侧边栏样式', NOW(), NOW()),
('page_size', '10', '每页文章数', NOW(), NOW()),
('hot_article_count', '5', '热门文章数', NOW(), NOW()),
('recent_comment_count', '5', '最新评论数', NOW(), NOW()),
('greeting_message', '欢迎来到我的博客！', '欢迎信息', NOW(), NOW()),
('about_content', '', '关于页面内容', NOW(), NOW()),
('seo_title', '', 'SEO标题', NOW(), NOW()),
('seo_description', '', 'SEO描述', NOW(), NOW()),
('seo_canonical_url', '', '规范URL', NOW(), NOW()),
('seo_robots', 'index,follow', 'Robots规则', NOW(), NOW()),
('seo_favicon', '', '网站图标', NOW(), NOW()),
('blog_avatar', '', '博主头像', NOW(), NOW()),
('blog_signature', '', '博主签名', NOW(), NOW()),
('blog_start_time', '2025-01-01', '博客创建时间', NOW(), NOW());

-- 更新现有的设置项（保持数据一致性）
UPDATE `blog_setting` SET `config_key` = 'footer_enabled' WHERE `config_key` = 'footer_enabled' OR `config_key` = 'blog_footer_enabled';
UPDATE `blog_setting` SET `config_key` = 'copyright_enabled' WHERE `config_key` = 'copyright_enabled' OR `config_key` = 'blog_copyright_enabled';
UPDATE `blog_setting` SET `config_key` = 'comment_enabled' WHERE `config_key` = 'comment_enabled' OR `config_key` = 'blog_comment_enable';

-- 查询验证设置是否添加成功
SELECT config_key, config_value, description FROM blog_setting 
WHERE config_key IN ('footer_enabled', 'copyright_enabled', 'comment_enabled', 'comment_review', 
                    'like_enabled', 'view_count_enabled', 'share_enabled', 'search_enabled', 
                    'sidebar_enabled', 'theme_color', 'page_size')
ORDER BY config_key;