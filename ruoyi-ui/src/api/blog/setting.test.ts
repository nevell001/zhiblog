import { describe, it, expect, vi } from 'vitest'
import { getBlogSettings, getBlogSettingsAnonymous, updateBlogSettings } from './setting'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('BlogSetting API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getBlogSettings', () => {
    it('应该导出 getBlogSettings 函数', () => {
      expect(getBlogSettings).toBeDefined()
      expect(typeof getBlogSettings).toBe('function')
    })
  })

  describe('getBlogSettingsAnonymous', () => {
    it('应该导出 getBlogSettingsAnonymous 函数', () => {
      expect(getBlogSettingsAnonymous).toBeDefined()
      expect(typeof getBlogSettingsAnonymous).toBe('function')
    })
  })

  describe('updateBlogSettings', () => {
    it('应该导出 updateBlogSettings 函数', () => {
      expect(updateBlogSettings).toBeDefined()
      expect(typeof updateBlogSettings).toBe('function')
    })
  })
})