import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTagsViewStore } from './tagsView'

describe('TagsView Store 测试', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  describe('初始状态', () => {
    it('应该初始化为空数组', () => {
      const store = useTagsViewStore()
      expect(store.visitedViews).toEqual([])
      expect(store.cachedViews).toEqual([])
      expect(store.iframeViews).toEqual([])
    })
  })

  describe('addView', () => {
    it('应该添加新的访问视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(view)
      expect(store.visitedViews).toHaveLength(1)
    })

    it('如果视图已存在则不重复添加', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(view)
      store.addView(view)
      expect(store.visitedViews).toHaveLength(1)
    })

    it('如果视图设置了 keepAlive 则添加到缓存视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { keepAlive: true } }
      store.addView(view)
      expect(store.cachedViews).toContain('Test')
    })

    it('如果视图没有 keepAlive 则不添加到缓存视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: {} }
      store.addView(view)
      expect(store.cachedViews).toHaveLength(0)
    })
  })

  describe('addIframeView', () => {
    it('应该添加 iframe 视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/iframe' }
      store.addIframeView(view)
      expect(store.iframeViews).toHaveLength(1)
    })
  })

  describe('delView', () => {
    it('应该删除指定的视图', async () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(view)
      const result = await store.delView(view)
      expect(result.visitedViews).toHaveLength(0)
      expect(result.cachedViews).toHaveLength(0)
    })

    it('应该返回更新后的视图列表', async () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(view)
      const result = await store.delView(view)
      expect(result).toHaveProperty('visitedViews')
      expect(result).toHaveProperty('cachedViews')
    })
  })

  describe('delVisitedView', () => {
    it('应该删除访问视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(view)
      const deleted = store.delVisitedView(view)
      expect(store.visitedViews).toHaveLength(0)
      expect(deleted).toMatchObject({ path: '/test' })
    })

    it('如果视图不存在则返回 undefined', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      const deleted = store.delVisitedView(view)
      expect(deleted).toBeUndefined()
    })
  })

  describe('delCachedView', () => {
    it('应该删除缓存视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { keepAlive: true } }
      store.addView(view)
      store.delCachedView(view)
      expect(store.cachedViews).toHaveLength(0)
    })
  })

  describe('delOthersViews', () => {
    it('应该删除其他视图', async () => {
      const store = useTagsViewStore()
      const view1 = { path: '/test1', name: 'Test1', meta: { title: 'Test1' } }
      const view2 = { path: '/test2', name: 'Test2', meta: { title: 'Test2' } }
      store.addView(view1)
      store.addView(view2)
      const result = await store.delOthersViews(view1)
      expect(result.visitedViews).toHaveLength(1)
      expect(result.visitedViews[0].path).toBe('/test1')
    })
  })

  describe('delAllViews', () => {
    it('应该删除所有视图', async () => {
      const store = useTagsViewStore()
      const view1 = { path: '/test1', name: 'Test1', meta: { title: 'Test1' } }
      const view2 = { path: '/test2', name: 'Test2', meta: { keepAlive: true } }
      store.addView(view1)
      store.addView(view2)
      const result = await store.delAllViews()
      expect(result.visitedViews).toHaveLength(0)
      expect(result.cachedViews).toHaveLength(0)
    })

    it('应该保留 affix 标签', async () => {
      const store = useTagsViewStore()
      const affixView = {
        path: '/dashboard',
        name: 'Dashboard',
        meta: { affix: true, title: 'Dashboard' }
      }
      const normalView = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(affixView)
      store.addView(normalView)
      const result = await store.delAllViews()
      expect(result.visitedViews).toHaveLength(1)
      expect(result.visitedViews[0].path).toBe('/dashboard')
    })
  })

  describe('updateVisitedView', () => {
    it('应该更新已存在的视图', () => {
      const store = useTagsViewStore()
      const view = { path: '/test', name: 'Test', meta: { title: 'Old Title' } }
      store.addView(view)
      store.updateVisitedView({ path: '/test', meta: { title: 'New Title' } })
      expect(store.visitedViews[0].meta?.title).toBe('New Title')
    })
  })

  describe('delRightTags', () => {
    it('应该删除右侧标签', async () => {
      const store = useTagsViewStore()
      const view1 = { path: '/test1', name: 'Test1', meta: { title: 'Test1' } }
      const view2 = { path: '/test2', name: 'Test2', meta: { title: 'Test2' } }
      const view3 = { path: '/test3', name: 'Test3', meta: { title: 'Test3' } }
      store.addView(view1)
      store.addView(view2)
      store.addView(view3)
      const result = await store.delRightTags(view2)
      expect(result.visitedViews).toHaveLength(2)
    })
  })

  describe('delLeftTags', () => {
    it('应该删除左侧标签', async () => {
      const store = useTagsViewStore()
      const view1 = { path: '/test1', name: 'Test1', meta: { title: 'Test1' } }
      const view2 = { path: '/test2', name: 'Test2', meta: { title: 'Test2' } }
      const view3 = { path: '/test3', name: 'Test3', meta: { title: 'Test3' } }
      store.addView(view1)
      store.addView(view2)
      store.addView(view3)
      const result = await store.delLeftTags(view2)
      expect(result.visitedViews).toHaveLength(2)
    })
  })

  describe('affix 标签处理', () => {
    it('affix 标签不应被删除', async () => {
      const store = useTagsViewStore()
      const affixView = {
        path: '/dashboard',
        name: 'Dashboard',
        meta: { affix: true, title: 'Dashboard' }
      }
      const normalView = { path: '/test', name: 'Test', meta: { title: 'Test' } }
      store.addView(affixView)
      store.addView(normalView)
      await store.delAllViews()
      expect(store.visitedViews).toHaveLength(1)
      expect(store.visitedViews[0].meta?.affix).toBe(true)
    })
  })
})
