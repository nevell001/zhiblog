# 基于 RuoYi-Vue 的博客系统

## 🚀 项目简介

基于 RuoYi-Vue 快速开发平台打造的现代化、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript 技术栈，支持文章发布、评论互动、标签分类、友情链接、后台管理等完整功能。

**项目特色**：
- 🏗️ **企业级架构**：基于成熟的 RuoYi-Vue 框架
- 📱 **前后端分离**：Vue 3 + Spring Boot，TypeScript 类型安全
- 🎨 **美观界面**：Element Plus 组件库，紫色主题设计
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署
- 📈 **监控完善**：Prometheus + Grafana 全链路监控
- 🖼️ **图片优化**：智能压缩、防盗链保护
- ✅ **测试完善**：656个测试用例，覆盖率≥60%

## 📦 版本历史

### v4.1.0 (2026-01-18)
**重大升级 - TypeScript 迁移完成 + 博客 UI 优化**

**TypeScript 迁移**:
- ✅ 前端项目全面迁移到 TypeScript 5.9.3（100% 完成）
- ✅ 新增完整的类型定义（src/types/）
- ✅ 配置 Vitest 测试框架（覆盖率目标 ≥70%）

**博客 UI 优化**:
- ✅ 统一配色方案为紫色系（#667eea, #764ba2）
- ✅ 修复深色主题选择器问题（:deep(.dark) -> html.dark）
- ✅ 优化博客详情页、首页、分类、标签、归档、关于页面 UI
- ✅ 统一设计规范（圆角、阴影、间距等）
- ✅ 新增博客设计规范文档（BLOG_DESIGN_STANDARDS.md）
- ✅ 优化深色主题配色和浅色主题底部配色

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
- 深色主题支持
- RSS 订阅

### 后台管理
- 文章管理（增删改查、发布/草稿、置顶）
- 分类管理、标签管理
- 评论管理（审核、删除）
- 友链管理
- 用户管理
- 系统设置
- 日志管理

### 系统功能
- 用户认证与授权（JWT + Spring Security）
- 图片上传与管理
- 图片智能压缩
- 防盗链保护
- XSS 防护
- 定时任务
- 代码生成器
- 监控告警

## 🏗️ 技术栈

### 后端
- Spring Boot 3.3.0
- MyBatis 3.0.3
- MySQL 8.4.0
- Redis 6.2
- Spring Security 6.1.5
- Druid 1.2.27
- Java 17

### 前端
- Vue 3.5.16
- TypeScript 5.9.3
- Vite 7.3.1
- Element Plus 2.10.7
- Pinia 3.0.2
- Vue Router 4.5.1
- Vitest 2.1.8

### 监控
- Prometheus
- Grafana
- Spring Boot Actuator

## 🔧 配置说明

### 环境变量
```bash
# 数据库
DB_HOST=localhost
DB_USERNAME=root
DB_PASSWORD=root
DB_NAME=newblog

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT
R_TOKEN_SECRET=your-very-long-and-secure-random-string-here

# 防盗链
REFERER_ENABLED=false
REFERER_ALLOWED_DOMAINS=localhost,127.0.0.1
```

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

## 📞 技术支持

### 相关文档
- [RuoYi官方文档](http://doc.ruoyi.vip/)
- [Vue 3文档](https://cn.vuejs.org/)
- [Element Plus文档](https://element-plus.org/)
- [Spring Boot文档](https://spring.io/projects/spring-boot)

### 许可证
本项目基于 [MIT许可证](LICENSE) 开源

---

**最后更新**: 2026-01-18  
**维护者**: nevell  
**项目地址**: https://gitee.com/nevell/newblog