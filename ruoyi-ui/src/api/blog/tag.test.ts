import { describe, it, expect, vi } from 'vitest'
import { getTagList, getTagDetail, getTagCloud, getArticlesByTag } from './tag'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Tag API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getTagList', () => {
    it('应该导出 getTagList 函数', () => {
      expect(getTagList).toBeDefined()
      expect(typeof getTagList).toBe('function')
    })
  })

  describe('getTagDetail', () => {
    it('应该导出 getTagDetail 函数', () => {
      expect(getTagDetail).toBeDefined()
      expect(typeof getTagDetail).toBe('function')
    })
  })

  describe('getTagCloud', () => {
    it('应该导出 getTagCloud 函数', () => {
      expect(getTagCloud).toBeDefined()
      expect(typeof getTagCloud).toBe('function')
    })
  })

  describe('getArticlesByTag', () => {
    it('应该导出 getArticlesByTag 函数', () => {
      expect(getArticlesByTag).toBeDefined()
      expect(typeof getArticlesByTag).toBe('function')
    })
  })
})