import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as dictDataApi from './data'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Dict Data API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listData', () => {
    it('应该导出 listData 函数', () => {
      expect(dictDataApi.listData).toBeDefined()
      expect(typeof dictDataApi.listData).toBe('function')
    })

    it('应该调用字典数据列表接口', () => {
      dictDataApi.listData({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getData', () => {
    it('应该导出 getData 函数', () => {
      expect(dictDataApi.getData).toBeDefined()
      expect(typeof dictDataApi.getData).toBe('function')
    })

    it('应该调用字典数据详情接口', () => {
      dictDataApi.getData(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data/123',
        method: 'get'
      })
    })
  })

  describe('getDicts', () => {
    it('应该导出 getDicts 函数', () => {
      expect(dictDataApi.getDicts).toBeDefined()
      expect(typeof dictDataApi.getDicts).toBe('function')
    })

    it('应该调用根据字典类型查询数据接口', () => {
      dictDataApi.getDicts('system')
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data/type/system',
        method: 'get'
      })
    })
  })

  describe('addData', () => {
    it('应该导出 addData 函数', () => {
      expect(dictDataApi.addData).toBeDefined()
      expect(typeof dictDataApi.addData).toBe('function')
    })

    it('应该调用新增字典数据接口', () => {
      const dictData = { dictCode: 'test', dictLabel: '测试标签', dictValue: '1' }
      dictDataApi.addData(dictData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data',
        method: 'post',
        data: dictData
      })
    })
  })

  describe('updateData', () => {
    it('应该导出 updateData 函数', () => {
      expect(dictDataApi.updateData).toBeDefined()
      expect(typeof dictDataApi.updateData).toBe('function')
    })

    it('应该调用修改字典数据接口', () => {
      const dictData = { dictCode: 123, dictLabel: '更新的标签', dictValue: '2' }
      dictDataApi.updateData(dictData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data',
        method: 'put',
        data: dictData
      })
    })
  })

  describe('delData', () => {
    it('应该导出 delData 函数', () => {
      expect(dictDataApi.delData).toBeDefined()
      expect(typeof dictDataApi.delData).toBe('function')
    })

    it('应该调用删除字典数据接口', () => {
      dictDataApi.delData(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/dict/data/123',
        method: 'delete'
      })
    })
  })
})
