# SQL 脚本目录

## 文件说明

### `00_init_database.sql` （幂等版）
**唯一需要的 SQL 文件**。完整的数据库初始化脚本，包含：
- 所有表结构（系统表 + 博客表 + Quartz 表）
- 所有索引（含 FULLTEXT 全文索引）
- 站内信通知表 `blog_notification`
- 触发器和存储过程
- 示例数据（管理员账号、菜单、字典等）

### 特性

✅ **完全幂等**：可重复执行，不会破坏已有数据
- `CREATE TABLE IF NOT EXISTS` - 表已存在时跳过
- `INSERT IGNORE INTO` - 数据已存在时跳过（依赖主键/唯一键）
- 索引通过 `sp_create_index_if_not_exists` 存储过程检查后创建

✅ **安全的反向兼容**：旧版数据库（v1.3.4 及更早）可直接重跑此脚本以补全缺失的表和索引

## 使用方法

### 新部署（首次安装）

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE zhiblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 导入初始化脚本
mysql -u root -p zhiblog < sql/00_init_database.sql
```

### 已有部署（升级补丁）

如果是从 v1.3.4 或更早版本升级，**直接重跑 00 脚本即可**：

```bash
# 在 Docker 环境
docker exec mysql sh -c 'mysql -uroot -p"$MYSQL_ROOT_PASSWORD" zhiblog < /docker-entrypoint-initdb.d/00_init_database.sql'

# 或本地环境
mysql -u root -p zhiblog < sql/00_init_database.sql
```

这会自动：
- 补建缺失的 `blog_notification` 表（评论通知功能依赖）
- 补建 `ft_article_title_content` 等 FULLTEXT 索引（首页搜索功能依赖）
- 补建其他缺失的表/索引/触发器
- **不会**删除或覆盖任何已有数据

### 在 Docker 中执行

`docker-compose.*.yml` 默认挂载 `./sql:/docker-entrypoint-initdb.d:ro`，**注意**：
- MySQL 容器只在**首次初始化**（数据卷为空）时执行 `docker-entrypoint-initdb.d` 中的脚本
- 已有数据的容器需要手动执行上述命令

## 历史迁移文件（已废弃）

以下文件的功能已合并到 `00_init_database.sql`，**不再需要**：
- ~~`01_fix_fulltext_index.sql`~~（已删除）
- ~~`02_fix_notifications_and_search.sql`~~（已删除）
- ~~`03_add_blog_user_menu_permissions.sql`~~（已删除，博客用户菜单权限已并入 00 脚本）

如果你在 git 历史中看到这些文件，它们是被合并到主初始化脚本中的迁移文件。
