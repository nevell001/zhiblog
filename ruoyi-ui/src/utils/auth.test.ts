import { describe, it, expect, vi, beforeEach } from 'vitest'
import Cookies from 'js-cookie'
import { getToken, setToken, removeToken, getBlogToken, setBlogToken, removeBlogToken } from './auth'

// Mock Cookies
vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn(() => undefined),
    set: vi.fn(),
    remove: vi.fn()
  }
}))

const TokenKey = 'Admin-Token'
const BlogTokenKey = 'Blog-Token'

describe('Auth 工具函数测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getToken', () => {
    it('应该导出 getToken 函数', () => {
      expect(getToken).toBeDefined()
      expect(typeof getToken).toBe('function')
    })

    it('应该从 cookies 获取 token', () => {
      const mockToken = 'test-token-123'
      vi.mocked(Cookies.get).mockReturnValue(mockToken)
      const result = getToken()
      expect(Cookies.get).toHaveBeenCalledWith(TokenKey)
      expect(result).toBe(mockToken)
    })

    it('token 不存在时应该返回 undefined', () => {
      vi.mocked(Cookies.get).mockReturnValue(undefined)
      const result = getToken()
      expect(result).toBeUndefined()
    })
  })

  describe('setToken', () => {
    it('应该导出 setToken 函数', () => {
      expect(setToken).toBeDefined()
      expect(typeof setToken).toBe('function')
    })

    it('应该设置 token 到 cookies', () => {
      const mockToken = 'test-token-123'
      setToken(mockToken)
      expect(Cookies.set).toHaveBeenCalledWith(TokenKey, mockToken)
    })
  })

  describe('removeToken', () => {
    it('应该导出 removeToken 函数', () => {
      expect(removeToken).toBeDefined()
      expect(typeof removeToken).toBe('function')
    })

    it('应该从 cookies 移除 token', () => {
      removeToken()
      expect(Cookies.remove).toHaveBeenCalledWith(TokenKey)
    })
  })

  describe('getBlogToken', () => {
    it('应该导出 getBlogToken 函数', () => {
      expect(getBlogToken).toBeDefined()
      expect(typeof getBlogToken).toBe('function')
    })

    it('应该从 cookies 获取博客 token', () => {
      const mockToken = 'blog-token-456'
      vi.mocked(Cookies.get).mockReturnValue(mockToken)
      const result = getBlogToken()
      expect(Cookies.get).toHaveBeenCalledWith(BlogTokenKey)
      expect(result).toBe(mockToken)
    })

    it('博客 token 不存在时应该返回 undefined', () => {
      vi.mocked(Cookies.get).mockReturnValue(undefined)
      const result = getBlogToken()
      expect(result).toBeUndefined()
    })
  })

  describe('setBlogToken', () => {
    it('应该导出 setBlogToken 函数', () => {
      expect(setBlogToken).toBeDefined()
      expect(typeof setBlogToken).toBe('function')
    })

    it('应该设置博客 token 到 cookies', () => {
      const mockToken = 'blog-token-456'
      setBlogToken(mockToken)
      expect(Cookies.set).toHaveBeenCalledWith(BlogTokenKey, mockToken)
    })
  })

  describe('removeBlogToken', () => {
    it('应该导出 removeBlogToken 函数', () => {
      expect(removeBlogToken).toBeDefined()
      expect(typeof removeBlogToken).toBe('function')
    })

    it('应该从 cookies 移除博客 token', () => {
      removeBlogToken()
      expect(Cookies.remove).toHaveBeenCalledWith(BlogTokenKey)
    })
  })
})