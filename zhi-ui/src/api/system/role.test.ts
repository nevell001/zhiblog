import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as roleApi from './role'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('System Role API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listRole', () => {
    it('应该导出 listRole 函数', () => {
      expect(roleApi.listRole).toBeDefined()
      expect(typeof roleApi.listRole).toBe('function')
    })

    it('应该调用角色列表接口', () => {
      roleApi.listRole({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getRole', () => {
    it('应该导出 getRole 函数', () => {
      expect(roleApi.getRole).toBeDefined()
      expect(typeof roleApi.getRole).toBe('function')
    })

    it('应该调用角色详情接口', () => {
      roleApi.getRole(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/123',
        method: 'get'
      })
    })
  })

  describe('addRole', () => {
    it('应该导出 addRole 函数', () => {
      expect(roleApi.addRole).toBeDefined()
      expect(typeof roleApi.addRole).toBe('function')
    })

    it('应该调用新增角色接口', () => {
      const roleData = { roleName: 'test', roleKey: 'test' }
      roleApi.addRole(roleData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role',
        method: 'post',
        data: roleData
      })
    })
  })

  describe('updateRole', () => {
    it('应该导出 updateRole 函数', () => {
      expect(roleApi.updateRole).toBeDefined()
      expect(typeof roleApi.updateRole).toBe('function')
    })

    it('应该调用修改角色接口', () => {
      const roleData = { roleId: 123, roleName: 'updated' }
      roleApi.updateRole(roleData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role',
        method: 'put',
        data: roleData
      })
    })
  })

  describe('dataScope', () => {
    it('应该导出 dataScope 函数', () => {
      expect(roleApi.dataScope).toBeDefined()
      expect(typeof roleApi.dataScope).toBe('function')
    })

    it('应该调用角色数据权限接口', () => {
      const roleData = { roleId: 123, dataScope: '1' }
      roleApi.dataScope(roleData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/dataScope',
        method: 'put',
        data: roleData
      })
    })
  })

  describe('changeRoleStatus', () => {
    it('应该导出 changeRoleStatus 函数', () => {
      expect(roleApi.changeRoleStatus).toBeDefined()
      expect(typeof roleApi.changeRoleStatus).toBe('function')
    })

    it('应该调用角色状态修改接口', () => {
      roleApi.changeRoleStatus(123, '1')
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/changeStatus',
        method: 'put',
        data: { roleId: 123, status: '1' }
      })
    })
  })

  describe('delRole', () => {
    it('应该导出 delRole 函数', () => {
      expect(roleApi.delRole).toBeDefined()
      expect(typeof roleApi.delRole).toBe('function')
    })

    it('应该调用删除角色接口', () => {
      roleApi.delRole(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/123',
        method: 'delete'
      })
    })
  })

  describe('allocatedUserList', () => {
    it('应该导出 allocatedUserList 函数', () => {
      expect(roleApi.allocatedUserList).toBeDefined()
      expect(typeof roleApi.allocatedUserList).toBe('function')
    })

    it('应该调用已授权用户列表接口', () => {
      roleApi.allocatedUserList({ roleId: 123 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/authUser/allocatedList',
        method: 'get',
        params: { roleId: 123 }
      })
    })
  })

  describe('unallocatedUserList', () => {
    it('应该导出 unallocatedUserList 函数', () => {
      expect(roleApi.unallocatedUserList).toBeDefined()
      expect(typeof roleApi.unallocatedUserList).toBe('function')
    })

    it('应该调用未授权用户列表接口', () => {
      roleApi.unallocatedUserList({ roleId: 123 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/authUser/unallocatedList',
        method: 'get',
        params: { roleId: 123 }
      })
    })
  })

  describe('authUserCancel', () => {
    it('应该导出 authUserCancel 函数', () => {
      expect(roleApi.authUserCancel).toBeDefined()
      expect(typeof roleApi.authUserCancel).toBe('function')
    })

    it('应该调用取消用户授权接口', () => {
      const data = { roleId: 123, userId: 456 }
      roleApi.authUserCancel(data)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/authUser/cancel',
        method: 'put',
        data: data
      })
    })
  })

  describe('authUserCancelAll', () => {
    it('应该导出 authUserCancelAll 函数', () => {
      expect(roleApi.authUserCancelAll).toBeDefined()
      expect(typeof roleApi.authUserCancelAll).toBe('function')
    })

    it('应该调用批量取消授权接口', () => {
      const data = { roleId: 123, userIds: [1, 2, 3] }
      roleApi.authUserCancelAll(data)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/authUser/cancelAll',
        method: 'put',
        params: data
      })
    })
  })

  describe('authUserSelectAll', () => {
    it('应该导出 authUserSelectAll 函数', () => {
      expect(roleApi.authUserSelectAll).toBeDefined()
      expect(typeof roleApi.authUserSelectAll).toBe('function')
    })

    it('应该调用授权用户选择接口', () => {
      const data = { roleId: 123, userIds: [1, 2, 3] }
      roleApi.authUserSelectAll(data)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/authUser/selectAll',
        method: 'put',
        params: data
      })
    })
  })

  describe('deptTreeSelect', () => {
    it('应该导出 deptTreeSelect 函数', () => {
      expect(roleApi.deptTreeSelect).toBeDefined()
      expect(typeof roleApi.deptTreeSelect).toBe('function')
    })

    it('应该调用查询部门树结构接口', () => {
      roleApi.deptTreeSelect(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/role/deptTree/123',
        method: 'get'
      })
    })
  })
})
