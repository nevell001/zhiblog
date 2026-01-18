import request from '@/utils/request'
import type { FriendLink, PageParams, QueryResult } from '@/types'

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
export function getFriendLink(id: number): Promise<FriendLink> {
  return request({
    url: '/system/friendLink/' + id,
    method: 'get'
  })
}

/**
 * 新增友链
 */
export function addFriendLink(data: FriendLink): Promise<any> {
  return request({
    url: '/system/friendLink',
    method: 'post',
    data: data
  })
}

/**
 * 修改友链
 */
export function updateFriendLink(data: FriendLink): Promise<any> {
  return request({
    url: '/system/friendLink',
    method: 'put',
    data: data
  })
}

/**
 * 删除友链
 */
export function delFriendLink(id: number): Promise<any> {
  return request({
    url: '/system/friendLink/' + id,
    method: 'delete'
  })
}

/**
 * 查询前台友链列表（用于前台展示）
 */
export function getFrontFriendLinkList(): Promise<FriendLink[]> {
  return request({
    url: '/system/friendLink/front/list',
    method: 'get'
  })
}
