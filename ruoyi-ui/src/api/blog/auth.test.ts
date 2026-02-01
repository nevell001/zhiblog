import { describe, it, expect, vi } from 'vitest'
import { blogLogin, blogRegister, blogLogout, getBlogUserInfo, sendRegisterCode } from './auth'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('BlogAuth API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('blogLogin', () => {
    it('应该导出 blogLogin 函数', () => {
      expect(blogLogin).toBeDefined()
      expect(typeof blogLogin).toBe('function')
    })
  })

  describe('blogRegister', () => {
    it('应该导出 blogRegister 函数', () => {
      expect(blogRegister).toBeDefined()
      expect(typeof blogRegister).toBe('function')
    })
  })

  describe('blogLogout', () => {
    it('应该导出 blogLogout 函数', () => {
      expect(blogLogout).toBeDefined()
      expect(typeof blogLogout).toBe('function')
    })
  })

  describe('getBlogUserInfo', () => {
    it('应该导出 getBlogUserInfo 函数', () => {
      expect(getBlogUserInfo).toBeDefined()
      expect(typeof getBlogUserInfo).toBe('function')
    })
  })

  describe('sendRegisterCode', () => {
    it('应该导出 sendRegisterCode 函数', () => {
      expect(sendRegisterCode).toBeDefined()
      expect(typeof sendRegisterCode).toBe('function')
    })
  })
})