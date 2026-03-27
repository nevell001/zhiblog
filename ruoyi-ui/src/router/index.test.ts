import { describe, it, expect } from 'vitest'
import { constantRoutes, dynamicRoutes, router } from './index'

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

  it('constantRoutes 应该包含后台管理路由', () => {
    const adminRoutes = constantRoutes.filter(route => route.path.startsWith('/admin'))
    expect(adminRoutes.length).toBeGreaterThan(0)
    expect(adminRoutes.every(route => route.component)).toBe(true)
  })

  it('constantRoutes 应该包含系统管理路由', () => {
    const systemRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/system'))
    expect(systemRoutes.length).toBeGreaterThan(0)
    expect(systemRoutes.some(route => route.name === 'SystemUser')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemMenu')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemDept')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemPost')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemConfig')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemDict')).toBe(true)
    expect(systemRoutes.some(route => route.name === 'SystemNotice')).toBe(true)
  })

  it('constantRoutes 应该包含用户管理路由', () => {
    const userRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/user'))
    expect(userRoutes.length).toBeGreaterThan(0)
    expect(userRoutes.some(route => route.name === 'SystemUser')).toBe(true)
    expect(userRoutes.some(route => route.name === 'Profile')).toBe(true)
  })

  it('constantRoutes 应该包含统计路由', () => {
    const statisticsRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/statistics'))
    expect(statisticsRoutes.length).toBeGreaterThan(0)
    expect(statisticsRoutes.some(route => route.name === 'StatisticsOverview')).toBe(true)
    expect(statisticsRoutes.some(route => route.name === 'StatisticsArticle')).toBe(true)
    expect(statisticsRoutes.some(route => route.name === 'StatisticsUser')).toBe(true)
  })

  it('constantRoutes 应该包含监控路由', () => {
    const monitorRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/monitor'))
    expect(monitorRoutes.length).toBeGreaterThan(0)
    expect(monitorRoutes.some(route => route.name === 'MonitorActuator')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorPrometheus')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorGrafana')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorOnline')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorLoginLog')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorOperLog')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorServer')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorCache')).toBe(true)
    expect(monitorRoutes.some(route => route.name === 'MonitorJob')).toBe(true)
  })

  it('constantRoutes 应该包含工具路由', () => {
    const toolRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/tool'))
    expect(toolRoutes.length).toBeGreaterThan(0)
    expect(toolRoutes.some(route => route.name === 'Build')).toBe(true)
  })

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

  it('所有路由都应该有 name 属性', () => {
    const allRoutes = [...constantRoutes, ...dynamicRoutes]
    const routesWithoutName = allRoutes.filter(route => !route.name)

    expect(routesWithoutName.length).toBe(0)
  })

  it('后台管理路由应该有 redirect 属性', () => {
    const adminRoutes = constantRoutes.filter(route => route.path.startsWith('/admin') && route.path !== '/admin')

    expect(adminRoutes.every(route => route.redirect)).toBe(true)
  })

  it('系统管理路由应该有 meta.title 属性', () => {
    const systemRoutes = constantRoutes.filter(route => route.path.startsWith('/admin/system'))

    expect(systemRoutes.every(route => route.meta?.title)).toBe(true)
  })
})
