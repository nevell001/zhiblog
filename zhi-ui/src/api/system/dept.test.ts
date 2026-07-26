import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as deptApi from './dept'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Dept API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listDept', () => {
    it('应该导出 listDept 函数', () => {
      expect(deptApi.listDept).toBeDefined()
      expect(typeof deptApi.listDept).toBe('function')
    })

    it('应该调用部门列表接口', () => {
      deptApi.listDept({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('listDeptExcludeChild', () => {
    it('应该导出 listDeptExcludeChild 函数', () => {
      expect(deptApi.listDeptExcludeChild).toBeDefined()
      expect(typeof deptApi.listDeptExcludeChild).toBe('function')
    })

    it('应该调用部门列表接口（排除节点）', () => {
      deptApi.listDeptExcludeChild(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept/list/exclude/123',
        method: 'get'
      })
    })
  })

  describe('getDept', () => {
    it('应该导出 getDept 函数', () => {
      expect(deptApi.getDept).toBeDefined()
      expect(typeof deptApi.getDept).toBe('function')
    })

    it('应该调用部门详情接口', () => {
      deptApi.getDept(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept/123',
        method: 'get'
      })
    })
  })

  describe('addDept', () => {
    it('应该导出 addDept 函数', () => {
      expect(deptApi.addDept).toBeDefined()
      expect(typeof deptApi.addDept).toBe('function')
    })

    it('应该调用新增部门接口', () => {
      const deptData = { deptName: '测试部门', parentId: 0 }
      deptApi.addDept(deptData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept',
        method: 'post',
        data: deptData
      })
    })
  })

  describe('updateDept', () => {
    it('应该导出 updateDept 函数', () => {
      expect(deptApi.updateDept).toBeDefined()
      expect(typeof deptApi.updateDept).toBe('function')
    })

    it('应该调用修改部门接口', () => {
      const deptData = { deptId: 123, deptName: '更新的部门' }
      deptApi.updateDept(deptData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept',
        method: 'put',
        data: deptData
      })
    })
  })

  describe('delDept', () => {
    it('应该导出 delDept 函数', () => {
      expect(deptApi.delDept).toBeDefined()
      expect(typeof deptApi.delDept).toBe('function')
    })

    it('应该调用删除部门接口', () => {
      deptApi.delDept(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dept/123',
        method: 'delete'
      })
    })
  })
})
