import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  login,
  register,
  getInfo,
  logout,
  getCodeImg
} from './login'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Login API 测试', () => {
  const request = vi.fn()
  const mockRequest = vi.mocked(request)

  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login', () => {
    it('应该导出 login 函数', () => {
      expect(login).toBeDefined()
      expect(typeof login).toBe('function')
    })

    it('应该调用 POST /login 并设置正确的 headers', async () => {
      mockRequest.mockResolvedValue({ code: 200, token: 'test-token' })

      await login('testuser', 'password123')

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/login',
        method: 'post',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        data: {
          username: 'testuser',
          password: 'password123',
          code: undefined,
          uuid: undefined
        }
      })
    })

    it('应该支持验证码参数', async () => {
      mockRequest.mockResolvedValue({ code: 200, token: 'test-token' })

      await login('testuser', 'password123', '1234', 'uuid-123')

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/login',
        method: 'post',
        headers: {
          isToken: false,
          repeatSubmit: false
        },
        data: {
          username: 'testuser',
          password: 'password123',
          code: '1234',
          uuid: 'uuid-123'
        }
      })
    })
  })

  describe('register', () => {
    it('应该导出 register 函数', () => {
      expect(register).toBeDefined()
      expect(typeof register).toBe('function')
    })

    it('应该调用 POST /register 并设置 isToken: false', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      const registerData = {
        username: 'newuser',
        password: 'password123',
        code: '1234',
        uuid: 'uuid-123'
      }
      await register(registerData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/register',
        method: 'post',
        headers: {
          isToken: false
        },
        data: registerData
      })
    })

    it('应该支持不带验证码的注册', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      const registerData = {
        username: 'newuser',
        password: 'password123'
      }
      await register(registerData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/register',
        method: 'post',
        headers: {
          isToken: false
        },
        data: registerData
      })
    })
  })

  describe('getInfo', () => {
    it('应该导出 getInfo 函数', () => {
      expect(getInfo).toBeDefined()
      expect(typeof getInfo).toBe('function')
    })

    it('应该调用 GET /getInfo', async () => {
      mockRequest.mockResolvedValue({ code: 200, user: { id: 1, username: 'test' } })

      await getInfo()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/getInfo',
        method: 'get'
      })
    })
  })

  describe('logout', () => {
    it('应该导出 logout 函数', () => {
      expect(logout).toBeDefined()
      expect(typeof logout).toBe('function')
    })

    it('应该调用 POST /logout', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      await logout()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/logout',
        method: 'post'
      })
    })
  })

  describe('getCodeImg', () => {
    it('应该导出 getCodeImg 函数', () => {
      expect(getCodeImg).toBeDefined()
      expect(typeof getCodeImg).toBe('function')
    })

    it('应该调用 GET /captchaImage 并设置 isToken: false 和超时时间', async () => {
      mockRequest.mockResolvedValue({ img: 'base64-image-data' })

      await getCodeImg()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/captchaImage',
        method: 'get',
        headers: {
          isToken: false
        },
        timeout: 20000
      })
    })
  })
})
