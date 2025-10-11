-- 博客示例数据（修正版）
-- 根据实际表结构调整字段名

-- 插入博客标签示例数据
INSERT INTO `blog_tag` (`tag_name`, `create_by`, `create_time`, `del_flag`) VALUES
('Java', 'admin', NOW(), '0'),
('Spring Boot', 'admin', NOW(), '0'),
('Vue.js', 'admin', NOW(), '0'),
('MySQL', 'admin', NOW(), '0'),
('Redis', 'admin', NOW(), '0'),
('前端开发', 'admin', NOW(), '0'),
('后端开发', 'admin', NOW(), '0'),
('数据库', 'admin', NOW(), '0'),
('框架学习', 'admin', NOW(), '0'),
('项目经验', 'admin', NOW(), '0');

-- 插入友情链接示例数据
INSERT INTO `blog_friend_link` (`name`, `url`, `logo`, `description`, `sort`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('Spring官网', 'https://spring.io/', '', 'Spring框架官方网站', 1, '0', 'admin', NOW(), '0'),
('Vue.js官网', 'https://vuejs.org/', '', 'Vue.js框架官方网站', 2, '0', 'admin', NOW(), '0'),
('Element Plus', 'https://element-plus.org/', '', 'Vue 3组件库', 3, '0', 'admin', NOW(), '0'),
('GitHub', 'https://github.com/', '', '全球最大的代码托管平台', 4, '0', 'admin', NOW(), '0'),
('MDN Web Docs', 'https://developer.mozilla.org/', '', 'Web开发者资源', 5, '0', 'admin', NOW(), '0');

-- 为现有文章添加标签关联（假设文章ID 25, 30存在）
INSERT INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(25, 1), (25, 2), (25, 7),  -- 第一篇文章关联Java、Spring Boot、后端开发
(30, 3), (30, 6), (30, 9);  -- 第二篇文章关联Vue.js、前端开发、框架学习

-- 更新博客设置（如果不存在则插入）
INSERT INTO `blog_setting` (`setting_key`, `setting_value`, `description`, `create_by`, `create_time`) VALUES
('blog_name', '我的技术博客', '博客名称', 'admin', NOW()),
('blog_desc', '分享技术，记录生活，成长路上的点点滴滴', '博客描述', 'admin', NOW()),
('blog_author', 'Nevell', '博客作者', 'admin', NOW()),
('blog_keywords', 'Java,Spring Boot,Vue.js,前端,后端,全栈开发', '博客关键词', 'admin', NOW()),
('blog_copyright', '© 2025 我的技术博客. All rights reserved.', '版权信息', 'admin', NOW()),
('blog_beian', 'ICP备案号：京ICP备xxxxxxxx号', '备案信息', 'admin', NOW())
ON DUPLICATE KEY UPDATE 
setting_value = VALUES(setting_value),
description = VALUES(description),
update_time = NOW();