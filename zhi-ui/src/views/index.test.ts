import { describe, it, expect } from 'vitest'

describe('Views Index 组件测试', () => {
  it('应该导出 Index 组件', async () => {
    const module = await import('./index.vue')
    expect(module.default).toBeDefined()
  })
})
