import { describe, it, expect } from 'vitest'
import { formatDate, formatTime, getTime } from './index'

describe('Date 工具函数测试', () => {
  describe('formatDate', () => {
    it('应该导出 formatDate 函数', () => {
      expect(formatDate).toBeDefined()
      expect(typeof formatDate).toBe('function')
    })

    it('应该格式化日期', () => {
      const date = new Date('2024-01-01T00:00:00Z')
      const result = formatDate(date)
      expect(result).toBeDefined()
      expect(typeof result).toBe('string')
    })

    it('空值应该返回空字符串', () => {
      expect(formatDate(null as any)).toBe('')
      expect(formatDate('')).toBe('')
    })
  })

  describe('formatTime', () => {
    it('应该导出 formatTime 函数', () => {
      expect(formatTime).toBeDefined()
      expect(typeof formatTime).toBe('function')
    })

    it('应该格式化时间为相对时间', () => {
      const now = Date.now()
      const result = formatTime(now - 1000) // 1秒前
      expect(result).toBeDefined()
    })
  })

  describe('getTime', () => {
    it('应该导出 getTime 函数', () => {
      expect(getTime).toBeDefined()
      expect(typeof getTime).toBe('function')
    })

    it('应该返回时间或日期对象', () => {
      const resultStart = getTime('start')
      const resultEnd = getTime('end')
      expect(resultStart).toBeDefined()
      expect(resultEnd).toBeDefined()
    })
  })
})
