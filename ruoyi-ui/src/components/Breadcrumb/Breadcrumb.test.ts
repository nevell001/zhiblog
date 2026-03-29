import { describe, it, expect, vi, beforeEach } from 'vitest'
import Breadcrumb from '../index.vue'
import { useRoute, useRouter } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import { constantRoutes } from '@/router/index'

// Mock 路由和 store
vi.mock('vue-router')
vi.mock('@/stores/permission')

describe('Breadcrumb 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 Breadcrumb 组件', () => {
    expect(Breadcrumb).toBeDefined()
  })

  it('应该初始化空的面包屑列表', () => {
    expect(Breadcrumb.levelList.value).toEqual([])
  })

  it('首页路由应该显示首页面包屑', () => {
    const mockRoute = { path: '/index', meta: { title: '首页' } }
    vi.mocked(useRoute).mockReturnValue(mockRoute)
    vi.mocked(usePermissionStore).mockReturnValue({ defaultRoutes: [
      { path: '/index', meta: { title: '首页' } }
    ])

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([{ path: '/index', meta: { title: '首页' } }])
  })

    expect(Breadcrumb.levelList.value.length).toBe(1)
  })

  it('二级菜单路由应该显示多级面包屑', () => {
    const mockRoute = { path: '/admin/user', meta: { title: '用户管理' }, children: [
      { path: 'profile', meta: { title: '个人中心' } }
    ]}
    vi.mocked(useRoute).mockReturnValue(mockRoute)
    vi.mocked(usePermissionStore).mockReturnValue({ defaultRoutes: [
      { path: '/admin/user', meta: { title: '用户管理' } },
      { path: '/admin/user/profile', meta: { title: '个人中心' } }
    ]})

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/admin/user', meta: { title: '用户管理' } },
      { path: '/admin/user/profile', meta: { title: '个人中心' } }
    ])

    expect(Breadcrumb.levelList.value.length).toBe(2)
  })

  it('系统管理路由应该显示多级面包屑', () => {
    const mockRoute = { path: '/admin/system', meta: { title: '系统管理' }, children: [
      { path: '/admin/system/user', meta: { title: '用户管理' } },
      { path: '/admin/system/menu', meta: { title: '菜单管理' } },
      { path: '/admin/system/role', meta: { title: '角色管理' } }
    ]}
    vi.mocked(useRoute).mockReturnValue(mockRoute)
    vi.mocked(usePermissionStore).mockReturnValue({ defaultRoutes: [
      { path: '/admin/system', meta: { title: '系统管理' } },
      { path: '/admin/system/user', meta: { title: '用户管理' } },
      { path: '/admin/system/menu', meta: { title: '菜单管理' } },
      { path: '/admin/system/role', meta: { title: '角色管理' } }
    ]})

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/admin/system', meta: { title: '系统管理' } },
      { path: '/admin/system/user', meta: { title: '用户管理' } },
      { path: '/admin/system/menu', meta: title: '菜单管理' } },
      { path: '/admin/system/role', meta: { title: '角色管理' } }
    ])

    expect(Breadcrumb.levelList.value.length).toBe(3)
  })

  it('文章详情路由应该显示文章面包屑', () => {
    const mockRoute = { path: '/blog/article/:id', meta: { title: '文章详情' } }
    vi.mocked(useRoute).mockReturnValue(mockRoute)

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/blog/article/:id', meta: { title: '文章详情' } }
    ])

    expect(Breadcrumb.levelList.value.length).toBe(1)
  })

  it('应该正确处理重定向路由', () => {
    const mockRoute = { path: '/redirect', redirect: '/blog' }
    vi.mocked(useRoute).mockReturnValue(mockRoute)

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([])
  })

    expect(Breadcrumb.isRedirect.value).toBe(false)
  })

  it('应该处理路径参数路由', () => {
    const mockRoute = { path: '/system/user/profile/:id', meta: { title: '个人中心' } }
    vi.mocked(useRoute).mockReturnValue(mockRoute)

    Breadcrumb.getBreadcrumb()
    Breadcrumb.isDashboard(mockRoute.path)

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/system/user/profile', meta: { title: '个人中心' } }
    ])
  })

  it('应该更新路由变化时重新计算面包屑', () => {
    const initialRoute = { path: '/admin/user', meta: { title: '用户管理' } }
    const updatedRoute = { path: '/admin/user/profile', meta: { title: '个人中心' } }

    vi.mocked(useRoute).mockReturnValue(initialRoute)
    Breadcrumb.getBreadcrumb()

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/admin/user', meta: { title: '用户管理' } },
      { path: '/admin/user/profile', meta: { title: '个人中心' } }
    ])

    // 模拟路由变化
    vi.mocked(useRoute).mockReturnValue(updatedRoute)
    Breadcrumb.getBreadcrumb()

    expect(Breadcrumb.levelList.value).toEqual([
      { path: '/admin/user', meta: { title: '用户管理' } },
      { path: '/admin/user/profile', meta: { title: '个人中心' } }
    ])
  })

  it('应该处理未匹配路由', () => {
    vi.mocked(useRoute).mockReturnValue(null)
    vi.mocked(usePermissionStore).mockReturnValue({ defaultRoutes: [] })

    Breadcrumb.getBreadcrumb()

    expect(Breadcrumb.levelList.value).toEqual([])
    expect(Breadcrumb.isRedirect.value).toBe(false)
  })
})
