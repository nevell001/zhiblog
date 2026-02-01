import { describe, it, expect } from 'vitest'

describe('Modal plugin 测试', () => {
  it('应该导出 modal 插件', async () => {
    const module = await import('./modal')
    expect(module).toBeDefined()
  })
})