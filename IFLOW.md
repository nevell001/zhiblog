# IFLOW 项目上下文文档

## 项目概述

**项目名称**: newblog (基于 RuoYi-Vue 的博客系统)
**项目类型**: 前后端分离的企业级博客系统
**当前版本**: v4.0.0
**开发语言**: Java 17 + Vue 3.5.16
**最后更新**: 2026-01-07

### 项目简介

本项目基于 RuoYi-Vue 3.9.1 快速开发平台，打造一个现代化、支持多用户、前后端分离的企业级博客系统。采用 Spring Boot 3.3.0 + Vue 3 + Element Plus 技术栈，集成文章发布、评论互动、标签分类、友情链接、后台管理等完整博客功能，适合个人或团队搭建高效、可扩展的博客平台。

### 技术栈

#### 后端技术栈
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

#### 前端技术栈
- **框架**: Vue 3.5.16
- **构建工具**: Vite 6.3.6
- **UI组件**: Element Plus 2.10.7
- **状态管理**: Pinia 3.0.2
- **路由**: Vue Router 4.5.1
- **富文本编辑器**: TinyMCE 8.1.2
- **代码高亮**: highlight.js 11.11.1
- **HTTP客户端**: Axios 1.9.0
- **图表**: ECharts 5.6.0
- **图片裁剪**: vue-cropper 1.1.1

## 项目结构

```
newblog/
├── ruoyi-admin/              # 后端主模块 (启动模块)
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
│   └── src/main/resources/
│       └── mapper/system/              # MyBatis XML
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
│   │       ├── utils/                  # 工具类
│   │       ├── filter/                 # 过滤器
│   │       ├── constant/               # 常量
│   │       └── core/domain/            # 核心领域对象
├── ruoyi-quartz/             # 定时任务
├── ruoyi-generator/          # 代码生成
├── ruoyi-ui/                 # Vue3前端项目
│   ├── src/
│   │   ├── views/            # 页面
│   │   │   ├── blog/         # 博客前台
│   │   │   └── admin/        # 管理后台
│   │   ├── components/       # 组件
│   │   ├── api/              # API接口
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia状态管理
│   │   └── utils/            # 工具函数
│   ├── Dockerfile.dev        # 开发环境Docker文件
│   ├── Dockerfile.prod       # 生产环境Docker文件
│   └── package.json
├── sql/                      # 数据库脚本
│   ├── init_database.sql    # 初始化脚本
│   ├── quartz.sql           # 定时任务脚本
│   ├── 00_setup_permissions.sql  # 权限设置
│   └── performance_indexes.sql   # 性能索引
├── docker-compose.dev.yml   # Docker编排(开发)
├── docker-compose.prod.yml  # Docker编排(生产)
├── Dockerfile-admin         # 后端Docker文件
├── prometheus/              # Prometheus配置
│   └── prometheus.yml
├── grafana/                 # Grafana配置
│   └── provisioning/
├── pom.xml                  # Maven主配置
└── README.md                # 项目文档
```

## 核心功能模块

### 1. 文章管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogArticleController.java`

**功能清单**:
- 文章CRUD操作
- 文章状态管理(草稿/发布)
- 置顶和推荐功能
- 文章与标签多对多关联
- 文章搜索功能
- 按分类/标签筛选
- 批量状态更新
- 浏览量、点赞数、评论数统计
- 图片智能压缩（封面图、移动端适配）

**关键方法**:
- `list()` - 分页查询文章列表
- `getInfo()` - 获取文章详情(含标签和分类)
- `add()` - 新增文章(含标签关联)
- `edit()` - 更新文章(含标签关联)
- `remove()` - 删除文章
- `getOptions()` - 获取分类和标签选项
- `updateStatus()` - 批量更新状态
- `search()` - 搜索文章
- `getByCategory()` - 按分类查询
- `getByTag()` - 按标签查询

### 2. 分类管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCategoryController.java`

**功能清单**:
- 分类CRUD操作
- 排序功能
- 软删除支持
- 分类唯一性验证

### 3. 标签管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogTagController.java`

**功能清单**:
- 标签CRUD操作
- 标签颜色和图标支持
- 标签唯一性验证
- 关联文章检查
- 标签导出功能

### 4. 评论系统
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCommentController.java`

**功能清单**:
- 支持用户评论和匿名评论
- 评论审核机制
- 父子评论(回复)支持
- IP地址记录
- 邮箱通知（可选）

### 5. 友情链接
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogFriendLinkController.java`

**功能清单**:
- 友链CRUD操作
- 状态控制(启用/禁用)
- Logo图片支持

### 6. 博客设置
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogSettingController.java`

**功能清单**:
- 键值对配置存储
- 博客基本信息配置
- 系统参数管理

**预置配置项**:
- `blog_name`: 博客名称
- `blog_desc`: 博客描述
- `blog_author`: 博客作者
- `blog_keywords`: 博客关键词
- `blog_copyright`: 版权信息
- `blog_beian`: 备案信息
- `blog_comment_enable`: 评论开关
- `blog_comment_audit`: 评论审核开关

### 7. 统计分析
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogStatisticsController.java`

**功能清单**:
- 文章统计（总数、发布数、草稿数）
- 用户统计（总数、活跃用户、新增用户）
- 访问统计（浏览量、点赞数、评论数）
- 分类统计（各分类文章数量）
- 标签统计（各标签文章数量）
- 时间趋势统计（按日/周/月）

## 数据库配置

- **数据库版本**: MySQL 8.4
- **默认端口**: 3306
- **默认用户名**: root
- **默认密码**: root
- **数据库名**: newblog
- **表前缀**: blog_

### 数据库表结构

**博客相关表** (7个):
- `blog_article` - 文章表
- `blog_category` - 分类表
- `blog_tag` - 标签表
- `blog_article_tag` - 文章标签关联表
- `blog_comment` - 评论表
- `blog_friend_link` - 友情链接表
- `blog_setting` - 系统设置表

**系统表** (22个):
- `sys_user` - 用户表
- `sys_role` - 角色表
- `sys_menu` - 菜单表
- `sys_dept` - 部门表
- `sys_post` - 岗位表
- `sys_config` - 参数配置表
- `sys_dict_type` - 字典类型表
- `sys_dict_data` - 字典数据表
- `sys_job` - 定时任务表
- `sys_job_log` - 定时任务日志表
- `sys_oper_log` - 操作日志表
- `sys_logininfor` - 登录日志表
- `sys_user_role` - 用户角色关联表
- `sys_user_post` - 用户岗位关联表
- `sys_role_menu` - 角色菜单关联表
- `sys_role_dept` - 角色部门关联表
- `gen_table` - 代码生成业务表
- `gen_table_column` - 代码生成字段表
- 等其他系统表...

## 构建和运行

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+
- **Maven**: 3.6+

### 后端启动

```bash
# 编译项目
cd /home/nevell/code/newblog
mvn clean install -DskipTests

# 启动后端服务
cd ruoyi-admin
mvn spring-boot:run
```

### 前端启动

```bash
# 安装依赖
cd /home/nevell/code/newblog/ruoyi-ui
npm install

# 启动开发服务器
npm run dev

# 生产环境构建
npm run build:prod

# 预发布环境构建
npm run build:stage
```

### Docker 一键部署

```bash
# 开发环境
cd /home/nevell/code/newblog
docker compose -f docker-compose.dev.yml up -d

# 生产环境
docker compose -f docker-compose.prod.yml up -d

# 查看服务状态
docker compose -f docker-compose.dev.yml ps

# 查看日志
docker compose -f docker-compose.dev.yml logs -f

# 停止服务
docker compose -f docker-compose.dev.yml down

# 重启服务
docker compose -f docker-compose.dev.yml restart
```

### 数据库初始化

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入完整数据库
mysql -u root -p newblog < sql/init_database.sql

# （可选）执行权限设置
mysql -u root -p < sql/00_setup_permissions.sql

# （可选）添加性能优化索引
mysql -u root -p newblog < sql/performance_indexes.sql
```

## 访问方式

| 访问地址 | 用途 | 说明 |
|----------|------|------|
| http://localhost:3000/blog | 博客前台 | 前台展示页面 |
| http://localhost:3000/admin | 后台管理 | 管理后台 |
| http://localhost:8080 | 后端API | 后端服务 |
| http://localhost:8080/swagger-ui.html | API文档 | Swagger文档 |
| http://localhost:8080/druid | 数据库监控 | Druid监控台 |
| http://localhost:9090 | Prometheus | 监控数据 |
| http://localhost:3001 | Grafana | 可视化监控 |

**默认账号**: admin / admin123

## 配置文件

### 后端配置
**配置文件**: `ruoyi-admin/src/main/resources/application.yml`

**关键配置项**:
- 服务端口: 8080
- 数据库连接: MySQL 8.4
- Redis配置: localhost:6379
- Token配置: 默认密钥（生产环境需更换）
- 图片压缩配置: 已启用
- 防盗链配置: 默认关闭
- Actuator监控: 已启用
- Prometheus监控: 已集成

### 前端配置
**配置文件**: `ruoyi-ui/vite.config.js`, `ruoyi-ui/.env.development`, `ruoyi-ui/.env.production`

**关键配置项**:
- 开发服务器端口: 3000
- API代理: /dev-api → http://localhost:8080
- 构建输出: dist/
- Docker环境支持: 自动检测并切换后端地址

### 监控配置

**Prometheus配置**: `prometheus/prometheus.yml`
- 采集频率: 15秒
- 数据保留: 15天
- 监控目标: Spring Boot Actuator 端点

**Grafana配置**: `grafana/provisioning/`
- 默认用户: admin
- 默认密码: admin
- 数据源: Prometheus
- 自动导入仪表板

## 开发规范

### 代码规范

**Java**:
- 类名使用大驼峰 (e.g., BlogArticleController)
- 方法名使用小驼峰 (e.g., getArticleList)
- 常量全大写下划线分隔 (e.g., MAX_PAGE_SIZE)
- 注释完整清晰 (e.g., @Override, @Deprecated)
- 遵循阿里巴巴Java开发手册

**Vue**:
- 组件名多单词 (e.g., BlogArticleList)
- Prop定义详细 (e.g., type, required, default)
- 使用组合式API (e.g., setup, ref, reactive)
- 遵循Vue官方风格指南

### Git提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试
chore: 构建/工具
```

## 权限与路由系统

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

**后台管理路由**:
- `/admin` - 后台管理系统
- 基于权限动态加载路由
- 路由守卫权限验证
- 组件懒加载优化

## Spring Boot 3.x 升级说明

### 重要变更
- Java 版本要求从 1.8 升级到 17
- 所有 javax.* 包迁移到 jakarta.*
- Spring Security 6.x API 重大变更
- Tomcat 版本升级到 10.x

### 主要迁移步骤
1. 更新 Java 版本到 17
2. 更新 pom.xml 中的依赖版本
3. 批量替换 javax.* 导入为 jakarta.*
4. 更新 Spring Security 配置
5. 更新 Filter 和 Servlet 配置
6. 测试所有功能

### API 变更示例
- `javax.servlet` → `jakarta.servlet`
- `javax.annotation` → `jakarta.annotation`
- `javax.validation` → `jakarta.validation`
- `antMatchers()` → `requestMatchers()`
- `authorizeRequests()` → `authorizeHttpRequests()`

## 特色功能

### 1. 图片智能压缩
- 支持头像、缩略图、封面图、移动端适配
- 可配置压缩阈值和质量
- 自动调整图片尺寸
- 基于 Thumbnailator 库
- 三种压缩策略：智能压缩、头像压缩、缩略图压缩

**压缩接口**:
- `POST /common/upload/compressed` - 智能压缩
- `POST /common/upload/avatar` - 头像压缩 (200x200)
- `POST /common/upload/thumbnail` - 缩略图压缩 (400x400)

### 2. 防盗链保护
- 白名单控制
- 保护静态资源
- 可配置启用/禁用
- 保护 `/profile/**` 路径下的资源

### 3. JSON XSS 防护
- 使用 JsonSanitizer
- 自动过滤恶意代码
- 保护数据安全
- 可配置排除路径

### 4. User-Agent 解析
- 使用 YAUAA 库
- 支持多种浏览器和操作系统检测
- 精确的设备识别
- 替代了旧的 BitWalker 库

### 5. 监控与告警
- Spring Boot Actuator 集成
- Prometheus 指标采集
- Grafana 可视化仪表板
- 健康检查、性能监控、日志管理

## 监控与运维

### Spring Boot Actuator
**端点配置**:
- `/manage/actuator/health` - 健康检查
- `/manage/actuator/info` - 应用信息
- `/manage/actuator/metrics` - 指标数据
- `/manage/actuator/env` - 环境变量
- `/manage/actuator/prometheus` - Prometheus 指标
- `/manage/actuator/configprops` - 配置属性

### Prometheus 集成
- 已集成 micrometer-registry-prometheus
- 支持应用指标收集
- 可配合 Grafana 进行可视化
- 15秒采集间隔
- 15天数据保留

### Grafana 仪表板
- 预配置数据源
- 自动导入仪表板
- 实时监控展示
- 告警通知支持

### 日志管理
- 日志级别配置
- MyBatis SQL 日志
- 操作日志记录
- 登录日志记录
- 定时任务日志

## 常见问题

### 1. 编译问题
**问题**: Java 版本不兼容
**解决**: 确保使用 Java 17 或更高版本

### 2. 数据库连接问题
**问题**: 无法连接到 MySQL
**解决**: 检查 MySQL 服务是否启动，端口配置是否正确

### 3. Redis 连接问题
**问题**: 无法连接到 Redis
**解决**: 检查 Redis 服务是否启动，密码配置是否正确

### 4. 前端构建问题
**问题**: npm install 失败
**解决**: 删除 node_modules 和 package-lock.json，重新安装

### 5. 权限问题
**问题**: 用户无法访问某些功能
**解决**: 检查角色权限配置，菜单权限是否正确分配

### 6. Docker 部署问题
**问题**: 容器启动失败
**解决**: 检查 docker-compose.yml 配置，确保端口未被占用，网络配置正确

### 7. 图片上传问题
**问题**: 图片上传失败或压缩不生效
**解决**: 检查 uploadPath 目录权限，确认图片压缩配置已启用

## Docker 服务说明

### 开发环境服务 (`docker-compose.dev.yml`)
- **ruoyi-admin**: 后端服务 (8080端口)
- **ruoyi-ui**: 前端开发服务器 (3000端口)
- **mysql**: MySQL 8.4 数据库 (3306端口)
- **redis**: Redis 6.2 缓存 (6379端口)
- **prometheus**: 监控数据采集 (9090端口)
- **grafana**: 可视化监控 (3001端口)

### 生产环境服务 (`docker-compose.prod.yml`)
- **ruoyi-admin**: 后端服务 (8080端口)
- **ruoyi-ui**: Nginx 静态文件服务 (80/443端口)
- **mysql**: MySQL 8.4 数据库 (3306端口)
- **redis**: Redis 6.2 缓存 (6379端口)
- **prometheus**: 监控数据采集 (9090端口)
- **grafana**: 可视化监控 (3001端口)

### 数据持久化
- MySQL 数据: `mysql_data` volume
- Prometheus 数据: `prometheus_data` volume
- Grafana 数据: `grafana_data` volume
- 上传文件: `./uploadPath` 目录挂载

## 项目优势

1. **企业级架构**: 基于成熟的 RuoYi-Vue 框架，稳定可靠
2. **前后端分离**: Vue 3 + Spring Boot，现代化开发体验
3. **美观界面**: Element Plus 组件库，响应式设计
4. **权限完善**: 基于 Spring Security 的细粒度权限控制
5. **功能丰富**: 文章、评论、标签、分类、统计等完整功能
6. **容器化部署**: Docker + Docker Compose 一键部署
7. **安全加固**: 多层安全防护，XSS、CSRF、SQL注入防护
8. **性能优化**: 图片压缩、Redis缓存、数据库索引
9. **监控完善**: Prometheus + Grafana 全链路监控
10. **开发友好**: 热部署、自动重启、详细日志

## 相关文档

- **项目文档**: [README.md](README.md)
- **项目检查报告**: [docs/项目检查报告.md](docs/项目检查报告.md)
- **图片压缩功能**: [docs/图片压缩功能使用指南.md](docs/图片压缩功能使用指南.md)
- **项目优化建议**: [docs/项目优化建议.md](docs/项目优化建议.md)
- **PR描述**: [docs/PR_DESCRIPTION.md](docs/PR_DESCRIPTION.md)
- **RuoYi官方文档**: http://doc.ruoyi.vip/
- **Vue 3文档**: https://cn.vuejs.org/
- **Element Plus文档**: https://element-plus.org/
- **Spring Boot文档**: https://spring.io/projects/spring-boot
- **Prometheus文档**: https://prometheus.io/docs/
- **Grafana文档**: https://grafana.com/docs/

## 许可证

本项目基于 [MIT许可证](LICENSE) 开源

---

**最后更新**: 2026-01-07
**维护者**: nevell
**项目地址**: https://gitee.com/nevell/newblog