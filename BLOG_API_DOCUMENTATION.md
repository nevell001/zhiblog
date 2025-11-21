# 博客系统 API 接口文档

## 📋 接口概述

本文档描述了基于 RuoYi-Vue 的博客系统的所有 REST API 接口。

**基础信息**:
- **Base URL**: `http://localhost:8080`
- **认证方式**: Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

---

## 🔐 认证接口

### 1. 用户登录

**接口地址**: `POST /login`

**请求参数**:
```json
{
    "username": "admin",
    "password": "admin123",
    "code": "验证码(可选)",
    "uuid": "验证码UUID(可选)"
}
```

**响应示例**:
```json
{
    "code": 200,
    "msg": "操作成功",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImxvZ2luX3VzZXJfa2V5IjoiYWYyZGMzNWUtNzljYi00ZTVmLWIwOGMtMDQ1NzE3ZjI1MDJiIn0.VdM-U3VlAHogElXTDm0bg33yALPoHA2QG2lcOMwSsJy2hCJjYwZiKF8iupOCBvyq_WLbi7LkamLgjebul8mYpw"
}
```

---

## 📝 文章管理接口

### 1. 获取文章列表

**接口地址**: `GET /system/article/list`

**权限要求**: `blog:article:list`

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认10 |
| title | string | 否 | 文章标题(模糊搜索) |
| categoryId | long | 否 | 分类ID |
| status | int | 否 | 文章状态(0:草稿 1:发布) |
| isTop | int | 否 | 是否置顶(0:否 1:是) |

**响应示例**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "total": 100,
    "rows": [
        {
            "id": 1,
            "title": "测试文章",
            "summary": "文章摘要",
            "content": "文章内容...",
            "categoryId": 1,
            "authorId": 1,
            "author": "admin",
            "status": 1,
            "isTop": 0,
            "isRecommend": 0,
            "viewCount": 100,
            "likeCount": 10,
            "commentCount": 5,
            "createTime": "2025-11-21 10:00:00",
            "updateTime": "2025-11-21 10:00:00",
            "category": {
                "id": 1,
                "name": "技术分享"
            },
            "tags": [
                {
                    "id": 1,
                    "name": "Java",
                    "color": "#409EFF"
                }
            ]
        }
    ]
}
```

### 2. 获取文章详情

**接口地址**: `GET /system/article/{id}`

**权限要求**: `blog:article:query`

**路径参数**:
| 参数名 | 类型 | 说明 |
|-------|------|------|
| id | long | 文章ID |

**响应示例**:
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "article": {
            "id": 1,
            "title": "测试文章",
            "summary": "文章摘要",
            "content": "文章内容...",
            "categoryId": 1,
            "status": 1
        },
        "tags": [
            {"id": 1, "name": "Java", "color": "#409EFF"}
        ],
        "category": {
            "id": 1,
            "name": "技术分享",
            "description": "技术相关文章"
        }
    }
}
```

### 3. 新增文章

**接口地址**: `POST /system/article`

**权限要求**: `blog:article:add`

**请求参数**:
```json
{
    "title": "文章标题",
    "summary": "文章摘要",
    "content": "文章内容(支持Markdown)",
    "categoryId": 1,
    "status": 1,
    "isTop": 0,
    "isRecommend": 0,
    "tagIds": [1, 2, 3]
}
```

**响应示例**:
```json
{
    "code": 200,
    "msg": "文章创建成功"
}
```

### 4. 更新文章

**接口地址**: `PUT /system/article`

**权限要求**: `blog:article:edit`

**请求参数**: 同新增文章，需包含 `id` 字段

**响应示例**:
```json
{
    "code": 200,
    "msg": "文章更新成功"
}
```

### 5. 删除文章

**接口地址**: `DELETE /system/article/{ids}`

**权限要求**: `blog:article:remove`

**路径参数**:
| 参数名 | 类型 | 说明 |
|-------|------|------|
| ids | string | 多个ID用逗号分隔，如: "1,2,3" |

**响应示例**:
```json
{
    "code": 200,
    "msg": "删除成功"
}
```

---

## 📂 分类管理接口

### 1. 获取分类列表

**接口地址**: `GET /system/category/list`

**权限要求**: `blog:category:list`

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| status | int | 否 | 状态(0:禁用 1:启用) |

**响应示例**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "data": [
        {
            "id": 1,
            "name": "技术分享",
            "description": "技术相关文章",
            "sort": 1,
            "status": 1,
            "createTime": "2025-11-21 10:00:00",
            "updateTime": "2025-11-21 10:00:00"
        }
    ]
}
```

### 2. 新增分类

**接口地址**: `POST /system/category`

**权限要求**: `blog:category:add`

**请求参数**:
```json
{
    "name": "分类名称",
    "description": "分类描述",
    "sort": 1,
    "status": 1
}
```

### 3. 更新分类

**接口地址**: `PUT /system/category`

**权限要求**: `blog:category:edit`

### 4. 删除分类

**接口地址**: `DELETE /system/category/{ids}`

**权限要求**: `blog:category:remove`

---

## 🏷️ 标签管理接口

### 1. 获取标签列表

**接口地址**: `GET /system/tag/list`

**权限要求**: `blog:tag:list`

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| name | string | 否 | 标签名称(模糊搜索) |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

**响应示例**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "total": 50,
    "rows": [
        {
            "id": 1,
            "name": "Java",
            "description": "Java编程语言",
            "color": "#409EFF",
            "icon": "java",
            "createTime": "2025-11-21 10:00:00"
        }
    ]
}
```

### 2. 新增标签

**接口地址**: `POST /system/tag`

**权限要求**: `blog:tag:add`

**请求参数**:
```json
{
    "name": "标签名称",
    "description": "标签描述",
    "color": "#409EFF",
    "icon": "tag"
}
```

### 3. 导出标签

**接口地址**: `POST /system/tag/export`

**权限要求**: `blog:tag:export`

---

## 💬 评论管理接口

### 1. 获取评论列表

**接口地址**: `GET /system/comment/list`

**权限要求**: `blog:comment:list`

**查询参数**:
| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| articleId | long | 否 | 文章ID |
| status | int | 否 | 审核状态(0:待审核 1:已通过 2:已拒绝) |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

### 2. 审核评论

**接口地址**: `PUT /system/comment/approve`

**权限要求**: `blog:comment:approve`

**请求参数**:
```json
{
    "ids": [1, 2, 3],
    "status": 1
}
```

---

## 🔗 友链管理接口

### 1. 获取友链列表

**接口地址**: `GET /system/friendLink/list`

**权限要求**: `blog:friendLink:list`

**响应示例**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "rows": [
        {
            "id": 1,
            "name": "友链名称",
            "url": "https://example.com",
            "logo": "https://example.com/logo.png",
            "description": "友链描述",
            "status": 1,
            "sort": 1,
            "createTime": "2025-11-21 10:00:00"
        }
    ]
}
```

### 2. 新增友链

**接口地址**: `POST /system/friendLink`

**权限要求**: `blog:friendLink:add`

**请求参数**:
```json
{
    "name": "友链名称",
    "url": "https://example.com",
    "logo": "https://example.com/logo.png",
    "description": "友链描述",
    "status": 1,
    "sort": 1
}
```

---

## ⚙️ 博客设置接口

### 1. 获取设置列表

**接口地址**: `GET /system/setting/list`

**权限要求**: `blog:setting:list`

**响应示例**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "data": [
        {
            "configKey": "blog_name",
            "configValue": "我的技术博客",
            "description": "博客网站名称"
        }
    ]
}
```

### 2. 更新设置

**接口地址**: `PUT /system/setting`

**权限要求**: `blog:setting:edit`

**请求参数**:
```json
{
    "configKey": "blog_name",
    "configValue": "我的技术博客",
    "description": "博客网站名称"
}
```

---

## 📊 通用响应格式

### 成功响应
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {} // 或 rows: [], total: 100
}
```

### 错误响应
```json
{
    "code": 500,
    "msg": "错误描述信息"
}
```

### 常见错误码
| 错误码 | 说明 |
|-------|------|
| 200 | 操作成功 |
| 401 | 未认证或token已过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 🔧 使用说明

### 1. 认证流程
1. 调用 `/login` 接口获取 token
2. 在后续请求的 Header 中添加: `Authorization: Bearer {token}`
3. Token 有效期: 24小时

### 2. 分页参数
- `pageNum`: 当前页码，从1开始
- `pageSize`: 每页记录数，默认10，最大100

### 3. 时间格式
所有时间字段使用 `yyyy-MM-dd HH:mm:ss` 格式

### 4. 文件上传
- 文件上传接口: `/common/upload`
- 支持格式: jpg, jpeg, png, gif
- 最大大小: 5MB

---

## 📝 更新日志

### v1.0.0 (2025-11-21)
- ✅ 初始版本发布
- ✅ 完成博客核心功能接口
- ✅ 支持文章CRUD、分类标签管理
- ✅ 集成评论和友链功能
- ✅ 完善权限控制

---

**文档维护**: 如有接口变更，将及时更新本文档