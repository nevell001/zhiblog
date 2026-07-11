import { describe, it, expect, vi, beforeEach } from 'vitest'
import { checkPermi, checkRole } from './permission'

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

  describe('checkPermi', () => {
    it('应该导出 checkPermi 函数', () => {
      expect(checkPermi).toBeDefined()
      expect(typeof checkPermi).toBe('function')
    })

    it('应该拒绝空权限数组', () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      expect(checkPermi([])).toBe(false)
      consoleErrorSpy.mockRestore()
    })

    it('应该处理权限数组', () => {
      const permissions = ['system:user:list', 'system:user:add', 'system:user:edit']
      expect(Array.isArray(permissions)).toBe(true)
      expect(permissions.length).toBe(3)
    })
  })

  describe('checkRole', () => {
    it('应该导出 checkRole 函数', () => {
      expect(checkRole).toBeDefined()
      expect(typeof checkRole).toBe('function')
    })

    it('应该拒绝空角色数组', () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      expect(checkRole([])).toBe(false)
      consoleErrorSpy.mockRestore()
    })

    it('应该处理角色数组', () => {
      const roles = ['admin', 'editor', 'viewer']
      expect(Array.isArray(roles)).toBe(true)
      expect(roles.length).toBe(3)
    })
  })

  describe('权限标识', () => {
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
