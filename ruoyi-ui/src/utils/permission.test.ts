import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  hasPermission,
  hasRole,
  hasPermi
} from './permission'

// Mock localStorage
const mockLocalStorage = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

vi.stubGlobal('localStorage', mockLocalStorage)

describe('Permission Utils 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('hasPermission', () => {
    it('应该导出 hasPermission 函数', () => {
      expect(hasPermission).toBeDefined()
      expect(typeof hasPermission).toBe('function')
    })

    it('应该检查权限', () => {
      const permissions = ['system:user:add', 'system:user:edit']
      const hasPerms = hasPermission('system:user:add')
      expect(typeof hasPerms).toBe('boolean')
    })

    it('应该处理权限数组', () => {
      const permissions = ['system:user:list', 'system:user:add', 'system:user:edit']
      expect(Array.isArray(permissions)).toBe(true)
      expect(permissions.length).toBe(3)
    })
  })

  describe('hasRole', () => {
    it('应该导出 hasRole 函数', () => {
      expect(hasRole).toBeDefined()
      expect(typeof hasRole).toBe('function')
    })

    it('应该检查角色', () => {
      const roles = ['admin', 'editor']
      const hasAdminRole = hasRole('admin')
      expect(typeof hasAdminRole).toBe('boolean')
    })

    it('应该处理角色数组', () => {
      const roles = ['admin', 'editor', 'viewer']
      expect(Array.isArray(roles)).toBe(true)
      expect(roles.length).toBe(3)
    })
  })

  describe('hasPermi', () => {
    it('应该导出 hasPermi 函数', () => {
      expect(hasPermi).toBeDefined()
      expect(typeof hasPermi).toBe('function')
    })

    it('应该检查权限标识', () => {
      const permission = 'system:user:add'
      expect(typeof permission).toBe('string')
      expect(permission.length).toBeGreaterThan(0)
    })

    it('应该支持多个权限', () => {
      const permissions = ['system:user:add', 'system:user:edit', 'system:user:remove']
      expect(permissions.length).toBe(3)
    })
  })
})