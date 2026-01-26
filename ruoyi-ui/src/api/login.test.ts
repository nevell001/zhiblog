import { describe, it, expect, vi, beforeEach } from 'vitest'
import { login, register, getInfo, logout, getCodeImg } from './login'

// Mock request 模块
vi.mock('@/utils/request', () => ({
  default: vi.fn()
}))

import request from '@/utils/request'
const mockRequest = vi.mocked(request)

describe('Login API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login 函数', () => {
    it('应该发送登录请求', async () => {
      mockRequest.mockResolvedValue({ code: 200, data: { token: 'test-token' } })

      const result = await login('admin', 'admin123')

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/login',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        data: {
          username: 'admin',
          password: 'admin123',
          code: undefined,
          uuid: undefined
        }
      })
      expect(result).toEqual({ code: 200, data: { token: 'test-token' } })
    })

    it('应该发送带验证码的登录请求', async () => {
      mockRequest.mockResolvedValue({ code: 200, data: { token: 'test-token' } })

      const result = await login('admin', 'admin123', '1234', 'uuid-123')

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/login',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        method: 'post',
        data: {
          username: 'admin',
          password: 'admin123',
          code: '1234',
          uuid: 'uuid-123'
        }
      })
      expect(result).toEqual({ code: 200, data: { token: 'test-token' } })
    })

    it('应该处理登录失败', async () => {
      mockRequest.mockResolvedValue({ code: 401, msg: '用户名或密码错误' })

      const result = await login('admin', 'wrong-password')

      expect(result).toEqual({ code: 401, msg: '用户名或密码错误' })
    })
  })

  describe('register 函数', () => {
    it('应该发送注册请求', async () => {
      mockRequest.mockResolvedValue({ code: 200, msg: '注册成功' })

      const result = await register({
        username: 'testuser',
        password: 'password123'
      })

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/register',
        headers: {
          isToken: false
        },
        method: 'post',
        data: {
          username: 'testuser',
          password: 'password123'
        }
      })
      expect(result).toEqual({ code: 200, msg: '注册成功' })
    })

    it('应该发送带验证码的注册请求', async () => {
      mockRequest.mockResolvedValue({ code: 200, msg: '注册成功' })

      const result = await register({
        username: 'testuser',
        password: 'password123',
        code: '5678',
        uuid: 'uuid-456'
      })

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/register',
        headers: {
          isToken: false
        },
        method: 'post',
        data: {
          username: 'testuser',
          password: 'password123',
          code: '5678',
          uuid: 'uuid-456'
        }
      })
      expect(result).toEqual({ code: 200, msg: '注册成功' })
    })
  })

  describe('getInfo 函数', () => {
    it('应该获取用户详细信息', async () => {
      const mockUserInfo = {
        user: {
          userId: 1,
          userName: 'admin',
          nickName: '管理员',
          email: 'admin@example.com'
        },
        roles: ['admin'],
        permissions: ['*:*:*']
      }

      mockRequest.mockResolvedValue({ code: 200, data: mockUserInfo })

      const result = await getInfo()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/getInfo',
        method: 'get'
      })
      expect(result).toEqual({ code: 200, data: mockUserInfo })
    })
  })

  describe('logout 函数', () => {
    it('应该发送退出请求', async () => {
      mockRequest.mockResolvedValue({ code: 200, msg: '退出成功' })

      const result = await logout()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/logout',
        method: 'post'
      })
      expect(result).toEqual({ code: 200, msg: '退出成功' })
    })
  })

  describe('getCodeImg 函数', () => {
    it('应该获取验证码图片', async () => {
      const mockCaptcha = {
        captchaEnabled: true,
        uuid: 'uuid-789',
        img: 'data:image/png;base64,...'
      }

      mockRequest.mockResolvedValue({ code: 200, data: mockCaptcha })

      const result = await getCodeImg()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/captchaImage',
        headers: {
          isToken: false
        },
        method: 'get',
        timeout: 20000
      })
      expect(result).toEqual({ code: 200, data: mockCaptcha })
    })

    it('应该处理验证码获取失败', async () => {
      mockRequest.mockResolvedValue({ code: 500, msg: '获取验证码失败' })

      const result = await getCodeImg()

      expect(result).toEqual({ code: 500, msg: '获取验证码失败' })
    })
  })
})