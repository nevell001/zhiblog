-- 创建blog_article表
USE newblog;

-- 如果表已存在则删除
DROP TABLE IF EXISTS blog_article;

-- 创建表
CREATE TABLE blog_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    title VARCHAR(200) NOT NULL COMMENT '文章标题',
    summary VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    content LONGTEXT NOT NULL COMMENT '文章内容',
    cover_url VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID',
    author_id BIGINT DEFAULT NULL COMMENT '作者ID',
    author_name VARCHAR(50) DEFAULT NULL COMMENT '作者名称',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶：0否 1是',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐：0否 1是',
    status TINYINT DEFAULT 0 COMMENT '文章状态：0草稿 1已发布',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志：0正常 1删除',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='博客文章表';

-- 创建索引
CREATE INDEX idx_category_id ON blog_article(category_id);
CREATE INDEX idx_author_id ON blog_article(author_id);
CREATE INDEX idx_status ON blog_article(status);
CREATE INDEX idx_is_top ON blog_article(is_top);
CREATE INDEX idx_create_time ON blog_article(create_time);

-- 插入测试数据
INSERT INTO blog_article (title, summary, content, category_id, author_id, author_name, status)
VALUES 
('欢迎使用博客系统', '这是第一篇测试文章，欢迎使用博客系统！', '<h1>欢迎使用博客系统</h1><p>这是第一篇测试文章，欢迎使用博客系统！</p>', 1, 1, 'admin', 1),
('博客系统功能介绍', '介绍博客系统的主要功能和特点', '<h1>博客系统功能介绍</h1><p>本系统提供文章管理、分类管理、标签管理等功能。</p>', 1, 1, 'admin', 1);

-- 验证表创建
SELECT 'blog_article表创建成功' AS result;
DESCRIBE blog_article;
SELECT COUNT(*) AS article_count FROM blog_article;