-- ============================================================================
-- ZhiBlog v1.4.3 一次性数据修复：注册博客用户的 user_type
--
-- 修什么
--   v1.4.2 及更早版本里，注册接口（BlogUserService）给用户设置了
--   userType='01'（博客用户），但 SysUserMapper.insertUser 的列清单漏了
--   user_type 列，该字段被静默丢弃，落库落到 DDL 默认值 '00'（系统用户）。
--   后果：注册用户在 /auth/user/info 里被当成系统用户，前台显示「管理后台」
--   入口，个人中心按管理员渲染出「写文章」，点击后跳 /admin/blog/article 404。
--
--   v1.4.3 起 insertUser 会持久化 user_type，新注册用户不再有此问题；
--   本脚本负责修存量库里已经被写成 '00' 的注册用户。
--
-- 为什么得单独跑
--   sql/00_init_database.sql 是幂等初始化脚本，不会修改已有数据行，
--   重跑它无法把已注册用户的 user_type 改回来。
--
-- 安全性
--   - 只改「持有且仅持有 blog_user 角色」的用户：注册流程必配该角色，
--     而系统侧账号（admin/common 等）不会命中，权限语义不受影响；
--   - 已经是 '01' 或角色集合里还有其它角色的账号一律跳过；
--   - 可重复执行：第二次跑 0 行受影响。
--
-- 执行前先备份：
--   mysqldump --single-transaction zhiblog sys_user sys_user_role sys_role > users.before.sql
--
-- 用法（脚本刻意不写 CREATE DATABASE / USE，库名由命令行给出，避免连错库）：
--   mysql -u root -p zhiblog < sql/99_fix_blog_user_type_v1.4.3.sql
--   docker exec -i mysql mysql -uroot -p"$DB_PASSWORD" zhiblog < sql/99_fix_blog_user_type_v1.4.3.sql
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 0. 自检（只读）：当前所有博客角色用户，user_type 非 '01' 的行数就是将被修复的行数
-- ---------------------------------------------------------------------------
SELECT u.user_id, u.user_name, u.nick_name, u.user_type AS 现类型,
       IF(u.user_type = '01', '正常(跳过)', '待修(将被改为 01)') AS 状态,
       u.create_time
  FROM sys_user u
  JOIN sys_user_role ur ON ur.user_id = u.user_id
  JOIN sys_role r ON r.role_id = ur.role_id
 WHERE r.role_key = 'blog_user'
 ORDER BY u.user_id;

-- ---------------------------------------------------------------------------
-- 1. 修复：仅持有 blog_user 角色的注册用户，user_type 统一为 '01'
-- ---------------------------------------------------------------------------
UPDATE sys_user u
  JOIN sys_user_role ur ON ur.user_id = u.user_id
  JOIN sys_role r ON r.role_id = ur.role_id
   SET u.user_type = '01'
 WHERE r.role_key = 'blog_user'
   AND (u.user_type IS NULL OR u.user_type <> '01')
   AND NOT EXISTS (
       SELECT 1
         FROM sys_user_role ur2
         JOIN sys_role r2 ON r2.role_id = ur2.role_id
        WHERE ur2.user_id = u.user_id
          AND r2.role_key <> 'blog_user'
   );

-- ---------------------------------------------------------------------------
-- 2. 复核：博客角色用户的 user_type 应全为 '01'
-- ---------------------------------------------------------------------------
SELECT u.user_id, u.user_name, u.user_type AS 修复后类型,
       GROUP_CONCAT(r.role_key ORDER BY r.role_id SEPARATOR ', ') AS 角色
  FROM sys_user u
  JOIN sys_user_role ur ON ur.user_id = u.user_id
  JOIN sys_role r ON r.role_id = ur.role_id
 GROUP BY u.user_id, u.user_name, u.user_type
 ORDER BY u.user_id;
