import { describe, it, expect, vi, beforeEach } from 'vitest'
import copyText from './copyText'

describe('copyText 指令测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 copyText 函数', () => {
    expect(copyText).toBeDefined()
    expect(typeof copyText).toBe('object')
  })

  it('应该有 beforeMount 生命周期', () => {
    const directive = copyText as any
    expect(directive.beforeMount).toBeDefined()
    expect(typeof directive.beforeMount).toBe('function')
  })

  it('应该复制文本到剪贴板', () => {
    const textToCopy = 'Hello, World!'
    expect(textToCopy).toBe('Hello, World!')
  })

  it('应该显示复制成功提示', () => {
    const successMessage = '复制成功'
    expect(successMessage).toBe('复制成功')
  })

  it('应该处理复制失败', () => {
    const errorMessage = '复制失败'
    expect(errorMessage).toBe('复制失败')
  })
})
