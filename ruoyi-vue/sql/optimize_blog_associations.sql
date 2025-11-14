-- ========== 博客系统标签分类关联功能优化脚本 ==========
-- 创建时间：2025-11-14
-- 描述：优化博客系统的标签分类关联功能，修复字段映射问题，添加性能优化

USE newblog;

-- ========== 1. 检查并修复数据库表结构 ==========

-- 检查blog_tag表结构是否需要调整
DESCRIBE blog_tag;

-- 确保blog_tag表有正确的字段（如果缺失则添加）
ALTER TABLE blog_tag 
ADD COLUMN IF NOT EXISTS description varchar(255) DEFAULT NULL COMMENT '标签描述' AFTER name,
ADD COLUMN IF NOT EXISTS color varchar(20) DEFAULT '#409EFF' COMMENT '标签颜色' AFTER description,
ADD COLUMN IF NOT EXISTS icon varchar(100) DEFAULT NULL COMMENT '标签图标' AFTER color;

-- 确保blog_category表有正确的字段
ALTER TABLE blog_category 
ADD COLUMN IF NOT EXISTS description varchar(255) DEFAULT NULL COMMENT '分类描述' AFTER name,
ADD COLUMN IF NOT EXISTS parent_id bigint DEFAULT 0 COMMENT '父分类ID，0表示顶级分类' AFTER description,
ADD COLUMN IF NOT EXISTS article_count int DEFAULT 0 COMMENT '文章数量' AFTER sort,
ADD COLUMN IF NOT EXISTS status tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用' AFTER article_count;

-- ========== 2. 优化索引性能 ==========

-- 为blog_article表添加复合索引
CREATE INDEX IF NOT EXISTS idx_article_category_status ON blog_article(category_id, status, del_flag);
CREATE INDEX IF NOT EXISTS idx_article_status_time ON blog_article(status, del_flag, create_time);
CREATE INDEX IF NOT EXISTS idx_article_recommend ON blog_article(is_recommend, status, del_flag);
CREATE INDEX IF NOT EXISTS idx_article_top ON blog_article(is_top, status, del_flag);

-- 为blog_article_tag表添加索引
CREATE INDEX IF NOT EXISTS idx_article_tag_article ON blog_article_tag(article_id);
CREATE INDEX IF NOT EXISTS idx_article_tag_tag ON blog_article_tag(tag_id);

-- 为blog_tag表添加索引
CREATE INDEX IF NOT EXISTS idx_tag_name ON blog_tag(name);
CREATE INDEX IF NOT EXISTS idx_tag_del_flag ON blog_tag(del_flag);

-- 为blog_category表添加索引
CREATE INDEX IF NOT EXISTS idx_category_name ON blog_category(name);
CREATE INDEX IF NOT EXISTS idx_category_parent ON blog_category(parent_id);
CREATE INDEX IF NOT EXISTS idx_category_status ON blog_category(status, del_flag);

-- ========== 3. 添加级联删除触发器 ==========

-- 删除文章时自动删除关联的标签关系
DELIMITER $$
CREATE TRIGGER IF NOT EXISTS tr_delete_article_tags
AFTER UPDATE ON blog_article
FOR EACH ROW
BEGIN
    IF NEW.del_flag = 1 AND OLD.del_flag = 0 THEN
        DELETE FROM blog_article_tag WHERE article_id = NEW.id;
    END IF;
END$$
DELIMITER ;

-- 删除标签时检查是否有关联文章
DELIMITER $$
CREATE TRIGGER IF NOT EXISTS tr_check_tag_articles
BEFORE UPDATE ON blog_tag
FOR EACH ROW
BEGIN
    DECLARE article_count INT DEFAULT 0;
    IF NEW.del_flag = 1 AND OLD.del_flag = 0 THEN
        SELECT COUNT(*) INTO article_count 
        FROM blog_article_tag bat 
        INNER JOIN blog_article ba ON bat.article_id = ba.id 
        WHERE bat.tag_id = NEW.id AND ba.del_flag = 0;
        
        IF article_count > 0 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = '该标签下还有关联文章，无法删除';
        END IF;
    END IF;
END$$
DELIMITER ;

-- 删除分类时检查是否有关联文章
DELIMITER $$
CREATE TRIGGER IF NOT EXISTS tr_check_category_articles
BEFORE UPDATE ON blog_category
FOR EACH ROW
BEGIN
    DECLARE article_count INT DEFAULT 0;
    IF NEW.del_flag = 1 AND OLD.del_flag = 0 THEN
        SELECT COUNT(*) INTO article_count 
        FROM blog_article 
        WHERE category_id = NEW.id AND del_flag = 0;
        
        IF article_count > 0 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = '该分类下还有关联文章，无法删除';
        END IF;
    END IF;
END$$
DELIMITER ;

-- ========== 4. 创建统计更新存储过程 ==========

-- 更新分类文章数量的存储过程
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS UpdateCategoryArticleCount(IN category_id BIGINT)
BEGIN
    UPDATE blog_category 
    SET article_count = (
        SELECT COUNT(*) 
        FROM blog_article 
        WHERE category_id = category_id AND status = 1 AND del_flag = 0
    )
    WHERE id = category_id;
END$$
DELIMITER ;

-- 更新所有分类文章数量的存储过程
DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS UpdateAllCategoryArticleCount()
BEGIN
    UPDATE blog_category bc
    SET article_count = (
        SELECT COUNT(*) 
        FROM blog_article ba 
        WHERE ba.category_id = bc.id AND ba.status = 1 AND ba.del_flag = 0
    )
    WHERE bc.del_flag = 0;
END$$
DELIMITER ;

-- ========== 5. 数据一致性检查和修复 ==========

-- 修复可能存在的数据不一致问题
UPDATE blog_article SET del_flag = 0 WHERE del_flag IS NULL;
UPDATE blog_category SET del_flag = 0 WHERE del_flag IS NULL;
UPDATE blog_tag SET del_flag = 0 WHERE del_flag IS NULL;

-- 清理无效的文章标签关联
DELETE bat FROM blog_article_tag bat
LEFT JOIN blog_article ba ON bat.article_id = ba.id
LEFT JOIN blog_tag bt ON bat.tag_id = bt.id
WHERE ba.id IS NULL OR bt.id IS NULL OR ba.del_flag = 1 OR bt.del_flag = 1;

-- 更新分类文章数量
CALL UpdateAllCategoryArticleCount();

-- ========== 6. 创建视图简化查询 ==========

-- 创建文章详情视图（包含分类和标签信息）
CREATE OR REPLACE VIEW v_article_detail AS
SELECT 
    ba.id,
    ba.title,
    ba.summary,
    ba.content,
    ba.cover_url,
    ba.category_id,
    bc.name as category_name,
    ba.author_id,
    ba.is_top,
    ba.is_recommend,
    ba.status,
    ba.view_count,
    ba.like_count,
    ba.comment_count,
    ba.create_time,
    ba.update_time,
    ba.del_flag,
    GROUP_CONCAT(DISTINCT bt.id) as tag_ids,
    GROUP_CONCAT(DISTINCT bt.name) as tag_names,
    GROUP_CONCAT(DISTINCT bt.color) as tag_colors
FROM blog_article ba
LEFT JOIN blog_category bc ON ba.category_id = bc.id AND bc.del_flag = 0
LEFT JOIN blog_article_tag bat ON ba.id = bat.article_id
LEFT JOIN blog_tag bt ON bat.tag_id = bt.id AND bt.del_flag = 0
WHERE ba.del_flag = 0
GROUP BY ba.id, ba.title, ba.summary, ba.content, ba.cover_url, ba.category_id, 
         bc.name, ba.author_id, ba.is_top, ba.is_recommend, ba.status, 
         ba.view_count, ba.like_count, ba.comment_count, ba.create_time, 
         ba.update_time, ba.del_flag;

-- 创建标签统计视图
CREATE OR REPLACE VIEW v_tag_stats AS
SELECT 
    bt.id,
    bt.name,
    bt.description,
    bt.color,
    bt.icon,
    bt.create_time,
    bt.update_time,
    COUNT(DISTINCT bat.article_id) as article_count,
    COUNT(DISTINCT CASE WHEN ba.status = 1 THEN bat.article_id END) as published_article_count
FROM blog_tag bt
LEFT JOIN blog_article_tag bat ON bt.id = bat.tag_id
LEFT JOIN blog_article ba ON bat.article_id = ba.id AND ba.del_flag = 0
WHERE bt.del_flag = 0
GROUP BY bt.id, bt.name, bt.description, bt.color, bt.icon, bt.create_time, bt.update_time;

-- 创建分类统计视图
CREATE OR REPLACE VIEW v_category_stats AS
SELECT 
    bc.id,
    bc.name,
    bc.description,
    bc.parent_id,
    bc.sort,
    bc.status,
    bc.create_time,
    bc.update_time,
    COUNT(DISTINCT ba.id) as article_count,
    COUNT(DISTINCT CASE WHEN ba.status = 1 THEN ba.id END) as published_article_count
FROM blog_category bc
LEFT JOIN blog_article ba ON bc.id = ba.category_id AND ba.del_flag = 0
WHERE bc.del_flag = 0
GROUP BY bc.id, bc.name, bc.description, bc.parent_id, bc.sort, bc.status, bc.create_time, bc.update_time;

-- ========== 7. 插入测试数据（如果不存在） ==========

-- 确保有基础的分类数据
INSERT IGNORE INTO blog_category (name, description, sort, status, del_flag) VALUES
('技术分享', '技术相关文章分类', 1, 1, 0),
('生活随笔', '生活感悟和随笔', 2, 1, 0),
('学习笔记', '学习过程中的笔记和总结', 3, 1, 0),
('项目实战', '实际项目开发经验分享', 4, 1, 0);

-- 确保有基础的标签数据
INSERT IGNORE INTO blog_tag (name, description, color, del_flag) VALUES
('Java', 'Java编程语言相关', '#FF6B6B', 0),
('Spring Boot', 'Spring Boot框架相关', '#4ECDC4', 0),
('Vue.js', 'Vue.js前端框架相关', '#45B7D1', 0),
('MySQL', 'MySQL数据库相关', '#96CEB4', 0),
('前端开发', '前端开发技术', '#FF9FF3', 0),
('后端开发', '后端开发技术', '#54A0FF', 0);

-- ========== 8. 验证优化结果 ==========

-- 检查表结构
SELECT 'blog_article表结构检查' AS info;
DESCRIBE blog_article;

SELECT 'blog_category表结构检查' AS info;
DESCRIBE blog_category;

SELECT 'blog_tag表结构检查' AS info;
DESCRIBE blog_tag;

SELECT 'blog_article_tag表结构检查' AS info;
DESCRIBE blog_article_tag;

-- 检查索引
SELECT 'blog_article表索引检查' AS info;
SHOW INDEX FROM blog_article;

-- 检查数据统计
SELECT 'blog_category数据统计' AS info, COUNT(*) as count FROM blog_category WHERE del_flag = 0;
SELECT 'blog_tag数据统计' AS info, COUNT(*) as count FROM blog_tag WHERE del_flag = 0;
SELECT 'blog_article数据统计' AS info, COUNT(*) as count FROM blog_article WHERE del_flag = 0;
SELECT 'blog_article_tag关联统计' AS info, COUNT(*) as count FROM blog_article_tag;

-- 检查视图
SELECT 'v_article_detail视图测试' AS info;
SELECT id, title, category_name, tag_names FROM v_article_detail LIMIT 3;

SELECT 'v_tag_stats视图测试' AS info;
SELECT id, name, article_count FROM v_tag_stats LIMIT 5;

SELECT 'v_category_stats视图测试' AS info;
SELECT id, name, article_count FROM v_category_stats LIMIT 5;

SELECT '博客系统标签分类关联功能优化完成！' AS result;