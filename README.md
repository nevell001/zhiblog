# 基于 RuoYi-Vue 的博客系统

## 🚀 项目简介

基于 RuoYi-Vue 快速开发平台打造的现代化、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript 技术栈，支持文章发布、评论互动、标签分类、友情链接、后台管理等完整功能。

**最新更新（v1.2.1）**：
- 🐛 修复个人中心404错误
- 🐛 修复头像上传失败问题
- 🎨 统一账号头像和博客头像管理
- 🐛 修复登录闪退问题
- 🔧 优化 Maven 配置（groupId 统一）
- 📊 版本号动态显示（从 pom.xml 读取）
- 🗄️ 修复数据库初始化文件（新增5个设置项：author_title、author_description、github_url、weibo_url、zhihu_url）

**项目特色**：
- 🏗️ **企业级架构**：基于成熟的 RuoYi-Vue 框架
- 📱 **前后端分离**：Vue 3 + Spring Boot，TypeScript 类型安全
- 🎨 **美观界面**：Element Plus 组件库，紫色主题设计
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署
- 📈 **监控完善**：Prometheus + Grafana 全链路监控
- 🖼️ **图片优化**：智能压缩、防盗链保护
- ✅ **测试完善**：656个测试用例，覆盖率≥60%
- 🔧 **版本管理**：版本号从 pom.xml 动态读取，保持一致性

## 📦 版本历史

### v1.2.1 (2026-01-20)
**功能修复与优化**

**系统功能修复**:
- ✅ 修复个人中心页面404错误（添加 /user/profile 路由）
- ✅ 修复头像上传失败问题（multipart/form-data 格式错误）
- ✅ 统一账号头像和博客头像（移除博客设置中的独立头像选项）
- ✅ 修复登录后闪退问题（process.env 未定义错误）
- ✅ 修复前端环境变量配置（Vite 兼容性问题）
- ✅ 修复 Maven 构建失败问题（pom.xml groupId 不一致）
- ✅ 优化版本号显示（从 pom.xml 动态读取）
- ✅ 添加版本号 API 接口（/system/version）

**代码优化**:
- ✅ 优化 FormData 请求处理（自动设置 Content-Type）
- ✅ 优化防重复提交机制（跳过 FormData 请求）
- ✅ 统一 pom.xml groupId 为 top.nevell
- ✅ 修复所有子模块依赖配置

**用户体验改进**:
- ✅ 简化头像管理流程（统一入口）
- ✅ 添加头像上传loading状态
- ✅ 优化错误提示信息

### v1.2.0 (2026-01-19)
**功能修复与优化**

**系统功能修复**:
- ✅ 修复前端组件加载失败问题（permission.ts 路径匹配优化）
- ✅ 创建表单构建占位页面（tool/build/index.vue）
- ✅ 修复日志管理页面空白问题（删除重复菜单）
- ✅ 允许修改超级管理员角色（移除所有限制）
- ✅ 修复 Grafana 监控页面环境变量访问问题
- ✅ 修复系统接口（Swagger）功能配置
- ✅ 修复监控页面排序和格式化函数错误
- ✅ 优化 Spring Security 配置（禁用 X-Frame-Options）
- ✅ 修复 .gitignore 配置（允许 tool/build 目录）

**代码优化**:
- ✅ 优化 permission.ts 组件加载逻辑（使用 Set 替代数组）
- ✅ 添加 SwaggerConfig 资源处理器配置
- ✅ 修复前端类型安全问题（import.meta.env 可选链）

**技术栈更新**:
- TypeScript 5.9.3
- Vitest 2.1.8 测试框架
- Vue Test Utils 2.4.6

## 🌐 访问方式

- **博客前台**: http://localhost:3000/blog
- **管理后台**: http://localhost:3000/admin
- **API文档**: http://localhost:8080/swagger-ui.html
- **Prometheus监控**: http://localhost:9090
- **Grafana可视化**: http://localhost:3001（admin/admin）
- **默认账号**: admin / admin123

## 🚀 快速开始

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+
- **Maven**: 3.6+
- **Docker**: 20.10+（可选）

### 1. 克隆项目
```bash
git clone https://gitee.com/nevell/newblog.git
cd newblog
```

### 2. 数据库初始化
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库
mysql -u root -p newblog < sql/00_init_database.sql
```

### 3. 后端启动
```bash
# 编译项目
mvn clean install -DskipTests

# 启动后端服务
cd ruoyi-admin
mvn spring-boot:run
```

### 4. 前端启动
```bash
# 安装依赖
cd ruoyi-ui
npm install

# 启动开发服务器
npm run dev

# 生产环境构建
npm run build:prod
```

### 5. Docker 一键部署

**开发环境**:
```bash
docker compose -f docker-compose.dev.yml up -d
```

**生产环境**:
```bash
docker compose -f docker-compose.prod.yml up -d
```

## 📁 项目结构

```
newblog/
├── ruoyi-admin/          # 后端主模块
├── ruoyi-system/         # 系统模块（博客功能）
├── ruoyi-framework/      # 框架核心
├── ruoyi-common/         # 通用工具
├── ruoyi-quartz/         # 定时任务
├── ruoyi-generator/      # 代码生成
├── ruoyi-ui/             # Vue3前端项目
│   ├── src/
│   │   ├── views/        # 页面
│   │   │   ├── blog/    # 博客前台
│   │   │   └── admin/   # 管理后台
│   │   ├── components/  # 组件
│   │   ├── api/         # API接口
│   │   ├── router/      # 路由配置
│   │   ├── stores/      # Pinia状态管理
│   │   └── types/       # TypeScript类型定义
│   └── package.json
├── sql/                  # 数据库脚本
├── prometheus/           # Prometheus配置
├── grafana/              # Grafana配置
├── docker-compose.yml    # Docker编排
├── pom.xml              # Maven主配置
└── README.md
```

## 🎯 核心功能

### 前台展示
- 文章列表与详情页（支持 Markdown）
- 文章目录导航（自动提取、平滑滚动）
- 分类、标签浏览
- 文章搜索、归档
- 热门文章排行
- 评论功能（支持匿名/登录评论）
- **头像展示**（统一使用账号头像）
- 深色主题支持
- RSS 订阅

### 后台管理
- 文章管理（增删改查、发布/草稿、置顶）
- 分类管理、标签管理
- 评论管理（审核、删除）
- 友链管理
- 用户管理
- **个人中心**（头像修改、基本信息、密码修改）
- 系统设置
- 日志管理

**头像管理**:
- ✅ 统一账号头像和博客头像（单一数据源）
- ✅ 支持图片裁剪和压缩
- ✅ 自动同步到前台展示

### 系统功能
- 用户认证与授权（JWT + Spring Security）
- 图片上传与管理
- **图片智能压缩**（支持头像、缩略图、封面图）
- 防盗链保护
- XSS 防护
- 定时任务
- 代码生成器
- 监控告警
- **版本号动态获取**（从 pom.xml 读取）

## 🏗️ 技术栈

### 后端
- Spring Boot 3.3.0
- MyBatis 3.0.3
- MySQL 8.4.0
- Redis 6.2
- Spring Security 6.1.5
- Druid 1.2.27
- Java 17
- **Maven**: 3.6+


### 前端
- Vue 3.5.16
- TypeScript 5.9.3
- Vite 7.3.1
- Element Plus 2.10.7
- Pinia 3.0.2
- Vue Router 4.5.1
- Vitest 2.1.8
- **Axios** 1.9.0（HTTP 客户端）

### 监控
- Prometheus
- Grafana
- Spring Boot Actuator

### 构建工具
- Maven 3.6+
- npm/pnpm
- Docker 20.10+

## 🔧 配置说明

### 环境变量
```bash
# 数据库
DB_HOST=localhost
DB_USERNAME=root
DB_PASSWORD={openssl rand -base64 16}
DB_NAME=newblog

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD={openssl rand -base64 16}

# JWT
R_TOKEN_SECRET={openssl rand -base64 64}

# 防盗链
REFERER_ENABLED=false
REFERER_ALLOWED_DOMAINS=localhost,127.0.0.1

# Druid
DRUID_USERNAME=ruoyi
# Druid监控密码（必须设置强密码）
DRUID_PASSWORD={openssl rand -base64 16}

```

**注意**：
- 版本号在 `pom.xml` 中定义，当前版本为 `1.2.1`
- 前端会自动从后端 API 获取版本号并显示在管理后台首页
- 修改版本号时请同步更新 `pom.xml` 和 `application.yml`

### 图片压缩配置
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

## 📋 开发规范

### Git 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试
chore: 构建/工具
```

### 代码规范
- **Java**: 遵循阿里巴巴 Java 开发手册
- **Vue**: 遵循 Vue 官方风格指南
- **TypeScript**: 严格类型检查，避免使用 any

### 版本号管理
- 版本号统一在 `pom.xml` 中定义
- 修改版本号时同步更新 `application.yml` 中的 `ruoyi.version`
- 遵循语义化版本规范（Major.Minor.Patch）

## 📞 技术支持

### 相关文档
- [RuoYi官方文档](http://doc.ruoyi.vip/)
- [Vue 3文档](https://cn.vuejs.org/)
- [Element Plus文档](https://element-plus.org/)
- [Spring Boot文档](https://spring.io/projects/spring-boot)
- [项目更新日志](#版本历史)
- [开发规范](#开发规范)

### 常见问题
1. **版本号显示不正确**：检查 `pom.xml` 和 `application.yml` 中的版本号是否一致
2. **头像上传失败**：确保后端服务正常运行，检查网络连接
3. **登录后闪退**：清除浏览器缓存，检查控制台错误信息
4. **个人中心404**：确保后端已重启，路由配置正确

### 许可证
本项目基于 [MIT许可证](LICENSE) 开源

### 项目信息
- **项目名称**: NewBlog
- **当前版本**: v1.2.1
- **Maven GroupId**: top.nevell
- **项目地址**: https://gitee.com/nevell/newblog
- **维护者**: nevell
- **最后更新**: 2026-01-20
