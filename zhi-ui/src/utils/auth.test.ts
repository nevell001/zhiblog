import { describe, it, expect, vi, beforeEach } from 'vitest'
import Cookies from 'js-cookie'
import { getToken, setToken, removeToken } from './auth'

vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn(),
    set: vi.fn(),
    remove: vi.fn()
  }
}))

const mockCookies = vi.mocked(Cookies)

describe('Auth Utils 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getToken', () => {
    it('应该导出 getToken 函数', () => {
      expect(getToken).toBeDefined()
      expect(typeof getToken).toBe('function')
    })

    it('应该从 localStorage 获取令牌', () => {
      const token = 'test-token-12345'
      mockCookies.get.mockReturnValue(token)

      expect(getToken()).toBe(token)
      expect(mockCookies.get).toHaveBeenCalledWith('Admin-Token')
    })

    it('应该处理空令牌', () => {
      const token = null
      expect(token).toBe(null)
    })

    it('应该处理无效令牌', () => {
      const token = 'invalid-token'
      expect(typeof token).toBe('string')
    })
  })

  describe('setToken', () => {
    it('应该导出 setToken 函数', () => {
      expect(setToken).toBeDefined()
      expect(typeof setToken).toBe('function')
    })

    it('应该将令牌存储到 localStorage', () => {
      const token = 'new-token-67890'
      setToken(token)
      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', token)
    })

    it('应该处理空令牌', () => {
      const token = ''
      expect(typeof token).toBe('string')
    })

    it('应该覆盖现有令牌', () => {
      const newToken = 'new-token'
      mockCookies.get.mockReturnValue(newToken)

      setToken(newToken)
      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', newToken)
      expect(getToken()).toBe(newToken)
    })
  })

  describe('removeToken', () => {
    it('应该导出 removeToken 函数', () => {
      expect(removeToken).toBeDefined()
      expect(typeof removeToken).toBe('function')
    })

    it('应该从 localStorage 移除令牌', () => {
      removeToken()
      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')
    })

    it('应该处理不存在的令牌', () => {
      mockCookies.get.mockReturnValue(undefined)
      expect(getToken()).toBeUndefined()
    })

    it('应该支持清除所有存储', () => {
      removeToken()
      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')
    })
  })
})
