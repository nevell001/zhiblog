# IFLOW 项目上下文文档

## 项目概述

**项目名称**: newblog (基于 RuoYi-Vue 的博客系统)
**项目类型**: 前后端分离的企业级博客系统
**当前版本**: v4.0.1
**开发语言**: Java 17 + Vue 3.5.16
**最后更新**: 2026-01-11

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
- **环境变量**: spring-dotenv 4.0.0
- **Java版本**: 17
- **测试框架**: JUnit 5.11.4 + Mockito 5.14.2
- **代码规范**: Checkstyle 10.12.5
- **测试覆盖率**: JaCoCo 0.8.12
- **代码质量**: SonarQube 集成

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
- **XSS防护**: DOMPurify 3.3.1
- **测试框架**: Vitest 2.1.8 + Vue Test Utils 2.4.6
- **代码规范**: ESLint 9.18.0 (Flat Config) + Prettier 3.4.2
- **测试覆盖率**: Vitest Coverage v8
- **代码质量**: SonarLint 集成

#### 监控技术栈
- **监控采集**: Prometheus
- **可视化**: Grafana
- **数据源**: Spring Boot Actuator
- **指标格式**: Prometheus metrics
- **数据保留**: 15天
- **代码质量分析**: SonarQube

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
│   │   │   │   ├── index.vue              # 博客首页
│   │   │   │   ├── about.vue              # 关于页面
│   │   │   │   ├── archive/               # 归档页面
│   │   │   │   ├── article/               # 文章详情
│   │   │   │   ├── category/              # 分类页面
│   │   │   │   └── tag/                   # 标签页面
│   │   │   └── admin/        # 管理后台
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
│   │   ├── composables/      # 组合式函数
│   │   └── tests/            # 测试文件
│   ├── Dockerfile.dev        # 开发环境Docker文件
│   ├── Dockerfile.prod       # 生产环境Docker文件
│   ├── vitest.config.ts      # Vitest测试配置
│   └── package.json
├── sql/                      # 数据库脚本
│   ├── init_database.sql    # 初始化脚本
│   ├── quartz.sql           # 定时任务脚本
│   ├── 00_setup_permissions.sql  # 权限设置
│   ├── 01_add_system_log_tables.sql # 系统日志表
│   └── performance_indexes.sql   # 性能索引
├── docker-compose.yml       # Docker编排(默认配置)
├── docker-compose.dev.yml   # Docker编排(开发)
├── docker-compose.prod.yml  # Docker编排(生产)
├── Dockerfile-admin         # 后端Docker文件
├── prometheus/              # Prometheus配置
│   └── prometheus.yml
├── grafana/                 # Grafana配置
│   └── provisioning/
├── checkstyle.xml           # Checkstyle代码规范配置
├── sonar-project.properties # SonarQube配置
├── .env.example             # 环境变量模板
├── .husky/                  # Git Hooks
│   ├── pre-commit          # 提交前检查
│   └── commit-msg          # 提交信息检查
├── .sonarlint/              # SonarLint配置
│   └── connectedMode.json
├── docs/                    # 项目文档
│   ├── CODE_REVIEW_GUIDE.md      # 代码审查指南
│   ├── CODE_STANDARDS.md         # 代码规范
│   ├── CODE_STANDARDS_SETUP.md   # 代码规范设置
│   ├── TESTING_GUIDE.md          # 测试指南
│   ├── TESTING_SETUP.md         # 测试环境设置
│   ├── TEST_REPORT.md            # 测试报告
│   ├── TEST_STATUS.md            # 测试状态
│   ├── 图片压缩功能使用指南.md
│   └── 项目优化建议.md
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
- 文章目录导航（ArticleTOC组件）

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

**系统表** (24个):
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
- **Docker**: 20.10+ (可选)

### 后端启动

```bash
# 编译项目
cd /Users/nevell/code/newblog
mvn clean install -DskipTests

# 启动后端服务
cd ruoyi-admin
mvn spring-boot:run
```

### 前端启动

```bash
# 安装依赖
cd /Users/nevell/code/newblog/ruoyi-ui
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
# 默认配置（推荐）
cd /Users/nevell/code/newblog
docker compose up -d

# 开发环境
docker compose -f docker-compose.dev.yml up -d

# 生产环境
docker compose -f docker-compose.prod.yml up -d

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f

# 停止服务
docker compose down

# 重启服务
docker compose restart
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

# （可选）添加系统日志表
mysql -u root -p < sql/01_add_system_log_tables.sql

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
| http://localhost:9000 | SonarQube | 代码质量分析 |

**默认账号**: admin / admin123

## 环境变量配置

项目使用 `.env` 文件管理环境变量，已提供 `.env.example` 模板文件。后端通过 spring-dotenv 4.0.0 自动加载 `.env` 文件。

### 环境变量模板

```bash
# 复制模板文件
cp .env.example .env

# 编辑 .env 文件，填入实际配置值
# 注意：.env 文件包含敏感信息，不要提交到版本控制系统
```

### 主要环境变量

**数据库配置**:
- `DB_HOST`: 数据库主机地址
- `DB_PORT`: 数据库端口
- `DB_NAME`: 数据库名称
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码

**Redis配置**:
- `REDIS_HOST`: Redis主机地址
- `REDIS_PORT`: Redis端口
- `REDIS_PASSWORD`: Redis密码
- `REDIS_DATABASE`: Redis数据库索引

**JWT配置**:
- `R_TOKEN_SECRET`: JWT密钥（必须修改为随机字符串，至少64位）
- `TOKEN_EXPIRE_TIME`: Token过期时间（分钟）

**Druid监控配置**:
- `DRUID_USERNAME`: Druid监控用户名
- `DRUID_PASSWORD`: Druid监控密码

**Grafana配置**:
- `GF_SECURITY_ADMIN_PASSWORD`: Grafana管理员密码

**SonarQube配置**:
- `SONAR_HOST_URL`: SonarQube 服务器地址
- `SONAR_LOGIN`: SonarQube 认证令牌

**应用配置**:
- `SERVER_PORT`: 应用端口
- `SPRING_PROFILES_ACTIVE`: 环境配置（dev/prod）
- `CAPTCHA_ENABLED`: 验证码开关

**Docker标识**:
- `DOCKER`: 设置为true表示在Docker环境中运行
- `VITE_API_BASE_URL`: 前端 API 基础地址（Docker 环境）

详细配置说明请参考 `.env.example` 文件。

## 配置文件

### 后端配置
**配置文件**: `ruoyi-admin/src/main/resources/application.yml`

**关键配置项**:
- 服务端口: 8080
- 数据库连接: MySQL 8.4
- Redis配置: localhost:6379
- Token配置: 必须通过环境变量 R_TOKEN_SECRET 设置
- 图片压缩配置: 已启用
- DOMPurify XSS 防护: 已启用
- 防盗链配置: 默认关闭
- Actuator监控: 已启用
- Prometheus监控: 已集成
- 验证码开关: 默认关闭（开发环境）
- 文件上传: 单文件 10MB，总请求 20MB
- Redis 缓存: 默认 10 分钟
- 环境变量: 通过 spring-dotenv 自动加载 .env 文件

### 前端配置
**配置文件**: `ruoyi-ui/vite.config.js`, `ruoyi-ui/.env.development`, `ruoyi-ui/.env.production`

**关键配置项**:
- 开发服务器端口: 3000
- API代理: /dev-api → http://localhost:8080
- 构建输出: dist/
- Docker环境支持: 自动检测并切换后端地址

**Vite 代理配置**:
```javascript
// 自动检测 Docker 环境
const inDocker = process.env.DOCKER === 'true'
const baseUrl = inDocker ? 'http://ruoyi-admin:8080' : 'http://localhost:8080'

// 接口代理 - RuoYi 默认 API 前缀
'/dev-api': {
  target: baseUrl,  // Docker环境: http://ruoyi-admin:8080, 本地: http://localhost:8080
  changeOrigin: true,
  rewrite: (path) => path.replace(/^\/dev-api/, '')
}
// 博客前台接口代理
'^/blog/api/': {
  target: baseUrl,
  changeOrigin: true,
  rewrite: (path) => path.replace(/^\/blog\/api/, '/blog')
}
// 静态资源代理
'/profile': {
  target: baseUrl,
  changeOrigin: true
}
// 监控端点代理
'/manage': {
  target: baseUrl,
  changeOrigin: true
}
```

### 监控配置

**Prometheus配置**: `prometheus/prometheus.yml`
```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

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

**Grafana配置**: `grafana/provisioning/`
- 默认用户: admin
- 默认密码: admin
- 数据源: Prometheus
- 自动导入仪表板

## 测试

### 前端测试

**测试框架**: Vitest + Vue Test Utils

**运行测试**:
```bash
cd ruoyi-ui

# 运行所有测试
npm run test

# 运行测试并生成覆盖率报告
npm run test:coverage

# 运行测试 UI
npm run test:ui
```

**测试覆盖率目标**:
- 行覆盖率: ≥ 70%
- 分支覆盖率: ≥ 70%
- 函数覆盖率: ≥ 70%
- 语句覆盖率: ≥ 70%

**测试文件结构**:
```
ruoyi-ui/src/
├── utils/
│   ├── validate.ts          # 源文件
│   └── validate.test.ts     # 测试文件
├── composables/
│   ├── useArticleList.ts    # 源文件
│   └── useArticleList.test.ts # 测试文件
└── tests/
    └── setup.ts            # 测试设置
```

**测试配置**: `ruoyi-ui/vitest.config.ts`

### 后端测试

**测试框架**: JUnit 5 + Mockito + Spring Boot Test

**运行测试**:
```bash
# 运行所有测试
mvn test

# 运行指定模块的测试
mvn test -pl ruoyi-system

# 运行单个测试类
mvn test -Dtest=BlogArticleServiceTest

# 运行单个测试方法
mvn test -Dtest=BlogArticleServiceTest#testInsertArticle

# 生成测试覆盖率报告
mvn test jacoco:report
```

**测试覆盖率目标**:
- 行覆盖率: ≥ 60%
- 分支覆盖率: ≥ 60%

**测试文件结构**:
```
ruoyi-system/src/test/java/com/ruoyi/system/
├── controller/
│   └── BlogArticleControllerTest.java
├── service/
│   └── impl/
│       └── BlogArticleServiceImplTest.java
└── mapper/
    └── BlogArticleMapperTest.java
```

**测试数据库**: H2 内存数据库（测试环境）

详细测试指南请参考 [docs/TESTING_GUIDE.md](docs/TESTING_GUIDE.md)

## 代码规范

### 前端代码规范

**工具**:
- **ESLint 9.18.0**: 代码质量检查（Flat Config 格式）
- **Prettier 3.4.2**: 代码格式化
- **SonarLint**: 代码质量分析

**配置文件**:
- `ruoyi-ui/eslint.config.js` - ESLint Flat Config 配置
- `ruoyi-ui/.prettierrc.json` - Prettier 配置
- `ruoyi-ui/.prettierignore` - Prettier 忽略文件

**ESLint 规则**:
- 基于 Vue 3 Flat Config 推荐配置
- 自动导入 Vue Composition API、Vue Router、Pinia、Element Plus 组件
- 代码风格：单引号、无分号、2 空格缩进
- 生产环境禁止 console 和 debugger

**使用方法**:
```bash
cd ruoyi-ui

# 检查代码规范
npm run lint:check

# 自动修复代码规范问题
npm run lint

# 格式化所有代码
npm run format

# 检查代码格式
npm run format:check
```

### 后端代码规范

**工具**:
- **Checkstyle 10.12.5**: Java 代码规范检查
- **SonarLint**: 代码质量分析

**配置文件**:
- `checkstyle.xml` - Checkstyle 配置（基于 Google Java Style Guide）

**Checkstyle 规则**:
- 行长度限制：120 字符
- 方法长度限制：150 行
- 参数数量限制：7 个
- 圈复杂度限制：15
- 命名规范：驼峰命名（类名大驼峰、方法/变量小驼峰、常量全大写）

**使用方法**:
```bash
# 检查代码规范
mvn checkstyle:check

# 生成检查报告
mvn checkstyle:checkstyle

# 报告生成在: target/checkstyle-result.xml
```

### Git Hooks

项目配置了 Git Hooks 来自动检查代码规范：

**配置的 Hooks**:
1. **pre-commit**: 提交前检查代码规范
2. **commit-msg**: 检查提交信息格式

**提交信息格式**:
```
type(scope): subject

body

footer
```

**类型（type）**:
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档
- `style`: 格式
- `refactor`: 重构
- `perf`: 性能
- `test`: 测试
- `chore`: 构建/工具
- `build`: 构建系统
- `ci`: CI配置

详细代码规范说明请参考 [docs/CODE_STANDARDS.md](docs/CODE_STANDARDS.md)

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
build: 构建系统
ci: CI配置
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

**压缩配置**:
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

### 2. 防盗链保护
- 白名单控制
- 保护静态资源
- 可配置启用/禁用
- 保护 `/profile/**` 路径下的资源

**防盗链配置**:
```yaml
referer:
  enabled: false              # 是否启用防盗链
  allowed-domains: localhost,127.0.0.1  # 允许的域名列表
```

### 3. DOMPurify XSS 防护
- 使用 DOMPurify 3.3.1 库
- 自动过滤恶意 HTML/JS 代码
- 保护富文本内容安全
- 支持自定义白名单配置
- 与 TinyMCE 编辑器集成

**配置**:
```yaml
xss:
  enabled: true
  excludes: /system/notice,/system/article,/manage/**
  urlPatterns: /system/*,/monitor/*,/tool/*
```

### 4. JSON XSS 防护
- 使用 JsonSanitizer
- 自动过滤恶意代码
- 保护数据安全
- 可配置排除路径

### 5. User-Agent 解析
- 使用 YAUAA 库
- 支持多种浏览器和操作系统检测
- 精确的设备识别
- 替代了旧的 BitWalker 库

### 5. 监控与告警
- Spring Boot Actuator 集成
- Prometheus 指标采集
- Grafana 可视化仪表板
- 健康检查、性能监控、日志管理

### 6. 文章目录导航
- 自动提取文章标题生成目录
- 支持平滑滚动
- 当前阅读位置高亮
- 可折叠/展开
- 固定定位显示

### 7. 热门文章排名
- Top 3 文章使用特殊渐变样式（金、银、铜）
- 排名徽章显示
- 悬停动画效果

### 8. 深色主题适配
- 全站深色主题支持
- 响应式主题切换
- CSS 变量管理
- 完善的深色主题样式

### 9. 环境变量自动加载
- 使用 spring-dotenv 4.0.0
- 自动加载 .env 文件
- 简化配置管理
- 支持开发/生产环境切换

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

**Prometheus 监控任务**:
- Spring Boot Actuator 监控: 10秒采集间隔
- Prometheus 自身监控
- 支持外部标签和自定义标签

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

### SonarQube 代码质量分析
- 已集成 SonarQube 代码质量分析平台
- 支持 7 种编程语言的代码分析
- 提供代码重复率、复杂度、安全漏洞等指标
- 集成 SonarLint IDE 插件实时检查

**配置文件**: `sonar-project.properties`
```properties
sonar.projectKey=your-project-key
sonar.projectName=newblog
sonar.sources=.
sonar.host.url=http://localhost:9000
sonar.login=sqp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

**SonarLint 配置**: `.sonarlint/connectedMode.json`
- 支持 VS Code、IntelliJ IDEA 等 IDE
- 实时代码质量检查
- 与 SonarQube 服务器同步规则

**运行分析**:
```bash
# 使用 SonarScanner CLI
sonar-scanner

# 或使用 Maven 插件
mvn sonar:sonar
```

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

### 8. 监控数据问题
**问题**: Prometheus 无法采集指标
**解决**: 检查 Actuator 端点是否启用，Prometheus 配置是否正确

### 9. 测试失败问题
**问题**: 测试运行失败
**解决**: 检查测试环境配置，确保依赖已正确安装，查看详细错误日志

### 10. 代码规范检查失败
**问题**: Checkstyle 或 ESLint 检查失败
**解决**: 运行自动修复命令，手动修复无法自动修复的问题

## Docker 服务说明

### 默认配置服务 (`docker-compose.yml`)
- **ruoyi-admin**: 后端服务 (8080端口)
  - 健康检查: curl -f http://localhost:8080
  - 环境变量: Docker 自动配置
  - 依赖: mysql, redis
- **mysql**: MySQL 8.4 数据库 (3306端口)
  - 健康检查: mysqladmin ping
  - 数据持久化: mysql_data volume
  - 自动初始化: sql 目录
- **redis**: Redis 6.2 缓存 (6379端口)
  - 健康检查: redis-cli ping
- **ruoyi-ui**: 前端开发服务器 (3000端口)
  - 支持热重载
  - 自动检测 Docker 环境
  - 依赖: mysql, redis, ruoyi-admin

### 开发环境服务 (`docker-compose.dev.yml`)
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

### 生产环境服务 (`docker-compose.prod.yml`)
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

## 项目优势

1. **企业级架构**: 基于成熟的 RuoYi-Vue 框架，稳定可靠
2. **前后端分离**: Vue 3 + Spring Boot，现代化开发体验
3. **美观界面**: Element Plus 组件库，响应式设计
4. **权限完善**: 基于 Spring Security 的细粒度权限控制
5. **功能丰富**: 文章、评论、标签、分类、统计等完整功能
6. **容器化部署**: Docker + Docker Compose 一键部署
7. **安全加固**: 多层安全防护，XSS、CSRF、SQL注入防护，DOMPurify 过滤
8. **性能优化**: 图片压缩、Redis缓存、数据库索引
9. **监控完善**: Prometheus + Grafana 全链路监控
10. **代码质量**: SonarQube + SonarLint 代码质量分析平台
11. **开发友好**: 热部署、自动重启、详细日志
12. **测试完善**: 前后端测试框架，测试覆盖率监控
13. **代码规范**: ESLint 9 Flat Config、Prettier、Checkstyle 自动检查
14. **环境管理**: 环境变量模板，Git Hooks 自动检查
15. **配置简化**: spring-dotenv 自动加载环境变量

## 相关文档

- **项目文档**: [README.md](README.md)
- **代码规范**: [docs/CODE_STANDARDS.md](docs/CODE_STANDARDS.md)
- **代码规范设置**: [docs/CODE_STANDARDS_SETUP.md](docs/CODE_STANDARDS_SETUP.md)
- **代码审查指南**: [docs/CODE_REVIEW_GUIDE.md](docs/CODE_REVIEW_GUIDE.md)
- **测试指南**: [docs/TESTING_GUIDE.md](docs/TESTING_GUIDE.md)
- **测试环境设置**: [docs/TESTING_SETUP.md](docs/TESTING_SETUP.md)
- **测试报告**: [docs/TEST_REPORT.md](docs/TEST_REPORT.md)
- **测试状态**: [docs/TEST_STATUS.md](docs/TEST_STATUS.md)
- **图片压缩功能**: [docs/图片压缩功能使用指南.md](docs/图片压缩功能使用指南.md)
- **项目优化建议**: [docs/项目优化建议.md](docs/项目优化建议.md)
- **PR描述**: [docs/PR_DESCRIPTION.md](docs/PR_DESCRIPTION.md)
- **RuoYi官方文档**: http://doc.ruoyi.vip/
- **Vue 3文档**: https://cn.vuejs.org/
- **Element Plus文档**: https://element-plus.org/
- **Spring Boot文档**: https://spring.io/projects/spring-boot
- **Prometheus文档**: https://prometheus.io/docs/
- **Grafana文档**: https://grafana.com/docs/
- **SonarQube文档**: https://docs.sonarqube.org/
- **SonarLint文档**: https://docs.sonarsource.com/sonarlint/
- **Vitest文档**: https://vitest.dev/
- **JUnit 5文档**: https://junit.org/junit5/docs/current/user-guide/

## 许可证

本项目基于 [MIT许可证](LICENSE) 开源

---

**最后更新**: 2026-01-11
**维护者**: nevell
**项目地址**: https://gitee.com/nevell/newblog