# 博客系统（基于 RuoYi-Vue）

## 项目简介
本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 框架开发，致力于打造现代化、可扩展、易维护的博客系统。支持多用户、内容管理、评论互动、权限控制等，适合个人或团队使用。

## 技术栈
- **后端**：Spring Boot、MyBatis、Spring Security（RuoYi 框架）
- **前端**：Vue.js、Element UI
- **数据库**：MySQL
- **缓存**：Redis
- **文件存储**：本地/OSS
- **部署**：Docker 支持

## 主要功能
- 用户与权限管理
- 博客内容管理（文章、分类、标签、草稿、发布、置顶、推荐、归档、搜索）
- 评论互动（多级评论、点赞、审核、敏感词过滤）
- 站点管理（信息、友情链接、公告、广告位）
- 媒体资源管理（图片、附件上传与管理）
- 日志与监控（操作日志、登录日志、系统监控）
- 前台展示（首页、文章页、分类页、标签页、归档页、个人中心、搜索、响应式设计）
- 其他扩展（RSS、API、多语言、夜间模式）

## 快速开始
### 1. 克隆项目
```bash
git clone <your_repo_url>
cd new_blog
```
### 2. 安装依赖并启动
- **后端**：
  ```bash
  cd ruoyi-vue
  mvn clean install -U
  cd ruoyi-admin
  mvn spring-boot:run
  ```
- **前端**：
  ```bash
  cd ruoyi-vue/ruoyi-ui
  npm install
  npm run dev
  ```
### 3. 配置数据库与 Redis
- 修改 `ruoyi-vue/ruoyi-admin/src/main/resources/application.yml` 配置数据库、Redis 连接信息
- 数据库脚本位于 `ruoyi-vue/sql/` 目录

### 4. 访问系统
- 后台管理：http://localhost:8080
- 前台博客：http://localhost:8000
- 默认账号：admin，密码：admin123

### 5. 常见问题
- 依赖下载慢或失败：建议配置国内 Maven 源或科学上网
- 数据库初始化失败：请确认 MySQL 版本与脚本兼容，检查配置
- 其他问题请查阅 [docs/faq.md](faq.md)

## 目录结构
```
new_blog/
  ├── docs/                   # 项目文档（设计、API、变更日志、部署、FAQ等）
  ├── ruoyi-vue/              # 主工程目录（后端+前端）
      ├── ruoyi-admin/        # 后端主服务（Spring Boot，业务逻辑）
      ├── ruoyi-ui/           # 前端管理后台（Vue.js + Element UI）
      ├── ruoyi-generator/    # 代码生成模块（低代码开发）
      ├── ruoyi-quartz/       # 定时任务模块（任务调度）
      ├── ruoyi-system/       # 系统基础模块（用户、权限、组织等）
      ├── ruoyi-framework/    # 框架扩展模块（通用功能、拦截器等）
      ├── ruoyi-common/       # 公共模块（工具类、通用代码）
      ├── sql/                # 数据库脚本
      ├── LICENSE             # 许可证
      ├── README.md           # RuoYi 官方说明
      └── ...                 # 其他模块
```

### 主要模块说明
- **docs/**：
  - `api.md`：后端接口文档
  - `architecture.md`：系统架构设计
  - `changelog.md`：项目变更日志
  - `deploy.md`：部署与运维说明
  - `frontend.md`：前端开发说明
  - `database.md`：数据库设计说明
  - `faq.md`：常见问题解答
- **ruoyi-vue/ruoyi-admin/**：后端主服务，包含所有业务逻辑、接口、权限等
- **ruoyi-vue/ruoyi-ui/**：前端管理后台，基于 Vue.js + Element UI
- **ruoyi-vue/ruoyi-generator/**：代码生成器，支持低代码开发
- **ruoyi-vue/ruoyi-quartz/**：定时任务调度模块
- **ruoyi-vue/ruoyi-system/**：系统基础功能模块
- **ruoyi-vue/ruoyi-framework/**：框架扩展与通用功能
- **ruoyi-vue/ruoyi-common/**：公共工具与通用代码
- **ruoyi-vue/sql/**：数据库初始化与升级脚本

## 参与贡献
1. Fork 本仓库
2. 新建分支（feature/xxx）
3. 提交代码并发起 Pull Request
4. 代码需附带注释和文档，遵循统一代码风格和分支命名规范

## 联系方式
- 作者邮箱：your_email@example.com
- Issues：https://gitee.com/your_project/issues

---

> 更多详细设计请参考 docs 目录下各子文档。