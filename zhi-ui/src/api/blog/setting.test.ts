import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getBlogSettings, getBlogSettingsAnonymous, clearBlogCache } from './setting'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Blog Setting API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getBlogSettings', () => {
    it('应该导出 getBlogSettings 函数', () => {
      expect(getBlogSettings).toBeDefined()
      expect(typeof getBlogSettings).toBe('function')
    })

    it('应该调用 GET /common/blog/setting 并添加时间戳参数', async () => {
      mockRequest.mockResolvedValue({ blog_name: '我的博客' })

      await getBlogSettings()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/blog/setting',
        method: 'get',
        headers: { isToken: false },
        params: { _t: expect.any(Number) }
      })
    })
  })

  describe('getBlogSettingsAnonymous', () => {
    it('应该导出 getBlogSettingsAnonymous 函数', () => {
      expect(getBlogSettingsAnonymous).toBeDefined()
      expect(typeof getBlogSettingsAnonymous).toBe('function')
    })

    it('应该调用 GET /common/blog/setting 并设置 isToken: false', async () => {
      mockRequest.mockResolvedValue({ blog_name: '我的博客' })

      await getBlogSettingsAnonymous()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/blog/setting',
        method: 'get',
        headers: { isToken: false },
        params: { _t: expect.any(Number) }
      })
    })
  })

  describe('clearBlogCache', () => {
    it('应该导出 clearBlogCache 函数', () => {
      expect(clearBlogCache).toBeDefined()
      expect(typeof clearBlogCache).toBe('function')
    })

    it('应该调用 GET /common/blog/setting/clear-blog-cache', async () => {
      mockRequest.mockResolvedValue({ code: 200, msg: '缓存已清除' })

      await clearBlogCache()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/blog/setting/clear-blog-cache',
        method: 'get'
      })
    })
  })
})
