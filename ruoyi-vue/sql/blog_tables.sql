-- 博客标签表
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `idx_tag_name` (`tag_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客标签表';

-- 文章标签关联表
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`) USING BTREE,
  KEY `idx_tag_id` (`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- 友情链接表
DROP TABLE IF EXISTS `blog_friend_link`;
CREATE TABLE `blog_friend_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '友链ID',
  `name` varchar(50) NOT NULL COMMENT '网站名称',
  `url` varchar(255) NOT NULL COMMENT '网站链接',
  `logo` varchar(255) DEFAULT NULL COMMENT '网站logo',
  `description` varchar(500) DEFAULT NULL COMMENT '网站描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接表';

-- 博客设置表
DROP TABLE IF EXISTS `blog_setting`;
CREATE TABLE `blog_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `setting_key` varchar(50) NOT NULL COMMENT '设置键',
  `setting_value` text COMMENT '设置值',
  `description` varchar(500) DEFAULT NULL COMMENT '设置描述',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_setting_key` (`setting_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客设置表';

-- 初始化博客设置数据
INSERT INTO `blog_setting` (`setting_key`, `setting_value`, `description`) VALUES
('blog_name', '我的博客', '博客名称'),
('blog_desc', '这是一个基于RuoYi-Vue的博客系统', '博客描述'),
('blog_author', 'nevell', '博客作者'),
('blog_keywords', '博客,RuoYi,Vue,Spring Boot', '博客关键词'),
('blog_copyright', 'Copyright © 2025 nevell', '版权信息'),
('blog_beian', 'ICP备XXXXXXXX号', '备案信息');