# ZhiBlog - 知博

现代化的前后端分离博客系统，采用 Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript 技术栈构建。

## 🚀 项目特色

- 🏗️ **企业级架构**：基于成熟的技术框架构建
- 📱 **前后端分离**：Vue 3 + Spring Boot，TypeScript 类型安全
- ✍️ **内容创作**：富文本（Quill）与 Markdown 双编辑器，文章定时发布
- 🔍 **SEO 友好**：每页动态 title/description/keywords/canonical，内置 `/sitemap.xml` 与 `/robots.txt`
- 💬 **读者互动**：评论（游客/登录 + 审核）、点赞/取消（文章与评论，按用户去重）、收藏、站内通知 + 邮件通知
- 📊 **统计完善**：按访客去重的浏览量、按日 PV/UV 聚合曲线、后台多维统计
- 🗂️ **媒体管理**：上传自动入库形成媒体库，支持检索与删除（联动物理文件）
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制（博客管理/媒体/友链审核等按角色分配）
- 🐳 **容器化部署**：Docker Compose 管理前端、后端、MySQL、Redis 与监控服务
- 📈 **监控完善**：Prometheus + Grafana + Actuator（含 PV/UV 基础统计）
- ✅ **测试完善**：后端 JUnit + JaCoCo（60% 门槛）、前端 Vitest（覆盖率门槛），覆盖工具/接口/页面
- 🔄 **版本管理**：统一的版本号管理机制，确保版本一致性
- 🛡️ **增强安全**：JWT + 图形验证码 + 邮箱验证码防爆破、IP 限流、XSS 消毒、防盗链白名单后台化、全局错误保护

## 📦 最近更新 (v1.4.0)

### 内容创作

- **定时发布**：文章支持 `publish_time` + 状态流转，到期自动发布
- **Markdown 文章**：`format`(html|markdown) + Markdown 源码入库，服务端 flexmark 渲染 HTML 双写（GFM 表格），与前端 `marked` 预览一致
- **自定义页面**：后台单页管理（标题/别名/Markdown 内容/SEO/导航显示/发布状态），
  已发布页面按 `/blog/page/<别名>` 渲染，可配置是否出现在前台导航
- 管理端编辑器支持富文本/Markdown 切换与实时预览

### 读者互动与前台体验

- **评论自洽**：游客可直接发表评论/回复（昵称/邮箱字段生效），登录用户走账号；保留审核、IP 限流
- **点赞体系**：文章/评论点赞支持取消与按用户去重（新表记录），并回显点赞态
- **留言板**：独立于文章的留言页 `/blog/guestbook`，匿名可提交（IP 限流 + 长度校验），
  是否待审复用评论审核开关（`comment_review`）；后台可审核/回复/删除/导出（回复即发布）
- **收藏**：支持收藏/取消并新增“我的收藏”列表页
- **前台通知铃铛**：未读数轮询、最近通知、已读跳转（BlogLayout）
- **移动端导航**：≤768px 汉堡菜单 + 下拉导航
- **独立搜索页** `/blog/search`，并让 `search_enabled` / `sidebar_enabled` / `footer_enabled` / `copyright_enabled` 真正生效
- **分类/标签总览页**、**作者主页**，文章标签/作者名可点击跳转
- 找回密码补图形验证码，注册/找回统一 BlogLayout 认证布局

### SEO / 站点能力

- 前台动态 title/description/keywords/canonical/OG（管理端新增 **SEO优化** 页签）
- 后端 `/sitemap.xml`（文章+静态页）与 `/robots.txt`
- 站点信息支持“站点访问地址”，防盗链域名白名单与开关可后台管理（数据库优先）

### 后台与统计

- **媒体库**：上传自动记录（含头像/富文本/封面/水印/移动端/图片处理等），管理列表 + 删除联动物理文件
- **友链申请与审核**：前台匿名申请（限流）→ 后台待审核/通过/拒绝，展示申请人邮箱
- **每日 PV/UV 聚合**：按访客（IP/用户）去重计数，定时汇总，文章统计页新增近 30 天曲线
- 统计页移除假数据兜底（接口失败显示错误/空态）
- 文章**评论数与真实评论联动**（发布/审核/编辑/删除路径同步），并提供历史一次性重算

### 工程与质量

- 删除死代码与孤儿依赖（BlogNav/BlogFooter、TinyMCE 整栈、旧 views/system 页面、无引用组件等）
- 评论/分类/标签导出按钮指向真实接口；修复若干统计假数据
- 各模块单测与守卫测试同步增强

## 🌐 访问方式

- **博客前台**: http://localhost:3000/blog
- **管理后台**: http://localhost:3000/admin
- **API文档**: http://localhost:8080/swagger-ui.html
- **Prometheus监控**: http://localhost:9090
- **Grafana可视化**: http://localhost:3001
- **默认账号**: admin（首次登录请修改默认密码）

## 🚀 快速开始

### 环境要求

- **Java**: JDK 17+
- **Node.js**: 20.19+（Vite 7 要求；22.12+ 亦可）
- **MySQL**: 8.4+
- **Redis**: 6.2+
- **Maven**: 3.6+
- **Docker**: Docker Engine / Docker Desktop / Colima（推荐用于完整本地环境）

### 1. 克隆项目

```bash
git clone https://gitee.com/nevell/zhiblog.git
cd ZhiBlog
```

### 2. 推荐启动方式：Docker Compose

复制环境变量示例文件并检查关键配置：

```bash
cp .env.example .env
```

开发环境默认数据库名应保持为：

```env
DB_NAME=zhiblog
```

启动完整开发环境：

```bash
docker compose -f docker-compose.dev.yml up -d --build
```

查看服务状态和后端日志：

```bash
docker compose -f docker-compose.dev.yml ps
docker compose -f docker-compose.dev.yml logs -f zhi-admin
```

生产或接近生产环境使用：

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

> 项目未提供默认 `docker-compose.yml`，请明确选择 `docker-compose.dev.yml` 或 `docker-compose.prod.yml`。

### 3. 手动开发启动（可选）

如果不使用 Docker，需要自行启动 MySQL 和 Redis，并初始化数据库：

```bash
mysql -u root -p
CREATE DATABASE zhiblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
mysql -u root -p zhiblog < sql/00_init_database.sql
```

后端启动：

```bash
# 1) 从根目录安装各模块（zhi-system 等以 jar 形式被 zhi-admin 依赖，
#    改了 zhi-system 后必须重新 install，否则 zhi-admin 用的是 ~/.m2 里的旧包）
mvn clean install -DskipTests

# 2) Spring Boot 不会读取 .env（.env 只被 docker compose 使用），
#    在宿主机跑必须先把变量导入当前 shell：
set -a; source .env; set +a

# 3) 启动（DB_HOST/DB_PORT/DB_USERNAME/DB_PASSWORD 均来自上面的环境变量）
mvn -pl zhi-admin spring-boot:run
```

> 常见坑：直接 `mvn spring-boot:run` 而不导入 `.env` 时，`DB_USERNAME` 会回落到
> `application.yml` 的默认值 `root`、密码为空，于是连到宿主机 `3306` 上的任意 MySQL
> 都会报 `Access denied for user 'root'@...`。若本机 3306 已被其它服务占用，
> 请把 `docker-compose.dev.yml` 中 mysql 的端口映射改为 `3307:3306`，
> 并设置 `DB_PORT=3307`（JDBC URL 已支持 `DB_PORT`）。

前端启动：

```bash
cd zhi-ui
npm install
npm run dev
```

### 4. 升级已有数据库（重要）

本仓库所有表/列/索引/菜单/角色权限变更都收敛在唯一脚本 `sql/00_init_database.sql` 中且**幂等**。**已有数据库升级时请重跑该脚本**：

```bash
mysql -u root -p zhiblog < sql/00_init_database.sql
```

重跑会自动补齐（无需手工建表/加列）：

- `blog_upload`（媒体库）、`blog_article_like` / `blog_comment_like`（点赞）、`blog_article_daily_stats`（每日 PV/UV）、`blog_bookmark`/`blog_notification`、`blog_message`（留言板）、`blog_page`（自定义页面）等表
- `blog_comment.like_count`、`blog_article.publish_time/format/content_md`、`blog_friend_link.email` 等列
- 新菜单与按钮权限（媒体管理、留言管理、页面管理等，含角色 1/2 分配）、`email_notify_enabled` 等设置种子
- **评论数一次性重算**（按已发布评论重写 `comment_count`）

> 若数据库跑在 Docker 里且数据卷已存在，`docker-entrypoint-initdb.d` **不会**再自动执行脚本，
> 必须按上面的命令手动重跑一次 `sql/00_init_database.sql`。
>
> 部署提示：`/sitemap.xml`、`/robots.txt` 位于后端根路径，若使用 Nginx 等对前端做同源反代，请将这两个路径转发到后端；PV/UV 曲线依赖定时任务每小时汇总（当天数据约 1 小时延迟）。

## 📁 项目结构

```
ZhiBlog/
├── zhi-admin/          # 后端主模块（启动入口 + 前台/个人中心控制器 + 定时任务）
├── zhi-system/         # 业务逻辑（Blog 管理控制器、Mapper、媒体/统计等）
├── zhi-framework/      # 框架核心（Security/JWT/过滤器）
├── zhi-common/         # 通用工具（含 Markdown 渲染、图片压缩、XSS）
├── zhi-quartz/         # Quartz 任务
├── zhi-generator/      # 代码生成
├── zhi-ui/             # Vue3 + TS 前端
│   ├── src/api/          # 请求封装与类型
│   ├── src/views/blog/   # 博客前台
│   ├── src/views/admin/  # 管理后台
│   └── ...
├── sql/                  # 数据库脚本（唯一初始化/迁移入口）
├── docs/                 # 项目文档
├── prometheus/           # Prometheus 配置
├── docker-compose.dev.yml
└── docker-compose.prod.yml
```

## 🎯 核心功能

### 前台展示

- 首页/文章详情/分类/标签/归档/关于/作者主页/独立搜索页/**留言板**/**自定义页面**
- 分类与标签**总览页**，标签与作者名可点击
- 文章**定时发布**；支持**富文本**与 **Markdown** 内容
- 文章目录（TOC）、相关/热门文章、RSS 订阅
- 评论（游客/登录、回复、审核）、文章/评论**点赞**、收藏
- 站内通知铃铛、个人中心、移动端汉堡导航
- 主题（浅色/深色/跟随系统 + mo-blog 主题）、功能开关生效

### 后台管理

- 文章管理（含草稿/发布/定时发布、Markdown 与富文本、置顶/推荐）
- 分类 / 标签 / 评论（审核、导出）/ 友链（申请审核）/ 博客设置（站点信息 + 功能开关 + 防盗链域名 + **SEO优化**）
- **留言管理**（审核/回复/删除/导出）、**页面管理**（自定义页面的增删改查与发布/下架）
- **媒体库**（上传记录检索、删除联动文件）
- 用户 / 角色权限 / 系统 / 日志 / 定时任务 / 代码生成

### 系统与统计

- 统一认证（JWT）、注册与邮箱找回密码（图形验证码 + 邮箱码防爆破）
- 图片压缩/缩放/水印上传、上传自动入库
- 防盗链（Referer 白名单，数据库后台可管理）、XSS 消毒
- 邮件通知（评论/回复/审核结果，可开关）+ 过期验证码定时清理
- 浏览量按访客 24h 去重；**每日 PV/UV** 聚合与曲线
- `/sitemap.xml` + `/robots.txt` + 前台动态 SEO meta
- 监控（Actuator/Prometheus/Grafana）、全局错误保护

## 🏗️ 技术栈

### 后端

- Spring Boot 3.3.0 / Java 17
- MyBatis + PageHelper / MySQL 8.4
- Redis 6.2+（浏览量缓冲、去重、限流、验证码）
- Spring Security 6 + JWT
- flexmark 0.42（Markdown→HTML 服务端渲染）
- Thumbnailator 0.4.20（图片压缩处理）
- Quartz 定时任务 / Prometheus + Actuator

### 前端

- Vue 3.5 / TypeScript 5.9 / Vite 7
- Element Plus 2.13 / Pinia / Vue Router
- Quill（富文本）与 marked（Markdown 预览）
- DOMPurify（内容消毒）、ECharts 6（统计图表）
- Vitest（单测 + 覆盖率门槛）、ESLint + Prettier

## 🔧 配置说明

### 环境变量（.env，模板见 .env.example）

```bash
# 数据库
DB_HOST=localhost
DB_USERNAME=zhiblog_app
DB_PASSWORD={your_app_password}
DB_ROOT_PASSWORD={your_root_password}
DB_NAME=zhiblog

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD={your_password}

# JWT（≥64 字符）
R_TOKEN_SECRET={your_secret_key}

# Druid / 安全校验 / 邮件 / 验证码 / OSS 等请查看 .env.example
```

安全要点：

- `R_TOKEN_SECRET` 建议 `openssl rand -base64 64` 生成，长度 ≥64
- 生产环境缺失关键密钥时 `SecurityConfigValidator` 会阻止启动；开发环境可用 `SECURITY_VALIDATION_ENABLED=false` 跳过
- 防盗链开关/白名单（`referer_*`）与邮件通知开关（`email_notify_enabled`）**默认取 .env/yml，保存到后台设置后以数据库为准**

### 图片压缩配置（application.yml `image.compress`）

```yaml
image:
  compress:
    enabled: true
    threshold-size: 2MB
    max-width: 2560
    max-height: 1440
    default-quality: 0.9
    avatar-size: 200
    thumbnail-size: 400
```

上传后自动写入媒体库；上传端点：`/common/upload[/compressed|avatar|thumbnail|article-cover|mobile|watermark]` 与 `/common/uploads`、用户头像 `/system/user/profile/avatar`、图片处理 `/system/image/*`。

## 📋 开发规范

### Git 提交规范

```
feat: 新功能      fix: 修复bug
docs: 文档更新    style: 代码格式
refactor: 重构    test: 测试
chore: 构建/工具
```

### 代码规范

- **Java**: 遵循阿里巴巴 Java 开发手册，Checkstyle 检查在 `validate` 阶段
- **Vue/TS**: 遵循官方风格指南；`vue-tsc` 严格类型检查；ESLint + Prettier
- 后台覆盖率门槛：JaCoCo 60%（行/分支）；Vitest lines/statements ≥70、functions ≥75、branches ≥55

### 版本管理

- 版本号统一在根 `pom.xml` 的 `<version>` 与 `<app.version>` 定义，修改时需同步 6 个子模块 parent `<version>`
- 前端版本（package.json）独立管理
- 详见 [版本管理指南](docs/VERSION_MANAGEMENT.md)

## 📦 版本历史

### v1.4.0 (2026-09)

- 收藏列表页、前台通知铃铛、移动端汉堡导航
- 点赞体系完整化（文章+评论、去重/取消）、独立搜索页与功能开关生效
- 定时发布、Markdown 文章（源码+HTML 双写、GFM 表格）
- SEO 闭环（动态 meta、sitemap、robots、后台 SEO 设置）
- 评论游客直发/回复、邮件通知、友链申请审核、媒体库全覆盖
- 每日 PV/UV 聚合、浏览/点赞去重、评论数与真实评论联动
- 留言板（匿名提交/审核/回复）与自定义页面（Markdown + SEO + 导航接入）
- 死代码/孤儿依赖清理、统计假数据与失效导出修复、认证页一致性
- 修复迁移脚本非幂等问题（分类/友链重跑重复插入）与若干失效路由/权限

### v1.3.6 (2026-07-30)

- 主题系统优化：完善跟随系统模式的图标显示和提示
- API 类型安全：添加 TypeScript 接口定义，替换 any 类型
- 搜索性能优化：实现 500ms 防抖
- 错误处理改进：优化空 catch 块、条件日志
- 环境适配：`getApiBaseUrl()` 动态获取 API 地址
- UI 样式修复：深色模式下博客设置页面边框显示问题

### v1.3.5 (2026-07-26)

- 修复三个用户注册功能缺陷（开关绕过、角色缺失静默失败、协议复选框装饰性）
- 合并 3 个 SQL 初始化脚本为单文件 `00_init_database.sql`
- 修复 `.husky/pre-commit` 过时路径引用；清理残余引用

### v1.3.4 (2026-07-18)

- 统一品牌名称为 "ZhiBlog - 知博"，默 Blog 作为主题名称
- 修复版本号不一致问题；优化代码质量与文档；TypeScript 类型完善

### v1.3.3 (2026-01-27)

- 统一 Docker 开发环境数据库默认库名 `zhiblog`
- 精简默认 Compose 文件，保留 dev/prod 两套入口；修复 UUID 测试偶发失败

### v1.3.2 (2026-01-27)

- 修复登录后需刷新才显示状态、首页误跳登录等问题
- 建立统一版本号管理机制与全局错误保护；新增 favicon.ico

### v1.3.1 (2026-01-26)

- 首页/关于页优化、编辑器缓存修复、统计 API 路径修复、userType 返回 null 修复等

### v1.3.0 (2026-01-26)

- 博客用户注册登录认证（UnifiedAuthController 统一认证）

### v1.2.x

- 标签管理修复与字段统一、验证码优化、生产安全配置、Actuator 监控等（详见 git 历史）

## 📞 技术支持

### 相关文档

- [版本管理指南](docs/VERSION_MANAGEMENT.md)
- [安全配置说明](docs/SECURITY_CONFIG.md)
- [图片压缩功能使用指南](docs/图片压缩功能使用指南.md)
- [GitHub 同步说明](SYNC_GITHUB.md)
- [Vue 3文档](https://cn.vuejs.org/) · [Element Plus文档](https://element-plus.org/) · [Spring Boot文档](https://spring.io/projects/spring-boot)

### 常见问题

1. **版本号显示不正确**：检查 `pom.xml` 父 POM 与各子模块版本是否一致
2. **升级后没有新菜单/新权限/新字段（媒体管理、每日 PV/UV 等）**：已有数据库需重跑一次 `sql/00_init_database.sql`（幂等）
3. **“文章统计 → 每日阅读 PV/UV”没有数据**：需等待定时任务（每小时）首次汇总当天数据
4. **`/sitemap.xml` 或 `/robots.txt` 404**：生产反代请把这俩根路径转发到后端（:8080）
5. **邮箱验证码/通知邮件收不到**：检查 SMTP 配置（.env 的 MAIL\_\*）；开发可设 `EMAIL_DEV_PRINT_CODE=true` 在控制台查看验证码
6. **媒体删除后文件仍在**：删除会“尽力”删除物理文件，文件被占用/权限异常时仅删记录（日志会提示）
7. **评论数与后台不一致**：重跑 `00_init_database.sql` 会按已发布评论自动重算
8. **角色 2（内容管理员）看不到媒体管理**：确认已重跑 00，并在后台角色管理中补勾权限
9. **个人中心 404 / 登录后闪退**：确保后端已重启；必要时清除浏览器缓存
10. **构建失败提示 "Non-resolvable parent POM"**：检查父 POM 与子模块版本号一致

## 📄 许可证

本项目基于 [MIT许可证](LICENSE) 开源

## 📊 项目信息

- **项目名称**: ZhiBlog - 知博
- **当前版本**: v1.4.0
- **Maven GroupId**: top.nevell
- **项目地址**: https://gitee.com/nevell/zhiblog
- **维护者**: nevell
- **最后更新**: 2026-09-05
