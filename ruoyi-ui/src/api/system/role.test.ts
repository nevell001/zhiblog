import { describe, it, expect, vi } from 'vitest'
import { listRole, getRole, delRole, addRole, updateRole, dataScope, changeRoleStatus } from './role'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Role API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listRole', () => {
    it('应该导出 listRole 函数', () => {
      expect(listRole).toBeDefined()
      expect(typeof listRole).toBe('function')
    })
  })

  describe('getRole', () => {
    it('应该导出 getRole 函数', () => {
      expect(getRole).toBeDefined()
      expect(typeof getRole).toBe('function')
    })
  })

  describe('delRole', () => {
    it('应该导出 delRole 函数', () => {
      expect(delRole).toBeDefined()
      expect(typeof delRole).toBe('function')
    })
  })

  describe('addRole', () => {
    it('应该导出 addRole 函数', () => {
      expect(addRole).toBeDefined()
      expect(typeof addRole).toBe('function')
    })
  })

  describe('updateRole', () => {
    it('应该导出 updateRole 函数', () => {
      expect(updateRole).toBeDefined()
      expect(typeof updateRole).toBe('function')
    })
  })

  describe('dataScope', () => {
    it('应该导出 dataScope 函数', () => {
      expect(dataScope).toBeDefined()
      expect(typeof dataScope).toBe('function')
    })
  })

  describe('changeRoleStatus', () => {
    it('应该导出 changeRoleStatus 函数', () => {
      expect(changeRoleStatus).toBeDefined()
      expect(typeof changeRoleStatus).toBe('function')
    })
  })
})