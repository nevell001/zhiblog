import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  listSetting,
  getSetting,
  updateSettingValueByKey,
  addSetting,
  updateSetting,
  delSetting
} from './index'
import request from '@/utils/request'

vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Blog Setting API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('listSetting 应调用 GET /system/setting/list', () => {
    listSetting({ key: 'blog_name' })
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting/list', method: 'get' })
    )
  })

  it('getSetting 应调用 GET /system/setting/value/:key', () => {
    getSetting('blog_name')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting/value/blog_name', method: 'get' })
    )
  })

  it('updateSettingValueByKey 应调用 PUT /system/setting/updateByKey', () => {
    updateSettingValueByKey('blog_name', '知博')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting/updateByKey', method: 'put' })
    )
  })

  it('addSetting 应调用 POST /system/setting', () => {
    addSetting({ key: 'k', value: 'v' } as any)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting', method: 'post' })
    )
  })

  it('updateSetting 应调用 PUT /system/setting', () => {
    updateSetting({ id: 1 } as any)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting', method: 'put' })
    )
  })

  it('delSetting 应调用 DELETE /system/setting/:ids', () => {
    delSetting(1)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/system/setting/1', method: 'delete' })
    )
  })
})
