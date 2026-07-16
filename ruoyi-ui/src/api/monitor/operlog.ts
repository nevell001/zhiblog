import request from '@/utils/request'
import type { PageParams, QueryResult } from '@/types'

/**
 * 操作日志
 */
export interface OperLog {
  operId?: number
  title?: string
  businessType?: number
  method?: string
  requestMethod?: string
  operatorType?: number
  operName?: string
  deptName?: string
  operUrl?: string
  operIp?: string
  operLocation?: string
  operParam?: string
  jsonResult?: string
  status?: number
  errorMsg?: string
  operTime?: string
  costTime?: number
}

/**
 * 查询操作日志列表
 */
export function list(query?: PageParams): Promise<QueryResult<OperLog>> {
  return request({
    url: '/monitor/operlog/list',
    method: 'get',
    params: query
  })
}

/**
 * 删除操作日志
 */
export function delOperlog(operId: number | number[]): Promise<any> {
  return request({
    url: '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

/**
 * 清空操作日志
 */
export function cleanOperlog(): Promise<any> {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete'
  })
}
