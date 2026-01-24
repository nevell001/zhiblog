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

    it('应该返回文章列表数据', async () => {
      const mockData = { total: 50, rows: [{ id: 1, title: 'Test Article' }] }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.getArticleListAnonymous({ pageNum: 1, pageSize: 10 })

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('getArticleList', () => {
    it('应该调用前台文章列表端点', () => {
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 100, rows: [] } })

      articleApi.getArticleList(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/list',
        method: 'get',
        params: query,
        headers: { isToken: false }
      })
    })

    it('应该返回文章列表数据', async () => {
      const mockData = { total: 50, rows: [{ id: 1, title: 'Test Article' }] }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.getArticleList({ pageNum: 1, pageSize: 10 })

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('getArticlesByCategory', () => {
    it('应该调用分类文章列表端点', () => {
      const categoryId = 1
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 20, rows: [] } })

      articleApi.getArticlesByCategory(categoryId, query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/category/1',
        method: 'get',
        params: query,
        headers: { isToken: false }
      })
    })

    it('应该返回分类文章数据', async () => {
      const mockData = { total: 20, rows: [{ id: 1, title: 'Category Article' }] }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.getArticlesByCategory(1, { pageNum: 1, pageSize: 10 })

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('getHotArticles', () => {
    it('应该调用热门文章端点', () => {
      const query = { pageNum: 1, pageSize: 5 }
      mockRequest.mockResolvedValue({ data: { total: 5, rows: [] } })

      articleApi.getHotArticles(query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/hot',
        method: 'get',
        params: { ...query, pageSize: 5 },
        headers: { isToken: false }
      })
    })
  })

  describe('getArticleDetail', () => {
    it('应该调用文章详情端点', () => {
      const articleId = 1
      mockRequest.mockResolvedValue({ data: { id: 1, title: 'Test Article' } })

      articleApi.getArticleDetail(articleId)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/1',
        method: 'get',
        headers: { isToken: false }
      })
    })

    it('应该返回文章详情数据', async () => {
      const mockData = { id: 1, title: 'Test Article', content: 'Test Content' }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.getArticleDetail(1)

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('updateArticleViewCount', () => {
    it('应该调用浏览量更新端点', () => {
      const articleId = 1
      mockRequest.mockResolvedValue({ code: 200 })

      articleApi.updateArticleViewCount(articleId)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/view/1',
        method: 'get',
        headers: { isToken: false }
      })
    })
  })

  describe('searchArticles', () => {
    it('应该调用搜索文章端点', () => {
      const keyword = 'test'
      const query = { pageNum: 1, pageSize: 10 }
      mockRequest.mockResolvedValue({ data: { total: 10, rows: [] } })

      articleApi.searchArticles(keyword, query)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/search',
        method: 'get',
        params: { ...query, keyword },
        headers: { isToken: false }
      })
    })

    it('应该返回搜索结果', async () => {
      const mockData = { total: 10, rows: [{ id: 1, title: 'Test Article' }] }
      mockRequest.mockResolvedValue({ data: mockData })

      const result = await articleApi.searchArticles('test', { pageNum: 1, pageSize: 10 })

      expect(result).toEqual({ data: mockData })
    })
  })

  describe('getRelatedArticles', () => {
    it('应该调用相关文章端点', () => {
      const articleId = 1
      mockRequest.mockResolvedValue({ data: [{ id: 2, title: 'Related Article' }] })

      articleApi.getRelatedArticles(articleId)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/blog/article/related/1',
        method: 'get',
        headers: { isToken: false }
      })
    })
  })

  describe('submitComment', () => {
    it('应该调用评论提交端点', () => {
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
