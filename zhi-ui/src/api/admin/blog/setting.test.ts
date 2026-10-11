import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as settingApi from './setting'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Blog Setting API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listSetting', () => {
    it('应该导出 listSetting 函数', () => {
      expect(settingApi.listSetting).toBeDefined()
      expect(typeof settingApi.listSetting).toBe('function')
    })

    it('应该调用博客设置列表接口', () => {
      settingApi.listSetting({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/setting/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getConfigByKey', () => {
    it('应该导出 getConfigByKey 函数', () => {
      expect(settingApi.getConfigByKey).toBeDefined()
      expect(typeof settingApi.getConfigByKey).toBe('function')
    })

    it('应该调用根据配置键查询值接口', () => {
      settingApi.getConfigByKey('test.key')
      expect(request).toHaveBeenCalledWith({
        url: '/system/setting/value/test.key',
        method: 'get'
      })
    })
  })

  describe('updateSettingValueByKey', () => {
    it('应该导出 updateSettingValueByKey 函数', () => {
      expect(settingApi.updateSettingValueByKey).toBeDefined()
      expect(typeof settingApi.updateSettingValueByKey).toBe('function')
    })

    it('应该调用根据键更新设置值接口', () => {
      settingApi.updateSettingValueByKey('test.key', 'new.value')
      expect(request).toHaveBeenCalledWith({
        url: '/system/setting/updateByKey',
        method: 'post',
        data: { settingKey: 'test.key', settingValue: 'new.value' }
      })
    })
  })

  describe('getRegistrationSwitch', () => {
    it('应该调用注册开关接口', () => {
      settingApi.getRegistrationSwitch()
      expect(request).toHaveBeenCalledWith({
        url: '/system/setting/registration',
        method: 'get'
      })
    })
  })
})
