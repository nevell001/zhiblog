-- 创建blog_category表
USE newblog;

-- 如果表已存在则删除
DROP TABLE IF EXISTS blog_category;

-- 创建表
CREATE TABLE blog_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    alias VARCHAR(100) DEFAULT NULL COMMENT '分类别名',
    description VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    article_count INT DEFAULT 0 COMMENT '文章数量',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志：0正常 1删除',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='博客分类表';

-- 创建索引
CREATE INDEX idx_parent_id ON blog_category(parent_id);
CREATE INDEX idx_status ON blog_category(status);
CREATE INDEX idx_sort_order ON blog_category(sort_order);

-- 插入测试数据
INSERT INTO blog_category (name, alias, description, parent_id, sort_order, status)
VALUES 
('技术博客', 'tech', '技术相关文章分类', 0, 1, 1),
('生活随笔', 'life', '生活感悟和随笔', 0, 2, 1),
('前端开发', 'frontend', '前端技术相关内容', 1, 1, 1),
('后端开发', 'backend', '后端技术相关内容', 1, 2, 1),
('数据库', 'database', '数据库相关知识', 1, 3, 1),
('阅读分享', 'reading', '读书笔记和好书推荐', 2, 1, 1);

-- 更新文章数量（如果article表已存在）
UPDATE blog_category SET article_count = 2 WHERE name = '技术博客';

-- 验证表创建
SELECT 'blog_category表创建成功' AS result;
DESCRIBE blog_category;
SELECT COUNT(*) AS category_count FROM blog_category;
SELECT * FROM blog_category;