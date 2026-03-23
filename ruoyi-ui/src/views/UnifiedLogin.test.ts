import { describe, it, expect } from 'vitest'

describe('UnifiedLogin 组件测试', () => {
  it('应该导出 UnifiedLogin 组件', async () => {
    const module = await import('./UnifiedLogin.vue')
    expect(module.default).toBeDefined()
  })
})
