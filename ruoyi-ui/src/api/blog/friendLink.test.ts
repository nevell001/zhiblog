import { describe, it, expect, vi } from 'vitest'
import { listFriendLink, getFriendLink, delFriendLink, addFriendLink, updateFriendLink } from './friendLink'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('FriendLink API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listFriendLink', () => {
    it('应该导出 listFriendLink 函数', () => {
      expect(listFriendLink).toBeDefined()
      expect(typeof listFriendLink).toBe('function')
    })
  })

  describe('getFriendLink', () => {
    it('应该导出 getFriendLink 函数', () => {
      expect(getFriendLink).toBeDefined()
      expect(typeof getFriendLink).toBe('function')
    })
  })

  describe('delFriendLink', () => {
    it('应该导出 delFriendLink 函数', () => {
      expect(delFriendLink).toBeDefined()
      expect(typeof delFriendLink).toBe('function')
    })
  })

  describe('addFriendLink', () => {
    it('应该导出 addFriendLink 函数', () => {
      expect(addFriendLink).toBeDefined()
      expect(typeof addFriendLink).toBe('function')
    })
  })

  describe('updateFriendLink', () => {
    it('应该导出 updateFriendLink 函数', () => {
      expect(updateFriendLink).toBeDefined()
      expect(typeof updateFriendLink).toBe('function')
    })
  })
})