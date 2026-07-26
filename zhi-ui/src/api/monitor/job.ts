import request from '@/utils/request'
import type { DataResult, PageParams, QueryResult } from '@/types'

interface Job {
  jobId?: number
  jobName?: string
  jobGroup?: string
  invokeTarget?: string
  cronExpression?: string
  misfirePolicy?: string
  concurrent?: string
  status?: string
}

/**
 * 查询定时任务调度列表
 */
export function listJob(query?: PageParams): Promise<QueryResult<Job>> {
  return request({
    url: '/monitor/job/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询定时任务调度详细
 */
export function getJob(jobId: number): Promise<DataResult<Job>> {
  return request({
    url: '/monitor/job/' + jobId,
    method: 'get'
  })
}

/**
 * 新增定时任务调度
 */
export function addJob(data: Partial<Job> | Record<string, any>): Promise<any> {
  return request({
    url: '/monitor/job',
    method: 'post',
    data: data
  })
}

/**
 * 修改定时任务调度
 */
export function updateJob(data: Partial<Job> | Record<string, any>): Promise<any> {
  return request({
    url: '/monitor/job',
    method: 'put',
    data: data
  })
}

/**
 * 删除定时任务调度
 */
export function delJob(jobId: number): Promise<any> {
  return request({
    url: '/monitor/job/' + jobId,
    method: 'delete'
  })
}

/**
 * 任务状态修改
 */
export function changeJobStatus(jobId: number, status: string): Promise<any> {
  const data = {
    jobId,
    status
  }
  return request({
    url: '/monitor/job/changeStatus',
    method: 'put',
    data: data
  })
}

/**
 * 定时任务立即执行一次
 */
export function runJob(jobId: number, jobGroup = ''): Promise<any> {
  const data = {
    jobId,
    jobGroup
  }
  return request({
    url: '/monitor/job/run',
    method: 'put',
    data: data
  })
}
