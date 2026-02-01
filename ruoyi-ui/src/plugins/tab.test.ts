import { describe, it, expect } from 'vitest'

describe('Tab plugin 测试', () => {
  it('应该导出 tab 插件', async () => {
    const module = await import('./tab')
    expect(module).toBeDefined()
  })
})