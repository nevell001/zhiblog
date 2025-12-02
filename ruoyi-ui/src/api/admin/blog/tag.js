import request from '@/utils/request'

// 查询标签列表
export function listTag(query) {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

// 查询标签详细
export function getTag(tagId) {
  return request({
    url: '/system/tag/' + tagId,
    method: 'get'
  })
}

// 新增标签
export function addTag(data) {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

// 修改标签
export function updateTag(data) {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

// 删除标签
export function delTag(tagIds) {
  return request({
    url: '/system/tag/' + tagIds,
    method: 'delete'
  })
}

// 导出标签
export function exportTag(query) {
  return request({
    url: '/system/tag/export',
    method: 'post',
    params: query
  })
}

// 获取所有标签列表
export function getAllTags() {
  return request({
    url: '/system/tag/all',
    method: 'get'
  })
}