import { describe, it, expect } from 'vitest'

describe('Router 配置测试', () => {
  it('应该导出 router', async () => {
    const module = await import('./index')
    expect(module.default).toBeDefined()
    expect(module.default).toHaveProperty('push')
    expect(module.default).toHaveProperty('replace')
  })

  it('应该导出 constantRoutes', async () => {
    const module = await import('./index')
    expect(module.constantRoutes).toBeDefined()
    expect(Array.isArray(module.constantRoutes)).toBe(true)
  })

  it('应该导出 dynamicRoutes', async () => {
    const module = await import('./index')
    expect(module.dynamicRoutes).toBeDefined()
    expect(Array.isArray(module.dynamicRoutes)).toBe(true)
  })
})