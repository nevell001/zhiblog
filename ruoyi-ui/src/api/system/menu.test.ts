import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as menuApi from './menu'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('System Menu API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listMenu', () => {
    it('应该导出 listMenu 函数', () => {
      expect(menuApi.listMenu).toBeDefined()
      expect(typeof menuApi.listMenu).toBe('function')
    })

    it('应该调用菜单列表接口', () => {
      menuApi.listMenu()
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu/list',
        method: 'get',
        params: undefined
      })
    })
  })

  describe('getMenu', () => {
    it('应该导出 getMenu 函数', () => {
      expect(menuApi.getMenu).toBeDefined()
      expect(typeof menuApi.getMenu).toBe('function')
    })

    it('应该调用菜单详情接口', () => {
      menuApi.getMenu(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu/123',
        method: 'get'
      })
    })
  })

  describe('treeselect', () => {
    it('应该导出 treeselect 函数', () => {
      expect(menuApi.treeselect).toBeDefined()
      expect(typeof menuApi.treeselect).toBe('function')
    })

    it('应该调用菜单下拉树结构接口', () => {
      menuApi.treeselect()
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu/treeselect',
        method: 'get'
      })
    })
  })

  describe('roleMenuTreeselect', () => {
    it('应该导出 roleMenuTreeselect 函数', () => {
      expect(menuApi.roleMenuTreeselect).toBeDefined()
      expect(typeof menuApi.roleMenuTreeselect).toBe('function')
    })

    it('应该调用角色菜单下拉树结构接口', () => {
      menuApi.roleMenuTreeselect(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu/roleMenuTreeselect/123',
        method: 'get'
      })
    })
  })

  describe('addMenu', () => {
    it('应该导出 addMenu 函数', () => {
      expect(menuApi.addMenu).toBeDefined()
      expect(typeof menuApi.addMenu).toBe('function')
    })

    it('应该调用新增菜单接口', () => {
      const menuData = { menuName: '测试菜单', path: '/test' }
      menuApi.addMenu(menuData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu',
        method: 'post',
        data: menuData
      })
    })
  })

  describe('updateMenu', () => {
    it('应该导出 updateMenu 函数', () => {
      expect(menuApi.updateMenu).toBeDefined()
      expect(typeof menuApi.updateMenu).toBe('function')
    })

    it('应该调用修改菜单接口', () => {
      const menuData = { menuId: 123, menuName: '更新后的菜单' }
      menuApi.updateMenu(menuData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu',
        method: 'put',
        data: menuData
      })
    })
  })

  describe('delMenu', () => {
    it('应该导出 delMenu 函数', () => {
      expect(menuApi.delMenu).toBeDefined()
      expect(typeof menuApi.delMenu).toBe('function')
    })

    it('应该调用删除菜单接口', () => {
      menuApi.delMenu(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/menu/123',
        method: 'delete'
      })
    })
  })
})
