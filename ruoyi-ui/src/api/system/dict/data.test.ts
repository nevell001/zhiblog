import { describe, it, expect, vi } from 'vitest'
import { listData, getData, addData, updateData, delData } from './data'

vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Dict Data API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listData', () => {
    it('应该导出 listData 函数', () => {
      expect(listData).toBeDefined()
      expect(typeof listData).toBe('function')
    })
  })

  describe('getData', () => {
    it('应该导出 getData 函数', () => {
      expect(getData).toBeDefined()
      expect(typeof getData).toBe('function')
    })
  })

  describe('addData', () => {
    it('应该导出 addData 函数', () => {
      expect(addData).toBeDefined()
      expect(typeof addData).toBe('function')
    })
  })

  describe('updateData', () => {
    it('应该导出 updateData 函数', () => {
      expect(updateData).toBeDefined()
      expect(typeof updateData).toBe('function')
    })
  })

  describe('delData', () => {
    it('应该导出 delData 函数', () => {
      expect(delData).toBeDefined()
      expect(typeof delData).toBe('function')
    })
  })
})
