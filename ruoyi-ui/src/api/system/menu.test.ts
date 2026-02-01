import { describe, it, expect, vi } from 'vitest'
import { listMenu, getMenu, delMenu, addMenu, updateMenu, treeselect, roleMenuTreeselect } from './menu'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Menu API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listMenu', () => {
    it('应该导出 listMenu 函数', () => {
      expect(listMenu).toBeDefined()
      expect(typeof listMenu).toBe('function')
    })
  })

  describe('getMenu', () => {
    it('应该导出 getMenu 函数', () => {
      expect(getMenu).toBeDefined()
      expect(typeof getMenu).toBe('function')
    })
  })

  describe('delMenu', () => {
    it('应该导出 delMenu 函数', () => {
      expect(delMenu).toBeDefined()
      expect(typeof delMenu).toBe('function')
    })
  })

  describe('addMenu', () => {
    it('应该导出 addMenu 函数', () => {
      expect(addMenu).toBeDefined()
      expect(typeof addMenu).toBe('function')
    })
  })

  describe('updateMenu', () => {
    it('应该导出 updateMenu 函数', () => {
      expect(updateMenu).toBeDefined()
      expect(typeof updateMenu).toBe('function')
    })
  })

  describe('treeselect', () => {
    it('应该导出 treeselect 函数', () => {
      expect(treeselect).toBeDefined()
      expect(typeof treeselect).toBe('function')
    })
  })

  describe('roleMenuTreeselect', () => {
    it('应该导出 roleMenuTreeselect 函数', () => {
      expect(roleMenuTreeselect).toBeDefined()
      expect(typeof roleMenuTreeselect).toBe('function')
    })
  })
})