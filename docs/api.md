# 后端接口文档

## 用户与权限
- POST /api/auth/login 用户登录
- POST /api/auth/register 用户注册
- GET /api/user/info 获取用户信息
- POST /api/user/update 更新用户信息
- GET /api/role/list 角色列表

## 博客内容
- GET /api/article/list 文章列表
- GET /api/article/{id} 文章详情
- POST /api/article 新建/编辑文章
- DELETE /api/article/{id} 删除文章
- GET /api/category/list 分类列表
- GET /api/tag/list 标签列表

## 评论与互动
- GET /api/comment/list?articleId= 文章评论列表
- POST /api/comment 新增评论
- POST /api/comment/like 评论点赞
- DELETE /api/comment/{id} 删除评论

## 站点管理
- GET /api/site/config 获取站点配置
- POST /api/site/config 更新站点配置
- GET /api/friendlink/list 友情链接
- GET /api/notice/list 公告列表

## 媒体资源
- POST /api/media/upload 文件上传
- GET /api/media/list 文件列表
- DELETE /api/media/{id} 删除文件

---

> 详细参数与返回值请结合实际开发补充。 