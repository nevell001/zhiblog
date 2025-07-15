# API 接口文档

## 一、认证与通用说明
- 所有接口基于 RESTful 风格，前后端通过 JSON 交互。
- 认证方式：JWT Token（登录后获取，后续请求需在 Header 中携带 Authorization: Bearer <token>）
- 通用返回格式：
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

## 二、主要接口分类

### 1. 用户与权限
- POST   /api/auth/login         用户登录
- POST   /api/auth/register      用户注册
- GET    /api/user/profile       获取用户信息
- PUT    /api/user/profile       修改用户信息
- POST   /api/auth/logout        退出登录

### 2. 文章管理
- GET    /api/article/list       文章列表
- GET    /api/article/{id}       文章详情
- POST   /api/article            新增/发布文章
- PUT    /api/article/{id}       编辑文章
- DELETE /api/article/{id}       删除文章

### 3. 分类与标签
- GET    /api/category/list      分类列表
- POST   /api/category           新增分类
- GET    /api/tag/list           标签列表
- POST   /api/tag                新增标签

### 4. 评论与互动
- GET    /api/comment/list       评论列表
- POST   /api/comment            新增评论
- POST   /api/comment/like       评论点赞

### 5. 站点与资源
- GET    /api/site/info          站点信息
- GET    /api/link/list          友情链接
- GET    /api/banner/list        轮播图
- POST   /api/upload             文件上传

### 6. 日志与监控
- GET    /api/log/operation      操作日志
- GET    /api/log/login          登录日志
- GET    /api/monitor/server     系统监控信息

## 三、接口示例

### 登录
```
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
返回：
{
  "code": 200,
  "msg": "success",
  "data": {
    "token": "xxxxxx"
  }
}
```

### 获取文章列表
```
GET /api/article/list?page=1&pageSize=10
返回：
{
  "code": 200,
  "msg": "success",
  "data": [
    {"id":1, "title":"xxx", ...}, ...
  ]
}
```

---

> 详细参数、字段说明、错误码等请结合实际代码和前后端约定补充。 