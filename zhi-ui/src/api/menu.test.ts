import { describe, it, expect, vi } from 'vitest'
import { getRouters } from './menu'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Menu API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getRouters', () => {
    it('应该导出 getRouters 函数', () => {
      expect(getRouters).toBeDefined()
      expect(typeof getRouters).toBe('function')
    })
  })
})
