import { describe, it, expect } from 'vitest'
import icons from './requireIcons'

describe('requireIcons 测试', () => {
  it('应该导出 icons 数组', () => {
    expect(icons).toBeDefined()
    expect(Array.isArray(icons)).toBe(true)
  })

  it('icons 数组应该包含 SVG 图标文件名', () => {
    expect(icons.length).toBeGreaterThan(0)
    icons.forEach(icon => {
      expect(typeof icon).toBe('string')
      expect(icon.endsWith('.svg')).toBe(true)
      expect(icon).toMatch(/^[a-zA-Z0-9\-_]+$/)
    })
  })

  it('icons 数组应该去重', () => {
    const hasDuplicates = icons.some((item, index) => 
      icons.indexOf(item) !== index
    )
    expect(hasDuplicates).toBe(false)
  })
})
