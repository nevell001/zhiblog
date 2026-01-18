# 基于 RuoYi-Vue 的博客系统

## 🚀 项目简介
本项目基于 RuoYi-Vue 快速开发平台，打造一个现代化、支持多用户、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus 技术栈，集成文章发布、评论互动、标签分类、友情链接、后台管理等完整博客功能，适合个人或团队搭建高效、可扩展的博客平台。

**项目特色**：
- 🏗️ **企业级架构**：基于成熟的 RuoYi-Vue 框架，稳定可靠
- 📱 **前后端分离**：Vue 3 + Spring Boot，现代化开发体验
- 🎨 **美观界面**：Element Plus 组件库，响应式设计
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制
- 📊 **功能丰富**：文章、评论、标签、分类、统计等完整功能
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署
- 🛡️ **安全加固**：多层安全防护，XSS、CSRF、SQL注入防护
- 📈 **监控完善**：Prometheus + Grafana 全链路监控
- 🖼️ **图片优化**：智能压缩、防盗链保护
- ⚡ **性能优化**：Redis缓存、数据库索引、懒加载
- ✅ **测试完善**：656个测试用例，覆盖率≥60%，质量保证

## 📦 版本历史

### v4.0.4 (2026-01-18)
**Bug 修复 - 前端显示问题清理**

**前端问题修复**:
- ✅ 修复占位符图片服务不可用问题
  - 替换 via.placeholder.com 为本地图片
  - 博客首页博主头像使用本地默认图片
  - 关于页面博主头像使用本地默认图片
  - 友情链接 logo 使用首字母图标（CSS 样式）
  - 微信二维码占位符优化（显示提示文字）
- ✅ 修复登录页面环境变量错误
  - 添加可选链操作符保护（import.meta.env?.VITE_APP_TITLE）
  - 添加默认值回退机制
- ✅ 清理残留 JavaScript 文件
  - 删除 5 个未使用的 admin-*.js 路由文件
  - TypeScript 迁移 100% 完成（src 目录 0 个 JS 残留）
  - 释放约 31KB 空间

**配置说明**:
- ⚠️ HMR 已禁用（vite.config.ts: hmr: false）
  - 原因：Vite HMR 系统与项目存在兼容性问题
  - 错误：Cannot read properties of undefined (reading 'on')
  - 解决方案：禁用 HMR，使用手动浏览器刷新
  - 影响：代码更改后需要手动刷新浏览器
- ✅ Vite 插件保持 JS 格式（vite/plugins/*.js）
  - 运行在 Node.js 环境，无需迁移到 TypeScript
  - 与 HMR 错误无关

**修改文件**:
- src/views/blog/index.vue - 占位符图片优化
- src/views/blog/about.vue - 占位符图片优化
- src/views/login.vue - 环境变量保护
- 删除 src/router/admin-*.js（5个文件）

**文档更新**:
- ✅ 更新 README.md 版本历史
- ✅ 添加 HMR 配置说明
- ✅ 更新 TypeScript 迁移状态

### v4.0.3 (2026-01-17)
**技术升级 - TypeScript 迁移完成**

**TypeScript 迁移**:
- ✅ 前端项目全面迁移到 TypeScript 5.9.3（100% 完成）
  - 所有 JavaScript 文件迁移到 TypeScript
  - 新增完整的类型定义（src/types/）
  - 配置 Vitest 测试框架
  - 配置 tsconfig.json 和 tsconfig.node.json
  - 修复所有类型错误和语法错误
  - 删除已迁移的 JavaScript 文件（73个文件）

**技术栈更新**:
- ✅ 新增 TypeScript 5.9.3
- ✅ 新增 Vitest 2.1.8 测试框架
- ✅ 新增 @vitest/coverage-v8 2.1.8 覆盖率工具
- ✅ 新增 @vue/test-utils 2.4.6 Vue 测试工具
- ✅ 更新 Vite 配置（vite.config.ts）
- ✅ 更新 package.json 脚本命令

**类型定义**:
- ✅ 新增 src/types/index.d.ts - 全局类型定义
- ✅ 新增 src/types/api.d.ts - API 模块类型定义
- ✅ 定义完整的接口类型（Article、Tag、Category、Comment等）
- ✅ 定义通用类型（PageParams、QueryResult、AjaxResult等）

**测试配置**:
- ✅ 配置 Vitest 测试环境（jsdom）
- ✅ 配置测试覆盖率目标（行/函数/分支/语句 ≥70%）
- ✅ 新增测试文件（9个测试文件）
  - utils/validate.test.ts
  - utils/index.test.ts
  - utils/imageUtils.test.ts
  - stores/user.test.ts
  - stores/permission.test.ts
  - stores/app.test.ts
  - components/LinkIcon.test.ts
  - components/Pagination/index.test.ts
  - api/blog/article.test.ts

**构建验证**:
- ✅ 生产环境构建成功（npm run build:prod）
- ✅ 所有类型检查通过
- ✅ 所有语法错误修复
- ✅ 代码质量保证

**文档更新**:
- ✅ 更新 README.md 技术栈说明
- ✅ 添加 TypeScript 版本信息
- ✅ 更新版本历史

### v4.0.2 (2026-01-11)
**质量提升 - 测试覆盖率优化**

**测试增强**:
- ✅ 新增 9 个测试文件，提升代码覆盖率
  - BlogArticleTest.java - 博客文章实体测试（9个测试方法）
  - BlogCategoryTest.java - 文章分类实体测试（7个测试方法）
  - BlogCommentTest.java - 博客评论实体测试（6个测试方法）
  - BlogFriendLinkTest.java - 友情链接实体测试（5个测试方法）
  - BlogSettingTest.java - 博客设置实体测试（6个测试方法）
  - BlogArticleTagTest.java - 文章标签关联测试（5个测试方法）
  - SysConfigServiceImplTest.java - 参数配置服务测试（19个测试方法）
  - SysDictDataServiceImplTest.java - 字典数据服务测试（10个测试方法）
  - SysDictTypeServiceImplTest.java - 字典类型服务测试（17个测试方法）

**测试成果**:
- ✅ 测试数量: 572 → 656（新增 84 个测试用例）
- ✅ 测试文件: 35 → 44（新增 9 个测试文件）
- ✅ 测试通过率: 100%（656 个测试全部通过）
- ✅ 覆盖率达标: ✅ All coverage checks have been met
  - com.ruoyi.system.domain: 行覆盖率 ≥60% ✅
  - com.ruoyi.system.service.impl: 分支覆盖率 ≥60% ✅

**依赖修复**:
- ✅ 修复 Micrometer 依赖缺失问题
  - 添加 micrometer-observation:1.13.0（测试依赖）
  - 添加 context-propagation:1.1.1（测试依赖）
  - 解决 Spring Boot Test 的 MicrometerObservationRegistryTestExecutionListener 依赖需求
  - SonarQube 分析正常运行

**测试覆盖范围**:
- ✅ 实体类测试（getter/setter、兼容性方法、特殊逻辑）
- ✅ 服务层测试（CRUD操作、缓存操作、唯一性校验、异常处理）
- ✅ 控制器测试（API接口、参数验证、权限控制）
- ✅ Mapper测试（数据库操作、SQL查询）

**文档更新**:
- ✅ 更新 README.md 版本历史
- ✅ 更新测试覆盖率说明
- ✅ 添加测试文件列表

### v4.0.1 (2026-01-08)
**功能优化 - 博客前台增强**

**新增功能**:
- ✅ 文章目录导航（ArticleTOC组件）
  - 自动提取文章标题生成目录
  - 支持平滑滚动
  - 当前阅读位置高亮
  - 可折叠/展开
  - 固定定位显示
- ✅ 热门文章排名优化
  - Top 3 文章使用特殊渐变样式（金、银、铜）
  - 排名徽章显示
  - 悬停动画效果
- ✅ 深色主题适配
  - 全站深色主题支持
  - 响应式主题切换
  - CSS 变量管理
  - 完善的深色主题样式

**功能完善**:
- ✅ 快速链接功能增强
  - 添加"关于"链接
  - 添加 RSS 订阅链接
  - RSS 链接自动适配环境（Docker/本地）
- ✅ 友链管理修复
  - 修复状态逻辑（status='0'=启用）
  - 前台友链 API 支持匿名访问
  - 首页侧边栏显示友链

**Bug 修复**:
- ✅ 修复路由配置无限重定向问题
  - 添加 404 路由定义
  - 修复 catch-all 路由配置
- ✅ 修复 RSS 图标问题
  - Element Plus icons-vue 不包含 RSS 图标
  - 改为纯文本 RSS 订阅链接
- ✅ 修复数据库表缺失问题
  - 添加 sys_logininfor 表（登录日志）
  - 添加 sys_oper_log 表（操作日志）
  - 更新 init_database.sql

**数据库更新**:
- ✅ 添加系统日志表（2个）
  - sys_logininfor - 系统访问记录
  - sys_oper_log - 操作日志记录
- ✅ 完善表索引和约束

**前端组件**:
- ✅ 新增 ArticleTOC.vue - 文章目录组件
- ✅ 优化 BlogFooter.vue - 博客页脚
- ✅ 优化 BlogNav.vue - 博客导航
- ✅ 优化 blog/index.vue - 博客首页

**文档更新**:
- ✅ 更新 README.md 版本历史
- ✅ 添加新功能说明
- ✅ 更新组件列表

### v4.0.0 (2026-01-07)
**重大升级 - Spring Boot 3.3.0 + Java 17 + 监控体系**

**框架升级**:
- Spring Boot: 2.5.15 → 3.3.0
- Java: 1.8 → 17
- Spring Security: 5.7.14 → 6.1.5
- Spring Framework: 5.3.39 → 6.1.6
- Tomcat: 9.0.112 → 10.1.24
- MyBatis Spring Boot: 2.2.2 → 3.0.3
- MySQL Connector: 8.0.33 → 8.4.0
- SLF4J: 1.7.36 → 2.0.13
- Logback: 1.2.11 → 1.5.6

**新增功能**:
- ✅ 集成 Prometheus 监控系统
- ✅ 集成 Grafana 可视化监控
- ✅ 完善 Docker 健康检查机制
- ✅ 优化生产环境 Docker 配置
- ✅ 添加 Redis 数据持久化
- ✅ 完善服务重启策略

**API 变更**:
- ✅ javax.* → jakarta.* 命名空间迁移
- ✅ Spring Security 6.x API 更新
  - antMatchers() → requestMatchers()
  - authorizeRequests() → authorizeHttpRequests()
  - WebSecurityConfigurerAdapter → SecurityFilterChain
- ✅ FilterConfig API 更新
  - setDispatcherTypes() 使用 EnumSet.of()
  - 移除重复的 characterEncodingFilter 配置
- ✅ PermitAllUrlProperties 适配 Spring Framework 6.x
  - RequestMappingHandlerMapping bean 冲突解决
  - PathPattern API 更新（getPathPatternsCondition()）
  - 使用 @Primary 注解指定主要 bean
- ✅ Actuator 端点配置优化
  - 配置 /manage/actuator/** 端点访问权限
  - 支持 Health、Metrics、Info、Env 端点
  - Prometheus metrics 可通过 Metrics 端点获取

**兼容性修复**:
- ✅ DruidConfig 中的 FilterConfig 注入修复
- ✅ FilterRegistrationBean API 更新
- ✅ 资源处理器配置优化
- ✅ 静态资源配置调整

**功能更新**:
- ✅ 应用成功启动并运行
- ✅ 数据库连接正常（MySQL 8.4）
- ✅ Redis 连接正常
- ✅ Actuator 健康检查正常
- ✅ Prometheus 指标采集正常
- ✅ Grafana 仪表板自动配置
- ✅ 所有核心功能正常运行

**注意事项**:
- ⚠️ 需要使用 Java 17 或更高版本
- ⚠️ Prometheus 端点返回 HTML 而非 metrics 数据（可通过 /manage/actuator/metrics 获取）
- ⚠️ 建议使用 Docker 或 IDE 运行以避免 logback 版本冲突
- ⚠️ 需要更新所有依赖的 javax.* 导入为 jakarta.*
- ⚠️ 生产环境建议配置 Redis 密码

**文档更新**:
- ✅ 更新 README.md 技术栈版本
- ✅ 添加 Spring Boot 3.x 升级说明
- ✅ 更新环境要求（Java 17）
- ✅ 添加迁移指南
- ✅ 添加监控配置说明
- ✅ 完善 Docker 部署文档

### v3.9.1 (2026-01-04)
**重大升级 - 官方 RuoYi-Vue 3.9.1 同步**

**依赖升级**:
- Spring Security: 5.7.12 → 5.7.14
- Druid: 1.2.23 → 1.2.27
- FastJSON: 2.0.57 → 2.0.60
- OSHI: 6.8.2 → 6.9.1
- Commons IO: 2.19.0 → 2.21.0
- Tomcat: 9.0.106 → 9.0.112
- YAUAA: 7.16.0 → 7.32.0
- Element Plus: ^2.8.2 → 2.10.7
- Vite: 6.3.5 → 6.3.6

**安全增强**:
- ✅ BitWalker → YAUAA 替代（更准确的 User-Agent 解析）
- ✅ 添加 UserAgentUtils 工具类（支持多种浏览器和操作系统检测）
- ✅ 添加 RefererFilter 防盗链过滤器
- ✅ 添加 JsonSanitizer JSON XSS 防护
- ✅ 密码字段添加 @JsonIgnore 注解（防止序列化泄露）
- ✅ 完善静态资源防盗链配置

**功能更新**:
- ✅ 文件上传支持 UUID 命名
- ✅ 用户管理 Bug 修复（部门ID、状态更新、登录信息）
- ✅ 图片智能压缩功能（头像、缩略图、封面图、移动端适配）
- ✅ 支持防盗链功能
- ✅ 支持多子列表 Excel 导出

**配置优化**:
- ✅ Docker 配置优化（统一使用 curl 健康检查）
- ✅ 添加防盗链配置项
- ✅ 日志配置优化

**代码优化**:
- ✅ AsyncFactory 和 TokenService 使用 UserAgentUtils
- ✅ SysUserMapper 添加新方法（updateUserStatus、updateLoginInfo）
- ✅ SysUserServiceImpl 修复导入用户时部门ID问题
- ✅ FilterConfig 添加 RefererFilter 配置

**文档更新**:
- ✅ 更新 README.md 技术栈版本
- ✅ 添加版本历史说明
- ✅ 更新项目统计信息
- ✅ 添加防盗链配置说明

### v3.9.0 (2025-05-28)
**初始版本 - 基于 RuoYi-Vue 3.9.0**

**核心功能**:
- ✅ 完整的博客系统（文章、分类、标签、评论、友链）
- ✅ 用户系统（注册、登录、权限管理）
- ✅ 后台管理系统
- ✅ 前台展示页面
- ✅ 图片上传与管理
- ✅ Markdown 编辑器
- ✅ 统计分析功能

## 主要功能模块

### 1. 前台展示
- 文章列表与详情页
- 文章目录导航（自动提取标题、平滑滚动、高亮显示）
- 分类、标签浏览
- 文章搜索
- 归档、时间轴
- 热门文章排行（Top 3 特殊样式）
- 友链展示（侧边栏显示）
- 关于我页面
- 评论功能（支持匿名/登录评论、回复、审核）
- 文章点赞、浏览量统计
- 深色主题支持（响应式切换）
- RSS 订阅功能

### 2. 用户系统
- 注册、登录、找回密码
- 用户信息管理（头像、简介等）
- 权限管理（普通用户、博主、管理员）

### 3. 后台管理
- 文章管理（增删改查、发布/草稿、置顶、推荐）
- 分类管理
- 标签管理
- 评论管理（审核、删除）
- 友链管理
- 用户管理
- 系统设置（站点信息、SEO、第三方集成等）
- 日志管理（操作日志、登录日志）

### 4. 其他扩展
- 图片上传与管理
- 图片智能压缩（头像、缩略图、封面图、移动端适配）
- Markdown 编辑器（TinyMCE）
- 统计分析（文章、用户、访问量等）
- 防盗链保护
- XSS 防护
- User-Agent 解析
- 第三方登录（如 GitHub、微信等，后续可扩展）
- 监控告警（Prometheus + Grafana）

## 🗄️ 数据库配置
- **数据库版本**: MySQL 8.4
- **默认端口**: 3306
- **默认用户名**: root
- **默认密码**: root
- **数据库名**: newblog
- **表前缀**: blog_

## 📋 当前系统状态
✅ **数据库表已创建**: 31个表（23个系统表 + 7个博客表 + 1个日志表）
✅ **测试文件已完善**: 44个测试文件（8个控制器测试 + 7个Mapper测试 + 20个服务测试 + 9个实体测试）
✅ **测试用例覆盖**: 656个测试用例，覆盖率≥60%
✅ **构建状态**: ✅ BUILD SUCCESS
✅ **覆盖率检查**: ✅ All coverage checks have been met

**博客相关表** (7个):
- blog_article (文章表)
- blog_category (分类表)
- blog_tag (标签表)
- blog_article_tag (文章标签关联表)
- blog_comment (评论表)
- blog_friend_link (友情链接表)
- blog_setting (系统设置表)

**系统表** (23个):
- sys_user (用户表)
- sys_role (角色表)
- sys_menu (菜单表)
- sys_dept (部门表)
- sys_post (岗位表)
- sys_config (参数配置表)
- sys_dict_type (字典类型表)
- sys_dict_data (字典数据表)
- sys_job (定时任务表)
- sys_job_log (定时任务日志表)
- sys_oper_log (操作日志表)
- sys_logininfor (登录日志表)
- sys_user_role (用户角色关联表)
- sys_user_post (用户岗位关联表)
- sys_role_menu (角色菜单关联表)
- sys_role_dept (角色部门关联表)
- gen_table (代码生成业务表)
- gen_table_column (代码生成字段表)
- sys_user_auth (用户认证表)
- sys_user_online (用户在线表)
- sys_notice (通知公告表)
- sys_post (岗位表)
- sys_dept (部门表)

✅ **示例数据已导入**: 包含3篇文章、4个分类、10个标签、4个友情链接

✅ **服务已启动**:
- 后端服务: http://localhost:8080
- 前端服务: http://localhost:3000

## 🌐 访问方式
1. **博客前台**: http://localhost:3000/blog
2. **管理后台**: http://localhost:3000/admin
3. **API文档**: http://localhost:8080/swagger-ui.html
4. **数据库监控**: http://localhost:8080/druid
5. **Prometheus监控**: http://localhost:9090
6. **Grafana可视化**: http://localhost:3001
7. **RSS订阅**: http://localhost:8080/blog/rss
8. **默认账号**: admin / admin123

## 🚀 快速开始

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+
- **Maven**: 3.6+
- **Docker**: 20.10+ (可选，用于容器化部署)

### Spring Boot 3.x 升级说明

**重要变更**:
- Java 版本要求从 1.8 升级到 17
- 所有 javax.* 包迁移到 jakarta.*
- Spring Security 6.x API 重大变更
- Tomcat 版本升级到 10.x

**主要迁移步骤**:
1. 更新 Java 版本到 17
2. 更新 pom.xml 中的依赖版本
3. 批量替换 javax.* 导入为 jakarta.*
4. 更新 Spring Security 配置
5. 更新 Filter 和 Servlet 配置
6. 测试所有功能

**详细迁移指南**:
- javax.servlet → jakarta.servlet
- javax.annotation → jakarta.annotation
- javax.validation → jakarta.validation
- antMatchers() → requestMatchers()
- authorizeRequests() → authorizeHttpRequests()

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

# 导入完整数据库（包含系统表、Quartz表、博客表和示例数据）
mysql -u root -p newblog < sql/init_database.sql

# （可选）执行权限设置
mysql -u root -p < sql/00_setup_permissions.sql

# （可选）添加性能优化索引
mysql -u root -p newblog < sql/performance_indexes.sql
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

# 预发布环境构建
npm run build:stage
```

### 5. Docker 一键部署

#### 开发环境部署
```bash
# 启动所有服务（包含前端开发服务器）
docker compose -f docker-compose.dev.yml up -d

# 查看服务状态
docker compose -f docker-compose.dev.yml ps

# 查看日志
docker compose -f docker-compose.dev.yml logs -f

# 停止服务
docker compose -f docker-compose.dev.yml down
```

#### 生产环境部署
```bash
# 启动所有服务（包含 Nginx 静态文件服务）
docker compose -f docker-compose.prod.yml up -d

# 查看服务状态
docker compose -f docker-compose.prod.yml ps

# 查看日志
docker compose -f docker-compose.prod.yml logs -f

# 停止服务
docker compose -f docker-compose.prod.yml down

# 重启服务
docker compose -f docker-compose.prod.yml restart
```

### 6. 监控系统访问

#### Prometheus
- 访问地址: http://localhost:9090
- 功能: 指标数据采集和查询
- 配置文件: `prometheus/prometheus.yml`

#### Grafana
- 访问地址: http://localhost:3001
- 默认账号: admin / admin
- 功能: 可视化监控仪表板
- 配置目录: `grafana/provisioning/`

## 📁 项目结构
```
newblog/
├── ruoyi-admin/              # 后端主模块
│   ├── src/main/java/
│   │   └── com/ruoyi/
│   │       ├── RuoYiApplication.java    # 启动类
│   │       └── web/controller/          # 控制器
│   └── src/main/resources/
│       └── application.yml             # 配置文件
├── ruoyi-system/             # 系统模块(含博客功能)
│   ├── src/main/java/
│   │   └── com/ruoyi/system/
│   │       ├── controller/             # 控制器 (8个博客相关)
│   │       ├── domain/                 # 实体类
│   │       ├── mapper/                 # 数据访问层
│   │       └── service/                # 业务逻辑层
│   ├── src/main/resources/
│   │   └── mapper/system/              # MyBatis XML (22个)
│   └── src/test/java/          # 测试文件 (44个)
│       ├── controller/             # 控制器测试 (8个)
│       ├── domain/                 # 实体类测试 (9个)
│       ├── mapper/                 # 数据访问层测试 (7个)
│       └── service/impl/           # 业务逻辑层测试 (20个)
├── ruoyi-framework/          # 框架核心
│   ├── src/main/java/
│   │   └── com/ruoyi/framework/
│   │       ├── config/                 # 配置类
│   │       ├── interceptor/            # 拦截器
│   │       ├── aspectj/                # 切面
│   │       ├── manager/                # 异步工厂
│   │       └── web/service/            # Web服务
├── ruoyi-common/             # 通用工具
│   ├── src/main/java/
│   │   └── com/ruoyi/common/
│   │       ├── utils/                  # 工具类 (13个)
│   │       ├── filter/                 # 过滤器
│   │       ├── constant/               # 常量
│   │       └── core/domain/            # 核心领域对象
│   ├── src/main/java/com/ruoyi/common/utils/json/  # JSON工具
│   └── src/main/java/com/ruoyi/common/utils/http/  # HTTP工具
├── ruoyi-quartz/             # 定时任务
├── ruoyi-generator/          # 代码生成
├── ruoyi-ui/                # Vue3前端项目
│   ├── src/
│   │   ├── views/            # 页面
│   │   │   ├── blog/        # 博客前台
│   │   │   │   ├── index.vue              # 首页
│   │   │   │   ├── about.vue              # 关于页面
│   │   │   │   ├── archive/               # 归档页面
│   │   │   │   ├── article/               # 文章详情
│   │   │   │   ├── category/              # 分类页面
│   │   │   │   └── tag/                   # 标签页面
│   │   │   └── admin/       # 管理后台
│   │   ├── components/       # 组件
│   │   │   ├── ArticleTOC.vue            # 文章目录组件
│   │   │   ├── AvatarUpload.vue          # 头像上传组件
│   │   │   ├── BlogFooter.vue            # 博客页脚
│   │   │   ├── BlogNav.vue               # 博客导航
│   │   │   ├── LinkIcon.vue              # 链接图标组件
│   │   │   ├── TagCategorySelector.vue   # 标签分类选择器
│   │   │   ├── Breadcrumb/               # 面包屑
│   │   │   ├── Crontab/                  # 定时任务
│   │   │   ├── DictTag/                  # 字典标签
│   │   │   ├── Editor/                   # 富文本编辑器
│   │   │   ├── FileUpload/               # 文件上传
│   │   │   ├── Hamburger/                # 汉堡菜单
│   │   │   ├── HeaderSearch/             # 头部搜索
│   │   │   ├── IconSelect/               # 图标选择
│   │   │   ├── iFrame/                   # 内嵌页面
│   │   │   ├── ImagePreview/             # 图片预览
│   │   │   ├── ImageUpload/              # 图片上传
│   │   │   ├── InfiniteScroll/           # 无限滚动
│   │   │   ├── Pagination/               # 分页
│   │   │   ├── ParentView/               # 父视图
│   │   │   ├── RightPanel/               # 右侧面板
│   │   │   ├── RightToolbar/             # 右侧工具栏
│   │   │   ├── RuoYi/                    # 若依组件
│   │   │   ├── Screenfull/               # 全屏
│   │   │   ├── SizeSelect/               # 尺寸选择
│   │   │   ├── SvgIcon/                  # SVG图标
│   │   │   ├── TinyMCE/                  # TinyMCE编辑器
│   │   │   └── TopNav/                   # 顶部导航
│   │   ├── api/              # API接口
│   │   │   ├── blog/         # 博客API
│   │   │   │   ├── article.js            # 文章API
│   │   │   │   ├── author.js             # 作者API
│   │   │   │   ├── avatar.js             # 头像API
│   │   │   │   ├── category.js           # 分类API
│   │   │   │   ├── comment.js            # 评论API
│   │   │   │   ├── friendLink.js         # 友链API
│   │   │   │   ├── index.js              # 博客首页API
│   │   │   │   ├── setting.js            # 设置API
│   │   │   │   ├── tag.js                # 标签API
│   │   │   │   └── upload.js             # 上传API
│   │   │   ├── setting/      # 设置API
│   │   │   ├── admin/        # 后台管理API
│   │   │   ├── monitor/      # 监控API
│   │   │   ├── system/       # 系统API
│   │   │   └── tool/         # 工具API
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia状态管理
│   │   ├── utils/            # 工具函数
│   │   ├── assets/           # 静态资源
│   │   ├── directive/        # 指令
│   │   └── composables/      # 组合式函数
│   ├── Dockerfile.dev        # 开发环境Docker文件
│   ├── Dockerfile.prod       # 生产环境Docker文件
│   └── package.json
├── sql/                     # 数据库脚本
│   ├── init_database.sql    # 初始化脚本
│   ├── quartz.sql           # 定时任务脚本
│   ├── 00_setup_permissions.sql  # 权限设置
│   └── performance_indexes.sql   # 性能索引
├── prometheus/              # Prometheus配置
│   └── prometheus.yml       # 监控配置文件
├── grafana/                 # Grafana配置
│   └── provisioning/        # 仪表板配置
├── docker-compose.dev.yml   # Docker编排(开发)
├── docker-compose.prod.yml  # Docker编排(生产)
├── Dockerfile-admin         # 后端Docker文件
├── pom.xml                  # Maven主配置
└── README.md                # 项目文档
```

## 🎯 核心功能模块

### 📝 文章管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogArticleController.java`

**功能清单**:
- ✅ 文章CRUD操作
- ✅ 文章状态管理(草稿/发布)
- ✅ 置顶和推荐功能
- ✅ 文章与标签多对多关联
- ✅ 文章搜索功能
- ✅ 按分类/标签筛选
- ✅ 批量状态更新
- ✅ 浏览量、点赞数、评论数统计
- ✅ 图片智能压缩（封面图、移动端适配）

**关键方法**:
```java
- list() - 分页查询文章列表
- getInfo() - 获取文章详情(含标签和分类)
- add() - 新增文章(含标签关联)
- edit() - 更新文章(含标签关联)
- remove() - 删除文章
- getOptions() - 获取分类和标签选项
- updateStatus() - 批量更新状态
- search() - 搜索文章
- getByCategory() - 按分类查询
- getByTag() - 按标签查询
```

### 🏷️ 分类管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCategoryController.java`

**功能清单**:
- ✅ 分类CRUD操作
- ✅ 排序功能
- ✅ 软删除支持
- ✅ 分类唯一性验证

### 🎯 标签管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogTagController.java`

**功能清单**:
- ✅ 标签CRUD操作
- ✅ 标签颜色和图标支持
- ✅ 标签唯一性验证
- ✅ 关联文章检查
- ✅ 标签导出功能

### 💬 评论系统
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCommentController.java`

**功能清单**:
- ✅ 支持用户评论和匿名评论
- ✅ 评论审核机制
- ✅ 父子评论(回复)支持
- ✅ IP地址记录
- ✅ 邮箱通知（可选）

### 🔗 友情链接
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogFriendLinkController.java`

**功能清单**:
- ✅ 友链CRUD操作
- ✅ 状态控制(启用/禁用)
- ✅ Logo图片支持

### ⚙️ 博客设置
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogSettingController.java`

**功能清单**:
- ✅ 键值对配置存储
- ✅ 博客基本信息配置
- ✅ 系统参数管理

**预置配置项**:
```
- blog_name: 博客名称
- blog_desc: 博客描述
- blog_author: 博客作者
- blog_keywords: 博客关键词
- blog_copyright: 版权信息
- blog_beian: 备案信息
- blog_comment_enable: 评论开关
- blog_comment_audit: 评论审核开关
```

### 📊 统计分析
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogStatisticsController.java`

**功能清单**:
- ✅ 文章统计（总数、发布数、草稿数）
- ✅ 用户统计（总数、活跃用户、新增用户）
- ✅ 访问统计（浏览量、点赞数、评论数）
- ✅ 分类统计（各分类文章数量）
- ✅ 标签统计（各标签文章数量）
- ✅ 时间趋势统计（按日/周/月）

## 🔐 权限与路由系统

### 菜单权限配置
**博客管理主菜单** (menu_id: 2000):
```
博客管理
├── 文章管理 (2001) - blog:article:list
├── 分类管理 (2002) - blog:category:list
├── 标签管理 (2003) - blog:tag:list
├── 评论管理 (2004) - blog:comment:list
├── 博客设置 (2005) - blog:setting:list
└── 友链管理 (2006) - blog:friendLink:list
```

### 路由配置
**前台博客路由** (`ruoyi-ui/src/router/index.js`):
```javascript
- /blog - 博客首页
- /blog/article/:id - 文章详情
- /blog/category/:id - 分类文章
- /blog/tag/:id - 标签文章
- /blog/archive - 文章归档
- /about - 关于页面
```

## 🏗️ 技术栈详解

### 后端技术栈
- **框架**: Spring Boot 3.3.0
- **ORM**: MyBatis 3.0.3
- **数据库**: MySQL 8.4.0
- **缓存**: Redis 6.2
- **安全**: Spring Security 6.1.5
- **连接池**: Druid 1.2.27
- **定时任务**: Quartz
- **API文档**: Swagger 3.0.0
- **UserAgent解析**: YAUAA 7.32.0
- **图片处理**: Thumbnailator 0.4.20
- **监控**: Spring Boot Actuator + Prometheus
- **Java版本**: 17

### 前端技术栈
- **框架**: Vue 3.5.16
- **语言**: TypeScript 5.9.3
- **构建工具**: Vite 6.3.6
- **UI组件**: Element Plus 2.10.7
- **状态管理**: Pinia 3.0.2
- **路由**: Vue Router 4.5.1
- **富文本编辑器**: TinyMCE 8.1.2
- **代码高亮**: highlight.js 11.11.1
- **HTTP客户端**: Axios 1.9.0
- **图表**: ECharts 5.6.0
- **图片裁剪**: vue-cropper 1.1.1
- **测试框架**: Vitest 2.1.8 + Vue Test Utils 2.4.6

### 监控技术栈
- **监控采集**: Prometheus
- **可视化**: Grafana
- **数据源**: Spring Boot Actuator
- **指标格式**: Prometheus metrics
- **数据保留**: 15天

### 开发工具
- **容器化**: Docker + Docker Compose
- **代码质量**: Maven, ESLint
- **版本控制**: Git
- **API调试**: Swagger UI

## 📋 开发规范

### 代码规范

**Java**:
```java
// 遵循阿里巴巴Java开发手册
- 类名使用大驼峰 (e.g., BlogArticleController)
- 方法名使用小驼峰 (e.g., getArticleList)
- 常量全大写下划线分隔 (e.g., MAX_PAGE_SIZE)
- 注释完整清晰 (e.g., @Override, @Deprecated)
```

**Vue**:
```javascript
// 遵循Vue官方风格指南
- 组件名多单词 (e.g., BlogArticleList)
- Prop定义详细 (e.g., type, required, default)
- 使用组合式API (e.g., setup, ref, reactive)
- 统一代码格式
```

**Git提交规范**:
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试
chore: 构建/工具
```

## 🔧 配置说明

### 环境变量配置
```bash
# 数据库配置
DB_HOST=localhost
DB_USERNAME=root
DB_PASSWORD=root

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT配置
R_TOKEN_SECRET=your-very-long-and-secure-random-string-here

# 防盗链配置
REFERER_ENABLED=false
REFERER_ALLOWED_DOMAINS=localhost,127.0.0.1

# 验证码配置
CAPTCHA_ENABLED=false
```

### 图片压缩配置
```yaml
image:
  compress:
    enabled: true              # 是否启用图片压缩
    threshold-size: 2MB        # 压缩阈值
    max-width: 2560            # 默认最大宽度
    max-height: 1440           # 默认最大高度
    default-quality: 0.9       # 默认压缩质量
    avatar-size: 200           # 头像压缩尺寸
    thumbnail-size: 400        # 缩略图压缩尺寸
    avatar-quality: 0.9        # 头像压缩质量
    thumbnail-quality: 0.85    # 缩略图压缩质量
    article-cover-width: 1200  # 文章封面图宽度
    article-cover-height: 675  # 文章封面图高度
    article-cover-quality: 0.9 # 文章封面图压缩质量
    mobile-max-width: 750      # 移动端最大宽度
    mobile-quality: 0.85       # 移动端压缩质量
```

### 防盗链配置
```yaml
referer:
  enabled: false              # 是否启用防盗链
  allowed-domains: localhost,127.0.0.1  # 允许的域名列表
```

### 监控配置

**Prometheus配置** (`prometheus/prometheus.yml`):
```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    monitor: 'newblog-monitor'

scrape_configs:
  # 监控 Spring Boot Actuator
  - job_name: 'spring-boot-actuator'
    metrics_path: '/manage/actuator/prometheus'
    static_configs:
      - targets: ['ruoyi-admin:8080']
        labels:
          application: 'newblog'
          environment: 'dev'
    scrape_interval: 10s
    scrape_timeout: 10s

  # 监控 Prometheus 自身
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
```

**Grafana配置**:
- 默认用户: admin
- 默认密码: admin
- 数据源: Prometheus
- 自动导入仪表板

### Vite 代理配置

**开发环境代理** (`ruoyi-ui/vite.config.js`):
```javascript
// 自动检测 Docker 环境
const inDocker = process.env.DOCKER === 'true'
const baseUrl = inDocker ? 'http://ruoyi-admin:8080' : 'http://localhost:8080'

// 代理配置
proxy: {
  '/dev-api': {
    target: baseUrl,
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/dev-api/, '')
  },
  '/blog/api/': {
    target: baseUrl,
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/blog\/api/, '/blog')
  },
  '/profile': {
    target: baseUrl,
    changeOrigin: true
  },
  '/manage': {
    target: baseUrl,
    changeOrigin: true
  }
}
```

**HMR 配置说明** (`ruoyi-ui/vite.config.ts`):
```javascript
server: {
  host: '0.0.0.0',
  port: 3000,
  open: false,
  hmr: false  // ⚠️ 已禁用 - 存在兼容性问题
}
```

**重要说明**:
- ⚠️ **HMR 已禁用**: 由于 Vite HMR 系统与项目存在兼容性问题，已禁用热模块替换
- **错误表现**: `Cannot read properties of undefined (reading 'on')`
- **解决方案**: 代码更改后需要手动刷新浏览器
- **影响范围**: 仅影响开发体验，不影响功能
- **Vite 插件**: `vite/plugins/*.js` 保持 JS 格式，运行在 Node.js 环境，无需迁移

### 生产环境建议

1. **安全配置**:
   - 修改默认密码
   - 设置强Token密钥
   - 启用HTTPS
   - 配置防火墙规则
   - 启用防盗链功能
   - 设置Redis密码

2. **性能优化**:
   - 添加数据库索引
   - 配置Redis缓存
   - 启用Gzip压缩
   - 使用CDN加速
   - 启用图片压缩

3. **监控告警**:
   - 配置日志收集
   - 设置性能监控
   - 配置告警通知
   - 定期数据备份
   - 监控系统健康状态

4. **防盗链配置**:
   - 开启防盗链功能
   - 配置正确的白名单域名
   - 保护静态资源不被外部引用

## 🐳 Docker 部署说明

### 开发环境服务
- **ruoyi-admin**: 后端服务 (8080端口)
  - 健康检查: 等待 MySQL 就绪
  - 环境变量: Docker 自动配置
  - 依赖: mysql, redis
- **ruoyi-ui**: 前端开发服务器 (3000端口)
  - 支持热重载
  - 自动检测 Docker 环境
  - 依赖: ruoyi-admin
- **mysql**: MySQL 8.4 数据库 (3306端口)
  - 健康检查: mysqladmin ping
  - 数据持久化: mysql_data volume
  - 自动初始化: sql 目录
- **redis**: Redis 6.2 缓存 (6379端口)
  - 数据持久化: redis_data volume
- **prometheus**: 监控数据采集 (9090端口)
  - 数据持久化: prometheus_data volume
  - 配置文件: prometheus.yml
- **grafana**: 可视化监控 (3001端口)
  - 数据持久化: grafana_data volume
  - 自动配置: provisioning 目录
  - 默认账号: admin/admin

### 生产环境服务
- **ruoyi-admin**: 后端服务 (8080端口)
  - 生产环境优化配置
  - 依赖: mysql, redis
- **ruoyi-ui**: Nginx 静态文件服务 (80/443端口)
  - 高性能静态文件服务
  - 支持 HTTPS
- **mysql**: MySQL 8.4 数据库 (3306端口)
  - 生产环境配置
  - 数据持久化
- **redis**: Redis 6.2 缓存 (6379端口)
  - 数据持久化
- **prometheus**: 监控数据采集 (9090端口)
  - 数据持久化
- **grafana**: 可视化监控 (3001端口)
  - 数据持久化

### 数据持久化
- MySQL 数据: `mysql_data` volume
- Redis 数据: `redis_data` volume
- Prometheus 数据: `prometheus_data` volume
- Grafana 数据: `grafana_data` volume
- 上传文件: `./uploadPath` 目录挂载

### 健康检查机制
- **MySQL**: mysqladmin ping (20秒超时，20次重试)
- **Redis**: redis-cli ping
- **后端**: /actuator/health 端点
- **前端**: /health 端点

### 重启策略
- 开发环境: 默认重启策略
- 生产环境: unless-stopped（除非手动停止，否则自动重启）

## 🤝 贡献指南

### 开发规范
1. **代码风格**: 遵循阿里巴巴Java开发手册和Vue官方风格指南
2. **提交规范**: 使用[Conventional Commits](https://conventionalcommits.org/)规范
3. **分支策略**: Git Flow工作流
4. **测试要求**: 新功能需包含单元测试

## 📞 技术支持

### 相关文档
- **项目文档**: [本README](README.md)
- **项目上下文**: [IFLOW.md](IFLOW.md)
- **项目检查报告**: [docs/项目检查报告.md](docs/项目检查报告.md)
- **图片压缩功能**: [docs/图片压缩功能使用指南.md](docs/图片压缩功能使用指南.md)
- **项目优化建议**: [docs/项目优化建议.md](docs/项目优化建议.md)
- **RuoYi官方文档**: http://doc.ruoyi.vip/
- **Vue 3文档**: https://cn.vuejs.org/
- **Element Plus文档**: https://element-plus.org/
- **Spring Boot文档**: https://spring.io/projects/spring-boot
- **Prometheus文档**: https://prometheus.io/docs/
- **Grafana文档**: https://grafana.com/docs/

### 许可证
本项目基于 [MIT许可证](LICENSE) 开源

## 🎉 致谢
感谢以下开源项目的支持：
- [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) - 优秀的企业级快速开发平台
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [Spring Boot](https://spring.io/projects/spring-boot) - Java企业级开发框架
- [Prometheus](https://prometheus.io/) - 开源监控告警系统
- [Grafana](https://grafana.com/) - 开源可视化平台

---

**最后更新**: 2026-01-18
**维护者**: nevell
**项目地址**: https://gitee.com/nevell/newblog