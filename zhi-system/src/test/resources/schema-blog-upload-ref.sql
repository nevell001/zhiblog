-- 媒体库引用检查（BlogUploadRefMapper.countFileReferences）的最小 H2 结构
-- 列取自 sql/00_init_database.sql：
--   blog_article  由共享 schema.sql 建（content / content_md / cover_url / del_flag），这里带上 IF NOT EXISTS 兜底
--   blog_page / blog_setting / blog_friend_link / sys_user 只有本测试需要，脚本内补齐

CREATE TABLE IF NOT EXISTS blog_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT,
    content_md LONGTEXT,
    cover_url VARCHAR(255),
    del_flag CHAR(1) DEFAULT '0'
);

CREATE TABLE IF NOT EXISTS blog_page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content LONGTEXT,
    del_flag TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS blog_setting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(64),
    config_value LONGTEXT
);

CREATE TABLE IF NOT EXISTS blog_friend_link (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logo VARCHAR(255),
    del_flag TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    avatar VARCHAR(100),
    del_flag CHAR(1) DEFAULT '0'
);
