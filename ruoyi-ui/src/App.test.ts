import { describe, it, expect } from 'vitest'

describe('App 组件测试', () => {
  it('应该导出 App 组件', async () => {
    const module = await import('./App.vue')
    expect(module.default).toBeDefined()
  })
})