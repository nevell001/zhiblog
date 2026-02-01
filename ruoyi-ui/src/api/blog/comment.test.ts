import { describe, it, expect, vi } from 'vitest'
import { listComment, getComment, delComment, addComment, updateComment } from './comment'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Comment API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listComment', () => {
    it('应该导出 listComment 函数', () => {
      expect(listComment).toBeDefined()
      expect(typeof listComment).toBe('function')
    })
  })

  describe('getComment', () => {
    it('应该导出 getComment 函数', () => {
      expect(getComment).toBeDefined()
      expect(typeof getComment).toBe('function')
    })
  })

  describe('delComment', () => {
    it('应该导出 delComment 函数', () => {
      expect(delComment).toBeDefined()
      expect(typeof delComment).toBe('function')
    })
  })

  describe('addComment', () => {
    it('应该导出 addComment 函数', () => {
      expect(addComment).toBeDefined()
      expect(typeof addComment).toBe('function')
    })
  })

  describe('updateComment', () => {
    it('应该导出 updateComment 函数', () => {
      expect(updateComment).toBeDefined()
      expect(typeof updateComment).toBe('function')
    })
  })
})