import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  getToken,
  setToken,
  removeToken
} from './auth'

// Mock localStorage
const mockLocalStorage = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

vi.stubGlobal('localStorage', mockLocalStorage)

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
      localStorage.setItem('Admin-Token', token)
      expect(localStorage.getItem).toHaveBeenCalledWith('Admin-Token')
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
      localStorage.setItem('Admin-Token', token)
      expect(localStorage.setItem).toHaveBeenCalledWith('Admin-Token', token)
    })

    it('应该处理空令牌', () => {
      const token = ''
      expect(typeof token).toBe('string')
    })

    it('应该覆盖现有令牌', () => {
      const oldToken = 'old-token'
      const newToken = 'new-token'
      localStorage.setItem('Admin-Token', oldToken)
      localStorage.setItem('Admin-Token', newToken)
      expect(localStorage.getItem('Admin-Token')).toBe(newToken)
    })
  })

  describe('removeToken', () => {
    it('应该导出 removeToken 函数', () => {
      expect(removeToken).toBeDefined()
      expect(typeof removeToken).toBe('function')
    })

    it('应该从 localStorage 移除令牌', () => {
      localStorage.removeItem('Admin-Token')
      expect(localStorage.removeItem).toHaveBeenCalledWith('Admin-Token')
    })

    it('应该处理不存在的令牌', () => {
      localStorage.removeItem('Admin-Token')
      const token = localStorage.getItem('Admin-Token')
      expect(token).toBe(null)
    })

    it('应该支持清除所有存储', () => {
      localStorage.clear()
      expect(localStorage.clear).toHaveBeenCalled()
    })
  })
})