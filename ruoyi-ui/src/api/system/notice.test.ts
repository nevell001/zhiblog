import { describe, it, expect, vi } from 'vitest'
import { listNotice, getNotice, delNotice, addNotice, updateNotice } from './notice'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Notice API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listNotice', () => {
    it('应该导出 listNotice 函数', () => {
      expect(listNotice).toBeDefined()
      expect(typeof listNotice).toBe('function')
    })
  })

  describe('getNotice', () => {
    it('应该导出 getNotice 函数', () => {
      expect(getNotice).toBeDefined()
      expect(typeof getNotice).toBe('function')
    })
  })

  describe('delNotice', () => {
    it('应该导出 delNotice 函数', () => {
      expect(delNotice).toBeDefined()
      expect(typeof delNotice).toBe('function')
    })
  })

  describe('addNotice', () => {
    it('应该导出 addNotice 函数', () => {
      expect(addNotice).toBeDefined()
      expect(typeof addNotice).toBe('function')
    })
  })

  describe('updateNotice', () => {
    it('应该导出 updateNotice 函数', () => {
      expect(updateNotice).toBeDefined()
      expect(typeof updateNotice).toBe('function')
    })
  })
})
