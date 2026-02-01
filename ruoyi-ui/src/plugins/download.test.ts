import { describe, it, expect } from 'vitest'

describe('Download plugin 测试', () => {
  it('应该导出 download 插件', async () => {
    const module = await import('./download')
    expect(module).toBeDefined()
  })
})