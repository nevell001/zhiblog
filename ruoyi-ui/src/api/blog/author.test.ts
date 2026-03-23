import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getAuthorInfo } from './author'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Author API 测试', () => {
  const request = vi.fn()
  const mockRequest = vi.mocked(request)

  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getAuthorInfo', () => {
    it('应该导出 getAuthorInfo 函数', () => {
      expect(getAuthorInfo).toBeDefined()
      expect(typeof getAuthorInfo).toBe('function')
    })

    it('应该调用 GET 获取作者信息', async () => {
      mockRequest.mockResolvedValue({ code: 200, author: { id: 1, name: '作者', bio: '简介' } })

      await getAuthorInfo()

      expect(mockRequest).toHaveBeenCalled()
    })
  })
})
