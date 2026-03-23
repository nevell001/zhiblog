import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as statisticsApi from './statistics'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Statistics API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getStatisticsOverview', () => {
    it('应该导出 getStatisticsOverview 函数', () => {
      expect(statisticsApi.getStatisticsOverview).toBeDefined()
      expect(typeof statisticsApi.getStatisticsOverview).toBe('function')
    })

    it('应该调用数据概览统计接口', () => {
      statisticsApi.getStatisticsOverview()
      expect(request).toHaveBeenCalledWith({
        url: '/system-stats/overview',
        method: 'get'
      })
    })
  })

  describe('getArticleStatistics', () => {
    it('应该导出 getArticleStatistics 函数', () => {
      expect(statisticsApi.getArticleStatistics).toBeDefined()
      expect(typeof statisticsApi.getArticleStatistics).toBe('function')
    })

    it('应该调用文章统计接口', () => {
      statisticsApi.getArticleStatistics()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/article',
        method: 'get'
      })
    })
  })

  describe('getUserStatistics', () => {
    it('应该导出 getUserStatistics 函数', () => {
      expect(statisticsApi.getUserStatistics).toBeDefined()
      expect(typeof statisticsApi.getUserStatistics).toBe('function')
    })

    it('应该调用用户统计接口', () => {
      statisticsApi.getUserStatistics()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/user',
        method: 'get'
      })
    })
  })

  describe('getArticleTrend', () => {
    it('应该导出 getArticleTrend 函数', () => {
      expect(statisticsApi.getArticleTrend).toBeDefined()
      expect(typeof statisticsApi.getArticleTrend).toBe('function')
    })

    it('应该调用文章发布趋势接口', () => {
      statisticsApi.getArticleTrend()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/article/trend',
        method: 'get'
      })
    })
  })

  describe('getUserActivity', () => {
    it('应该导出 getUserActivity 函数', () => {
      expect(statisticsApi.getUserActivity).toBeDefined()
      expect(typeof statisticsApi.getUserActivity).toBe('function')
    })

    it('应该调用用户活跃度接口', () => {
      statisticsApi.getUserActivity()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/user/activity',
        method: 'get'
      })
    })
  })

  describe('getUserRegisterTrend', () => {
    it('应该导出 getUserRegisterTrend 函数', () => {
      expect(statisticsApi.getUserRegisterTrend).toBeDefined()
      expect(typeof statisticsApi.getUserRegisterTrend).toBe('function')
    })

    it('应该调用用户注册趋势接口', () => {
      statisticsApi.getUserRegisterTrend()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/user/register-trend',
        method: 'get'
      })
    })
  })

  describe('getUserRoleDistribution', () => {
    it('应该导出 getUserRoleDistribution 函数', () => {
      expect(statisticsApi.getUserRoleDistribution).toBeDefined()
      expect(typeof statisticsApi.getUserRoleDistribution).toBe('function')
    })

    it('应该调用用户角色分布接口', () => {
      statisticsApi.getUserRoleDistribution()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/user/role-distribution',
        method: 'get'
      })
    })
  })

  describe('getArticleCategoryDistribution', () => {
    it('应该导出 getArticleCategoryDistribution 函数', () => {
      expect(statisticsApi.getArticleCategoryDistribution).toBeDefined()
      expect(typeof statisticsApi.getArticleCategoryDistribution).toBe('function')
    })

    it('应该调用文章分类分布接口', () => {
      statisticsApi.getArticleCategoryDistribution()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/article/category-distribution',
        method: 'get'
      })
    })
  })

  describe('getHotTags', () => {
    it('应该导出 getHotTags 函数', () => {
      expect(statisticsApi.getHotTags).toBeDefined()
      expect(typeof statisticsApi.getHotTags).toBe('function')
    })

    it('应该调用热门标签接口', () => {
      statisticsApi.getHotTags()
      expect(request).toHaveBeenCalledWith({
        url: '/statistics/article/hot-tags',
        method: 'get'
      })
    })
  })
})
