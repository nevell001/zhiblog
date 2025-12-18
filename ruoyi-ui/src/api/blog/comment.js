import request from '@/utils/request'

// 查询博客评论列表
export function listComment(query) {
  return request({
    url: '/system/comment/list',
    method: 'get',
    params: query
  })
}

// 查询博客评论详细
export function getComment(id) {
  return request({
    url: '/system/comment/' + id,
    method: 'get'
  })
}

// 新增博客评论
export function addComment(data) {
  return request({
    url: '/system/comment',
    method: 'post',
    data: data
  })
}

// 修改博客评论
export function updateComment(data) {
  return request({
    url: '/system/comment',
    method: 'put',
    data: data
  })
}

// 删除博客评论
export function delComment(id) {
  return request({
    url: '/system/comment/' + id,
    method: 'delete'
  })
}

// 前台专用接口
// 获取文章评论列表（前台用）
export function getArticleComments(articleId) {
  return request({
    url: '/dev-api/blog/comment/article/' + articleId,
    method: 'get'
  })
}

// 添加评论（前台用）
export function addBlogComment(data) {
  return request({
    url: '/dev-api/blog/comment',
    method: 'post',
    data: data
  })
}

// 审核评论
export function auditComment(id) {
  return request({
    url: '/system/comment/audit/' + id,
    method: 'put'
  })
}

// 驳回评论
export function rejectComment(id) {
  return request({
    url: '/system/comment/reject/' + id,
    method: 'put'
  })
}