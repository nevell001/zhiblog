import request from '@/utils/request'

// 查询评论列表
export function listComment(query) {
  return request({
    url: '/system/comment/list',
    method: 'get',
    params: query
  })
}

// 查询评论详细
export function getComment(id) {
  return request({
    url: '/system/comment/' + id,
    method: 'get'
  })
}

// 新增评论
export function addComment(data) {
  return request({
    url: '/system/comment',
    method: 'post',
    data: data
  })
}

// 修改评论
export function updateComment(data) {
  return request({
    url: '/system/comment',
    method: 'put',
    data: data
  })
}

// 删除评论
export function delComment(ids) {
  return request({
    url: '/system/comment/' + ids,
    method: 'delete'
  })
}

// 审核通过评论
export function auditComment(ids) {
  return request({
    url: '/system/comment/audit/' + ids,
    method: 'put'
  })
}

// 审核拒绝评论
export function rejectComment(ids) {
  return request({
    url: '/system/comment/reject/' + ids,
    method: 'put'
  })
}