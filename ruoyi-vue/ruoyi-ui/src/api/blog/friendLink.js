import request from '@/utils/request'

// 查询友链列表
export function listFriendLink(query) {
  return request({
    url: '/system/friendLink/list',
    method: 'get',
    params: query
  })
}

// 查询友链详细
export function getFriendLink(id) {
  return request({
    url: '/system/friendLink/' + id,
    method: 'get'
  })
}

// 新增友链
export function addFriendLink(data) {
  return request({
    url: '/system/friendLink',
    method: 'post',
    data: data
  })
}

// 修改友链
export function updateFriendLink(data) {
  return request({
    url: '/system/friendLink',
    method: 'put',
    data: data
  })
}

// 删除友链
export function delFriendLink(id) {
  return request({
    url: '/system/friendLink/' + id,
    method: 'delete'
  })
}