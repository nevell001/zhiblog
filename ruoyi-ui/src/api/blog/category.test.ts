import { describe, it, expect, vi } from 'vitest'
import { listCategory, getCategory, delCategory, addCategory, updateCategory } from './category'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Category API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listCategory', () => {
    it('应该导出 listCategory 函数', () => {
      expect(listCategory).toBeDefined()
      expect(typeof listCategory).toBe('function')
    })
  })

  describe('getCategory', () => {
    it('应该导出 getCategory 函数', () => {
      expect(getCategory).toBeDefined()
      expect(typeof getCategory).toBe('function')
    })
  })

  describe('delCategory', () => {
    it('应该导出 delCategory 函数', () => {
      expect(delCategory).toBeDefined()
      expect(typeof delCategory).toBe('function')
    })
  })

  describe('addCategory', () => {
    it('应该导出 addCategory 函数', () => {
      expect(addCategory).toBeDefined()
      expect(typeof addCategory).toBe('function')
    })
  })

  describe('updateCategory', () => {
    it('应该导出 updateCategory 函数', () => {
      expect(updateCategory).toBeDefined()
      expect(typeof updateCategory).toBe('function')
    })
  })
})