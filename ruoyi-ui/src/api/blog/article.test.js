import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import * as articleApi from '@/api/blog/article'

// Mock request 模块
vi.mock('@/utils/request')

// 获取模拟的 request 函数
import request from '@/utils/request'
const mockRequest = vi.mocked(request)

describe('文章 API 测试', () => {
  beforeEach(() => {
    mockRequest.mockClear()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('listArticle', () => {
    it('应该调用正确的端点获取文章列表', () => {
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 100, rows: [] } })

      articleApi.listArticle(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article/list',
        method: 'get',
        params: query
      })
    })

    it('应该返回文章列表数据', async () => {
      const mockData = { total: 50, rows: [{ id: 1, title: 'Test Article' }] }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.listArticle({ pageNum: 1, pageSize: 10 })

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('getArticleListAnonymous', () => {
    it('应该调用前台匿名访问端点', () => {
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 100, rows: [] } })

      articleApi.getArticleListAnonymous(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/list',
        method: 'get',
        params: query,
        headers: { isToken: false }
      })
    })
  })

  describe('getArticle', () => {
    it('应该调用正确的端点获取文章详情', () => {
      mockRequest.mockResolvedValue({ data: { id: 1, title: 'Test Article' } })

      articleApi.getArticle(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article/1',
        method: 'get'
      })
    })
  })

  describe('addArticle', () => {
    it('应该调用正确的端点新增文章', () => {
      const articleData = { title: 'New Article', content: 'Content' }
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.addArticle(articleData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article',
        method: 'post',
        data: articleData
      })
    })
  })

  describe('updateArticle', () => {
    it('应该调用正确的端点更新文章', () => {
      const articleData = { id: 1, title: 'Updated Article' }
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.updateArticle(articleData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article',
        method: 'put',
        data: articleData
      })
    })
  })

  describe('delArticle', () => {
    it('应该调用正确的端点删除文章', () => {
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.delArticle(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article/1',
        method: 'delete'
      })
    })
  })

  describe('addViewCount', () => {
    it('应该调用正确的端点增加浏览量', () => {
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.addViewCount(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/article/view/1',
        method: 'get'
      })
    })
  })

  describe('getHotArticles', () => {
    it('应该调用正确的端点获取热门文章', () => {
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 10, rows: [] } })

      articleApi.getHotArticles(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/hot',
        method: 'get',
        params: { ...query, pageSize: 10 },
        headers: { isToken: false }
      })
    })

    it('应该使用默认的 pageSize', () => {
      mockRequest.mockResolvedValue({ data: { total: 5, rows: [] } })

      articleApi.getHotArticles({ pageNum: 1 })

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/hot',
        method: 'get',
        params: { pageNum: 1, pageSize: 5 },
        headers: { isToken: false }
      })
    })
  })

  describe('getArticleDetail', () => {
    it('应该调用前台端点获取文章详情', () => {
      mockRequest.mockResolvedValue({ data: { id: 1, title: 'Test Article', content: 'Content' } })

      articleApi.getArticleDetail(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/1',
        method: 'get',
        headers: { isToken: false }
      })
    })
  })

  describe('searchArticles', () => {
    it('应该调用搜索端点并传递关键词', () => {
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 5, rows: [] } })

      articleApi.searchArticles('test keyword', query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/search',
        method: 'get',
        params: { ...query, keyword: 'test keyword' },
        headers: { isToken: false }
      })
    })
  })

  describe('getArticleComments', () => {
    it('应该调用正确的端点获取文章评论', () => {
      mockRequest.mockResolvedValue({ data: [] })

      articleApi.getArticleComments(1)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/comment/article/1',
        method: 'get',
        headers: { isToken: false }
      })
    })
  })

  describe('submitComment', () => {
    it('应该调用正确的端点提交评论', () => {
      const commentData = { articleId: 1, content: 'Test Comment' }
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.submitComment(commentData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/comment',
        method: 'post',
        data: commentData
      })
    })
  })
})