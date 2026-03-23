import { describe, it, expect, vi } from 'vitest'
import { listType, getType, addType, updateType, delType, refreshCache } from './type'

vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Dict Type API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listType', () => {
    it('应该导出 listType 函数', () => {
      expect(listType).toBeDefined()
      expect(typeof listType).toBe('function')
    })
  })

  describe('getType', () => {
    it('应该导出 getType 函数', () => {
      expect(getType).toBeDefined()
      expect(typeof getType).toBe('function')
    })
  })

  describe('addType', () => {
    it('应该导出 addType 函数', () => {
      expect(addType).toBeDefined()
      expect(typeof addType).toBe('function')
    })
  })

  describe('updateType', () => {
    it('应该导出 updateType 函数', () => {
      expect(updateType).toBeDefined()
      expect(typeof updateType).toBe('function')
    })
  })

  describe('delType', () => {
    it('应该导出 delType 函数', () => {
      expect(delType).toBeDefined()
      expect(typeof delType).toBe('function')
    })
  })

  describe('refreshCache', () => {
    it('应该导出 refreshCache 函数', () => {
      expect(refreshCache).toBeDefined()
      expect(typeof refreshCache).toBe('function')
    })
  })
})
