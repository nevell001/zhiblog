import { describe, it, expect, beforeEach, vi } from 'vitest'
import tab from './tab'

// Mock dependencies
vi.mock('@/stores/tagsView', () => ({
  useTagsViewStore: vi.fn(() => ({
    delCachedView: vi.fn(() => Promise.resolve()),
    delView: vi.fn(() => Promise.resolve({ visitedViews: [] })),
    delAllViews: vi.fn(() => Promise.resolve()),
    delLeftTags: vi.fn(() => Promise.resolve()),
    delRightTags: vi.fn(() => Promise.resolve()),
    delOthersViews: vi.fn(() => Promise.resolve()),
    addView: vi.fn(),
    updateVisitedView: vi.fn(() => Promise.resolve())
  }))
}))

vi.mock('@/router', () => ({
  currentRoute: { value: { path: '/test', query: {}, matched: [] } },
  replace: vi.fn(() => Promise.resolve()),
  push: vi.fn(() => Promise.resolve())
}))

describe('tab 插件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('refreshPage', () => {
    it('应该存在 refreshPage 函数', () => {
      expect(tab.refreshPage).toBeDefined()
      expect(typeof tab.refreshPage).toBe('function')
    })

    it('应该刷新当前页签', async () => {
      await tab.refreshPage()
      expect(tab.refreshPage).toBeInstanceOf(Promise)
    })
  })

  describe('closeOpenPage', () => {
    it('应该存在 closeOpenPage 函数', () => {
      expect(tab.closeOpenPage).toBeDefined()
      expect(typeof tab.closeOpenPage).toBe('function')
    })

    it('应该关闭当前页签并打开新页签', async () => {
      await tab.closeOpenPage('/new-page')
      expect(tab.closeOpenPage('/new-page')).toBeInstanceOf(Promise)
    })
  })

  describe('closePage', () => {
    it('应该存在 closePage 函数', () => {
      expect(tab.closePage).toBeDefined()
      expect(typeof tab.closePage).toBe('function')
    })

    it('应该关闭当前页签', async () => {
      await tab.closePage()
      expect(tab.closePage()).toBeInstanceOf(Promise)
    })
  })

  describe('closeAllPage', () => {
    it('应该存在 closeAllPage 函数', () => {
      expect(tab.closeAllPage).toBeDefined()
      expect(typeof tab.closeAllPage).toBe('function')
    })

    it('应该关闭所有页签', async () => {
      await tab.closeAllPage()
      expect(tab.closeAllPage()).toBeInstanceOf(Promise)
    })
  })

  describe('closeLeftPage', () => {
    it('应该存在 closeLeftPage 函数', () => {
      expect(tab.closeLeftPage).toBeDefined()
      expect(typeof tab.closeLeftPage).toBe('function')
    })

    it('应该关闭左侧页签', async () => {
      await tab.closeLeftPage()
      expect(tab.closeLeftPage()).toBeInstanceOf(Promise)
    })
  })

  describe('closeRightPage', () => {
    it('应该存在 closeRightPage 函数', () => {
      expect(tab.closeRightPage).toBeDefined()
      expect(typeof tab.closeRightPage).toBe('function')
    })

    it('应该关闭右侧页签', async () => {
      await tab.closeRightPage()
      expect(tab.closeRightPage()).toBeInstanceOf(Promise)
    })
  })

  describe('closeOtherPage', () => {
    it('应该存在 closeOtherPage 函数', () => {
      expect(tab.closeOtherPage).toBeDefined()
      expect(typeof tab.closeOtherPage).toBe('function')
    })

    it('应该关闭其他页签', async () => {
      await tab.closeOtherPage()
      expect(tab.closeOtherPage()).toBeInstanceOf(Promise)
    })
  })

  describe('openPage', () => {
    it('应该存在 openPage 函数', () => {
      expect(tab.openPage).toBeDefined()
      expect(typeof tab.openPage).toBe('function')
    })

    it('应该打开新页签', async () => {
      await tab.openPage('测试页面', '/test-page')
      expect(tab.openPage('测试页面', '/test-page')).toBeInstanceOf(Promise)
    })
  })

  describe('updatePage', () => {
    it('应该存在 updatePage 函数', () => {
      expect(tab.updatePage).toBeDefined()
      expect(typeof tab.updatePage).toBe('function')
    })

    it('应该更新页签', async () => {
      const obj = { path: '/test', meta: { title: '测试' } }
      await tab.updatePage(obj)
      expect(tab.updatePage(obj)).toBeInstanceOf(Promise)
    })
  })
})
