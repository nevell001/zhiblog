import request from '@/utils/request'
import type { PageParams, QueryResult } from '@/types'

interface Logininfor {
  infoId?: number
  userName?: string
  ipaddr?: string
  loginLocation?: string
  browser?: string
  os?: string
  status?: string
  msg?: string
  loginTime?: string
}

/**
 * 查询登录日志列表
 */
export function list(query?: PageParams): Promise<QueryResult<Logininfor>> {
  return request({
    url: '/monitor/logininfor/list',
    method: 'get',
    params: query
  })
}

/**
 * 删除登录日志
 */
export function delLogininfor(infoId: number): Promise<any> {
  return request({
    url: '/monitor/logininfor/' + infoId,
    method: 'delete'
  })
}

/**
 * 解锁用户登录状态
 */
export function unlockLogininfor(userName: string): Promise<any> {
  return request({
    url: '/monitor/logininfor/unlock/' + userName,
    method: 'get'
  })
}

/**
 * 清空登录日志
 */
export function cleanLogininfor(): Promise<any> {
  return request({
    url: '/monitor/logininfor/clean',
    method: 'delete'
  })
}
