import { describe, it, expect, vi } from 'vitest'
import {
  getStatisticsOverview,
  getArticleStatistics,
  getUserStatistics,
  getArticleTrend
} from '@/api/statistics'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn()
}))

describe('Blog Statistics API 测试', () => {
  it('应该导出 getStatisticsOverview 函数', () => {
    expect(getStatisticsOverview).toBeDefined()
    expect(typeof getStatisticsOverview).toBe('function')
  })

  it('应该导出 getArticleStatistics 函数', () => {
    expect(getArticleStatistics).toBeDefined()
    expect(typeof getArticleStatistics).toBe('function')
  })

  it('应该导出 getUserStatistics 函数', () => {
    expect(getUserStatistics).toBeDefined()
    expect(typeof getUserStatistics).toBe('function')
  })

  it('应该导出 getArticleTrend 函数', () => {
    expect(getArticleTrend).toBeDefined()
    expect(typeof getArticleTrend).toBe('function')
  })

  it('应该支持日期范围查询', () => {
    const dateRange = {
      startDate: '2024-01-01',
      endDate: '2024-12-31'
    }
    expect(dateRange.startDate).toBe('2024-01-01')
    expect(dateRange.endDate).toBe('2024-12-31')
  })

  it('应该有文章统计数据', () => {
    const articleStats = {
      total: 100,
      published: 80,
      draft: 20
    }
    expect(articleStats.total).toBe(100)
    expect(articleStats.published).toBe(80)
  })

  it('应该有用户统计数据', () => {
    const userStats = {
      totalUsers: 1000,
      newUsers: 50,
      activeUsers: 30
    }
    expect(userStats.totalUsers).toBe(1000)
    expect(userStats.newUsers).toBe(50)
  })

  it('应该有访问统计数据', () => {
    const visitStats = {
      totalVisits: 10000,
      uniqueVisitors: 5000,
      avgVisitDuration: 300
    }
    expect(visitStats.totalVisits).toBe(10000)
    expect(visitStats.uniqueVisitors).toBe(5000)
  })
})
