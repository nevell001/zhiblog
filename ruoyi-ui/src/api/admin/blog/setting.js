import request from '@/utils/request'

// 查询博客设置列表
export function listSetting(query) {
  return request({
    url: '/system/setting/list',
    method: 'get',
    params: query
  })
}

// 查询博客设置详细
export function getSetting(id) {
  return request({
    url: '/system/setting/' + id,
    method: 'get'
  })
}

// 根据配置键查询配置值
export function getConfigByKey(configKey) {
  return request({
    url: '/system/setting/configKey/' + configKey,
    method: 'get'
  })
}

// 新增博客设置
export function addSetting(data) {
  return request({
    url: '/system/setting',
    method: 'post',
    data: data
  })
}

// 修改博客设置
export function updateSetting(data) {
  return request({
    url: '/system/setting',
    method: 'put',
    data: data
  })
}

// 删除博客设置
export function delSetting(ids) {
  return request({
    url: '/system/setting/' + ids,
    method: 'delete'
  })
}

// 刷新缓存
export function refreshCache() {
  return request({
    url: '/system/setting/refreshCache',
    method: 'delete'
  })
}