import request from '@/utils/request'

// 查询博客标签列表
export function listTag(query) {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

// 查询博客标签详细
export function getTag(id) {
  return request({
    url: '/system/tag/' + id,
    method: 'get'
  })
}

// 新增博客标签
export function addTag(data) {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

// 修改博客标签
export function updateTag(data) {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

// 删除博客标签
export function delTag(id) {
  return request({
    url: '/system/tag/' + id,
    method: 'delete'
  })
}

// 前台专用接口
// 获取标签列表（前台用）
export function getTagList(query) {
  return request({
    url: '/blog/tag/list',
    method: 'get',
    params: query
  })
}

// 获取标签详情（前台用）
export function getTagDetail(id) {
  return request({
    url: '/blog/tag/' + id,
    method: 'get'
  })
}
