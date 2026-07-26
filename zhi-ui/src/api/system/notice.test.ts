import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as noticeApi from './notice'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Notice API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listNotice', () => {
    it('应该导出 listNotice 函数', () => {
      expect(noticeApi.listNotice).toBeDefined()
      expect(typeof noticeApi.listNotice).toBe('function')
    })

    it('应该调用公告列表接口', () => {
      noticeApi.listNotice({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/system/notice/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getNotice', () => {
    it('应该导出 getNotice 函数', () => {
      expect(noticeApi.getNotice).toBeDefined()
      expect(typeof noticeApi.getNotice).toBe('function')
    })

    it('应该调用公告详情接口', () => {
      noticeApi.getNotice(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/notice/123',
        method: 'get'
      })
    })
  })

  describe('addNotice', () => {
    it('应该导出 addNotice 函数', () => {
      expect(noticeApi.addNotice).toBeDefined()
      expect(typeof noticeApi.addNotice).toBe('function')
    })

    it('应该调用新增公告接口', () => {
      const noticeData = { noticeTitle: '测试公告', noticeContent: '公告内容', noticeType: '1' }
      noticeApi.addNotice(noticeData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/notice',
        method: 'post',
        data: noticeData
      })
    })
  })

  describe('updateNotice', () => {
    it('应该导出 updateNotice 函数', () => {
      expect(noticeApi.updateNotice).toBeDefined()
      expect(typeof noticeApi.updateNotice).toBe('function')
    })

    it('应该调用修改公告接口', () => {
      const noticeData = { noticeId: 123, noticeTitle: '更新的公告' }
      noticeApi.updateNotice(noticeData)
      expect(request).toHaveBeenCalledWith({
        url: '/system/notice',
        method: 'put',
        data: noticeData
      })
    })
  })

  describe('delNotice', () => {
    it('应该导出 delNotice 函数', () => {
      expect(noticeApi.delNotice).toBeDefined()
      expect(typeof noticeApi.delNotice).toBe('function')
    })

    it('应该调用删除公告接口', () => {
      noticeApi.delNotice(123)
      expect(request).toHaveBeenCalledWith({
        url: '/system/notice/123',
        method: 'delete'
      })
    })
  })
})
