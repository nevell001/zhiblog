import { describe, it, expect, vi } from 'vitest'
import { listConfig, getConfig, delConfig, addConfig, updateConfig, refreshCache } from './config'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Config API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listConfig', () => {
    it('应该导出 listConfig 函数', () => {
      expect(listConfig).toBeDefined()
      expect(typeof listConfig).toBe('function')
    })
  })

  describe('getConfig', () => {
    it('应该导出 getConfig 函数', () => {
      expect(getConfig).toBeDefined()
      expect(typeof getConfig).toBe('function')
    })
  })

  describe('delConfig', () => {
    it('应该导出 delConfig 函数', () => {
      expect(delConfig).toBeDefined()
      expect(typeof delConfig).toBe('function')
    })
  })

  describe('addConfig', () => {
    it('应该导出 addConfig 函数', () => {
      expect(addConfig).toBeDefined()
      expect(typeof addConfig).toBe('function')
    })
  })

  describe('updateConfig', () => {
    it('应该导出 updateConfig 函数', () => {
      expect(updateConfig).toBeDefined()
      expect(typeof updateConfig).toBe('function')
    })
  })

  describe('refreshCache', () => {
    it('应该导出 refreshCache 函数', () => {
      expect(refreshCache).toBeDefined()
      expect(typeof refreshCache).toBe('function')
    })
  })
})