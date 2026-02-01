import { describe, it, expect } from 'vitest'

describe('Login 组件测试', () => {
  it('应该导出 Login 组件', async () => {
    const module = await import('./login.vue')
    expect(module.default).toBeDefined()
  })
})