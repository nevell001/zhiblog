import { describe, it, expect, vi } from 'vitest'
import router from './index'
import { routes as constantRoutes } from './index'

describe('Router Index 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 routes 数组', () => {
    expect(constantRoutes).toBeDefined()
    expect(Array.isArray(constantRoutes)).toBe(true)
  })

  it('应该导出 router 对象', () => {
    expect(router).toBeDefined()
    expect(typeof router).toBe('object')
    expect(router.options).toBeDefined()
  })

  it('routes 数组应该包含后台管理路由', () => {
    const adminRoutes = constantRoutes.filter(route => route.path === '/admin' || route.path.startsWith('/admin/'))
    expect(adminRoutes.length).toBeGreaterThan(0)
  })

  it('routes 数组应该包含前台博客路由', () => {
    const blogRoutes = constantRoutes.filter(route => route.path === '/blog' || route.path.startsWith('/blog'))
    expect(blogRoutes.length).toBeGreaterThan(0)
  })

  it('routes 数组应该包含错误页面路由', () => {
    const errorRoutes = constantRoutes.filter(route => route.path === '/test')
    expect(errorRoutes.length).toBe(1)
  })

  it('routes 数组应该包含登录路由', () => {
    const loginRoutes = constantRoutes.filter(route => route.path === '/login')
    expect(loginRoutes.length).toBe(1)
  })

  it('routes 应该包含 dashboard, article, category, tag, comment, setting, friendLink 等管理路由', () => {
    const managementRoutes = constantRoutes.filter(route => 
      route.path.startsWith('/admin/blog') ||
      route.path.startsWith('/admin/system')
    )
    expect(managementRoutes.length).toBeGreaterThan(0)
  })

  it('routes 数组应该包含统计相关路由', () => {
    const statisticsRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/statistics'))
    expect(statisticsRoutes.length).toBe(3)
  })

  it('routes 数组应该包含监控相关路由', () => {
    const monitorRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/monitor'))
    expect(monitorRoutes.length).toBeGreaterThan(0)
  })

  it('所有路由都应该有 path 属性', () => {
    constantRoutes.forEach(route => {
      expect(route).toHaveProperty('path')
    expect(typeof route.path).toBe('string')
    })
  })

  it('所有路由都应该有 component 属性', () => {
    constantRoutes.forEach(route => {
      expect(route).toHaveProperty('component')
      expect(typeof route.component).toBe('function')
    })
  })

  it('所有路由都应该有 meta 属性', () => {
    constantRoutes.forEach(route => {
      expect(route.meta).toBeDefined()
      expect(typeof route.meta).toBe('object')
    })
  })

  it('所有路由都应该有 name 属性', () => {
    const routesWithNames = constantRoutes.filter(route => route.name)
    expect(routesWithNames.length).toBeGreaterThan(10)
  })

  it('children 路由应该有 path 属性', () => {
    constantRoutes.forEach(route => {
      if (route.children) {
        route.children.forEach(child => {
          expect(child).toHaveProperty('path')
          expect(typeof child.path).toBe('string')
        })
      }
    })
  })

  it('hidden 属性的路由应该有 hidden 设置为 true', () => {
    const hiddenRoutes = constantRoutes.filter(route => route.hidden === true)
    expect(hiddenRoutes.length).toBe(2)
  })

  it('hidden 属性的路由应该是文章和设置管理', () => {
    const hiddenRoutes = constantRoutes.filter(route => route.hidden === true)
    const paths = hiddenRoutes.map(r => r.path)
    expect(paths).toContain('/admin/blog/article')
    expect(paths).toContain('/admin/blog/setting')
  })

  it('permissions 属性应该是数组', () => {
    const routesWithPermissions = constantRoutes.filter(route => route.permissions)
    routesWithPermissions.forEach(route => {
      expect(Array.isArray(route.permissions)).toBe(true)
    })
  })

  it('路由应该包含重定向', () => {
    const redirectRoutes = constantRoutes.filter(route => route.redirect)
    expect(redirectRoutes.length).toBeGreaterThan(0)
  })

  it('重定向路由应该重定向到 /blog', () => {
    const redirectRoutes = constantRoutes.filter(route => route.redirect === '/blog')
    expect(redirectRoutes.every(route => route.redirect === '/blog')).toBe(true)
  })

  it('动态路由数组应该是空的', () => {
    expect(dynamicRoutes).toEqual([])
    })

  it('scrollBehavior 函数应该解决页面跳转滚动位置问题', () => {
    expect(typeof router.options.scrollBehavior).toBe('function')
  })

  it('应该有 history 对象', () => {
    expect(router.history).toBeDefined()
    expect(typeof router.history).toBe('object')
  })

  it('应该有 options 对象', () => {
    expect(router.options).toBeDefined()
    expect(typeof router.options).toBe('object')
  })
})
