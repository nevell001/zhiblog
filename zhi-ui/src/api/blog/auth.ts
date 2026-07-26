import request from '@/utils/request'

/**
 * 博客登录参数
 */
export interface BlogLoginData {
  username: string
  password: string
  code?: string
  uuid?: string
}

/**
 * 博客注册参数
 */
export interface BlogRegisterData {
  username: string
  password: string
  confirmPassword: string
  email: string
  emailCode: string
  code?: string
  uuid?: string
}

/**
 * 博客用户登录
 */
export function blogLogin(data: BlogLoginData): Promise<any> {
  return request({
    url: '/blog/auth/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 博客用户注册
 */
export function blogRegister(data: BlogRegisterData): Promise<any> {
  return request({
    url: '/blog/auth/register',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 发送注册验证码
 */
export function sendRegisterCode(email: string): Promise<any> {
  return request({
    url: '/blog/auth/send-register-code',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: { email }
  })
}

/**
 * 发送密码重置验证码
 */
export function sendResetCode(email: string): Promise<any> {
  return request({
    url: '/blog/auth/send-reset-code',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: { email }
  })
}

/**
 * 重置密码
 */
export function resetPassword(data: {
  email: string
  code: string
  newPassword: string
  confirmPassword: string
}): Promise<any> {
  return request({
    url: '/blog/auth/reset-password',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 获取当前博客用户信息
 */
export function getBlogUserInfo(): Promise<any> {
  return request({
    url: '/blog/auth/info',
    method: 'get'
  })
}

/**
 * 博客用户登出
 */
export function blogLogout(): Promise<any> {
  return request({
    url: '/blog/auth/logout',
    method: 'post'
  })
}

/**
 * 获取验证码图片（复用现有的）
 */
export function getCodeImg(): Promise<any> {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
