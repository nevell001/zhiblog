-- 安全的博客数据库优化脚本
USE newblog;

-- 1. 安全地添加索引（如果不存在）
-- 检查并添加索引的存储过程
DELIMITER $$

CREATE PROCEDURE AddIndexIfNotExists(
    IN table_name VARCHAR(128),
    IN index_name VARCHAR(128),
    IN index_columns VARCHAR(255)
)
BEGIN
    DECLARE index_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO index_exists
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
    AND table_name = table_name
    AND index_name = index_name;
    
    IF index_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE ', table_name, ' ADD INDEX ', index_name, ' (', index_columns, ')');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('Index ', index_name, ' added to table ', table_name) as result;
    ELSE
        SELECT CONCAT('Index ', index_name, ' already exists on table ', table_name) as result;
    END IF;
END$$

DELIMITER ;

-- 调用存储过程添加索引
CALL AddIndexIfNotExists('blog_article', 'idx_status', 'status');
CALL AddIndexIfNotExists('blog_article', 'idx_create_time', 'create_time');
CALL AddIndexIfNotExists('blog_article', 'idx_title', 'title(100)');
CALL AddIndexIfNotExists('blog_article_tag', 'idx_article_id', 'article_id');
CALL AddIndexIfNotExists('blog_article_tag', 'idx_tag_id', 'tag_id');
CALL AddIndexIfNotExists('blog_category', 'idx_name', 'name');
CALL AddIndexIfNotExists('blog_category', 'idx_status', 'status');
CALL AddIndexIfNotExists('blog_tag', 'idx_name', 'name');
CALL AddIndexIfNotExists('blog_tag', 'idx_color', 'color');

-- 删除临时存储过程
DROP PROCEDURE AddIndexIfNotExists;

-- 2. 安全地添加列（如果不存在）
-- 检查并添加列的存储过程
DELIMITER $$

CREATE PROCEDURE AddColumnIfNotExists(
    IN table_name VARCHAR(128),
    IN column_name VARCHAR(128),
    IN column_definition TEXT
)
BEGIN
    DECLARE column_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO column_exists
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name = table_name
    AND column_name = column_name;
    
    IF column_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE ', table_name, ' ADD COLUMN ', column_name, ' ', column_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('Column ', column_name, ' added to table ', table_name) as result;
    ELSE
        SELECT CONCAT('Column ', column_name, ' already exists on table ', table_name) as result;
    END IF;
END$$

DELIMITER ;

-- 添加统计字段
CALL AddColumnIfNotExists('blog_category', 'article_count', 'INT DEFAULT 0 COMMENT \'文章数量\'');
CALL AddColumnIfNotExists('blog_tag', 'article_count', 'INT DEFAULT 0 COMMENT \'文章数量\'');

-- 删除临时存储过程
DROP PROCEDURE AddColumnIfNotExists;

-- 3. 更新统计数据
UPDATE blog_category c 
SET article_count = (
    SELECT COUNT(*) 
    FROM blog_article a 
    WHERE a.category_id = c.id AND a.status = 1
);

UPDATE blog_tag t 
SET article_count = (
    SELECT COUNT(*) 
    FROM blog_article_tag at 
    INNER JOIN blog_article a ON at.article_id = a.id 
    WHERE at.tag_id = t.id AND a.status = 1
);

-- 4. 创建或替换统计视图
DROP VIEW IF EXISTS v_article_stats;

CREATE VIEW v_article_stats AS
SELECT 
    a.id,
    a.title,
    a.summary,
    a.content,
    a.cover_image,
    a.status,
    a.view_count,
    a.like_count,
    a.comment_count,
    a.create_time,
    a.update_time,
    c.id as category_id,
    c.name as category_name,
    c.color as category_color,
    GROUP_CONCAT(t.id) as tag_ids,
    GROUP_CONCAT(t.name) as tag_names,
    GROUP_CONCAT(t.color) as tag_colors
FROM blog_article a
LEFT JOIN blog_category c ON a.category_id = c.id
LEFT JOIN blog_article_tag at ON a.id = at.article_id
LEFT JOIN blog_tag t ON at.tag_id = t.id
GROUP BY a.id, c.id;

-- 5. 安全地创建触发器
-- 删除可能存在的触发器
DROP TRIGGER IF EXISTS tr_article_insert_category_count;
DROP TRIGGER IF EXISTS tr_article_update_category_count;
DROP TRIGGER IF EXISTS tr_article_delete_category_count;
DROP TRIGGER IF EXISTS tr_article_tag_insert_count;
DROP TRIGGER IF EXISTS tr_article_tag_delete_count;

DELIMITER $$

-- 文章插入时更新分类统计
CREATE TRIGGER tr_article_insert_category_count
AFTER INSERT ON blog_article
FOR EACH ROW
BEGIN
    IF NEW.category_id IS NOT NULL AND NEW.status = 1 THEN
        UPDATE blog_category 
        SET article_count = article_count + 1 
        WHERE id = NEW.category_id;
    END IF;
END$$

-- 文章更新时更新分类统计
CREATE TRIGGER tr_article_update_category_count
AFTER UPDATE ON blog_article
FOR EACH ROW
BEGIN
    -- 如果分类发生变化
    IF OLD.category_id != NEW.category_id OR OLD.status != NEW.status THEN
        -- 减少旧分类的计数
        IF OLD.category_id IS NOT NULL AND OLD.status = 1 THEN
            UPDATE blog_category 
            SET article_count = GREATEST(article_count - 1, 0)
            WHERE id = OLD.category_id;
        END IF;
        
        -- 增加新分类的计数
        IF NEW.category_id IS NOT NULL AND NEW.status = 1 THEN
            UPDATE blog_category 
            SET article_count = article_count + 1 
            WHERE id = NEW.category_id;
        END IF;
    END IF;
END$$

-- 文章删除时更新分类统计
CREATE TRIGGER tr_article_delete_category_count
AFTER DELETE ON blog_article
FOR EACH ROW
BEGIN
    IF OLD.category_id IS NOT NULL AND OLD.status = 1 THEN
        UPDATE blog_category 
        SET article_count = GREATEST(article_count - 1, 0)
        WHERE id = OLD.category_id;
    END IF;
END$$

-- 文章标签关联插入时更新标签统计
CREATE TRIGGER tr_article_tag_insert_count
AFTER INSERT ON blog_article_tag
FOR EACH ROW
BEGIN
    DECLARE article_status INT DEFAULT 0;
    
    SELECT status INTO article_status 
    FROM blog_article 
    WHERE id = NEW.article_id;
    
    IF article_status = 1 THEN
        UPDATE blog_tag 
        SET article_count = article_count + 1 
        WHERE id = NEW.tag_id;
    END IF;
END$$

-- 文章标签关联删除时更新标签统计
CREATE TRIGGER tr_article_tag_delete_count
AFTER DELETE ON blog_article_tag
FOR EACH ROW
BEGIN
    DECLARE article_status INT DEFAULT 0;
    
    SELECT status INTO article_status 
    FROM blog_article 
    WHERE id = OLD.article_id;
    
    IF article_status = 1 THEN
        UPDATE blog_tag 
        SET article_count = GREATEST(article_count - 1, 0)
        WHERE id = OLD.tag_id;
    END IF;
END$$

DELIMITER ;

-- 6. 插入一些测试数据（如果表为空）
INSERT IGNORE INTO blog_category (id, name, description, color, sort_order, status, create_time, update_time) VALUES
(1, '技术分享', '技术相关的文章', '#409EFF', 1, 1, NOW(), NOW()),
(2, '生活随笔', '生活感悟和随笔', '#67C23A', 2, 1, NOW(), NOW()),
(3, '学习笔记', '学习过程中的笔记', '#E6A23C', 3, 1, NOW(), NOW());

INSERT IGNORE INTO blog_tag (id, name, description, color, icon, sort_order, status, create_time, update_time) VALUES
(1, 'Vue.js', 'Vue.js相关', '#4FC08D', 'el-icon-cpu', 1, 1, NOW(), NOW()),
(2, 'JavaScript', 'JavaScript相关', '#F7DF1E', 'el-icon-document', 2, 1, NOW(), NOW()),
(3, 'MySQL', 'MySQL数据库相关', '#4479A1', 'el-icon-coin', 3, 1, NOW(), NOW()),
(4, '前端开发', '前端开发相关', '#61DAFB', 'el-icon-monitor', 4, 1, NOW(), NOW());

-- 7. 验证数据完整性
SELECT 'Categories:' as info, COUNT(*) as count FROM blog_category;
SELECT 'Tags:' as info, COUNT(*) as count FROM blog_tag;
SELECT 'Articles:' as info, COUNT(*) as count FROM blog_article;
SELECT 'Article-Tag Relations:' as info, COUNT(*) as count FROM blog_article_tag;

-- 显示完成信息
SELECT 'Database optimization completed successfully!' as result;