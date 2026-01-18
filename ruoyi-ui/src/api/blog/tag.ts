import request from '@/utils/request'
import type { Tag, PageParams, QueryResult } from '@/types'

/**
 * 查询博客标签列表
 */
export function listTag(query?: PageParams): Promise<QueryResult<Tag>> {
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
 * 获取标签列表（前台用）
 */
export function getTagList(query?: PageParams): Promise<QueryResult<Tag>> {
  return request({
    url: '/blog/tag/list',
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 获取标签详情（前台用）
 */
export function getTagDetail(id: number): Promise<Tag> {
  return request({
    url: '/blog/tag/' + id,
    method: 'get'
  })
}

/**
 * 获取标签云
 */
export function getTagCloud(): Promise<Tag[]> {
  return request({
    url: '/blog/tag/cloud',
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 根据标签获取文章列表
 */
export function getArticlesByTag(tagId: number, query?: PageParams): Promise<any> {
  return request({
    url: '/blog/article/tag/' + tagId,
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}
