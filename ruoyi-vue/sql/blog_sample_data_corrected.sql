-- 博客示例数据（修正版）- 根据实际表结构

-- 插入博客分类示例数据
INSERT INTO `blog_category` (`name`, `sort`, `create_time`, `del_flag`) VALUES
('技术分享', 1, NOW(), '0'),
('生活随笔', 2, NOW(), '0'),
('学习笔记', 3, NOW(), '0'),
('项目实战', 4, NOW(), '0');

-- 插入博客标签示例数据
INSERT INTO `blog_tag` (`name`, `create_time`, `del_flag`) VALUES
('Java', NOW(), '0'),
('Spring Boot', NOW(), '0'),
('Vue.js', NOW(), '0'),
('MySQL', NOW(), '0'),
('Redis', NOW(), '0'),
('前端开发', NOW(), '0'),
('后端开发', NOW(), '0'),
('数据库', NOW(), '0'),
('框架学习', NOW(), '0'),
('项目经验', NOW(), '0');

-- 插入博客文章示例数据
INSERT INTO `blog_article` (`title`, `summary`, `content`, `cover_url`, `category_id`, `author_id`, `is_top`, `is_recommend`, `status`, `view_count`, `like_count`, `comment_count`, `create_time`, `del_flag`) VALUES
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
'', 1, 1, 1, 1, 1, 0, 0, 0, NOW(), '0'),

('MySQL数据库优化实践', '分享MySQL数据库性能优化的实用技巧和经验', 
'# MySQL数据库优化实践

## 索引优化
合理使用索引可以大幅提升查询性能。

## 查询优化
避免全表扫描，优化SQL语句结构。

## 配置优化
调整MySQL配置参数，提升整体性能。', 
'', 1, 1, 0, 1, 1, 0, 0, 0, NOW(), '0'),

('Vue.js 3.0 新特性详解', '深入了解Vue.js 3.0的Composition API等新特性', 
'# Vue.js 3.0 新特性详解

## Composition API
新的组合式API提供了更好的逻辑复用能力。

## 性能提升
Vue 3在性能方面有显著提升。

## TypeScript支持
更好的TypeScript集成。', 
'', 1, 1, 0, 0, 1, 0, 0, 0, NOW(), '0');

-- 插入文章标签关联数据
INSERT INTO `blog_article_tag` (`article_id`, `tag_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 7),
(2, 1), (2, 4), (2, 8),
(3, 3), (3, 6), (3, 9);

-- 插入友情链接示例数据
INSERT INTO `blog_friend_link` (`name`, `url`, `logo`, `description`, `status`, `create_time`) VALUES
('Spring官网', 'https://spring.io/', '', 'Spring框架官方网站', 1, NOW()),
('Vue.js官网', 'https://vuejs.org/', '', 'Vue.js框架官方网站', 1, NOW()),
('Element Plus', 'https://element-plus.org/', '', 'Vue 3组件库', 1, NOW()),
('GitHub', 'https://github.com/', '', '全球最大的代码托管平台', 1, NOW());

-- 插入博客设置示例数据
INSERT INTO `blog_setting` (`config_key`, `config_value`, `description`, `create_time`) VALUES
('blog_name', '我的技术博客', '博客名称', NOW()),
('blog_desc', '分享技术，记录生活，成长路上的点点滴滴', '博客描述', NOW()),
('blog_author', 'Nevell', '博客作者', NOW()),
('blog_keywords', 'Java,Spring Boot,Vue.js,前端,后端,全栈开发', '博客关键词', NOW()),
('blog_copyright', '© 2025 我的技术博客. All rights reserved.', '版权信息', NOW()),
('blog_beian', 'ICP备案号：京ICP备xxxxxxxx号', '备案信息', NOW())
ON DUPLICATE KEY UPDATE 
config_value = VALUES(config_value),
description = VALUES(description),
update_time = NOW();