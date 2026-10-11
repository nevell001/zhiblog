-- ============================================================================
-- ZhiBlog v1.4.5 一次性清理：删除前后端均无实现的死菜单权限
--
-- 清理什么（9 条按钮权限行，sys_menu 行 + 对应 sys_role_menu 授权）
--   · blog:articleTag:list / query / add / edit / remove × 5
--       「文章标签关联」的一整套管理端点：前端没有任何 API 或页面引用它，
--       后端 v1.4.5 已把 Controller/Service 删除（此前仅被自身测试调用）。
--       文章-标签关联一直由 BlogArticleServiceImpl 直接操作 BlogArticleTagMapper，
--       从不经过这些端点。
--   · statistics:article:query / statistics:article:export × 2
--   · statistics:user:query / statistics:user:export × 2
--       数据统计页实际使用的是 statistics:article:list/trend/... 与
--       statistics:user:list/activity/...，这 4 个 perms 后端从未实现、
--       前端从未引用。
--
-- 为什么得单独跑
--   sql/00_init_database.sql 的种子已删掉这些行，但存量库里的旧行不会被重跑
--   清理（INSERT IGNORE 只跳过已有行，不删除）。
--
-- 安全性
--   - 按 perms 字符串定位（不硬编码 menu_id），只删这 9 个权限串对应的行；
--   - 先删授权（sys_role_menu join sys_menu）、再删菜单本身，不留悬挂引用；
--   - 幂等：可重复执行，第二次跑 0 行受影响。
--
-- 执行前先备份：
--   mysqldump --single-transaction zhiblog sys_menu sys_role_menu > menus.before.sql
--
-- 用法（脚本刻意不写 CREATE DATABASE / USE，库名由命令行给出，避免连错库）：
--   mysql -u root -p zhiblog < sql/99_cleanup_dead_menus_v1.4.5.sql
--   docker exec -i mysql mysql -uroot -p"$DB_PASSWORD" zhiblog < sql/99_cleanup_dead_menus_v1.4.5.sql
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 0. 自检（只读）：列出将被清理的菜单行与其授权数
-- ---------------------------------------------------------------------------
SELECT m.menu_id, m.menu_name, m.perms,
       (SELECT COUNT(*) FROM sys_role_menu rm WHERE rm.menu_id = m.menu_id) AS 授权数
  FROM sys_menu m
 WHERE m.perms IN ('blog:articleTag:list', 'blog:articleTag:query', 'blog:articleTag:add',
                   'blog:articleTag:edit', 'blog:articleTag:remove',
                   'statistics:article:query', 'statistics:article:export',
                   'statistics:user:query', 'statistics:user:export')
 ORDER BY m.perms;

-- ---------------------------------------------------------------------------
-- 1. 清理授权行（先删引用，覆盖所有角色）
-- ---------------------------------------------------------------------------
DELETE rm FROM sys_role_menu rm
  JOIN sys_menu m ON m.menu_id = rm.menu_id
 WHERE m.perms IN ('blog:articleTag:list', 'blog:articleTag:query', 'blog:articleTag:add',
                   'blog:articleTag:edit', 'blog:articleTag:remove',
                   'statistics:article:query', 'statistics:article:export',
                   'statistics:user:query', 'statistics:user:export');

-- ---------------------------------------------------------------------------
-- 2. 清理菜单行
-- ---------------------------------------------------------------------------
DELETE FROM sys_menu
 WHERE perms IN ('blog:articleTag:list', 'blog:articleTag:query', 'blog:articleTag:add',
                 'blog:articleTag:edit', 'blog:articleTag:remove',
                 'statistics:article:query', 'statistics:article:export',
                 'statistics:user:query', 'statistics:user:export');

-- ---------------------------------------------------------------------------
-- 3. 复核（只读）：两段计数都应为 0
-- ---------------------------------------------------------------------------
SELECT COUNT(*) AS 剩余死权限菜单行
  FROM sys_menu
 WHERE perms IN ('blog:articleTag:list', 'blog:articleTag:query', 'blog:articleTag:add',
                 'blog:articleTag:edit', 'blog:articleTag:remove',
                 'statistics:article:query', 'statistics:article:export',
                 'statistics:user:query', 'statistics:user:export');

SELECT COUNT(*) AS 剩余死权限授权行
  FROM sys_role_menu rm
  JOIN sys_menu m ON m.menu_id = rm.menu_id
 WHERE m.perms IN ('blog:articleTag:list', 'blog:articleTag:query', 'blog:articleTag:add',
                   'blog:articleTag:edit', 'blog:articleTag:remove',
                   'statistics:article:query', 'statistics:article:export',
                   'statistics:user:query', 'statistics:user:export');
