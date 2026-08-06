import request from '@/utils/request'

/**
 * 统一登录接口
 * @param data 登录信息
 */
export function unifiedLogin(data: any): Promise<any> {
  return request({
    url: '/auth/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 获取当前登录用户信息
 */
export function getUserInfo(): Promise<any> {
  return request({
    url: '/auth/user/info',
    method: 'get'
  })
}

/**
 * 统一登出
 */
export function logout(): Promise<any> {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}
