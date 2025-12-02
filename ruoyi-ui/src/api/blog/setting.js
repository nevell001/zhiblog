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

// 获取当前博客设置
export function getCurrentSetting() {
  return request({
    url: '/system/setting/current',
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
export function delSetting(id) {
  return request({
    url: '/system/setting/' + id,
    method: 'delete'
  })
}

// 根据键获取设置值
export function getSettingValueByKey(key) {
  return request({
    url: '/system/setting/value/' + key,
    method: 'get'
  })
}

// 根据键更新设置值
export function updateSettingValueByKey(key, value) {
  return request({
    url: '/system/setting/value/' + key,
    method: 'put',
    data: { value: value }
  })
}

// 前台专用接口
// 获取博客设置（前台用）
export function getBlogSettings() {
  return request({
    url: '/blog/setting',
    method: 'get',
    headers: { isToken: false } // 不携带token，允许匿名访问
  })
}

// 匿名访问博客设置
export function getBlogSettingsAnonymous() {
  return request({
    url: '/blog/setting',
    method: 'get',
    headers: { isToken: false }
  })
}