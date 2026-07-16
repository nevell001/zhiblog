import request from '@/utils/request'
import type { DataResult, PageParams, QueryResult } from '@/types'

/**
 * 配置信息
 */
export interface Config {
  configId?: number
  configName: string
  configKey: string
  configValue: string
  configType?: string
  remark?: string
}

/**
 * 查询参数列表
 */
export function listConfig(query?: PageParams): Promise<QueryResult<Config>> {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询参数详细
 */
export function getConfig(configId: number): Promise<DataResult<Config>> {
  return request({
    url: '/system/config/' + configId,
    method: 'get'
  })
}

/**
 * 根据参数键名查询参数值
 */
export function getConfigKey(configKey: string): Promise<Config> {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

/**
 * 新增参数配置
 */
export function addConfig(data: Partial<Config> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/config',
    method: 'post',
    data: data
  })
}

/**
 * 修改参数配置
 */
export function updateConfig(data: Partial<Config> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/config',
    method: 'put',
    data: data
  })
}

/**
 * 删除参数配置
 */
export function delConfig(configId: number): Promise<any> {
  return request({
    url: '/system/config/' + configId,
    method: 'delete'
  })
}

/**
 * 刷新参数缓存
 */
export function refreshCache(): Promise<any> {
  return request({
    url: '/system/config/refreshCache',
    method: 'delete'
  })
}
