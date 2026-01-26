import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import { getToken, setToken, removeToken } from './auth'

// Mock js-cookie
vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn(),
    set: vi.fn(),
    remove: vi.fn()
  }
}))

import Cookies from 'js-cookie'
const mockCookies = vi.mocked(Cookies)

describe('Auth 工具测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('getToken 函数', () => {
    it('应该获取 Token', () => {
      mockCookies.get.mockReturnValue('test-token-123')

      const token = getToken()

      expect(mockCookies.get).toHaveBeenCalledWith('Admin-Token')
      expect(token).toBe('test-token-123')
    })

    it('应该返回 undefined 当 Token 不存在时', () => {
      mockCookies.get.mockReturnValue(undefined)

      const token = getToken()

      expect(mockCookies.get).toHaveBeenCalledWith('Admin-Token')
      expect(token).toBeUndefined()
    })

    it('应该返回 null 当 Token 为 null 时', () => {
      mockCookies.get.mockReturnValue(null as any)

      const token = getToken()

      expect(mockCookies.get).toHaveBeenCalledWith('Admin-Token')
      expect(token).toBeNull()
    })
  })

  describe('setToken 函数', () => {
    it('应该设置 Token', () => {
      mockCookies.set.mockReturnValue('test-token-456')

      const result = setToken('test-token-456')

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', 'test-token-456')
      expect(result).toBe('test-token-456')
    })

    it('应该设置空字符串 Token', () => {
      mockCookies.set.mockReturnValue('')

      const result = setToken('')

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', '')
      expect(result).toBe('')
    })

    it('应该设置长 Token', () => {
      const longToken = 'a'.repeat(1000)
      mockCookies.set.mockReturnValue(longToken)

      const result = setToken(longToken)

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', longToken)
      expect(result).toBe(longToken)
    })
  })

  describe('removeToken 函数', () => {
    it('应该移除 Token', () => {
      removeToken()

      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')
    })

    it('应该多次调用移除 Token', () => {
      removeToken()
      removeToken()
      removeToken()

      expect(mockCookies.remove).toHaveBeenCalledTimes(3)
      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的 Token 操作流程', () => {
      // 初始状态：没有 Token
      mockCookies.get.mockReturnValue(undefined)
      expect(getToken()).toBeUndefined()

      // 设置 Token
      mockCookies.set.mockReturnValue('new-token')
      setToken('new-token')
      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', 'new-token')

      // 获取 Token
      mockCookies.get.mockReturnValue('new-token')
      expect(getToken()).toBe('new-token')

      // 移除 Token
      removeToken()
      expect(mockCookies.remove).toHaveBeenCalledWith('Admin-Token')

      // 验证 Token 已移除
      mockCookies.get.mockReturnValue(undefined)
      expect(getToken()).toBeUndefined()
    })

    it('应该支持 Token 更新', () => {
      // 设置初始 Token
      mockCookies.set.mockReturnValue('old-token')
      setToken('old-token')

      // 更新 Token
      mockCookies.set.mockReturnValue('new-token')
      setToken('new-token')

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', 'new-token')
    })

    it('应该处理 Token 过期场景', () => {
      // 设置 Token
      mockCookies.set.mockReturnValue('expired-token')
      setToken('expired-token')

      // 模拟 Token 过期（返回 undefined）
      mockCookies.get.mockReturnValue(undefined)
      expect(getToken()).toBeUndefined()

      // 重新设置新 Token
      mockCookies.set.mockReturnValue('new-token')
      setToken('new-token')

      expect(mockCookies.set).toHaveBeenCalledWith('Admin-Token', 'new-token')
    })
  })
})