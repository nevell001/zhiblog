import request from '@/utils/request'
import type { PageParams, QueryResult } from '@/types'

/**
 * 在线用户
 */
export interface OnlineUser {
  tokenId: string
  userName?: string
  ipaddr?: string
  loginLocation?: string
  browser?: string
  os?: string
  status?: string
  loginTime?: string
}

/**
 * 查询在线用户列表
 */
export function list(query?: PageParams): Promise<QueryResult<OnlineUser>> {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

/**
 * 强退用户
 */
export function forceLogout(tokenId: string): Promise<any> {
  return request({
    url: '/monitor/online/' + tokenId,
    method: 'delete'
  })
}
