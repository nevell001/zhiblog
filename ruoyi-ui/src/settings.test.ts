import { describe, it, expect } from 'vitest'

describe('Settings 配置测试', () => {
  it('应该导出 settings 模块', async () => {
    const module = await import('./settings')
    expect(module).toBeDefined()
  })
})