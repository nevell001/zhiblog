import request from '@/utils/request'
import type { AxiosPromise } from 'axios'
import type { ServerInfo } from '@/types'

/**
 * 获取服务信息
 */
export function getServer(): AxiosPromise<ServerInfo> {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}
