-- ============================================================================
-- ZhiBlog v1.4.5 一次性数据迁移：把旧「关于内容」设置搬到「页面管理」
--
-- 迁移什么
--   v1.4.5 起关于页改由「页面管理」中 slug='about' 的页面行驱动：
--   /blog/about 仅渲染该行（无其它兜底数据源），导航「关于」入口也由该行的
--   「导航显示」开关控制；此前的 blog_setting.about_content 设置键全链路退役，
--   前后端均不再读取。
--
--   后果：存量站点如果自定义过 about_content，重跑 00_init_database.sql 只会
--   INSERT IGNORE 插入一条演示版「关于」页（slug 已被他因占用时会跳过），
--   自定义内容不会自动继承；不重跑则关于页空白、导航入口消失。本脚本负责把
--   旧 about_content 的内容搬进 blog_page。
--
-- 为什么得单独跑
--   sql/00_init_database.sql 幂等但不迁移/覆盖已有数据行，
--   blog_setting.about_content 的值不会自动出现在 blog_page 里。
--
-- 安全性
--   - 只做「插入缺失行」：仅当 blog_page 中不存在 slug='about'（含逻辑删除，
--     删除即删除，与 00 脚本的 INSERT IGNORE 语义一致）时才插入，
--     不覆盖站长在页面管理里的任何现有编辑；
--   - 插入行取 show_in_nav='1'：升级前导航固定带「关于」入口，迁移后保持入口的
--     既有行为（00 脚本的演示行默认 '0'，那是新装的选择，与此无关）；
--   - 旧值缺失或为空白时不插入任何行（可重跑 00 脚本获取演示内容）；
--   - 可重复执行：第二次跑 0 行受影响。
--
-- 执行前先备份：
--   mysqldump --single-transaction zhiblog blog_setting blog_page > about.before.sql
--
-- 用法（脚本刻意不写 CREATE DATABASE / USE，库名由命令行给出，避免连错库）：
--   mysql -u root -p zhiblog < sql/99_fix_about_page_v1.4.5.sql
--   docker exec -i mysql mysql -uroot -p"$DB_PASSWORD" zhiblog < sql/99_fix_about_page_v1.4.5.sql
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 0. 自检（只读）：旧设置内容预览 + 目标行是否已存在
-- ---------------------------------------------------------------------------
SELECT config_key, LEFT(config_value, 80) AS 旧内容预览, CHAR_LENGTH(config_value) AS 长度
  FROM blog_setting WHERE config_key = 'about_content';

SELECT id, title, slug, status, show_in_nav, del_flag, update_time
  FROM blog_page WHERE slug = 'about';

-- ---------------------------------------------------------------------------
-- 1. 迁移：仅当目标行不存在且旧内容非空时，插入 slug='about' 页面行
-- ---------------------------------------------------------------------------
SET @about_page_exists := (SELECT COUNT(*) FROM blog_page WHERE slug = 'about');
SET @legacy_about := (SELECT config_value FROM blog_setting WHERE config_key = 'about_content' LIMIT 1);

INSERT INTO blog_page (title, slug, summary, content, status, show_in_nav, sort, create_by, create_time, update_time)
SELECT '关于', 'about', '', @legacy_about, '1', '1', 0, 'admin', NOW(), NOW()
  FROM DUAL
 WHERE @about_page_exists = 0
   AND @legacy_about IS NOT NULL
   AND TRIM(@legacy_about) <> '';

-- ---------------------------------------------------------------------------
-- 2. 复核：关于页行应存在，内容长度与自检里的旧内容一致
--    （若无行返回：说明旧内容为空未插入，可重跑 00_init_database.sql 获取演示内容）
-- ---------------------------------------------------------------------------
SELECT id, title, slug, status, show_in_nav, del_flag,
       CHAR_LENGTH(content) AS 内容长度, update_time
  FROM blog_page WHERE slug = 'about';
