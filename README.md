# ZhiBlog - 知博

现代化的前后端分离博客系统：Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript。

## 🚀 项目特色

- **内容创作**：富文本（Quill）与 Markdown 双编辑器，支持定时发布与自定义页面
- **读者互动**：评论（游客/登录 + 审核）、点赞/取消、收藏、站内通知 + 邮件通知
- **SEO 友好**：动态 title/description/keywords/canonical、`/sitemap.xml`、`/robots.txt`
- **统计完善**：按访客去重的浏览量、每日 PV/UV 聚合、访问明细（可筛选/导出/清理）
- **安全**：JWT + 图形验证码 + 邮箱码防爆破、IP 限流、XSS 消毒、防盗链白名单后台化
- **权限与开关**：Spring Security 细粒度权限；博客功能开关前台隐藏入口且接口拒绝
- **一键部署**：Docker Compose 管理前后端、MySQL、Redis 与 Prometheus/Grafana
- **工程化**：JaCoCo 60% / Vitest 覆盖率门槛，Checkstyle + ESLint + Prettier，统一版本管理

## 📦 最近更新 (v1.4.0)

- 定时发布、Markdown 文章（源码 + 服务端 HTML 双写）、自定义页面（Markdown + SEO + 导航接入）
- 点赞体系（文章/评论、去重/取消）、收藏列表页、前台通知铃铛、移动端汉堡导航
- 留言板（图形验证码 + 输入时限 + 审核/回复）、友链申请验证码与前台开关、独立搜索页
- 分类/标签总览页、作者主页；全部功能开关统一判定并在后端生效
- 访问明细（PV/UV 逐条日志 + 排行/导出/清理，默认保留 90 天）、每日 PV/UV 聚合曲线
- 深浅色配色审计：默认主题与 mo-blog 主题在浅色/深色四种组合下，前台文字全部达到 WCAG AA

## 🌐 访问方式

| 入口 | 地址 |
| --- | --- |
| 博客前台 | http://localhost:3000/blog |
| 管理后台 | http://localhost:3000/admin |
| API 文档 | http://localhost:8080/swagger-ui.html |
| Prometheus / Grafana | http://localhost:9090 / http://localhost:3001 |

默认账号 `admin`（首次登录请修改默认密码）。

## 🚀 快速开始

### 环境要求

- Java 17+、Maven 3.6+
- Node.js 20.19+（Vite 7 要求，22.12+ 亦可）
- MySQL 8.4+、Redis 6.2+
- Docker Engine / Desktop / Colima（推荐，用于完整本地环境）

### 1. Docker Compose（推荐）

```bash
git clone https://gitee.com/nevell/zhiblog.git
cd ZhiBlog
cp .env.example .env          # 按需修改关键配置，DB_NAME 保持 zhiblog
docker compose -f docker-compose.dev.yml up -d --build

docker compose -f docker-compose.dev.yml ps            # 查看状态
docker compose -f docker-compose.dev.yml logs -f zhi-admin
```

生产/接近生产改用 `docker-compose.prod.yml`。项目**没有**默认 `docker-compose.yml`，必须明确指定 dev 或 prod。

### 2. 手动开发启动（可选）

需自行准备 MySQL 与 Redis 并初始化数据库：

```bash
mysql -u root -p
CREATE DATABASE zhiblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
mysql -u root -p zhiblog < sql/00_init_database.sql
```

后端：

```bash
# 1) 从根目录安装各模块（zhi-system 以 jar 形式被 zhi-admin 依赖，
#    改过 zhi-system 后必须重新 install，否则 zhi-admin 用的是 ~/.m2 里的旧包）
mvn clean install -DskipTests

# 2) Spring Boot 不读 .env（.env 只给 docker compose 用），宿主机运行要先导入变量
set -a; source .env; set +a

# 3) 启动
mvn -pl zhi-admin spring-boot:run
```

前端：

```bash
cd zhi-ui && npm install && npm run dev
```

> 常见坑：不导入 `.env` 直接 `mvn spring-boot:run` 时，`DB_USERNAME` 会回落成 `application.yml`
> 的默认值 `root`、密码为空，于是连到宿主机 3306 上的任意 MySQL 都会 `Access denied`。
> 若本机 3306 被占用，可把 `docker-compose.dev.yml` 里 mysql 的端口映射改成 `3307:3306`，
> 并设置 `DB_PORT=3307`（JDBC URL 已支持 `DB_PORT`）。

### 3. 升级已有数据库（重要）

所有表/列/索引/菜单/权限变更都收敛在唯一且**幂等**的 `sql/00_init_database.sql`，**已有库直接重跑**即可，会自动补齐：

- 表：媒体库 `blog_upload`、点赞、每日 PV/UV、收藏、通知、留言板、自定义页面、访问明细等
- 列：`blog_comment.like_count`、`blog_article.publish_time/format/content_md`、`blog_friend_link.email` 等
- 新菜单与按钮权限（含角色 1/2 分配）、`email_notify_enabled` / `friend_link_apply_enabled` 等设置种子
- 按已发布评论**重算评论数**

```bash
mysql -u root -p zhiblog < sql/00_init_database.sql
```

> 数据卷已存在的 Docker 数据库**不会**再自动执行 `docker-entrypoint-initdb.d`，必须手动重跑一次。
> 部署提示：`/sitemap.xml`、`/robots.txt` 在后端根路径，Nginx 同源反代时请转发到后端；PV/UV 曲线依赖每小时定时汇总（当天数据约 1 小时延迟）。

## 📁 项目结构

```
ZhiBlog/
├── zhi-admin/          # 后端主模块（启动入口 + 前台/个人中心控制器 + 定时任务）
├── zhi-system/         # 业务逻辑（Blog 管理控制器、Mapper、媒体/统计等）
├── zhi-framework/      # 框架核心（Security/JWT/过滤器）
├── zhi-common/         # 通用工具（Markdown 渲染、图片压缩、XSS）
├── zhi-quartz/         # Quartz 定时任务
├── zhi-generator/      # 代码生成
├── zhi-ui/             # Vue3 + TS 前端（src/api、src/views/blog、src/views/admin）
├── sql/                # 数据库脚本（唯一初始化/迁移入口）
├── docs/               # 项目文档
├── prometheus/         # Prometheus 配置
├── docker-compose.dev.yml
└── docker-compose.prod.yml
```

## 🎯 核心功能

### 前台展示

- 首页/文章详情/分类/标签/归档/关于/作者主页/搜索页/留言板/自定义页面
- 分类与标签总览页；文章标签与作者名可点击
- 富文本与 Markdown 文章、定时发布、文章目录（TOC）、相关/热门文章、RSS
- 评论（游客/登录、回复、审核）、点赞、收藏、通知铃铛、个人中心
- 主题（浅色/深色/跟随系统 + mo-blog 主题），深浅色文字均按 WCAG AA 校验

### 后台管理

- 文章（草稿/发布/定时、Markdown 与富文本、置顶/推荐）、分类、标签、评论（审核/导出）
- 留言管理（审核/回复/删除/导出）、页面管理、友链申请审核
- 博客设置（站点信息 + 功能开关 + 防盗链域名 + SEO 优化）、媒体库、访问明细
- 用户 / 角色权限 / 系统 / 日志 / 定时任务 / 代码生成

### 系统与统计

- 统一认证（JWT）、注册与邮箱找回密码（图形验证码 + 邮箱码防爆破）
- 图片压缩/缩放/水印上传并自动入库、防盗链（Referer 白名单后台可管理）、XSS 消毒
- 邮件通知（评论/回复/审核结果，可开关）+ 过期验证码定时清理
- 浏览量按访客 24h 去重；每日 PV/UV 聚合与曲线
- `/sitemap.xml` + `/robots.txt` + 前台动态 SEO meta
- 监控（Actuator / Prometheus / Grafana）、全局错误保护

## 🏗️ 技术栈

- **后端**：Spring Boot 3.3 / Java 17、MyBatis + PageHelper、MySQL 8.4、Redis 6.2、Spring Security 6 + JWT、flexmark（Markdown 渲染）、Thumbnailator（图片压缩）、Quartz
- **前端**：Vue 3.5 / TypeScript 5.9 / Vite 7、Element Plus 2.13、Pinia、Vue Router、Quill + marked、DOMPurify、ECharts
- **测试与质量**：JUnit + JaCoCo（60%）、Vitest（覆盖率门槛）、Checkstyle、ESLint + Prettier

## 🔧 配置说明

环境变量模板见 `.env.example`，核心项：

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
```

- `R_TOKEN_SECRET` 建议用 `openssl rand -base64 64` 生成
- 生产环境缺关键密钥时 `SecurityConfigValidator` 会**阻止启动**；开发环境可用 `SECURITY_VALIDATION_ENABLED=false` 跳过
- 防盗链（`referer_*`）与邮件通知（`email_notify_enabled`）默认取 .env/yml，**保存到后台设置后以数据库为准**
- 图片压缩见 `application.yml` 的 `image.compress`（默认开启，阈值 2MB、最大 2560×1440、头像 200、缩略图 400）
- 上传端点：`/common/upload[/compressed|avatar|thumbnail|article-cover|mobile|watermark]`、`/common/uploads`、用户头像 `/system/user/profile/avatar`、图片处理 `/system/image/*`

## 📋 开发规范

- **提交**：`feat` / `fix` / `docs` / `style` / `refactor` / `test` / `chore`（可带 scope）
- **Java**：Checkstyle 在 `validate` 阶段执行；**Vue/TS**：`vue-tsc` 严格类型检查 + ESLint + Prettier
- **覆盖率门槛**：后端 JaCoCo 60%（行/分支）；前端 Vitest lines/statements ≥70、functions ≥75、branches ≥55
- **版本管理**：版本号统一在根 `pom.xml` 的 `<version>` 与 `<app.version>`，修改时同步 6 个子模块 parent `<version>`；详见[版本管理指南](docs/VERSION_MANAGEMENT.md)

## 📦 版本历史

### v1.4.0 (2026-09)

- 见上方「最近更新」；更细的变更记录见 git 历史

### v1.3.6 (2026-07-30)

- 主题系统优化（跟随系统模式图标与提示）、TypeScript 类型完善、搜索 500ms 防抖
- 错误处理与空 catch 优化、`getApiBaseUrl()` 动态适配、深色下博客设置页边框修复

### v1.3.0 ~ v1.3.5 (2026-01 ~ 2026-07)

- 统一认证 `UnifiedAuthController`：博客用户注册/登录/找回密码
- 三个注册缺陷修复（开关绕过、角色缺失静默失败、协议复选框装饰性）
- 合并 SQL 脚本为单文件 `00_init_database.sql`；统一版本号管理机制与全局错误保护
- Docker 开发库名统一为 `zhiblog`，精简为 dev/prod 两套 Compose 入口

### v1.2.x 及更早

- 标签管理与字段统一、验证码优化、生产安全配置、Actuator 监控等，详见 git 历史

## 📞 相关文档与常见问题

- [版本管理指南](docs/VERSION_MANAGEMENT.md) · [安全配置说明](docs/SECURITY_CONFIG.md) · [图片压缩指南](docs/图片压缩功能使用指南.md) · [GitHub 同步说明](SYNC_GITHUB.md)
- 外部文档：[Vue 3](https://cn.vuejs.org/) · [Element Plus](https://element-plus.org/) · [Spring Boot](https://spring.io/projects/spring-boot)

常见问题：

1. **版本号显示不正确 / 构建报 “Non-resolvable parent POM”**：检查根 `pom.xml` 与 6 个子模块 parent `<version>` 是否一致
2. **升级后缺新菜单/新权限/新字段，或评论数不一致**：重跑 `sql/00_init_database.sql`（幂等，会补齐并重算评论数）
3. **「每日阅读 PV/UV」没有数据**：等定时任务（每小时）首次汇总；当天数据约 1 小时延迟
4. **`/sitemap.xml`、`/robots.txt` 404**：生产反代把这两个根路径转发到后端（:8080）
5. **邮箱验证码/通知邮件收不到**：检查 `.env` 的 `MAIL_*`；开发可设 `EMAIL_DEV_PRINT_CODE=true` 在控制台查看
6. **媒体删除后文件仍在 / 角色 2 看不到媒体管理**：前者是“尽力删除”（占用时仅删记录）；后者确认已重跑 00 并在角色管理中补勾权限
7. **个人中心 404、登录后闪退、改了前端代码页面没变化**：确认后端已重启；前端清浏览器缓存，容器内 dev server 已启用轮询监听

## 📄 许可证

本项目基于 [MIT 许可证](LICENSE) 开源。

## 📊 项目信息

- **项目名称**：ZhiBlog - 知博　**当前版本**：v1.4.0　**Maven GroupId**：top.nevell
- **项目地址**：https://gitee.com/nevell/zhiblog　**维护者**：nevell
- **最后更新**：2026-09-11
