# 基于 RuoYi-Vue 的博客系统

## 🚀 项目简介

基于 RuoYi-Vue 快速开发平台打造的现代化、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript 技术栈，支持文章发布、评论互动、标签分类、友情链接、后台管理等完整功能。

**最新更新（v1.2.9）**：

- 🐛 修复标签管理编辑功能（修复缓存键解析错误，导致所有标签显示相同数据）
- 🔧 统一标签字段命名（tagId/tagName → id/name，保持前后端一致）
- 🐛 修复验证码 Base64 编码错误（使用 Java 内置编码器替代自定义实现）
- 📊 优化博客设置管理（删除未使用的配置项，合并标签页）
- 🎨 优化博客设置页面标题（动态更新页面标题）
- 📦 版本号更新到 v1.2.9

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

### v1.2.9 (2026-01-25)
**标签管理与缓存优化**

**Bug 修复**:
- ✅ 修复标签管理编辑功能（修复缓存键解析错误，导致所有标签显示相同数据）
- ✅ 修复验证码 Base64 编码错误（使用 Java 内置编码器替代自定义实现）
- ✅ 修复博客设置页面标题显示问题（动态更新页面标题）

**代码优化**:
- ✅ 统一标签字段命名（tagId/tagName → id/name，保持前后端一致）
- ✅ 优化缓存键解析逻辑（支持包含 # 的表达式解析）
- ✅ 删除 BlogTag 兼容性方法（简化代码结构）

**功能优化**:
- ✅ 优化博客设置管理（删除未使用的配置项，合并标签页）
- ✅ 删除未使用的博客配置（主题颜色、创建时间、博主签名等）
- ✅ 优化博客设置页面（从 6 个标签页合并为 4 个）

### v1.2.8 (2026-01-24)
**验证码功能重构与优化**

**验证码功能**:

- ✅ 优化验证码配置（移除无效的 captchaEnabled 配置项）
- ✅ 简化验证码逻辑（优先使用环境变量 CAPTCHA_ENABLED，其次使用数据库配置）
- ✅ 验证码类型配置（支持 math 数学计算和 char 字符验证）
- ✅ 修复验证码配置混乱问题（统一配置入口）

**前端优化**:
- ✅ 管理后台首页优化（简化设计风格，与其他页面保持一致）
- ✅ 优化登录页面（移除验证码类型切换，简化用户操作）

**代码质量**:
- ✅ 清理无效代码（删除未使用的配置和组件）
- ✅ 统一配置管理（验证码配置集中管理）
- ✅ 提升代码可维护性（简化验证码逻辑）

### v1.2.7 (2026-01-23)
**生产环境安全配置与性能优化**

**安全配置优化**:
- ✅ 生产环境 Actuator 安全配置（只暴露 health, info, metrics, prometheus 端点）
- ✅ 移除 env 和 configprops 端点（防止泄露数据库密码、API密钥等敏感信息）
- ✅ Spring Security 配置优化（生产环境 Actuator 端点放行）
- ✅ 健康检查子路径放行（/health/**）
- ✅ 生产环境 Swagger 接口禁用（显示友好提示而非 404）

**性能优化**:
- ✅ 优化批量置顶功能（从循环调用优化为批量更新，性能提升 90%）
- ✅ 优化批量推荐功能（从循环调用优化为批量更新，性能提升 90%）
- ✅ 添加批量更新置顶状态接口（PUT /system/article/top）
- ✅ 添加批量更新推荐状态接口（PUT /system/article/recommend）

**功能增强**:
- ✅ 添加 Actuator 监控页面优化（模板端点禁用、环境自适应 URL）
- ✅ 添加应用信息配置（info 端点显示应用、构建、Java、OS 信息）
- ✅ 添加 Nginx 代理配置（/manage/ 路径代理到后端）
- ✅ 优化 Actuator 监控页面 UI（模板端点按钮禁用、Tooltip 提示）

**代码优化**:
- ✅ Service 层添加批量更新方法（updateArticleTopStatus, updateArticleRecommendStatus）
- ✅ Mapper 层添加批量更新 SQL
- ✅ Controller 层添加批量更新接口
- ✅ 前端 API 添加批量更新方法
- ✅ 前端页面使用新的批量更新接口

**详细说明**:
1. **生产环境 Actuator 安全配置**:
   - 只暴露必要的监控端点（health, info, metrics, prometheus）
   - 移除可能泄露敏感信息的端点（env, configprops）
   - 健康检查不显示详细信息（show-details: never）
2. **批量操作性能优化**:
   - 优化前：批量操作 10 篇文章需要调用 10 次 API
   - 优化后：批量操作 10 篇文章只需调用 1 次 API
   - 性能提升约 90%
3. **Actuator 监控页面优化**:
   - 模板端点（如 /metrics/{requiredMetricName}）按钮禁用
   - 添加 Tooltip 提示信息
   - 环境自适应 URL（生产环境使用相对路径，开发环境使用 localhost）

### v1.2.6 (2026-01-23)
**Bug修复与功能优化**

**Bug修复**:
- ✅ 修复文章发布时表单重置问题（点击新增按钮时显示上一次的内容）
- ✅ 修复用户统计页面图表显示模拟数据问题
- ✅ 修复 SysRoleServiceImplTest 测试的用户上下文问题

**功能优化**:
- ✅ 实现用户注册趋势和角色分布的真实数据库查询
- ✅ 移除标签选择时显示的文章数（非动态数据）
- ✅ 更新项目版权信息为 Nevell（2025-2026）
- ✅ 更新源码地址为 https://gitee.com/nevell/newblog.git

**代码优化**:
- ✅ 优化前端代码规范（ESLint 自动修复 231 个问题）
- ✅ 创建 tinymce.ts 工具文件，优化 TinyMCE 组件结构
- ✅ 为编辑器组件添加 key 属性，确保表单正确重置

**详细说明**:
1. **文章发布表单重置**: 使用 `:key="form.id || 'new'"` 强制编辑器组件重新渲染
2. **用户统计图表**: 实现真实数据库查询，替换模拟数据
3. **前端代码规范**: 修复所有 ESLint 问题，包括 Vue 组件格式化和代码风格
4. **版权信息更新**: 统一更新所有文件的版权声明

### v1.2.5 (2026-01-22)
**功能完善与Bug修复**

**系统功能修复**:
- ✅ 修复分类管理排序功能（修复 sort 和 sort_order 双字段同步问题）
- ✅ 修复评论审核功能（真正响应博客设置开关控制）
- ✅ 修复浏览统计功能（后台增加浏览量和前台显示完全受开关控制）
- ✅ 验证所有功能开关有效性（评论、点赞、浏览统计、分享、搜索、侧边栏、底部、版权）

**代码优化**:
- ✅ 优化 BlogCategoryMapper.xml（使用 COALESCE 处理双排序字段）
- ✅ 优化 BlogFrontController（根据功能开关控制浏览量统计）
- ✅ 修复 BlogCategoryMapper.xml XML 语法错误（删除重复的 </select> 标签）

**文档更新**:
- ✅ 更新 CLAUDE.md 项目文档（添加功能开关检查报告、已知问题、修复记录）
- ✅ 更新 README.md 版本历史

**详细说明**:
1. **分类管理排序修复**: 修改时同步更新 `sort` 和 `sort_order` 字段，确保列表排序正确显示
2. **评论审核功能**: 评论提交时根据 `comment_review` 开关决定是否需要审核
3. **浏览统计功能**: 根据 `view_count_enabled` 开关控制后台浏览量增加和前台显示
4. **功能开关验证**: 全面检查所有 8 个功能开关的前后台实现，确保全部有效

### v1.2.4 (2026-01-21)
**功能完善与优化**

**系统功能修复**:
- ✅ 修复友情链接状态开关逻辑（之前开启的会关闭，关闭的会开启）
- ✅ 调整系统菜单排序（博客管理→数据统计→系统监控→系统工具→系统管理）
- ✅ 调整博客管理子菜单排序（文章→分类→标签→评论→友链→设置）
- ✅ 修复所有功能开关（评论、点赞、浏览统计、分享、搜索、侧边栏、底部、版权）
- ✅ 优化博客设置默认值（只启用Vue.js官网和Element Plus两个友链）
- ✅ 优化首页路由（根路径重定向到博客首页）
- ✅ 优化路由守卫（博客相关路径直接放行，无需登录）

**前台功能新增**:
- ✅ 添加首页搜索栏功能（支持关键词搜索、回车触发、分页）
- ✅ 优化搜索栏样式（修复放大镜图标与输入框脱离问题）
- ✅ 搜索栏支持清除按钮和搜索图标
- ✅ 搜索结果支持分页，保持搜索条件

**代码优化**:
- ✅ 修复功能开关判断逻辑（同时支持布尔值和字符串值）
- ✅ 添加 blogSettingsStore.isFeatureEnabled 辅助方法
- ✅ 优化博客设置加载逻辑（更新 store 而不是直接设置值）
- ✅ 统一所有页面使用 blogSettingsStore

### v1.2.3 (2026-01-21)
**功能完善与优化**

**系统功能修复**:
- ✅ 修复友情链接状态开关逻辑（之前开启的会关闭，关闭的会开启）
- ✅ 调整系统菜单排序（博客管理→数据统计→系统监控→系统工具→系统管理）
- ✅ 调整博客管理子菜单排序（文章→分类→标签→评论→友链→设置）
- ✅ 修复所有功能开关（评论、点赞、浏览统计、分享、搜索、侧边栏、底部、版权）
- ✅ 优化博客设置默认值（只启用Vue.js官网和Element Plus两个友链）
- ✅ 优化首页路由（根路径重定向到博客首页）
- ✅ 优化路由守卫（博客相关路径直接放行，无需登录）

**前台功能新增**:
- ✅ 添加首页搜索栏功能（支持关键词搜索、回车触发、分页）
- ✅ 优化搜索栏样式（修复放大镜图标与输入框脱离问题）
- ✅ 搜索栏支持清除按钮和搜索图标
- ✅ 搜索结果支持分页，保持搜索条件

**代码优化**:
- ✅ 修复功能开关判断逻辑（同时支持布尔值和字符串值）
- ✅ 添加 blogSettingsStore.isFeatureEnabled 辅助方法
- ✅ 优化博客设置加载逻辑（更新 store 而不是直接设置值）
- ✅ 统一所有页面使用 blogSettingsStore

### v1.2.2 (2026-01-20)
**功能完善与优化**

**前台展示优化**:
- ✅ 完善分类、标签主页面（添加 fadeInUp 动画效果）
- ✅ 完善关于页面（移除技能专长、成长历程部分）
- ✅ 移除关于页面联系表单功能
- ✅ 添加关于我部分（支持富文本内容，与博客设置同步）
- ✅ 修复博主头像显示问题（使用默认头像 SVG）
- ✅ 使用 Element Plus 图标替换关于页面社交图标（Promotion、Message、Star、Connection）
- ✅ 修复友情链接显示问题（修复 SQL 查询条件 status = '1'）
- ✅ 添加友情链接缓存清除功能（@BlogCacheEvict 注解）

**博客设置优化**:
- ✅ 移除博主信息中的微信二维码功能
- ✅ 同步博主信息到关于页面（GitHub地址、微博地址、位置信息、个人网站）
- ✅ 在其他设置中添加关于页面内容字段（使用富文本编辑器）

**问题修复**:
- ✅ 修复关于页面联系信息字段不显示问题（修复缓存清除逻辑和默认值设置）
- ✅ 修复关于页面分类和标签数量显示为0的问题（使用和首页相同的方式获取）
- ✅ 修复友情链接状态值不匹配问题（SQL 查询条件改为 status = '1'）
- ✅ 修复缓存注解参数错误（使用 value 而不是 cacheNames）

**代码优化**:
- ✅ 添加友情链接缓存常量（BLOG_FRIEND_LINK_LIST）
- ✅ 添加友情链接加载调试日志
- ✅ 优化头像 URL 处理逻辑（添加错误处理）
- ✅ 优化统计数据 API（使用正确的 Service 方法）

### v1.2.1 (2026-01-20)
**功能修复与优化**

**系统功能修复**:
- ✅ 修复博客首页分类和标签的显示
- ✅ 增加了底部和版权的显示

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
- **文章搜索**（支持关键词搜索、回车触发、分页）
- 归档
- 热门文章排行
- 评论功能（支持匿名/登录评论）
- **头像展示**（统一使用账号头像）
- 深色主题支持
- RSS 订阅

### 后台管理
- 文章管理（增删改查、发布/草稿、置顶）
- 分类管理、标签管理
- 评论管理（审核、删除）
- 友链管理（状态开关控制）
- **博客设置**（功能开关：评论、点赞、浏览统计、分享、搜索、侧边栏、底部、版权）
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
- 版本号在 `pom.xml` 中定义，当前版本为 `1.2.9`
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
- **当前版本**: v1.2.9
- **Maven GroupId**: top.nevell
- **项目地址**: https://gitee.com/nevell/newblog
- **维护者**: nevell
- **最后更新**: 2026-01-23
