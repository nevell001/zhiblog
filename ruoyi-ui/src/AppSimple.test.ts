import { describe, it, expect } from 'vitest'

describe('AppSimple 组件测试', () => {
  it('应该导出 AppSimple 组件', async () => {
    const module = await import('./AppSimple.vue')
    expect(module.default).toBeDefined()
  })
})
