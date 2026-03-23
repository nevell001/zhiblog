import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as authApi from './auth'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Blog Auth API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('blogLogin', () => {
    it('应该导出 blogLogin 函数', () => {
      expect(authApi.blogLogin).toBeDefined()
      expect(typeof authApi.blogLogin).toBe('function')
    })

    it('应该调用博客登录接口', () => {
      const loginData = { username: 'test', password: 'password' }
      authApi.blogLogin(loginData)
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/login',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        data: loginData
      })
    })
  })

  describe('blogRegister', () => {
    it('应该导出 blogRegister 函数', () => {
      expect(authApi.blogRegister).toBeDefined()
      expect(typeof authApi.blogRegister).toBe('function')
    })

    it('应该调用博客注册接口', () => {
      const registerData = { 
        username: 'test', 
        password: 'password', 
        confirmPassword: 'password',
        email: 'test@example.com',
        emailCode: '123456'
      }
      authApi.blogRegister(registerData)
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/register',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        data: registerData
      })
    })
  })

  describe('sendRegisterCode', () => {
    it('应该导出 sendRegisterCode 函数', () => {
      expect(authApi.sendRegisterCode).toBeDefined()
      expect(typeof authApi.sendRegisterCode).toBe('function')
    })

    it('应该调用发送注册验证码接口', () => {
      authApi.sendRegisterCode('test@example.com')
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/send-register-code',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        params: { email: 'test@example.com' }
      })
    })
  })

  describe('sendResetCode', () => {
    it('应该导出 sendResetCode 函数', () => {
      expect(authApi.sendResetCode).toBeDefined()
      expect(typeof authApi.sendResetCode).toBe('function')
    })

    it('应该调用发送密码重置验证码接口', () => {
      authApi.sendResetCode('test@example.com')
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/send-reset-code',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        params: { email: 'test@example.com' }
      })
    })
  })

  describe('resetPassword', () => {
    it('应该导出 resetPassword 函数', () => {
      expect(authApi.resetPassword).toBeDefined()
      expect(typeof authApi.resetPassword).toBe('function')
    })

    it('应该调用重置密码接口', () => {
      const resetData = { 
        email: 'test@example.com',
        code: '123456',
        newPassword: 'newPassword',
        confirmPassword: 'newPassword'
      }
      authApi.resetPassword(resetData)
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/reset-password',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        params: resetData
      })
    })
  })

  describe('getBlogUserInfo', () => {
    it('应该导出 getBlogUserInfo 函数', () => {
      expect(authApi.getBlogUserInfo).toBeDefined()
      expect(typeof authApi.getBlogUserInfo).toBe('function')
    })

    it('应该调用获取博客用户信息接口', () => {
      authApi.getBlogUserInfo()
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/info',
        method: 'get'
      })
    })
  })

  describe('blogLogout', () => {
    it('应该导出 blogLogout 函数', () => {
      expect(authApi.blogLogout).toBeDefined()
      expect(typeof authApi.blogLogout).toBe('function')
    })

    it('应该调用博客登出接口', () => {
      authApi.blogLogout()
      expect(request).toHaveBeenCalledWith({
        url: '/blog/auth/logout',
        method: 'post'
      })
    })
  })

  describe('getCodeImg', () => {
    it('应该导出 getCodeImg 函数', () => {
      expect(authApi.getCodeImg).toBeDefined()
      expect(typeof authApi.getCodeImg).toBe('function')
    })

    it('应该调用获取验证码图片接口', () => {
      authApi.getCodeImg()
      expect(request).toHaveBeenCalledWith({
        url: '/captchaImage',
        headers: {
          isToken: false
        },
        method: 'get',
        timeout: 20000
      })
    })
  })
})
