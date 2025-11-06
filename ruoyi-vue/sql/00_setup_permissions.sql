-- 设置MySQL权限和连接允许
-- 这个文件会被MySQL容器自动执行

-- MySQL 8.4+ 版本需要先创建用户再授权
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;