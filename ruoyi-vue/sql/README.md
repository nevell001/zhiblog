# 数据库初始化说明

## 文件结构

- `init_database.sql` - **主初始化文件**，整合了所有SQL文件，包含完整的数据库结构和数据
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
- 博客管理菜单配置
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
- 数据库字符集统一为 utf8mb4
- 支持Docker环境自动初始化
- 包含完整的博客管理菜单和权限配置