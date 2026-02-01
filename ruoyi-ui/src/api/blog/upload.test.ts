import { describe, it, expect, vi } from 'vitest'
import { upload } from './upload'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Upload API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('upload', () => {
    it('应该导出 upload 函数', () => {
      expect(upload).toBeDefined()
      expect(typeof upload).toBe('function')
    })
  })
})