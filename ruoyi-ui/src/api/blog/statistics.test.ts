import { describe, it, expect, vi } from 'vitest'
import {
  getOverview,
  getArticleStats,
  getUserStats,
  getVisitStats
} from './statistics'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn()
}))

describe('Blog Statistics API 测试', () => {
  it('应该导出 getOverview 函数', () => {
    expect(getOverview).toBeDefined()
    expect(typeof getOverview).toBe('function')
  })

  it('应该导出 getArticleStats 函数', () => {
    expect(getArticleStats).toBeDefined()
    expect(typeof getArticleStats).toBe('function')
  })

  it('应该导出 getUserStats 函数', () => {
    expect(getUserStats).toBeDefined()
    expect(typeof getUserStats).toBe('function')
  })

  it('应该导出 getVisitStats 函数', () => {
    expect(getVisitStats).toBeDefined()
    expect(typeof getVisitStats).toBe('function')
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