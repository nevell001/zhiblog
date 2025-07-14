# 数据库表结构设计

## 用户与权限相关
- sys_user：用户表
- sys_role：角色表
- sys_user_role：用户角色关联表
- sys_menu：菜单权限表
- sys_role_menu：角色菜单关联表

## 博客内容相关
- blog_article：文章表
- blog_category：分类表
- blog_tag：标签表
- blog_article_tag：文章标签关联表

## 评论与互动
- blog_comment：评论表（自关联实现多级评论）
- blog_comment_like：评论点赞表

## 站点管理
- blog_site_config：站点配置信息表
- blog_friend_link：友情链接表
- blog_notice：公告表

## 媒体资源管理
- blog_media：媒体资源表

## 日志与监控
- sys_oper_log：操作日志表
- sys_login_log：登录日志表

---

> 详细字段设计请结合实际开发补充。 