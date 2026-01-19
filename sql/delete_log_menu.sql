-- 删除多余的"日志管理"菜单（menu_id=4）
-- 因为登录日志和操作日志已经直接放在"系统监控"下面了

DELETE FROM sys_menu WHERE menu_id = 4;