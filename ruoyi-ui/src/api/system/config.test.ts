import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as configApi from './config'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('System Config API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listConfig', () => {
    it('应该导出 listConfig 函数', () => {
      expect(configApi.listConfig).toBeDefined()
      expect(typeof configApi.listConfig).toBe('function')
    })

    it('应该调用参数列表接口', () => {
      configApi.listConfig({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/config/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getConfig', () => {
    it('应该导出 getConfig 函数', () => {
      expect(configApi.getConfig).toBeDefined()
      expect(typeof configApi.getConfig).toBe('function')
    })

    it('应该调用参数详情接口', () => {
      configApi.getConfig(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/config/123',
        method: 'get'
      })
    })
  })

  describe('getConfigKey', () => {
    it('应该导出 getConfigKey 函数', () => {
      expect(configApi.getConfigKey).toBeDefined()
      expect(typeof configApi.getConfigKey).toBe('function')
    })

    it('应该根据键名查询参数值', () => {
      configApi.getConfigKey('test.key')
      expect(request).toHaveBeenCalledWith({
        url: '/system/config/configKey/test.key',
        method: 'get'
      })
    })
  })

  describe('addConfig', () => {
    it('应该导出 addConfig 函数', () => {
      expect(configApi.addConfig).toBeDefined()
      expect(typeof configApi.addConfig).toBe('function')
    })

    it('应该调用新增参数配置接口', () => {
      const configData = { configName: '测试配置', configKey: 'test.key', configValue: 'value' }
      configApi.addConfig(configData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/config',
        method: 'post',
        data: configData
      })
    })
  })

  describe('updateConfig', () => {
    it('应该导出 updateConfig 函数', () => {
      expect(configApi.updateConfig).toBeDefined()
      expect(typeof configApi.updateConfig).toBe('function')
    })

    it('应该调用修改参数配置接口', () => {
      const configData = { configId: 123, configValue: 'updated value' }
      configApi.updateConfig(configData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/config',
        method: 'put',
        data: configData
      })
    })
  })

  describe('delConfig', () => {
    it('应该导出 delConfig 函数', () => {
      expect(configApi.delConfig).toBeDefined()
      expect(typeof configApi.delConfig).toBe('function')
    })

    it('应该调用删除参数配置接口', () => {
      configApi.delConfig(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/config/123',
        method: 'delete'
      })
    })
  })

  describe('refreshCache', () => {
    it('应该导出 refreshCache 函数', () => {
      expect(configApi.refreshCache).toBeDefined()
      expect(typeof configApi.refreshCache).toBe('function')
    })

    it('应该调用刷新参数缓存接口', () => {
      configApi.refreshCache()
      expect(request).toHaveBeenCalledWith({
        url: '/system/config/refreshCache',
        method: 'delete'
      })
    })
  })
})
