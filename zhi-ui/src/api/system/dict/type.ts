import request from '@/utils/request'
import type { DataResult, DictType, DictParams, QueryResult } from '@/types'

/**
 * 查询字典类型列表
 */
export function listType(query?: DictParams): Promise<QueryResult<DictType>> {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询字典类型详细
 */
export function getType(dictId: number): Promise<DataResult<DictType>> {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'get'
  })
}

/**
 * 新增字典类型
 */
export function addType(data: Partial<DictType> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data: data
  })
}

/**
 * 修改字典类型
 */
export function updateType(data: Partial<DictType> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data: data
  })
}

/**
 * 删除字典类型
 */
export function delType(dictId: number): Promise<any> {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'delete'
  })
}

/**
 * 刷新字典缓存
 */
export function refreshCache(): Promise<any> {
  return request({
    url: '/system/dict/type/refreshCache',
    method: 'delete'
  })
}

/**
 * 获取字典选择框列表
 */
export function optionselect(): Promise<any> {
  return request({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}
