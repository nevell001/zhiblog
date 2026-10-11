import request from '@/utils/request'
import type { DataResult } from '@/types'

/**
 * 获取博客设置（前台用）
 */
export function getBlogSettings(): Promise<DataResult<Record<string, unknown>>> {
  return request({
    url: '/common/blog/setting',
    method: 'get',
    headers: { isToken: false },
    params: { _t: Date.now() }
  })
}

/**
 * 匿名访问博客设置
 */
export function getBlogSettingsAnonymous(): Promise<DataResult<Record<string, unknown>>> {
  return request({
    url: '/common/blog/setting',
    method: 'get',
    headers: { isToken: false },
    params: { _t: Date.now() }
  })
}

/**
 * 清除博客设置缓存
 */
export function clearBlogCache(): Promise<{ code: number; msg: string }> {
  return request({
    url: '/common/blog/setting/clear-blog-cache',
    method: 'get'
  })
}
