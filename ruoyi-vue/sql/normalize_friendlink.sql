-- normalize_friendlink.sql
-- 目的：仅把 perms 中的 `friendLink` 片段替换为 `friend_link`（dry-run + apply + 回滚）
-- 使用前请先备份数据库（mysqldump）或确认已有备份表。
-- 运行示例（dry-run + apply）：
--   mysql -u root -p newblog < ruoyi-vue/sql/normalize_friendlink.sql

USE newblog;

-- 1) 备份匹配行（覆盖旧备份）
DROP TABLE IF EXISTS backup_sys_menu_perms_friendlink_before_normalize;
CREATE TABLE backup_sys_menu_perms_friendlink_before_normalize AS
SELECT * FROM sys_menu WHERE menu_type='F' AND perms LIKE '%friendLink%';

-- 2) Dry-run：显示将被修改的行及替换后的预期值
SELECT '=== DRY-RUN: proposed replacements ===' AS header;
SELECT menu_id, menu_name, perms AS old_perms, REPLACE(perms, 'friendLink', 'friend_link') AS proposed_perms
FROM sys_menu
WHERE menu_type='F' AND perms LIKE '%friendLink%'
ORDER BY menu_id;
SELECT '=== END DRY-RUN ===' AS footer;

-- 3) Apply：若 dry-run 结果确认无误，取消下面注释并执行或直接运行本脚本（本脚本默认包含执行语句）
START TRANSACTION;

UPDATE sys_menu
SET perms = REPLACE(perms, 'friendLink', 'friend_link')
WHERE menu_type='F' AND perms LIKE '%friendLink%';

COMMIT;

-- 4) 显示应用后的行
SELECT '=== AFTER APPLY ===' AS header2;
SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_type='F' AND perms LIKE '%friend_link%' ORDER BY menu_id;
SELECT '=== END ===' AS footer2;

-- 5) 回滚（如需恢复为备份表）
-- mysql -u root -p -D newblog -e "UPDATE sys_menu m JOIN backup_sys_menu_perms_friendlink_before_normalize b ON m.menu_id=b.menu_id SET m.perms=b.perms;"

-- 说明：
-- - 本脚本针对性强、风险低，仅修改包含 `friendLink` 的 perms。建议先运行 dry-run 部分检查输出后再执行 apply（或在脚本中注释掉 apply 段，手动执行）。
-- - 若需要我同时把这些 perms 分配给管理员（role_id=1），我可以生成一条 INSERT 语句并保存为另一个文件。