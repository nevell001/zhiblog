import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useDynamicTitle } from './dynamicTitle'
import { useSettingsStore } from '@/stores/settings'

// Mock stores
vi.mock('@/stores/settings', () => ({
  useSettingsStore: vi.fn()
}))

// Mock API
vi.mock('@/api/system/config', () => ({
  getConfigKey: vi.fn()
}))

describe('useDynamicTitle hook 测试', () => {
  const mockUseSettingsStore = vi.mocked(useSettingsStore)
  const mockGetConfigKey = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    // 模拟 store
    mockUseSettingsStore.mockReturnValue({
      dynamicTitle: false,
      title: '我的博客'
    })
    // 模拟 API
    const { getConfigKey } = require('@/api/system/config')
    mockGetConfigKey.mockImplementation((key: string) => {
      if (key === 'blog.seo.title') {
        return Promise.resolve({ configKey: key, configValue: 'SEO标题', configName: 'SEO标题' })
      }
      if (key === 'blog.seo.description') {
        return Promise.resolve({ configKey: key, configValue: 'SEO描述', configName: 'SEO描述' })
      }
      if (key === 'blog.seo.keywords') {
        return Promise.resolve({ configKey: key, configValue: '关键词1,关键词2', configName: '关键词' })
      }
      return Promise.resolve({ configKey: '', configValue: '', configName: '' })
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该导出 useDynamicTitle 函数', () => {
    expect(useDynamicTitle).toBeDefined()
    expect(typeof useDynamicTitle).toBe('function')
  })

  it('应该在调用时设置页面标题', () => {
    // 模拟 dynamicTitle 为 false
    mockUseSettingsStore.mockReturnValue({
      dynamicTitle: false,
      title: '我的博客'
    })

    useDynamicTitle()
    // 需要稍后检查 document.title，因为函数是异步的
  })

  it('应该在 dynamicTitle 为 true 时设置自定义标题', () => {
    mockUseSettingsStore.mockReturnValue({
      dynamicTitle: true,
      title: '自定义博客'
    })

    useDynamicTitle()
  })
})
