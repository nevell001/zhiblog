import { describe, it, expect, vi } from 'vitest'
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from './user'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('User API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listUser', () => {
    it('应该导出 listUser 函数', () => {
      expect(listUser).toBeDefined()
      expect(typeof listUser).toBe('function')
    })
  })

  describe('getUser', () => {
    it('应该导出 getUser 函数', () => {
      expect(getUser).toBeDefined()
      expect(typeof getUser).toBe('function')
    })
  })

  describe('delUser', () => {
    it('应该导出 delUser 函数', () => {
      expect(delUser).toBeDefined()
      expect(typeof delUser).toBe('function')
    })
  })

  describe('addUser', () => {
    it('应该导出 addUser 函数', () => {
      expect(addUser).toBeDefined()
      expect(typeof addUser).toBe('function')
    })
  })

  describe('updateUser', () => {
    it('应该导出 updateUser 函数', () => {
      expect(updateUser).toBeDefined()
      expect(typeof updateUser).toBe('function')
    })
  })

  describe('resetUserPwd', () => {
    it('应该导出 resetUserPwd 函数', () => {
      expect(resetUserPwd).toBeDefined()
      expect(typeof resetUserPwd).toBe('function')
    })
  })

  describe('changeUserStatus', () => {
    it('应该导出 changeUserStatus 函数', () => {
      expect(changeUserStatus).toBeDefined()
      expect(typeof changeUserStatus).toBe('function')
    })
  })

  describe('deptTreeSelect', () => {
    it('应该导出 deptTreeSelect 函数', () => {
      expect(deptTreeSelect).toBeDefined()
      expect(typeof deptTreeSelect).toBe('function')
    })
  })
})