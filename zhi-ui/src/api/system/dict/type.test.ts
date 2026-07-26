import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as dictTypeApi from './type'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Dict Type API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listType', () => {
    it('应该导出 listType 函数', () => {
      expect(dictTypeApi.listType).toBeDefined()
      expect(typeof dictTypeApi.listType).toBe('function')
    })

    it('应该调用字典类型列表接口', () => {
      dictTypeApi.listType({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getType', () => {
    it('应该导出 getType 函数', () => {
      expect(dictTypeApi.getType).toBeDefined()
      expect(typeof dictTypeApi.getType).toBe('function')
    })

    it('应该调用字典类型详情接口', () => {
      dictTypeApi.getType(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type/123',
        method: 'get'
      })
    })
  })

  describe('addType', () => {
    it('应该导出 addType 函数', () => {
      expect(dictTypeApi.addType).toBeDefined()
      expect(typeof dictTypeApi.addType).toBe('function')
    })

    it('应该调用新增字典类型接口', () => {
      const dictTypeData = { dictName: '测试字典', dictType: 'system' }
      dictTypeApi.addType(dictTypeData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type',
        method: 'post',
        data: dictTypeData
      })
    })
  })

  describe('updateType', () => {
    it('应该导出 updateType 函数', () => {
      expect(dictTypeApi.updateType).toBeDefined()
      expect(typeof dictTypeApi.updateType).toBe('function')
    })

    it('应该调用修改字典类型接口', () => {
      const dictTypeData = { dictId: 123, dictName: '更新的字典' }
      dictTypeApi.updateType(dictTypeData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type',
        method: 'put',
        data: dictTypeData
      })
    })
  })

  describe('delType', () => {
    it('应该导出 delType 函数', () => {
      expect(dictTypeApi.delType).toBeDefined()
      expect(typeof dictTypeApi.delType).toBe('function')
    })

    it('应该调用删除字典类型接口', () => {
      dictTypeApi.delType(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type/123',
        method: 'delete'
      })
    })
  })

  describe('refreshCache', () => {
    it('应该导出 refreshCache 函数', () => {
      expect(dictTypeApi.refreshCache).toBeDefined()
      expect(typeof dictTypeApi.refreshCache).toBe('function')
    })

    it('应该调用刷新字典缓存接口', () => {
      dictTypeApi.refreshCache()
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type/refreshCache',
        method: 'delete'
      })
    })
  })

  describe('optionselect', () => {
    it('应该导出 optionselect 函数', () => {
      expect(dictTypeApi.optionselect).toBeDefined()
      expect(typeof dictTypeApi.optionselect).toBe('function')
    })

    it('应该调用字典选择框列表接口', () => {
      dictTypeApi.optionselect()
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/type/optionselect',
        method: 'get'
      })
    })
  })
})
