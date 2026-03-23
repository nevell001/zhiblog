import { describe, it, expect, vi, beforeEach } from 'vitest'
import { checkPermi, checkRole } from './permission'

// Mock user store
vi.mock('@/stores/user', () => ({
  useUserStore: vi.fn()
}))

describe('permission 工具测试', () => {
  const mockUseUserStore = vi.mocked(useUserStore)

  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('checkPermi', () => {
    it('应该导出 checkPermi 函数', () => {
      expect(checkPermi).toBeDefined()
      expect(typeof checkPermi).toBe('function')
    })

    it('应该返回 true 当用户有所需权限', () => {
      mockUseUserStore.mockReturnValue({
        permissions: ['system:user:add', 'system:user:edit'],
        roles: ['admin']
      })

      expect(checkPermi(['system:user:add'])).toBe(true)
      expect(checkPermi(['system:user:edit'])).toBe(true)
    })

    it('应该返回 false 当用户没有所需权限', () => {
      mockUseUserStore.mockReturnValue({
        permissions: ['system:user:list'],
        roles: ['editor']
      })

      expect(checkPermi(['system:user:add'])).toBe(false)
      expect(checkPermi(['system:user:edit'])).toBe(false)
    })
  })

  describe('checkRole', () => {
    it('应该导出 checkRole 函数', () => {
      expect(checkRole).toBeDefined()
      expect(typeof checkRole).toBe('function')
    })

    it('应该返回 true 当用户是 admin 角色', () => {
      mockUseUserStore.mockReturnValue({
        permissions: [],
        roles: ['admin']
      })

      expect(checkRole(['admin'])).toBe(true)
    })

    it('应该返回 true 当用户拥有所需角色', () => {
      mockUseUserStore.mockReturnValue({
        permissions: [],
        roles: ['editor', 'admin']
      })

      expect(checkRole(['editor'])).toBe(true)
      expect(checkRole(['admin'])).toBe(true)
    })

    it('应该返回 false 当用户没有所需角色', () => {
      mockUseUserStore.mockReturnValue({
        permissions: [],
        roles: ['guest']
      })

      expect(checkRole(['admin'])).toBe(false)
      expect(checkRole(['editor'])).toBe(false)
      expect(checkRole(['guest'])).toBe(false)
    })
  })
})
