import { describe, it, expect } from 'vitest'

describe('Permission 模块测试', () => {
  it('应该导出 permission 模块', async () => {
    const module = await import('./permission')
    expect(module).toBeDefined()
  })
})
