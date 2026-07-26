-- =====================================================
-- 修复脚本：补建 blog_article 全文检索索引
-- =====================================================
-- 用途：解决首页文章搜索报错
--       "Can't find FULLTEXT index matching the column list"
--
-- 适用场景：
--   - 数据库使用旧版本 00_init_database.sql 初始化
--   - 部署时间早于 v1.3.5
--   - blog_article 表缺少 (title, content) 复合 FULLTEXT 索引
--
-- 使用方法：
--   mysql -u root -p zhiblog < sql/01_fix_fulltext_index.sql
--
-- 特性：幂等，可重复执行
-- =====================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_create_index_if_not_exists$$
CREATE PROCEDURE sp_create_index_if_not_exists(
    IN table_name_param VARCHAR(64),
    IN index_name_param VARCHAR(64),
    IN create_sql_param TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.STATISTICS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = table_name_param
          AND INDEX_NAME = index_name_param
    ) THEN
        SET @create_index_sql = create_sql_param;
        PREPARE create_index_stmt FROM @create_index_sql;
        EXECUTE create_index_stmt;
        DEALLOCATE PREPARE create_index_stmt;
        SELECT CONCAT('已创建索引: ', index_name_param, ' on ', table_name_param) AS result;
    ELSE
        SELECT CONCAT('索引已存在，跳过: ', index_name_param) AS result;
    END IF;
END$$

DELIMITER ;

-- 文章标题全文索引（用于单独搜索标题）
CALL sp_create_index_if_not_exists('blog_article', 'ft_article_title',
    'CREATE FULLTEXT INDEX ft_article_title ON blog_article(title)');

-- 文章内容全文索引（用于单独搜索内容）
CALL sp_create_index_if_not_exists('blog_article', 'ft_article_content',
    'CREATE FULLTEXT INDEX ft_article_content ON blog_article(content)');

-- 文章标题+内容复合全文索引（用于首页综合搜索）
-- 这是修复 "Can't find FULLTEXT index matching the column list" 错误的关键索引
CALL sp_create_index_if_not_exists('blog_article', 'ft_article_title_content',
    'CREATE FULLTEXT INDEX ft_article_title_content ON blog_article(title, content)');

DROP PROCEDURE IF EXISTS sp_create_index_if_not_exists;
