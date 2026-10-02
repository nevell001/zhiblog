# SQL 脚本目录

## 文件说明

### `00_init_database.sql` （幂等版）
数据库初始化/迁移的**唯一入口**（日常升级只需重跑它），包含：
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
- 缺失的列通过 `information_schema.COLUMNS` 检查后 `ALTER TABLE … ADD COLUMN` 补齐

✅ **安全的反向兼容**：旧版数据库（v1.3.4 及更早）可直接重跑此脚本以补全缺失的表和索引

⚠️ **但"幂等"不等于"会修复已有行"**：`INSERT IGNORE` + 按标题判存在只会**跳过**已存在的数据。
需要改动既有数据行的一次性修复，放在 `99_fix_*.sql` 里，见下。

⚠️ **`CREATE TABLE IF NOT EXISTS` 也不等于"会补列"**：表已存在时整段建表语句被跳过，后来新增的列
不会凭空出现。因此**新增列必须同时写两处** —— ①`CREATE TABLE` 里给新装用；②文件后半段的
`SET @has_x := (SELECT COUNT(*) FROM information_schema.COLUMNS …)` + `PREPARE/EXECUTE` 补齐块里
给存量库用（照 `blog_message.create_by` / `blog_friend_link.email` 的写法抄）。
这条规则由 `zhi-system` 的 `MapperColumnSchemaContractTest` 兜底：它比对 mapper 引用的列与脚本里的表结构。

### 案例：留言管理 500（v1.4.2 之后修复）
`blog_message` 初建时漏了 `create_by` / `update_by`，而 `BlogMessageMapper` 从生成那天起就 select 这两列，
于是后台「留言管理」列表、审核、回复三处都抛
`Unknown column 'create_by' in 'field list'`；前台提交留言不写这两列，所以留言板一直是好的 —— 缺陷只在后台第一次打开时暴露。

- **怎么修**：重跑 00 即可（新增的补齐块对缺列的库执行 `ALTER TABLE … ADD COLUMN`，已有列的库跳过），
  **只改库、不需要重建镜像**。
- 只想动这一张表的话，把脚本里 `-- 老库升级：补齐 blog_message` 到
  `DEALLOCATE PREPARE add_message_update_by_stmt;` 那一段单独执行也可以。
- 验证：`SHOW COLUMNS FROM blog_message LIKE '%_by';` 应看到 `reply_by` / `create_by` / `update_by` 三行。

### `99_fix_sample_articles_v1.4.2.sql` （v1.4.2 一次性补丁）
修 **已有安装** 的 6 篇示例文章：正文是 Markdown 却按 HTML 渲染（整页显示 `#` 与代码围栏）、
4 处分类挂错、标签关联错乱并残留幽灵 `tag_id=19`。

- **谁需要跑**：v1.4.2 之前就已初始化过数据库的实例。**新装不需要**（00 脚本里的种子已修正）。
- **为什么不能靠重跑 00**：00 的示例文章受「按标题判存在」保护，重跑直接跳过，坏数据原地留着。
- **安全性**：只按 6 个示例标题定位行；正文只在 `content LIKE '# %'`（仍是坏数据）时重写，
  作者自己编辑过的文章不会被覆盖；标签按标题 + 标签名重建，映射不到就不插；显式钉住 `update_time`。
  可重复执行，第二次跑 0 行受影响。
- **执行前请备份，脚本刻意不写 `CREATE DATABASE` / `USE`**，库名由命令行给出：

```bash
mysql -u root -p zhiblog < sql/99_fix_sample_articles_v1.4.2.sql
# Docker
docker exec -i mysql mysql -uroot -p"$DB_PASSWORD" zhiblog < sql/99_fix_sample_articles_v1.4.2.sql
```

脚本首尾各有一段只读 `SELECT`，跑完对照「正文形态应全为 HTML(正常)、标签数 3/3/4/3/2/1」。

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

但也因为「不会覆盖已有数据」，涉及既有数据行的修复要另外跑 `99_*.sql`：
v1.4.2 的示例文章正文/分类/标签就是这种情况，见上面的
`99_fix_sample_articles_v1.4.2.sql`。

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
