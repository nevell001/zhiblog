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
- Markdown 编辑器
- 统计分析（文章、用户、访问量等）
- 第三方登录（如 GitHub、微信等，后续可扩展）



---



## 🗄️ 数据库配置
- **数据库版本**: MySQL 8.4
- **默认端口**: 3306
- **默认用户名**: root
- **默认密码**: root
- **数据库名**: newblog
- **表前缀**: blog_

## 📋 当前系统状态
✅ **数据库表已创建**: 7个博客相关表
- blog_article (文章表)
- blog_category (分类表)
- blog_tag (标签表)
- blog_article_tag (文章标签关联表)
- blog_comment (评论表)
- blog_friend_link (友情链接表)
- blog_setting (系统设置表)

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
- **Java**: JDK 1.8+
- **Node.js**: 16.0+
- **MySQL**: 8.4+
- **Redis**: 6.2+

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

# 导入数据表和示例数据
mysql -u root -p newblog < sql/ry_20250522.sql
```

### 3. 后端启动
```bash
# 编译项目
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
├── ruoyi-admin/          # 后端主模块 ✅
├── ruoyi-system/         # 系统模块(含博客功能) ✅
├── ruoyi-framework/      # 框架核心 ✅
├── ruoyi-common/         # 通用工具 ✅
├── ruoyi-quartz/         # 定时任务 ✅
├── ruoyi-generator/      # 代码生成 ✅
├── ruoyi-ui/            # Vue3前端项目 ✅
│   ├── src/
│   │   ├── views/
│   │   │   ├── blog/           # 博客前台页面
│   │   │   └── admin/          # 后台管理页面
│   │   ├── components/         # 公共组件
│   │   ├── api/               # API接口
│   │   ├── router/            # 路由配置
│   │   └── stores/            # Pinia状态管理
│   └── package.json
├── sql/                   # 数据库脚本 ✅
│   ├── ry_20250522.sql    # 主数据库脚本
│   └── init_database.sql  # 初始化脚本
├── docker-compose.yml     # Docker编排 ✅
├── Dockerfile-admin       # 后端Docker文件
├── pom.xml               # Maven主配置
└── README.md             # 项目文档
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

**数据库字段**:
```sql
- id: 主键
- name: 标签名称
- description: 标签描述
- color: 标签颜色(默认#409EFF)
- icon: 标签图标
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
- **框架**: Spring Boot 2.5.15
- **ORM**: MyBatis
- **数据库**: MySQL 8.4
- **缓存**: Redis 6.2
- **安全**: Spring Security 5.7.12
- **连接池**: Druid 1.2.23
- **定时任务**: Quartz
- **API文档**: Swagger 3.0.0
- **Java版本**: 1.8

### 前端技术栈
- **框架**: Vue 3.5.16
- **构建工具**: Vite 6.3.6
- **UI组件**: Element Plus 2.8.2
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



## 🔍 项目优化建议

### 🎨 前端架构
**页面组件** (20+个):
```
views/
├── blog/              # 博客前台 (5个)
├── system/            # 系统管理 (3个博客相关)
├── admin/             # 后台管理
└── error/             # 错误页面
```

**公共组件** (15+个):
```
components/
├── BlogNav.vue        # 博客导航
├── TagCategorySelector.vue  # 标签分类选择器
├── TinyMCE/          # 富文本编辑器
├── ImageUpload/      # 图片上传
└── ...
```

**状态管理**:
```javascript
1. user.js        - 用户状态 ✅
2. permission.js  - 权限路由 ✅
3. app.js         - 应用配置 ✅
4. settings.js    - 系统设置 ✅
5. tagsView.js    - 标签视图 ✅
```

### 🗄️ 数据库优化
**表结构概览** (22个表):
- **博客相关表** (7个): blog_article, blog_category, blog_tag, blog_article_tag, blog_comment, blog_friend_link, blog_setting
- **系统表** (15个): sys_user, sys_role, sys_menu 等

**索引优化建议**:
```sql
-- 文章表
ALTER TABLE blog_article ADD INDEX idx_category_id (category_id);
ALTER TABLE blog_article ADD INDEX idx_status (status);
ALTER TABLE blog_article ADD INDEX idx_create_time (create_time);

-- 标签关联表
ALTER TABLE blog_article_tag ADD INDEX idx_article_id (article_id);
ALTER TABLE blog_article_tag ADD INDEX idx_tag_id (tag_id);
```

### ⚡ 性能优化
**后端优化**:
- 分页查询避免全表扫描
- Redis缓存热门内容 (TTL: 1-24小时)
- 使用DTO减少数据传输
- 添加接口缓存和限流

**前端优化**:
- 路由懒加载 ✅ (已实现)
- 图片懒加载和CDN加速
- 组件按需加载
- Vite打包优化配置

### 📊 监控与日志
**日志配置建议**:
```yaml
logging:
  level:
    com.ruoyi: info
    org.springframework: warn
  file:
    path: /var/log/ruoyi
    max-size: 100MB
    max-history: 30
```

**监控工具推荐**:
- Spring Boot Actuator ✅ (已集成)
- Prometheus + Grafana (推荐)
- ELK Stack (日志分析)

---

## 📋 开发计划与规范

### 🎯 优先级行动计划

**第一阶段: 安全加固** (1-2天)
- [ ] 生成并配置强Token密钥
- [ ] 设置Redis密码
- [ ] 使用环境变量管理敏感配置
- [ ] 创建生产环境配置文件

**第二阶段: 代码清理** (2-3天)
- [ ] 统一路由路径规范
- [ ] 清理SQL脚本重复定义

**第三阶段: 功能完善** (1周内)
- [ ] 完善文章详情页组件
- [ ] 实现图片上传功能
- [ ] 优化博客前台展示
- [ ] 添加文章搜索功能

**第四阶段: 性能优化** (2周内)
- [ ] 添加数据库索引
- [ ] 实现Redis缓存策略
- [ ] 前端资源优化
- [ ] 接口性能优化

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

### 🎓 技术债务管理

**高优先级**:
1. 安全配置不完善 (Token密钥、Redis密码等)
2. 数据库SQL脚本重复定义

**中优先级**:
1. 缺少单元测试和集成测试
2. API文档不完整
3. 错误处理不统一

**低优先级**:
1. 代码注释不完整
2. 部分组件可复用性差
3. 日志级别已优化 ✅

## 📊 项目统计信息

### 代码统计
- **Java代码**: ~15,000 行
- **Vue代码**: ~12,000 行
- **数据库表**: 22个 (15个系统表 + 7个博客表)
- **API接口**: 50+ 个
- **前端页面**: 25+ 个

### 功能完成度
| 模块 | 完成度 | 说明 |
|------|--------|------|
| 文章管理 | ✅ 100% | CRUD、状态管理、搜索等 |
| 分类管理 | ✅ 100% | 完整分类体系 |
| 标签管理 | ✅ 100% | 多对多关联、颜色支持 |
| 评论系统 | ✅ 100% | 审核、回复、匿名支持 |
| 友链管理 | ✅ 100% | 状态控制、Logo支持 |
| 博客设置 | ✅ 100% | 键值配置、系统参数 |
| 权限系统 | ✅ 100% | 菜单权限、按钮权限 |
| 前台展示 | ✅ 90% | 基础页面完成，细节优化中 |

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
```

### 生产环境建议
1. **安全配置**:
   - 修改默认密码
   - 设置强Token密钥
   - 启用HTTPS
   - 配置防火墙规则

2. **性能优化**:
   - 添加数据库索引
   - 配置Redis缓存
   - 启用Gzip压缩
   - 使用CDN加速

3. **监控告警**:
   - 配置日志收集
   - 设置性能监控
   - 配置告警通知
   - 定期数据备份

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
