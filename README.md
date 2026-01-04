# 基于 RuoYi-Vue 的博客系统

## 🚀 项目简介
本项目基于 RuoYi-Vue 快速开发平台，打造一个现代化、支持多用户、前后端分离的企业级博客系统。采用 Spring Boot + Vue 3 + Element Plus 技术栈，集成文章发布、评论互动、标签分类、友情链接、后台管理等完整博客功能，适合个人或团队搭建高效、可扩展的博客平台。

**项目特色**：
- 🏗️ **企业级架构**：基于成熟的 RuoYi-Vue 框架，稳定可靠
- 📱 **前后端分离**：Vue 3 + Spring Boot，现代化开发体验
- 🎨 **美观界面**：Element Plus 组件库，响应式设计
- 🔒 **权限完善**：基于 Spring Security 的细粒度权限控制
- 📊 **功能丰富**：文章、评论、标签、分类、统计等完整功能
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署
- 🛡️ **安全加固**：多层安全防护，XSS、CSRF、SQL注入防护

## 📦 版本历史

### v4.0.0 (2026-01-04)
**重大升级 - Spring Boot 3.3.0 + Java 17**

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
- ✅ 所有核心功能正常运行

**注意事项**:
- ⚠️ 需要使用 Java 17 或更高版本
- ⚠️ Prometheus 端点返回 HTML 而非 metrics 数据（可通过 /manage/actuator/metrics 获取）
- ⚠️ 建议使用 Docker 或 IDE 运行以避免 logback 版本冲突
- ⚠️ 需要更新所有依赖的 javax.* 导入为 jakarta.*

**文档更新**:
- ✅ 更新 README.md 技术栈版本
- ✅ 添加 Spring Boot 3.x 升级说明
- ✅ 更新环境要求（Java 17）
- ✅ 添加迁移指南

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
- 分类、标签浏览
- 文章搜索
- 归档、时间轴
- 友链展示
- 关于我页面
- 评论功能（支持匿名/登录评论、回复、审核）
- 文章点赞、浏览量统计

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
- 图片压缩（智能压缩、头像、缩略图）
- 防盗链保护（白名单控制）
- JSON XSS 防护
- UserAgent 解析（浏览器和操作系统检测）



---



## 🗄️ 数据库配置
- **数据库版本**: MySQL 8.4
- **默认端口**: 3306
- **默认用户名**: root
- **默认密码**: root
- **数据库名**: newblog
- **表前缀**: blog_

## 📋 当前系统状态
✅ **数据库表已创建**: 29个表（22个系统表 + 7个博客表）

**博客相关表**:
- blog_article (文章表)
- blog_category (分类表)
- blog_tag (标签表)
- blog_article_tag (文章标签关联表)
- blog_comment (评论表)
- blog_friend_link (友情链接表)
- blog_setting (系统设置表)

**系统表**:
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

✅ **示例数据已导入**: 包含3篇文章、4个分类、10个标签、4个友情链接

✅ **服务已启动**:
- 后端服务: http://localhost:8080
- 前端服务: http://localhost:3000

## 🌐 访问方式
1. **博客前台**: http://localhost:3000/blog
2. **管理后台**: http://localhost:3000/admin
3. **API文档**: http://localhost:8080/swagger-ui.html
4. **默认账号**: admin / admin123

## 🚀 快速开始

### 环境要求
- **Java**: JDK 17+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+

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
cd ruoyi-admin
mvn clean install

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
```

### 5. Docker 一键部署
```bash
# 构建并启动所有服务
docker compose -f docker-compose.dev.yml up -d  //开发环境

docker compose -f docker-compose.prod.yml up -d  //生产环境

# 查看服务状态
docker compose -f docker-compose.dev.yml ps

# 查看日志
docker compose -f docker-compose.dev.yml logs -f
```

## 📁 项目结构
```
newblog /
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
│   └── src/main/resources/
│       └── mapper/system/              # MyBatis XML (22个)
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
│   │   ├── views/            # 页面 (58个)
│   │   │   ├── blog/        # 博客前台 (6个)
│   │   │   │   ├── index.vue              # 首页
│   │   │   │   ├── article/detail.vue     # 文章详情
│   │   │   │   ├── category/              # 分类页面
│   │   │   │   ├── tag/                   # 标签页面
│   │   │   │   ├── archive/               # 归档页面
│   │   │   │   └── about.vue              # 关于页面
│   │   │   ├── admin/blog/   # 博客后台 (7个)
│   │   │   │   ├── article/               # 文章管理
│   │   │   │   ├── category/              # 分类管理
│   │   │   │   ├── tag/                   # 标签管理
│   │   │   │   ├── comment/               # 评论管理
│   │   │   │   ├── friendLink/            # 友链管理
│   │   │   │   └── setting/               # 博客设置
│   │   │   ├── monitor/      # 系统监控
│   │   │   ├── system/       # 系统管理
│   │   │   └── tool/         # 工具
│   │   ├── components/       # 组件 (36个)
│   │   ├── api/              # API接口 (38个)
│   │   │   ├── blog/         # 博客API
│   │   │   ├── admin/        # 管理API
│   │   │   ├── monitor/      # 监控API
│   │   │   └── system/       # 系统API
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia状态管理
│   │   ├── assets/           # 静态资源
│   │   ├── directive/        # 指令
│   │   └── utils/            # 工具函数
│   └── package.json
├── sql/                     # 数据库脚本 
│   ├── init_database.sql    # 初始化脚本
│   ├── quartz.sql           # 定时任务脚本
│   ├── 00_setup_permissions.sql  # 权限设置
│   └── performance_indexes.sql   # 性能索引
├── docker-compose.dev.yml   # Docker编排(开发) 
├── docker-compose.prod.yml  # Docker编排(生产) 
├── Dockerfile-admin         # 后端Docker文件 
├── Dockerfile.dev           # 前端Docker文件(开发) 
├── Dockerfile.prod          # 前端Docker文件(生产) 
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

**数据库字段**:
```sql
- id: 主键
- title: 文章标题
- content: 文章内容
- summary: 文章摘要
- cover_image: 封面图片
- category_id: 分类ID
- author: 作者
- status: 状态(0草稿 1发布)
- is_top: 是否置顶
- is_recommend: 是否推荐
- view_count: 浏览量
- like_count: 点赞数
- comment_count: 评论数
- create_time: 创建时间
- update_time: 更新时间
- del_flag: 删除标志
```

### 🏷️ 分类管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCategoryController.java`

**功能清单**:
- ✅ 分类CRUD操作
- ✅ 排序功能
- ✅ 软删除支持
- ✅ 分类唯一性验证

**数据库字段**:
```sql
- id: 主键
- name: 分类名称
- description: 分类描述
- icon: 分类图标
- sort_order: 排序
- article_count: 文章数量
- create_time: 创建时间
- update_time: 更新时间
- del_flag: 删除标志
```

### 🎯 标签管理
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogTagController.java`

**功能清单**:
- ✅ 标签CRUD操作
- ✅ 标签颜色和图标支持
- ✅ 标签唯一性验证
- ✅ 关联文章检查
- ✅ 标签导出功能

**数据库字段**:
```sql
- id: 主键
- name: 标签名称
- description: 标签描述
- color: 标签颜色(默认#409EFF)
- icon: 标签图标
- article_count: 文章数量
- create_time: 创建时间
- update_time: 更新时间
- del_flag: 删除标志
```

### 💬 评论系统
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogCommentController.java`

**功能清单**:
- ✅ 支持用户评论和匿名评论
- ✅ 评论审核机制
- ✅ 父子评论(回复)支持
- ✅ IP地址记录
- ✅ 邮箱通知（可选）

**数据库字段**:
```sql
- id: 主键
- article_id: 文章ID
- user_id: 用户ID(可为空)
- parent_id: 父评论ID
- nickname: 昵称
- email: 邮箱
- content: 评论内容
- status: 状态(0待审核 1已通过 2已拒绝)
- ip_address: IP地址
- create_time: 创建时间
```

### 🔗 友情链接
**控制器**: `ruoyi-system/src/main/java/com/ruoyi/system/controller/BlogFriendLinkController.java`

**功能清单**:
- ✅ 友链CRUD操作
- ✅ 状态控制(启用/禁用)
- ✅ Logo图片支持

**数据库字段**:
```sql
- id: 主键
- name: 网站名称
- url: 网站链接
- logo: Logo图片
- description: 描述
- status: 状态(0禁用 1启用)
- sort_order: 排序
- create_time: 创建时间
- update_time: 更新时间
```

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

**按钮权限** (22个):
- 文章管理: query, add, edit, remove (4个)
- 分类管理: query, add, edit, remove (4个)
- 标签管理: query, add, edit, remove, export (5个)
- 评论管理: query, approve, remove (3个)
- 博客设置: query, edit (2个)
- 友链管理: query, add, edit, remove (4个)

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

## 🏗️ 技术栈详解

### 后端技术栈
- **框架**: Spring Boot 3.3.0
- **ORM**: MyBatis
- **数据库**: MySQL 8.4
- **缓存**: Redis 6.2
- **安全**: Spring Security 6.1.5
- **连接池**: Druid 1.2.27
- **定时任务**: Quartz
- **API文档**: Swagger 3.0.0
- **UserAgent解析**: YAUAA 7.32.0
- **Java版本**: 17

### 前端技术栈
- **框架**: Vue 3.5.16
- **构建工具**: Vite 6.3.6
- **UI组件**: Element Plus 2.10.7
- **状态管理**: Pinia 3.0.2
- **路由**: Vue Router 4.5.1
- **富文本编辑器**: TinyMCE 8.1.2
- **代码高亮**: highlight.js 11.11.1
- **HTTP客户端**: Axios 1.9.0
- **图表**: ECharts 5.6.0

### 开发工具
- **容器化**: Docker + Docker Compose
- **代码质量**: Maven, ESLint
- **版本控制**: Git
- **API调试**: Swagger UI

---

## 📋 开发规范

### 📝 代码规范

**Java**:
- 类名使用大驼峰 (e.g., BlogArticleController)
- 方法名使用小驼峰 (e.g., getArticleList)
- 常量全大写下划线分隔 (e.g., MAX_PAGE_SIZE)
- 注释完整清晰 (e.g., @Override, @Deprecated)

**Vue**:
- 组件名多单词 (e.g., BlogArticleList)
- Prop定义详细 (e.g., type, required, default)
- 使用组合式API (e.g., setup, ref, reactive)
### 📝 开发规范

**代码规范**:
```java
// Java - 遵循阿里巴巴Java开发手册
- 类名使用大驼峰
- 方法名使用小驼峰
- 常量全大写下划线分隔
- 注释完整清晰
```

```javascript
// Vue - 遵循Vue官方风格指南
- 组件名多单词
- Prop定义详细
- 使用组合式API
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
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/newblog
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# Redis配置
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=

# JWT配置
JWT_SECRET=your-secret-key
JWT_EXPIRATION=7200

# 防盗链配置
REFERER_ENABLED=true
REFERER_ALLOWED_DOMAINS=localhost,127.0.0.1
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

**使用方法**:
1. 在 `application.yml` 中设置 `referer.enabled: true`
2. 配置允许的域名列表 `referer.allowed-domains: yourdomain.com,localhost`
3. 防盗链会自动保护 `/profile/**` 路径下的资源

**注意事项**:
- 开发环境建议关闭防盗链
- 生产环境建议开启并配置正确的域名
- 允许的域名列表使用逗号分隔

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

4. **防盗链配置**:
   - 开启防盗链功能
   - 配置正确的白名单域名
   - 保护静态资源不被外部引用

## 🤝 贡献指南

### 开发规范
1. **代码风格**: 遵循阿里巴巴Java开发手册和Vue官方风格指南
2. **提交规范**: 使用[Conventional Commits](https://conventionalcommits.org/)规范
3. **分支策略**: Git Flow工作流
4. **测试要求**: 新功能需包含单元测试

# 📞 技术支持

### 相关文档
- **项目文档**: [本README](README.md)
- **RuoYi官方文档**: http://doc.ruoyi.vip/
- **Vue 3文档**: https://cn.vuejs.org/
- **Element Plus文档**: https://element-plus.org/
- **Spring Boot文档**: https://spring.io/projects/spring-boot

### 许可证
本项目基于 [MIT许可证](LICENSE) 开源

---

## 🎉 致谢
感谢以下开源项目的支持：
- [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) - 优秀的企业级快速开发平台
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [Spring Boot](https://spring.io/projects/spring-boot) - Java企业级开发框架
