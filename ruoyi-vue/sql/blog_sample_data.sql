-- 博客示例数据
-- 插入博客分类示例数据
INSERT INTO `blog_category` (`category_name`, `description`, `sort`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('技术分享', 'Java、前端、数据库等技术文章', 1, '0', 'admin', NOW(), '0'),
('生活随笔', '日常生活感悟和随想', 2, '0', 'admin', NOW(), '0'),
('学习笔记', '学习过程中的笔记和总结', 3, '0', 'admin', NOW(), '0'),
('项目实战', '实际项目开发经验分享', 4, '0', 'admin', NOW(), '0');

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

-- 插入博客文章示例数据
INSERT INTO `blog_article` (`title`, `summary`, `content`, `cover_image`, `category_id`, `view_count`, `like_count`, `comment_count`, `is_top`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('Spring Boot + Vue.js 全栈开发实战', '本文介绍如何使用Spring Boot和Vue.js构建现代化的全栈Web应用', 
'# Spring Boot + Vue.js 全栈开发实战

## 项目介绍
本项目是基于Spring Boot 2.5.15和Vue.js 3.x构建的现代化博客系统。

## 技术栈
- 后端：Spring Boot + MyBatis + MySQL + Redis
- 前端：Vue.js + Element Plus + Vite

## 功能特性
1. 用户管理
2. 文章管理
3. 分类标签
4. 评论系统
5. 权限控制

## 总结
通过本项目的实践，可以深入理解前后端分离开发的完整流程。', 
'', 1, 0, 0, 0, '1', '0', 'admin', NOW(), '0'),

('MySQL数据库优化实践', '分享MySQL数据库性能优化的实用技巧和经验', 
'# MySQL数据库优化实践

## 索引优化
合理使用索引可以大幅提升查询性能。

## 查询优化
避免全表扫描，优化SQL语句结构。

## 配置优化
调整MySQL配置参数，提升整体性能。', 
'', 1, 0, 0, 0, '0', '0', 'admin', NOW(), '0'),

('Vue.js 3.0 新特性详解', '深入了解Vue.js 3.0的Composition API等新特性', 
'# Vue.js 3.0 新特性详解

## Composition API
新的组合式API提供了更好的逻辑复用能力。

## 性能提升
Vue 3在性能方面有显著提升。

## TypeScript支持
更好的TypeScript集成。', 
'', 1, 0, 0, 0, '0', '0', 'admin', NOW(), '0');

-- 插入文章标签关联数据
INSERT INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 7),
(2, 1), (2, 4), (2, 8),
(3, 3), (3, 6), (3, 9);

-- 插入友情链接示例数据
INSERT INTO `blog_friend_link` (`name`, `url`, `logo`, `description`, `sort`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('Spring官网', 'https://spring.io/', '', 'Spring框架官方网站', 1, '0', 'admin', NOW(), '0'),
('Vue.js官网', 'https://vuejs.org/', '', 'Vue.js框架官方网站', 2, '0', 'admin', NOW(), '0'),
('Element Plus', 'https://element-plus.org/', '', 'Vue 3组件库', 3, '0', 'admin', NOW(), '0'),
('GitHub', 'https://github.com/', '', '全球最大的代码托管平台', 4, '0', 'admin', NOW(), '0');

-- 插入博客设置示例数据
INSERT INTO `blog_setting` (`setting_key`, `setting_value`, `description`, `create_by`, `create_time`) VALUES
('blog_name', '我的技术博客', '博客名称', 'admin', NOW()),
('blog_desc', '分享技术，记录生活，成长路上的点点滴滴', '博客描述', 'admin', NOW()),
('blog_author', 'Nevell', '博客作者', 'admin', NOW()),
('blog_keywords', 'Java,Spring Boot,Vue.js,前端,后端,全栈开发', '博客关键词', 'admin', NOW()),
('blog_copyright', '© 2025 我的技术博客. All rights reserved.', '版权信息', 'admin', NOW()),
('blog_beian', 'ICP备案号：京ICP备xxxxxxxx号', '备案信息', 'admin', NOW());