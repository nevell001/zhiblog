import { describe, it, expect } from 'vitest'

describe('Register 组件测试', () => {
  it('应该导出 Register 组件', async () => {
    const module = await import('./register.vue')
    expect(module.default).toBeDefined()
  })
})