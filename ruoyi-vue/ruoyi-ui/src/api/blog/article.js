import request from '@/utils/request'

// 查询博客文章列表
export function listArticle(query) {
  return request({
    url: '/system/article/list',
    method: 'get',
    params: query
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
// 获取文章列表（前台用，包含分类和标签信息）
export function getArticleList(query) {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: query
  })
}

// 获取文章详情（前台用，包含完整内容）
export function getArticleDetail(id) {
  return request({
    url: '/blog/article/' + id,
    method: 'get'
  })
}

// 获取热门文章
export function getHotArticles(query) {
  return request({
    url: '/blog/article/hot',
    method: 'get',
    params: query
  })
}

// 更新文章浏览量
export function updateArticleViewCount(id) {
  return request({
    url: '/blog/article/view/' + id,
    method: 'post'
  })
}
