import request from '@/utils/request'
import type { PageParams, QueryResult } from '@/types'

interface JobLog {
  jobLogId?: number
  jobName?: string
  jobGroup?: string
  invokeTarget?: string
  jobMessage?: string
  status?: string
  exceptionInfo?: string
  createTime?: string
  startTime?: string
  stopTime?: string
}

/**
 * 查询调度日志列表
 */
export function listJobLog(query?: PageParams): Promise<QueryResult<JobLog>> {
  return request({
    url: '/monitor/jobLog/list',
    method: 'get',
    params: query
  })
}

/**
 * 删除调度日志
 */
export function delJobLog(jobLogId: number | number[]): Promise<any> {
  return request({
    url: '/monitor/jobLog/' + jobLogId,
    method: 'delete'
  })
}

/**
 * 清空调度日志
 */
export function cleanJobLog(): Promise<any> {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete'
  })
}
