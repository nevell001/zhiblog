# 数据库初始化说明

## 文件结构

- `init_database.sql` - **主初始化文件**，整合了所有SQL文件，包含完整的数据库结构和数据
- `blog_menu_setup.sql` - **博客菜单权限配置**，独立的博客管理菜单和权限SQL脚本
- `fix_blog_schema.sql` - **博客数据库结构修复**，修复字段缺失和SQL兼容性问题
- 其他SQL文件 - 已整合到主文件中，可以删除

## 整合内容

### 1. 若依系统基础表
- 部门表 (sys_dept)
- 用户信息表 (sys_user) 
- 岗位信息表 (sys_post)
- 角色信息表 (sys_role)
- 菜单权限表 (sys_menu)
- 角色菜单关联表 (sys_role_menu)

### 2. Quartz定时任务表
- 完整的Quartz表结构 (QRTZ_*)

### 3. 博客系统表
- 博客文章表 (blog_article)
- 博客分类表 (blog_category)
- 博客标签表 (blog_tag)
- 文章标签关联表 (blog_article_tag)
- 博客评论表 (blog_comment)
- 友情链接表 (blog_friend_link)
- 博客设置表 (blog_setting)

### 4. 初始化数据
- 若依系统基础数据
- 博客系统示例数据
- 博客管理菜单配置 (包含在blog_menu_setup.sql中)
- 管理员权限配置

## Docker Compose集成

Docker Compose配置已更新，MySQL容器会自动执行 `init_database.sql` 文件进行数据库初始化。

## 使用说明

1. 启动Docker Compose：
   ```bash
   docker-compose up -d
   ```

2. 数据库将自动初始化，包含：
   - 完整的若依系统
   - 博客系统表结构
   - 示例数据
   - 菜单和权限配置

3. 访问系统：
   - 后端API: http://localhost:8080
   - 前端界面: http://localhost:3000
   - 管理员账号: admin / admin123

## 注意事项

- 所有SQL文件已整合到 `init_database.sql`
- `blog_menu_setup.sql` 为独立脚本，用于单独配置博客菜单权限
- 数据库字符集统一为 utf8mb4
- 支持Docker环境自动初始化
- 包含完整的博客管理菜单和权限配置

## 博客菜单权限配置

如果需要单独配置博客菜单权限，请执行：

```bash
# 方式1：直接执行SQL文件
mysql -u root -proot newblog < sql/blog_menu_setup.sql

# 方式2：在Docker环境中执行
docker exec -i $(docker ps -q -f name=mysql) mysql -u root -proot newblog < sql/blog_menu_setup.sql
```

### 菜单结构说明

脚本将创建以下菜单权限：

1. **博客管理主菜单** (menu_id: 2000)
2. **6个子菜单**：
   - 文章管理 (2001)
   - 分类管理 (2002)
   - 标签管理 (2003)
   - 评论管理 (2004)
   - 博客设置 (2005)
   - 友链管理 (2006)
3. **22个按钮权限**：每个子菜单包含相应的CRUD权限
4. **管理员角色权限分配**：为超级管理员角色分配所有博客管理权限

### 验证配置

```sql
-- 查看博客菜单
SELECT menu_id, menu_name, parent_id, perms
FROM sys_menu
WHERE menu_id >= 2000 ORDER BY menu_id;

-- 查看角色权限
SELECT rm.role_id, r.role_name, rm.menu_id, m.menu_name
FROM sys_role_menu rm
JOIN sys_role r ON rm.role_id = r.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE rm.menu_id >= 2000;
```

## 博客数据库结构修复

如果遇到字段缺失错误，请执行：

```bash
# 修复blog_tag表字段缺失问题
mysql -u root -proot newblog < sql/fix_blog_schema.sql

# 或者在Docker环境中执行
docker exec -i $(docker ps -q -f name=mysql) mysql -u root -proot newblog < sql/fix_blog_schema.sql
```

### 修复内容说明

脚本将为 `blog_tag` 表添加以下缺失字段：
- `description` varchar(255) - 标签描述
- `color` varchar(20) - 标签颜色（默认 '#409EFF'）
- `icon` varchar(100) - 标签图标

### 验证修复结果

```sql
-- 查看修复后的表结构
DESCRIBE blog_tag;

-- 测试标签云查询是否正常
SELECT bt.id, bt.name, bt.color, COUNT(bat.article_id) as article_count
FROM blog_tag bt
LEFT JOIN blog_article_tag bat ON bt.id = bat.tag_id
LEFT JOIN blog_article ba ON bat.article_id = ba.id AND ba.status = 1 AND ba.del_flag = 0
WHERE bt.del_flag = 0
GROUP BY bt.id, bt.name, bt.color
ORDER BY article_count DESC, bt.create_time DESC;
```