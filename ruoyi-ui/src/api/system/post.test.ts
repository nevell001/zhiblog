import { describe, it, expect, vi } from 'vitest'
import { listPost, getPost, delPost, addPost, updatePost } from './post'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Post API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listPost', () => {
    it('应该导出 listPost 函数', () => {
      expect(listPost).toBeDefined()
      expect(typeof listPost).toBe('function')
    })
  })

  describe('getPost', () => {
    it('应该导出 getPost 函数', () => {
      expect(getPost).toBeDefined()
      expect(typeof getPost).toBe('function')
    })
  })

  describe('delPost', () => {
    it('应该导出 delPost 函数', () => {
      expect(delPost).toBeDefined()
      expect(typeof delPost).toBe('function')
    })
  })

  describe('addPost', () => {
    it('应该导出 addPost 函数', () => {
      expect(addPost).toBeDefined()
      expect(typeof addPost).toBe('function')
    })
  })

  describe('updatePost', () => {
    it('应该导出 updatePost 函数', () => {
      expect(updatePost).toBeDefined()
      expect(typeof updatePost).toBe('function')
    })
  })
})
