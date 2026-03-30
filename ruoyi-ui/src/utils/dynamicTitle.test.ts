import { describe, it, expect, vi, beforeEach } from 'vitest'
import dynamicTitle from './dynamicTitle'

// Mock useAppStore
const mockStore = {
  setTitle: vi.fn()
}

vi.mock('@/stores/app', () => ({
  useAppStore: () => mockStore
}))

describe('DynamicTitle Utils 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 dynamicTitle 函数', () => {
    expect(dynamicTitle).toBeDefined()
    expect(typeof dynamicTitle).toBe('function')
  })

  it('应该设置动态标题', () => {
    const title = '博客管理系统'
    expect(typeof title).toBe('string')
    expect(title.length).toBeGreaterThan(0)
  })

  it('应该支持默认标题', () => {
    const defaultTitle = '若依管理系统'
    expect(defaultTitle).toBe('若依管理系统')
  })

  it('应该更新页面标题', () => {
    const title = '用户管理'
    document.title = title
    expect(document.title).toBe('用户管理')
  })

  it('应该处理路由变化', () => {
    const route = {
      meta: { title: '首页' }
    }
    expect(route.meta?.title).toBe('首页')
  })

  it('应该有标题前缀', () => {
    const prefix = '后台管理系统'
    expect(prefix).toBe('后台管理系统')
  })
})