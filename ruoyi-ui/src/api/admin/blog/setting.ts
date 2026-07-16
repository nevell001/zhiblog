import request from '@/utils/request'
import type { BlogSetting, DataResult, PageParams, QueryResult } from '@/types'

/**
 * 查询博客设置列表
 */
export function listSetting(
  query?: PageParams
): Promise<QueryResult<BlogSetting & Record<string, any>>> {
  return request({
    url: '/system/setting/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询博客设置详细
 */
export function getSetting(id: number): Promise<DataResult<BlogSetting>> {
  return request({
    url: '/system/setting/' + id,
    method: 'get'
  })
}

/**
 * 根据配置键查询配置值
 */
export function getConfigByKey(configKey: string): Promise<any> {
  return request({
    url: '/system/setting/value/' + configKey,
    method: 'get'
  })
}

/**
 * 新增博客设置
 */
export function addSetting(data: Partial<BlogSetting> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/setting',
    method: 'post',
    data: data
  })
}

/**
 * 修改博客设置
 */
export function updateSetting(data: Partial<BlogSetting> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/setting',
    method: 'put',
    data: data
  })
}

/**
 * 删除博客设置
 */
export function delSetting(ids: number | number[]): Promise<any> {
  return request({
    url: '/system/setting/' + ids,
    method: 'delete'
  })
}

/**
 * 刷新缓存
 */
export function refreshCache(): Promise<any> {
  return request({
    url: '/system/setting/refreshCache',
    method: 'delete'
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
