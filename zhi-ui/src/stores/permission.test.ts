import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { usePermissionStore } from './permission'
import { getRouters } from '@/api/menu'

vi.mock('@/api/menu', () => ({
  getRouters: vi.fn()
}))

const mockGetRouters = vi.mocked(getRouters)

describe('Permission Store 测试', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    mockGetRouters.mockReset()
  })

  describe('初始状态', () => {
    it('应该初始化为默认值', () => {
      const store = usePermissionStore()
      expect(store.routes).toEqual([])
      expect(store.addRoutes).toEqual([])
      expect(store.defaultRoutes).toEqual([])
      expect(store.topbarRouters).toEqual([])
      expect(store.sidebarRouters).toEqual([])
    })
  })

  describe('setRoutes', () => {
    it('应该设置路由', () => {
      const store = usePermissionStore()
      const routes = [
        { path: '/home', name: 'Home' },
        { path: '/about', name: 'About' }
      ]
      store.setRoutes(routes)
      expect(store.addRoutes).toEqual(routes)
      expect(store.routes).toContainEqual(routes[0])
    })
  })

  describe('setDefaultRoutes', () => {
    it('应该设置默认路由', () => {
      const store = usePermissionStore()
      const routes = [{ path: '/dashboard', name: 'Dashboard' }]
      store.setDefaultRoutes(routes)
      expect(store.defaultRoutes).toContainEqual(routes[0])
    })
  })

  describe('setTopbarRoutes', () => {
    it('应该设置顶部栏路由', () => {
      const store = usePermissionStore()
      const routes = [{ path: '/top1', name: 'Top1' }]
      store.setTopbarRoutes(routes)
      expect(store.topbarRouters).toEqual(routes)
    })
  })

  describe('setSidebarRouters', () => {
    it('应该设置侧边栏路由', () => {
      const store = usePermissionStore()
      const routes = [{ path: '/side1', name: 'Side1' }]
      store.setSidebarRouters(routes)
      expect(store.sidebarRouters).toEqual(routes)
    })
  })

  describe('generateRoutes', () => {
    it('应该把后端动态路由同时写入新增路由和侧边栏菜单', async () => {
      mockGetRouters.mockResolvedValue({
        data: [
          {
            path: '/admin/generated-menu',
            name: 'BlogManage',
            component: 'Layout',
            meta: { title: '博客管理' },
            children: [
              {
                path: 'article',
                name: 'BlogArticle',
                component: 'blog/article/index',
                meta: { title: '文章管理' }
              }
            ]
          }
        ]
      } as any)

      const store = usePermissionStore()
      const accessRoutes = await store.generateRoutes()

      expect(accessRoutes.some(route => route.path === '/admin/generated-menu')).toBe(true)
      expect(store.addRoutes.some(route => route.path === '/admin/generated-menu')).toBe(true)
      expect(store.routes.some(route => route.path === '/admin/generated-menu')).toBe(true)
      expect(store.sidebarRouters.some(route => route.path === '/admin/generated-menu')).toBe(true)
      expect(store.defaultRoutes.some(route => route.path === '/admin/generated-menu')).toBe(true)
      expect(store.topbarRouters.some(route => route.path === '/:pathMatch(.*)*')).toBe(true)
    })
  })
})
