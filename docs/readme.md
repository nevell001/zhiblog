# 博客系统（基于 RuoYi）

## 项目简介
本项目基于 [RuoYi](https://gitee.com/y_project/RuoYi) 框架开发，旨在打造一个现代化、可扩展、易维护的博客系统，支持多用户、内容管理、评论互动、权限控制等功能，适合个人或团队使用。

## 技术栈说明
- 后端：RuoYi（Spring Boot + MyBatis + Spring Security）
- 前端：Vue.js + Element UI
- 数据库：MySQL
- 缓存：Redis
- 文件存储：本地/OSS
- 部署：Docker 支持

## 功能列表
1. 用户与权限管理
2. 博客内容管理（文章、分类、标签、草稿、发布、置顶、推荐、归档、搜索）
3. 评论与互动（多级评论、点赞、审核、敏感词过滤）
4. 站点管理（站点信息、友情链接、公告、广告位）
5. 媒体资源管理（图片、附件上传与管理）
6. 日志与监控（操作日志、登录日志、系统监控）
7. 前台展示（首页、文章页、分类页、标签页、归档页、个人中心、搜索、响应式设计）
8. 其他扩展功能（RSS、API、多语言、夜间模式）

## 快速开始
### 1. 克隆项目
```bash
git clone <your_repo_url>
cd new_blog
```
### 2. 安装依赖并启动
- 后端：参考 RuoYi 官方文档进行环境搭建与启动
- 前端：
```bash
cd vue
npm install
npm run dev
```
### 3. 配置数据库与Redis
- 修改 application.yml 配置数据库、Redis 连接信息

### 4. 访问系统
- 后台管理：http://localhost:8080
- 前台博客：http://localhost:8000

## 目录结构说明
```
new_blog/
  ├── docs/                # 项目文档
  ├── ruoyi/               # 后端代码（RuoYi）
  ├── vue/                 # 前端代码（Vue.js）
  └── ...
```

## 参与贡献方式
1. Fork 本仓库
2. 新建分支（feature/xxx）
3. 提交代码并发起 Pull Request
4. 代码需附带注释和文档

## 联系方式
- 作者邮箱：your_email@example.com
- Issues：https://gitee.com/your_project/issues

---

> 更多详细设计请参考 docs 目录下各子文档。