import request from '@/utils/request'

// 查询博客文章列表
export function listArticle(query) {
  return request({
    url: '/system/article/list',
    method: 'get',
    params: query
  })
}

// 前台匿名访问文章列表
export function getArticleListAnonymous(query) {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: query,
    headers: { isToken: false } // 不携带token
  })
}

// 查询博客文章详细
export function getArticle(id) {
  return request({
    url: '/system/article/' + id,
    method: 'get'
  })
}

// 新增博客文章
export function addArticle(data) {
  return request({
    url: '/system/article',
    method: 'post',
    data: data
  })
}

// 修改博客文章
export function updateArticle(data) {
  return request({
    url: '/system/article',
    method: 'put',
    data: data
  })
}

// 删除博客文章
export function delArticle(id) {
  return request({
    url: '/system/article/' + id,
    method: 'delete'
  })
}

// 增加浏览量
export function addViewCount(id) {
  return request({
    url: '/system/article/view/' + id,
    method: 'get'
  })
}

// 前台专用接口
// 获取文章列表（前台用，包含分类和标签信息，支持分页）
export function getArticleList(query) {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: query,
    headers: { isToken: false } // 不携带token，允许匿名访问
  })
}

// 根据分类获取文章列表（前台用，支持分页）
export function getArticlesByCategory(categoryId, query) {
  return request({
    url: '/blog/article/category/' + categoryId,
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

// 根据标签获取文章列表（前台用，支持分页）
export function getArticlesByTag(tagId, query) {
  return request({
    url: '/blog/article/tag/' + tagId,
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

// 获取热门文章（支持分页）
export function getHotArticles(query) {
  return request({
    url: '/blog/article/hot',
    method: 'get',
    params: { ...query, pageSize: query.pageSize || 5 },
    headers: { isToken: false }
  })
}

// 获取文章详情（前台用，包含完整内容和上下篇文章信息）
export function getArticleDetail(id) {
  return request({
    url: '/blog/article/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

// 更新文章浏览量
export function updateArticleViewCount(id) {
  return request({
    url: '/blog/article/view/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

// 获取文章归档
export function getArticleArchive() {
  return request({
    url: '/blog/article-archive',
    method: 'get',
    headers: { isToken: false }
  })
}

// 搜索文章（支持分页）
export function searchArticles(keyword, query) {
  return request({
    url: '/blog/article/search',
    method: 'get',
    params: { ...query, keyword: keyword },
    headers: { isToken: false }
  })
}

// 获取相关文章
export function getRelatedArticles(id) {
  return request({
    url: '/blog/article/related/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}

// 获取文章评论
export function getArticleComments(articleId) {
  return request({
    url: '/blog/comment/article/' + articleId,
    method: 'get',
    headers: { isToken: false }
  })
}

// 提交评论
export function submitComment(data) {
  return request({
    url: '/blog/comment',
    method: 'post',
    data: data
  })
}
