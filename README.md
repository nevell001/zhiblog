# NewBlog - 基于 RuoYi-Vue 的博客系统

基于 RuoYi-Vue 快速开发平台打造的现代化、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript 技术栈。

## 🚀 项目特色

- 🏗️ **企业级架构**：基于成熟的 RuoYi-Vue 框架
- 📱 **前后端分离**：Vue 3 + Spring Boot，TypeScript 类型安全
- 🎨 **美观界面**：Element Plus 组件库，紫色主题设计
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署
- 📈 **监控完善**：Prometheus + Grafana 全链路监控
- ✅ **测试完善**：656个测试用例，覆盖率≥60%

## 📦 最新更新 (v1.3.1)

- ✨ 优化博客首页和关于页面（每页8篇文章，移除冗余社交链接）
- 🔧 修复文章编辑器内容缓存问题（强制重新渲染）
- 🐛 修复关于页面统计数据API路径错误
- 🎨 优化关于页面UI设计（简约图标风格）
- 🔐 修复 userType 返回 null 问题（用户类型区分显示）
- 🔄 优化登录后状态显示（无需刷新页面）
- 🎯 简化UI组件（合并登录/注册按钮，移除多余链接）

## 🌐 访问方式

- **博客前台**: http://localhost:3000/blog
- **管理后台**: http://localhost:3000/admin
- **API文档**: http://localhost:8080/swagger-ui.html
- **Prometheus监控**: http://localhost:9090
- **Grafana可视化**: http://localhost:3001
- **默认账号**: admin / admin123

## 🚀 快速开始

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+
- **Maven**: 3.6+

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
cd ruoyi-ui
npm install
npm run dev
```

### 5. Docker 一键部署
```bash
# 开发环境
docker compose -f docker-compose.dev.yml up -d

# 生产环境
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
├── sql/                  # 数据库脚本
├── prometheus/           # Prometheus配置
├── grafana/              # Grafana配置
└── docker-compose.yml    # Docker编排
```

## 🎯 核心功能

### 前台展示
- 文章列表与详情页（支持 Markdown）
- 文章目录导航（自动提取、平滑滚动）
- 分类、标签浏览
- 文章搜索（支持关键词搜索、分页）
- 归档、热门文章排行
- 评论功能（支持匿名/登录评论）
- 深色主题支持
- RSS 订阅

### 后台管理
- 文章管理（增删改查、发布/草稿、置顶）
- 分类管理、标签管理
- 评论管理（审核、删除）
- 友链管理
- 博客设置（功能开关控制）
- 用户管理
- 系统设置
- 日志管理

### 系统功能
- 用户认证与授权（JWT + Spring Security）
- 图片上传与管理
- 图片智能压缩（头像、缩略图、封面图）
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
DB_PASSWORD={your_password}
DB_NAME=newblog

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD={your_password}

# JWT
R_TOKEN_SECRET={your_secret_key}

# Druid
DRUID_USERNAME=ruoyi
DRUID_PASSWORD={your_password}
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

### 版本号管理
- 版本号统一在 `pom.xml` 中定义
- 修改版本号时使用 `mvn versions:set -DnewVersion=1.x.x`
- 遵循语义化版本规范（Major.Minor.Patch）

## 📦 版本历史

### v1.3.1 (2026-01-26)
- 优化博客首页和关于页面（每页8篇文章）
- 修复文章编辑器内容缓存问题
- 修复关于页面统计数据API路径错误
- 优化关于页面UI设计（简约图标风格）
- 修复 userType 返回 null 问题
- 优化登录后状态显示（无需刷新页面）
- 简化UI组件（合并登录/注册按钮）

### v1.3.0 (2026-01-26)
- 实现博客用户注册登录认证系统
- 新增 UnifiedAuthController 统一认证控制器
- 支持用户注册、登录、JWT 认证
- 优化认证流程和安全性

### v1.2.9 (2026-01-25)
- 修复标签管理编辑功能（缓存键解析错误）
- 统一标签字段命名（tagId/tagName → id/name）
- 修复验证码 Base64 编码错误
- 优化博客设置管理

### v1.2.8 (2026-01-24)
- 优化验证码配置
- 简化验证码逻辑
- 管理后台首页优化

### v1.2.7 (2026-01-23)
- 生产环境安全配置
- 批量操作性能优化
- Actuator 监控页面优化

## 📞 技术支持

### 相关文档
- [RuoYi官方文档](http://doc.ruoyi.vip/)
- [Vue 3文档](https://cn.vuejs.org/)
- [Element Plus文档](https://element-plus.org/)
- [Spring Boot文档](https://spring.io/projects/spring-boot)

### 常见问题
1. **版本号显示不正确**：检查 `pom.xml` 中的版本号
2. **头像上传失败**：确保后端服务正常运行
3. **登录后闪退**：清除浏览器缓存
4. **个人中心404**：确保后端已重启

## 📄 许可证

本项目基于 [MIT许可证](LICENSE) 开源

## 📊 项目信息

- **项目名称**: NewBlog
- **当前版本**: v1.3.1
- **Maven GroupId**: top.nevell
- **项目地址**: https://gitee.com/nevell/newblog
- **维护者**: nevell
- **最后更新**: 2026-01-26