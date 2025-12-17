-- =====================================================
-- 博客系统性能优化索引脚本
-- 创建日期：2025-12-03
-- 优化目标：提升查询性能，支持高并发访问
-- =====================================================

-- 1. 博客文章表索引优化
-- -------------------------------------------

-- 文章主键索引（已存在，无需重复创建）
-- ALTER TABLE blog_article ADD PRIMARY KEY (id);

-- 文章状态和删除标志复合索引（用于列表查询）
CREATE INDEX IF NOT EXISTS idx_article_status_del_flag ON blog_article(status, del_flag);

-- 文章分类ID索引（用于按分类查询）
CREATE INDEX IF NOT EXISTS idx_article_category_id ON blog_article(category_id);

-- 文章创建时间索引（用于排序和归档）
CREATE INDEX IF NOT EXISTS idx_article_create_time ON blog_article(create_time);

-- 文章浏览量索引（用于热门文章排序）
CREATE INDEX IF NOT EXISTS idx_article_view_count ON blog_article(view_count DESC);

-- 文章置顶和推荐状态复合索引（用于首页展示）
CREATE INDEX IF NOT EXISTS idx_article_is_top_recommend ON blog_article(is_top DESC, is_recommend DESC, status DESC, del_flag);

-- 作者ID索引（用于用户文章查询）
CREATE INDEX IF NOT EXISTS idx_article_author_id ON blog_article(author_id);

-- 文章标题全文索引（用于搜索功能）
CREATE FULLTEXT INDEX IF NOT EXISTS ft_article_title ON blog_article(title);
CREATE FULLTEXT INDEX IF NOT EXISTS ft_article_content ON blog_article(content);
CREATE FULLTEXT INDEX IF NOT EXISTS ft_article_title_content ON blog_article(title, content);

-- 2. 博客分类表索引优化
-- -------------------------------------------

-- 分类主键索引（已存在）
-- ALTER TABLE blog_category ADD PRIMARY KEY (id);

-- 分类删除标志索引
CREATE INDEX IF NOT EXISTS idx_category_del_flag ON blog_category(del_flag);

-- 分类排序索引
CREATE INDEX IF NOT EXISTS idx_category_order_num ON blog_category(order_num);

-- 3. 博客标签表索引优化
-- -------------------------------------------

-- 标签主键索引（已存在）
-- ALTER TABLE blog_tag ADD PRIMARY KEY (id);

-- 标签删除标志索引
CREATE INDEX IF NOT EXISTS idx_tag_del_flag ON blog_tag(del_flag);

-- 标签使用次数索引（用于热门标签）
CREATE INDEX IF NOT EXISTS idx_tag_article_count ON blog_tag(article_count DESC);

-- 4. 文章标签关联表索引优化
-- -------------------------------------------

-- 文章ID索引（用于查询文章的所有标签）
CREATE INDEX IF NOT EXISTS idx_article_tag_article_id ON blog_article_tag(article_id);

-- 标签ID索引（用于查询标签的所有文章）
CREATE INDEX IF NOT EXISTS idx_article_tag_tag_id ON blog_article_tag(tag_id);

-- 复合唯一索引（防止重复关联）
CREATE UNIQUE INDEX IF NOT EXISTS uk_article_tag_unique ON blog_article_tag(article_id, tag_id);

-- 5. 博客评论表索引优化
-- -------------------------------------------

-- 评论主键索引（已存在）
-- ALTER TABLE blog_comment ADD PRIMARY KEY (id);

-- 文章ID索引（用于查询文章的评论）
CREATE INDEX IF NOT EXISTS idx_comment_article_id ON blog_comment(article_id);

-- 评论状态索引（用于查询已发布评论）
CREATE INDEX IF NOT EXISTS idx_comment_status ON blog_comment(status);

-- 评论创建时间索引（用于排序）
CREATE INDEX IF NOT EXISTS idx_comment_create_time ON blog_comment(create_time DESC);

-- 复合索引（用于前台展示评论）
CREATE INDEX IF NOT EXISTS idx_comment_article_status_time ON blog_comment(article_id, status, create_time DESC);

-- 6. 博客设置表索引优化
-- -------------------------------------------

-- 设置键唯一索引（已存在）
-- ALTER TABLE blog_setting ADD UNIQUE KEY uk_config_key (config_key);

-- 更新时间索引（用于缓存失效判断）
CREATE INDEX IF NOT EXISTS idx_setting_update_time ON blog_setting(update_time);

-- 7. 友情链接表索引优化
-- -------------------------------------------

-- 链接主键索引（已存在）
-- ALTER TABLE blog_friend_link ADD PRIMARY KEY (id);

-- 链接状态索引（用于查询有效链接）
CREATE INDEX IF NOT EXISTS idx_friend_link_status ON blog_friend_link(status);

-- 链接排序索引
CREATE INDEX IF NOT EXISTS idx_friend_link_order_num ON blog_friend_link(order_num DESC);

-- 8. 性能监控视图
-- -------------------------------------------

-- 创建慢查询监控视图
CREATE OR REPLACE VIEW v_slow_queries AS
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    (DATA_LENGTH + INDEX_LENGTH) as TOTAL_SIZE
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME LIKE 'blog_%'
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;

-- 创建索引使用情况视图
CREATE OR REPLACE VIEW v_index_usage AS
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    CARDINALITY,
    SUB_PART,
    NULLABLE,
    INDEX_TYPE
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME LIKE 'blog_%'
ORDER BY TABLE_NAME, INDEX_NAME;

-- =====================================================
-- 索引优化建议和说明
-- =====================================================

/*
1. 索引命名规范：
   - idx_前缀：普通索引
   - uk_前缀：唯一索引
   - ft_前缀：全文索引

2. 复合索引顺序：
   - 区分度高的字段放在前面
   - 常用查询条件的字段放在前面

3. 全文索引：
   - 用于提升搜索性能
   - 需要InnoDB引擎和MySQL 5.6+

4. 定期维护：
   - 定期分析表统计信息：ANALYZE TABLE blog_article;
   - 定期优化表：OPTIMIZE TABLE blog_article;

5. 监控索引使用情况：
   - 查看索引使用：SELECT * FROM v_index_usage;
   - 查看表大小：SELECT * FROM v_slow_queries;

6. 注意事项：
   - 索引会降低写入性能，但大幅提升查询性能
   - 合理控制索引数量，避免过度索引
   - 定期检查索引的实际使用情况
*/

-- 执行统计信息更新
ANALYZE TABLE blog_article;
ANALYZE TABLE blog_category;
ANALYZE TABLE blog_tag;
ANALYZE TABLE blog_article_tag;
ANALYZE TABLE blog_comment;
ANALYZE TABLE blog_setting;
ANALYZE TABLE blog_friend_link;