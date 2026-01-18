import request from '@/utils/request'
import type { PageParams, QueryResult } from '@/types'

/**
 * 通知公告
 */
export interface Notice {
  noticeId?: number
  noticeTitle: string
  noticeType?: string
  noticeContent?: string
  status?: string
  remark?: string
}

/**
 * 查询公告列表
 */
export function listNotice(query?: PageParams): Promise<QueryResult<Notice>> {
  return request({
    url: '/system/notice/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询公告详细
 */
export function getNotice(noticeId: number): Promise<Notice> {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'get'
  })
}

/**
 * 新增公告
 */
export function addNotice(data: Notice): Promise<any> {
  return request({
    url: '/system/notice',
    method: 'post',
    data: data
  })
}

/**
 * 修改公告
 */
export function updateNotice(data: Notice): Promise<any> {
  return request({
    url: '/system/notice',
    method: 'put',
    data: data
  })
}

/**
 * 删除公告
 */
export function delNotice(noticeId: number): Promise<any> {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'delete'
  })
}
