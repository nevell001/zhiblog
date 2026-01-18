import request from '@/utils/request'
import type { BlogSetting, PageParams, QueryResult } from '@/types'

/**
 * 查询博客设置列表
 */
export function listSetting(query?: PageParams): Promise<QueryResult<BlogSetting>> {
  return request({
    url: '/system/setting/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询博客设置详细
 */
export function getSetting(id: number): Promise<BlogSetting> {
  return request({
    url: '/system/setting/' + id,
    method: 'get'
  })
}

/**
 * 获取当前博客设置
 */
export function getCurrentSetting(): Promise<BlogSetting[]> {
  return request({
    url: '/system/setting/current',
    method: 'get'
  })
}

/**
 * 新增博客设置
 */
export function addSetting(data: BlogSetting): Promise<any> {
  return request({
    url: '/system/setting',
    method: 'post',
    data: data
  })
}

/**
 * 修改博客设置
 */
export function updateSetting(data: BlogSetting): Promise<any> {
  return request({
    url: '/system/setting',
    method: 'put',
    data: data
  })
}

/**
 * 删除博客设置
 */
export function delSetting(id: number | number[]): Promise<any> {
  return request({
    url: '/system/setting/' + id,
    method: 'delete'
  })
}

/**
 * 根据键获取设置值
 */
export function getSettingValueByKey(key: string): Promise<any> {
  return request({
    url: '/system/setting/value/' + key,
    method: 'get'
  })
}

/**
 * 根据键更新设置值
 */
export function updateSettingValueByKey(key: string, value: string): Promise<any> {
  return request({
    url: '/system/setting/updateByKey',
    method: 'post',
    data: { settingKey: key, settingValue: value }
  })
}

/**
 * 获取博客设置（前台用）
 */
export function getBlogSettings(): Promise<any> {
  return request({
    url: '/common/blog/setting',
    method: 'get',
    headers: { isToken: false },
    params: { _t: Date.now() }
  })
}

/**
 * 匿名访问博客设置
 */
export function getBlogSettingsAnonymous(): Promise<any> {
  return request({
    url: '/common/blog/setting',
    method: 'get',
    headers: { isToken: false },
    params: { _t: Date.now() }
  })
}

/**
 * 更新博客设置
 */
export function updateBlogSettings(data: Record<string, any>): Promise<any> {
  return request({
    url: '/common/blog/setting/update',
    method: 'post',
    data: data
  })
}

/**
 * 清除博客设置缓存
 */
export function clearBlogCache(): Promise<{ code: number; msg: string }> {
  return request({
    url: '/common/blog/setting/clear-blog-cache',
    method: 'get'
  })
}
