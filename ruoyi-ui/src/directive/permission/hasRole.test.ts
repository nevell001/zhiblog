import { describe, it, expect } from 'vitest'

describe('hasRole 指令测试', () => {
  it('应该导出 hasRole 指令', async () => {
    const module = await import('./hasRole')
    expect(module.default).toBeDefined()
  })
})