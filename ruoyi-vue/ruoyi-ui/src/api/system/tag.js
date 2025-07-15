import request from '@/utils/request'

// 查询文章-标签关联列表
export function listTag(query) {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

// 查询文章-标签关联详细
export function getTag(articleId) {
  return request({
    url: '/system/tag/' + articleId,
    method: 'get'
  })
}

// 新增文章-标签关联
export function addTag(data) {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

// 修改文章-标签关联
export function updateTag(data) {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

// 删除文章-标签关联
export function delTag(articleId) {
  return request({
    url: '/system/tag/' + articleId,
    method: 'delete'
  })
}
