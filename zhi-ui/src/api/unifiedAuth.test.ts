import { describe, it, expect, vi } from 'vitest'
import { unifiedLogin, getUserInfo, logout } from './unifiedAuth'

vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('UnifiedAuth API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('unifiedLogin', () => {
    it('应该导出 unifiedLogin 函数', () => {
      expect(unifiedLogin).toBeDefined()
      expect(typeof unifiedLogin).toBe('function')
    })
  })

  describe('getUserInfo', () => {
    it('应该导出 getUserInfo 函数', () => {
      expect(getUserInfo).toBeDefined()
      expect(typeof getUserInfo).toBe('function')
    })
  })

  describe('logout', () => {
    it('应该导出 logout 函数', () => {
      expect(logout).toBeDefined()
      expect(typeof logout).toBe('function')
    })
  })
})
