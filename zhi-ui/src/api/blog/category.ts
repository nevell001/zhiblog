import request from '@/utils/request'
import type { Category, PageParams, QueryResult } from '@/types'

/**
 * 获取分类列表（前台用）
 */
export function getCategoryList(query?: PageParams): Promise<QueryResult<Category>> {
  return request({
    url: '/blog/api/category/list',
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

/**
 * 获取分类详情（前台用）
 */
export function getCategoryDetail(id: number): Promise<Category & { data?: Category }> {
  return request({
    url: '/blog/api/category/' + id,
    method: 'get',
    headers: { isToken: false }
  })
}
