import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getCategoryList, getCategoryDetail } from './category'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Category API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getCategoryList', () => {
    it('应该导出 getCategoryList 函数', () => {
      expect(getCategoryList).toBeDefined()
      expect(typeof getCategoryList).toBe('function')
    })

    it('应该调用 GET /blog/category/list 并设置 isToken: false', async () => {
      mockRequest.mockResolvedValue({ rows: [], total: 0 })

      const query = { pageNum: 1, pageSize: 10 }
      await getCategoryList(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/api/category/list',
        method: 'get',
        params: query,
        headers: { isToken: false }
      })
    })

    it('应该在没有参数时仍然调用 API', async () => {
      mockRequest.mockResolvedValue({ rows: [], total: 0 })

      await getCategoryList()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/api/category/list',
        method: 'get',
        params: undefined,
        headers: { isToken: false }
      })
    })
  })

  describe('getCategoryDetail', () => {
    it('应该导出 getCategoryDetail 函数', () => {
      expect(getCategoryDetail).toBeDefined()
      expect(typeof getCategoryDetail).toBe('function')
    })

    it('应该调用 GET /blog/category/:id 并设置 isToken: false', async () => {
      mockRequest.mockResolvedValue({ id: 1, categoryName: '测试' })

      await getCategoryDetail(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/api/category/1',
        method: 'get',
        headers: { isToken: false }
      })
    })

    it('应该处理不同的 ID', async () => {
      mockRequest.mockResolvedValue({ id: 123, categoryName: '测试' })

      await getCategoryDetail(123)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/api/category/123',
        method: 'get',
        headers: { isToken: false }
      })
    })
  })
})
