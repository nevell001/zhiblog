import { describe, it, expect } from 'vitest'

describe('hasPermi 指令测试', () => {
  it('应该导出 hasPermi 指令', async () => {
    const module = await import('./hasPermi')
    expect(module.default).toBeDefined()
  })
})
