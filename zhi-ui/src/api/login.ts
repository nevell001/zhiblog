import request from '@/utils/request'
import type { DataResult, OperResult } from '@/types'

/**
 * 登录参数
 */
export interface LoginData {
  username: string
  password: string
  code?: string
  uuid?: string
  rememberMe?: boolean
}

/**
 * 注册参数
 */
export interface RegisterData {
  username: string
  password: string
  code?: string
  uuid?: string
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
}

/**
 * 用户信息响应
 */
export interface UserInfoResponse {
  user: {
    id: number
    userName: string
    nickName: string
    avatar?: string
    userType?: string
    [key: string]: unknown
  }
  roles: string[]
  permissions: string[]
}

/**
 * 验证码响应
 */
export interface CaptchaResponse {
  img: string
  uuid: string
  captchaEnabled?: boolean
}

/**
 * 登录方法
 */
export function login(
  username: string,
  password: string,
  code?: string,
  uuid?: string,
  rememberMe?: boolean
): Promise<LoginResponse> {
  const data: LoginData = {
    username,
    password,
    code,
    uuid,
    rememberMe: rememberMe || false
  }
  return request({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 注册方法
 */
export function register(data: RegisterData): Promise<OperResult> {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 获取用户详细信息
 */
export function getInfo(): Promise<DataResult<UserInfoResponse>> {
  return request({
    url: '/getInfo',
    method: 'get'
  })
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
