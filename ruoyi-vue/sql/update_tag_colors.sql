-- 更新博客标签的颜色和图标
UPDATE `blog_tag` SET `color` = '#FF6B6B', `icon` = 'el-icon-coffee-cup' WHERE `id` = 1 AND `name` = 'Java';
UPDATE `blog_tag` SET `color` = '#4ECDC4', `icon` = 'el-icon-leaf' WHERE `id` = 2 AND `name` = 'Spring Boot';
UPDATE `blog_tag` SET `color` = '#45B7D1', `icon` = 'el-icon-monitor' WHERE `id` = 3 AND `name` = 'Vue.js';
UPDATE `blog_tag` SET `color` = '#96CEB4', `icon` = 'el-icon-data-board' WHERE `id` = 4 AND `name` = 'MySQL';
UPDATE `blog_tag` SET `color` = '#FECA57', `icon` = 'el-icon-cloudy' WHERE `id` = 5 AND `name` = 'Redis';
UPDATE `blog_tag` SET `color` = '#FF9FF3', `icon` = 'el-icon-brush' WHERE `id` = 6 AND `name` = '前端开发';
UPDATE `blog_tag` SET `color` = '#54A0FF', `icon` = 'el-icon-cpu' WHERE `id` = 7 AND `name` = '后端开发';
UPDATE `blog_tag` SET `color` = '#5F27CD', `icon` = 'el-icon-database' WHERE `id` = 8 AND `name` = '数据库';
UPDATE `blog_tag` SET `color` = '#00D2D3', `icon` = 'el-icon-reading' WHERE `id` = 9 AND `name` = '框架学习';
UPDATE `blog_tag` SET `color` = '#FF9F43', `icon` = 'el-icon-trophy' WHERE `id` = 10 AND `name` = '项目经验';

-- 为新创建的标签设置默认颜色
UPDATE `blog_tag` SET `color` = '#409EFF', `icon` = 'el-icon-collection-tag' WHERE `color` IS NULL OR `color` = '';