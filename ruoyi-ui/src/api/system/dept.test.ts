import { describe, it, expect, vi } from 'vitest'
import { listDept, getDept, addDept, updateDept, delDept } from './dept'

vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Dept API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listDept', () => {
    it('应该导出 listDept 函数', () => {
      expect(listDept).toBeDefined()
      expect(typeof listDept).toBe('function')
    })
  })

  describe('getDept', () => {
    it('应该导出 getDept 函数', () => {
      expect(getDept).toBeDefined()
      expect(typeof getDept).toBe('function')
    })
  })

  describe('addDept', () => {
    it('应该导出 addDept 函数', () => {
      expect(addDept).toBeDefined()
      expect(typeof addDept).toBe('function')
    })
  })

  describe('updateDept', () => {
    it('应该导出 updateDept 函数', () => {
      expect(updateDept).toBeDefined()
      expect(typeof updateDept).toBe('function')
    })
  })

  describe('delDept', () => {
    it('应该导出 delDept 函数', () => {
      expect(delDept).toBeDefined()
      expect(typeof delDept).toBe('function')
    })
  })
})