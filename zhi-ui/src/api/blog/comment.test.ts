import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getArticleComments, addBlogComment } from './comment'
import type { Comment } from '@/types'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Comment API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getArticleComments', () => {
    it('应该导出 getArticleComments 函数', () => {
      expect(getArticleComments).toBeDefined()
      expect(typeof getArticleComments).toBe('function')
    })

    it('应该调用 GET /blog/comment/article/:articleId', async () => {
      mockRequest.mockResolvedValue({ rows: [], total: 0 })

      await getArticleComments(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/comment/article/1',
        method: 'get',
        params: undefined
      })
    })

    it('应该支持分页参数', async () => {
      mockRequest.mockResolvedValue({ rows: [], total: 0 })

      const query = { pageNum: 1, pageSize: 20 }
      await getArticleComments(1, query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/comment/article/1',
        method: 'get',
        params: query
      })
    })
  })

  describe('addBlogComment', () => {
    it('应该导出 addBlogComment 函数', () => {
      expect(addBlogComment).toBeDefined()
      expect(typeof addBlogComment).toBe('function')
    })

    it('应该调用 POST /blog/comment', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      const comment: Comment = {
        id: 0,
        content: '新评论',
        articleId: 1,
        createTime: ''
      }
      await addBlogComment(comment)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/comment',
        method: 'post',
        data: comment
      })
    })
  })
})
