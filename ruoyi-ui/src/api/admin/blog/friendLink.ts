import request from '@/utils/request'
import type { DataResult, FriendLink, PageParams, QueryResult } from '@/types'

/**
 * 查询友链列表
 */
export function listFriendLink(query?: PageParams): Promise<QueryResult<FriendLink>> {
  return request({
    url: '/system/friendLink/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询友链详细
 */
export function getFriendLink(id: number): Promise<DataResult<FriendLink>> {
  return request({
    url: '/system/friendLink/' + id,
    method: 'get'
  })
}

/**
 * 新增友链
 */
export function addFriendLink(data: Partial<FriendLink> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/friendLink',
    method: 'post',
    data: data
  })
}

/**
 * 修改友链
 */
export function updateFriendLink(data: Partial<FriendLink> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/friendLink',
    method: 'put',
    data: data
  })
}

/**
 * 删除友链
 */
export function delFriendLink(ids: number | number[]): Promise<any> {
  return request({
    url: '/system/friendLink/' + ids,
    method: 'delete'
  })
}

/**
 * 导出友链
 */
export function exportFriendLink(query?: PageParams): Promise<any> {
  return request({
    url: '/system/friendLink/export',
    method: 'post',
    params: query
  })
}
