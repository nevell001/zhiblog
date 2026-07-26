-- =====================================================
-- 统一修复脚本 v1.3.6：通知表 + 全文检索索引
-- =====================================================
-- 用途：解决以下问题
--   1. 评论后管理后台收不到通知 → 数据库缺少 blog_notification 表
--   2. 首页搜索报错 "Can't find FULLTEXT index matching the column list"
--
-- 适用场景：
--   - 数据库使用旧版本 00_init_database.sql 初始化
--   - 升级到 v1.3.5+ 后未补建通知表和索引
--
-- 使用方法：
--   mysql -u root -p zhiblog < sql/02_fix_notifications_and_search.sql
--
-- 特性：幂等，可重复执行（多次执行不会有副作用）
-- =====================================================

-- =====================================================
-- 1. 创建站内信通知表（如不存在）
-- =====================================================

CREATE TABLE IF NOT EXISTS `blog_notification` (
    `id`            BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '主键ID',
    `recipient_id`  BIGINT(20)      NOT NULL                    COMMENT '接收用户ID',
    `sender_name`   VARCHAR(64)     NOT NULL                    COMMENT '发送者昵称',
    `type`          VARCHAR(20)     NOT NULL                    COMMENT '通知类型: comment=评论, reply=回复, audit=审核通过, reject=审核拒绝',
    `title`         VARCHAR(255)    DEFAULT ''                  COMMENT '通知标题',
    `content`       VARCHAR(500)    DEFAULT ''                  COMMENT '通知内容(评论摘要)',
    `article_id`    BIGINT(20)      NOT NULL                    COMMENT '关联文章ID',
    `article_title` VARCHAR(255)    DEFAULT ''                  COMMENT '关联文章标题',
    `comment_id`    BIGINT(20)      DEFAULT NULL                COMMENT '关联评论ID',
    `is_read`       INT(1)          DEFAULT 0                   COMMENT '是否已读: 0=未读, 1=已读',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP    COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_recipient_read` (`recipient_id`, `is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内信通知表';

-- =====================================================
-- 2. 幂等创建辅助存储过程
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

-- =====================================================
-- 3. 补建 blog_article 全文检索索引
-- =====================================================

CALL sp_create_index_if_not_exists('blog_article', 'ft_article_title',
    'CREATE FULLTEXT INDEX ft_article_title ON blog_article(title)');

CALL sp_create_index_if_not_exists('blog_article', 'ft_article_content',
    'CREATE FULLTEXT INDEX ft_article_content ON blog_article(content)');

-- 关键索引：解决 "Can't find FULLTEXT index matching the column list" 错误
CALL sp_create_index_if_not_exists('blog_article', 'ft_article_title_content',
    'CREATE FULLTEXT INDEX ft_article_title_content ON blog_article(title, content)');

-- =====================================================
-- 4. 清理辅助存储过程
-- =====================================================

DROP PROCEDURE IF EXISTS sp_create_index_if_not_exists;

-- =====================================================
-- 验证：查询当前库的通知表和全文索引状态
-- =====================================================

SELECT '=== 修复完成状态检查 ===' AS check_step
UNION ALL
SELECT CONCAT('blog_notification 表记录数: ', COUNT(*)) FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'blog_notification'
UNION ALL
SELECT CONCAT('blog_article FULLTEXT 索引数: ', COUNT(*))
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'blog_article' AND INDEX_TYPE = 'FULLTEXT';
