import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { usePermissionStore } from '@/stores/permission'

// Mock router
vi.mock('@/router', () => ({
  constantRoutes: [
    { path: '/login', name: 'Login', component: 'Login' },
    { path: '/404', name: 'NotFound', component: 'NotFound' }
  ],
  dynamicRoutes: [
    {
      path: '/admin',
      name: 'Admin',
      component: 'Layout',
      permissions: ['system:user:list']
    }
  ]
}))

// Mock menu API
vi.mock('@/api/menu', () => ({
  getRouters: vi.fn()
}))

// Mock user store
vi.mock('@/store/modules/user', () => ({
  default: vi.fn(() => ({
    roles: ['admin'],
    permissions: ['*:*:*']
  }))
}))

// Mock Layout 组件
vi.mock('@/layout/index.vue', () => ({
  default: 'Layout'
}))

// Mock ParentView 组件
vi.mock('@/components/ParentView', () => ({
  default: 'ParentView'
}))

// Mock InnerLink 组件
vi.mock('@/layout/components/InnerLink', () => ({
  default: 'InnerLink'
}))

// Mock modules
vi.mock('@/router', () => ({
  constantRoutes: [
    { path: '/login', name: 'Login', component: 'Login' },
    { path: '/404', name: 'NotFound', component: 'NotFound' }
  ],
  dynamicRoutes: [
    {
      path: '/admin',
      name: 'Admin',
      component: 'Layout',
      permissions: ['system:user:list']
    }
  ]
}))

describe('Permission Store 测试', () => {
  let permissionStore
  let getRouters

  beforeEach(() => {
    setActivePinia(createPinia())
    permissionStore = usePermissionStore()
    getRouters = require('@/api/menu').getRouters
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('state 初始化', () => {
    it('应该正确初始化状态', () => {
      expect(permissionStore.routes).toEqual([])
      expect(permissionStore.addRoutes).toEqual([])
      expect(permissionStore.defaultRoutes).toEqual([])
      expect(permissionStore.topbarRouters).toEqual([])
      expect(permissionStore.sidebarRouters).toEqual([])
    })
  })

  describe('setRoutes action', () => {
    it('应该设置路由', () => {
      const routes = [
        { path: '/home', name: 'Home', component: 'Home' }
      ]
      permissionStore.setRoutes(routes)

      expect(permissionStore.addRoutes).toEqual(routes)
      expect(permissionStore.routes).toHaveLength(3) // constantRoutes + routes
    })
  })

  describe('setDefaultRoutes action', () => {
    it('应该设置默认路由', () => {
      const routes = [
        { path: '/home', name: 'Home', component: 'Home' }
      ]
      permissionStore.setDefaultRoutes(routes)

      expect(permissionStore.defaultRoutes).toHaveLength(3) // constantRoutes + routes
    })
  })

  describe('setTopbarRoutes action', () => {
    it('应该设置顶部栏路由', () => {
      const routes = [
        { path: '/home', name: 'Home', component: 'Home' }
      ]
      permissionStore.setTopbarRoutes(routes)

      expect(permissionStore.topbarRouters).toEqual(routes)
    })
  })

  describe('setSidebarRouters action', () => {
    it('应该设置侧边栏路由', () => {
      const routes = [
        { path: '/home', name: 'Home', component: 'Home' }
      ]
      permissionStore.setSidebarRouters(routes)

      expect(permissionStore.sidebarRouters).toEqual(routes)
    })
  })

  describe('generateRoutes action', () => {
    it('应该成功生成路由', async () => {
      const mockResponse = {
        data: [
          {
            path: '/system',
            name: 'System',
            component: 'Layout',
            children: [
              {
                path: 'user',
                name: 'User',
                component: 'system/user/index',
                meta: { title: '用户管理' }
              }
            ]
          }
        ]
      }
      getRouters.mockResolvedValue(mockResponse)

      await permissionStore.generateRoutes()

      expect(getRouters).toHaveBeenCalled()
      expect(permissionStore.routes).toBeDefined()
      expect(permissionStore.sidebarRouters).toBeDefined()
      expect(permissionStore.topbarRouters).toBeDefined()
    })

    it('应该处理路由生成失败', async () => {
      const error = new Error('获取路由失败')
      getRouters.mockRejectedValue(error)

      await expect(permissionStore.generateRoutes()).rejects.toThrow('获取路由失败')
    })
  })

  describe('路由状态管理', () => {
    it('应该正确管理路由状态', () => {
      const routes = [
        { path: '/home', name: 'Home' },
        { path: '/about', name: 'About' }
      ]

      permissionStore.setRoutes(routes)
      expect(permissionStore.addRoutes).toEqual(routes)

      permissionStore.setTopbarRoutes(routes)
      expect(permissionStore.topbarRouters).toEqual(routes)

      permissionStore.setSidebarRouters(routes)
      expect(permissionStore.sidebarRouters).toEqual(routes)

      permissionStore.setDefaultRoutes(routes)
      expect(permissionStore.defaultRoutes).toHaveLength(3)
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的路由生成流程', async () => {
      const mockResponse = {
        data: [
          {
            path: '/blog',
            name: 'Blog',
            component: 'Layout',
            meta: { title: '博客管理' },
            children: [
              {
                path: 'article',
                name: 'Article',
                component: 'blog/article/index',
                meta: { title: '文章管理' }
              }
            ]
          }
        ]
      }
      getRouters.mockResolvedValue(mockResponse)

      // 生成路由
      await permissionStore.generateRoutes()

      // 验证路由已设置
      expect(getRouters).toHaveBeenCalled()
      expect(permissionStore.routes).toBeDefined()
      expect(permissionStore.sidebarRouters).toBeDefined()
      expect(permissionStore.topbarRouters).toBeDefined()
    })
  })
})