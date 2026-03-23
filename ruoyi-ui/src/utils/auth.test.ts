import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { getToken, setToken, removeToken, getBlogToken, setBlogToken, removeBlogToken } from './auth'

// Mock Cookies
const mockCookies = {
  get: vi.fn(),
  set: vi.fn(),
  remove: vi.fn()
}

beforeEach(() => {
  vi.clearAllMocks()

  // Mock Cookies
  Object.defineProperty(global, 'Cookies', {
    value: mockCookies,
    writable: true,
    configurable: true
  })
})

afterEach(() => {
  vi.restoreAllMocks()
})

describe('auth 工具测试', () => {
  describe('Token 管理函数', () => {
    it('应该导出 getToken 函数', () => {
      expect(getToken).toBeDefined()
      expect(typeof getToken).toBe('function')
    })

    it('应该从 Cookies 获取 Admin-Token', () => {
      mockCookies.get.mockReturnValue('test-token')

      const result = getToken()

      expect(mockCookies.get).toHaveBeenCalledWith('Admin-Token')
      expect(result).toBe('test-token')
    })

    it('应该从 Cookies 获取 Blog-Token', () => {
      mockCookies.get.mockReturnValue('blog-token')

      const result = getBlogToken()

      expect(mockCookies.get).toHaveBeenCalledWith('Blog-Token')
      expect(result).toBe('blog-token')
    })

    it('应该设置 Admin-Token 到 Cookies', () => {
      mockCookies.set.mockReturnValue(undefined)

      const result = setToken('test-token')

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', 'test-token')
      expect(result).toBe('test-token')
    })

    it('应该设置 Blog-Token 到 Cookies', () => {
      mockCookies.set.mockReturnValue(undefined)

      const result = setBlogToken('blog-token')

      expect(mockCookies.set).toHaveBeenCalledWith('Blog-Token', 'blog-token')
      expect(result).toBe('blog-token')
    })

    it('应该移除 Admin-Token', () => {
      mockCookies.remove.mockReturnValue(undefined)

      removeToken()

      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')
    })

    it('应该移除 Blog-Token', () => {
      mockCookies.remove.mockReturnValue(undefined)

      removeBlogToken()

      expect(mockCookies.remove).toHaveBeenCalledWith('Blog-Token')
    })
  })
})
