# 系统架构设计

## 整体架构
本博客系统采用前后端分离架构，后端基于 RuoYi 框架，前端采用 Vue.js 实现，数据库使用 MySQL，缓存采用 Redis，支持 Docker 部署。

```
[用户] ←→ [前端 Vue.js] ←→ [后端 RuoYi (Spring Boot)] ←→ [MySQL/Redis/OSS]
```

## 主要技术选型
- Spring Boot：后端主框架，提供 RESTful API
- MyBatis-Plus：简化数据库操作
- Spring Security：权限与认证
- Redis：缓存与会话管理
- Vue.js：前端框架，组件化开发
- Element UI：前端 UI 组件库
- Docker：容器化部署

## 各模块关系说明
- 用户与权限管理：统一认证与授权，保护各业务接口
- 博客内容管理：文章、分类、标签等内容的增删改查
- 评论与互动：与文章关联，支持多级评论
- 站点管理：全局配置、友情链接、公告等
- 媒体资源管理：统一文件上传与管理
- 日志与监控：系统运行与安全监控
- 前台展示：通过 RESTful API 获取数据，动态渲染页面

---

> 详细接口与表结构请参考 api.md 和 database.md。 