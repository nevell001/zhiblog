import request from '@/utils/request'
import type { Tag, TagParams, QueryResult } from '@/types'

/**
 * 查询博客标签列表
 */
export function listTag(query?: TagParams): Promise<QueryResult<Tag>> {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询博客标签详细
 */
export function getTag(id: number): Promise<Tag> {
  return request({
    url: '/system/tag/' + id,
    method: 'get'
  })
}

/**
 * 新增博客标签
 */
export function addTag(data: Tag): Promise<any> {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

/**
 * 修改博客标签
 */
export function updateTag(data: Tag): Promise<any> {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

/**
 * 删除博客标签
 */
export function delTag(id: number): Promise<any> {
  return request({
    url: '/system/tag/' + id,
    method: 'delete'
  })
}

/**
 * 导出博客标签
 */
export function exportTag(query?: TagParams): Promise<any> {
  return request({
    url: '/system/tag/export',
    method: 'get',
    params: query
  })
}
