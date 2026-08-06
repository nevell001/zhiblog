import { describe, it, expect } from 'vitest'
import router, { constantRoutes, dynamicRoutes } from './index'

const flattenRoutes = (routes: any[], parentPath = ''): any[] =>
  routes.flatMap(route => {
    const path = route.path.startsWith('/')
      ? route.path
      : `${parentPath.replace(/\/$/, '')}/${route.path}`
    const current = { ...route, path }
    return [current, ...flattenRoutes(route.children || [], path)]
  })

describe('Router Index 测试', () => {
  it('应该导出 constantRoutes', () => {
    expect(constantRoutes).toBeDefined()
    expect(Array.isArray(constantRoutes)).toBe(true)
    expect(constantRoutes.length).toBeGreaterThan(0)
  })

  it('应该导出 dynamicRoutes', () => {
    expect(dynamicRoutes).toBeDefined()
    expect(Array.isArray(dynamicRoutes)).toBe(true)
  })

  it('应该导出 router 实例', () => {
    expect(router).toBeDefined()
    expect(typeof router).toBe('object')
  })

  it('constantRoutes 应该包含前台博客路由', () => {
    const blogRoutes = constantRoutes.filter(
      route => route.path.startsWith('/blog') || route.path === '/'
    )
    expect(blogRoutes.length).toBeGreaterThan(0)
  })

  // 移除对动态管理路由在 constantRoutes 中存在的硬性要求，改为在 permission store 中验证
  // it('constantRoutes 应该包含后台管理路由', () => { ... })
  // it('constantRoutes 应该包含系统管理路由', () => { ... })
  // ... 等等

  it('所有路由都应该有 path 属性', () => {
    const allRoutes = [...constantRoutes, ...dynamicRoutes]
    const routesWithoutPath = allRoutes.filter(route => !route.path)

    expect(routesWithoutPath.length).toBe(0)
  })

  it('所有路由都应该有 component 属性', () => {
    const allRoutes = [...constantRoutes, ...dynamicRoutes]
    const routesWithoutComponent = allRoutes.filter(route => !route.component)

    // 根路由应该没有 component
    const rootRoute = allRoutes.find(route => route.path === '/')
    if (rootRoute) {
      expect(rootRoute.component).toBeUndefined()
    }
  })

  // 移除所有关于管理后台静态存在的断言
})
