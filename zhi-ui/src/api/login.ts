import request from '@/utils/request'
import type { OperResult } from '@/types'

/**
 * 验证码响应
 */
export interface CaptchaResponse {
  img: string
  uuid: string
  captchaEnabled?: boolean
}

/**
 * 退出方法
 */
export function logout(): Promise<OperResult> {
  return request({
    url: '/logout',
    method: 'post'
  })
}

/**
 * 获取验证码
 */
export function getCodeImg(): Promise<CaptchaResponse> {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
