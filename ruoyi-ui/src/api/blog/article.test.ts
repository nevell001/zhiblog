import { describe, it, expect, vi } from 'vitest'
import {
  getArticleListAnonymous,
  getArticleList,
  getArticlesByCategory,
  getHotArticles,
  getTopArticles,
  getRecommendArticles,
  getArticleDetail,
  updateArticleViewCount,
  getArticleArchive,
  getArticlesByArchive,
  searchArticles,
  getRelatedArticles,
  submitComment
} from './article'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Article API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getArticleListAnonymous', () => {
    it('应该导出 getArticleListAnonymous 函数', () => {
      expect(getArticleListAnonymous).toBeDefined()
      expect(typeof getArticleListAnonymous).toBe('function')
    })
  })

  describe('getArticleList', () => {
    it('应该导出 getArticleList 函数', () => {
      expect(getArticleList).toBeDefined()
      expect(typeof getArticleList).toBe('function')
    })
  })

  describe('getArticlesByCategory', () => {
    it('应该导出 getArticlesByCategory 函数', () => {
      expect(getArticlesByCategory).toBeDefined()
      expect(typeof getArticlesByCategory).toBe('function')
    })
  })

  describe('getHotArticles', () => {
    it('应该导出 getHotArticles 函数', () => {
      expect(getHotArticles).toBeDefined()
      expect(typeof getHotArticles).toBe('function')
    })
  })

  describe('getTopArticles', () => {
    it('应该导出 getTopArticles 函数', () => {
      expect(getTopArticles).toBeDefined()
      expect(typeof getTopArticles).toBe('function')
    })
  })

  describe('getRecommendArticles', () => {
    it('应该导出 getRecommendArticles 函数', () => {
      expect(getRecommendArticles).toBeDefined()
      expect(typeof getRecommendArticles).toBe('function')
    })
  })

  describe('getArticleDetail', () => {
    it('应该导出 getArticleDetail 函数', () => {
      expect(getArticleDetail).toBeDefined()
      expect(typeof getArticleDetail).toBe('function')
    })
  })

  describe('updateArticleViewCount', () => {
    it('应该导出 updateArticleViewCount 函数', () => {
      expect(updateArticleViewCount).toBeDefined()
      expect(typeof updateArticleViewCount).toBe('function')
    })
  })

  describe('getArticleArchive', () => {
    it('应该导出 getArticleArchive 函数', () => {
      expect(getArticleArchive).toBeDefined()
      expect(typeof getArticleArchive).toBe('function')
    })
  })

  describe('getArticlesByArchive', () => {
    it('应该导出 getArticlesByArchive 函数', () => {
      expect(getArticlesByArchive).toBeDefined()
      expect(typeof getArticlesByArchive).toBe('function')
    })
  })

  describe('searchArticles', () => {
    it('应该导出 searchArticles 函数', () => {
      expect(searchArticles).toBeDefined()
      expect(typeof searchArticles).toBe('function')
    })
  })

  describe('getRelatedArticles', () => {
    it('应该导出 getRelatedArticles 函数', () => {
      expect(getRelatedArticles).toBeDefined()
      expect(typeof getRelatedArticles).toBe('function')
    })
  })

  describe('submitComment', () => {
    it('应该导出 submitComment 函数', () => {
      expect(submitComment).toBeDefined()
      expect(typeof submitComment).toBe('function')
    })
  })
})