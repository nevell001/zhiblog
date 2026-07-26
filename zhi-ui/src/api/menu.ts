import request from '@/utils/request'
import type { Menu } from '@/types'

/**
 * 获取路由
 */
export function getRouters(): Promise<Menu[]> {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}
