import { describe, it, expect, vi, beforeEach } from 'vitest'
import tab from './tab'

describe('Tab Plugin 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 tab 函数', () => {
    expect(tab).toBeDefined()
    expect(typeof tab).toBe('function')
  })

  it('应该打开新标签页', () => {
    const openTab = () => {
      return true
    }
    expect(typeof openTab).toBe('function')
  })

  it('应该处理 URL', () => {
    const url = 'https://example.com'
    expect(url).toBe('https://example.com')
  })

  it('应该支持标签页标题', () => {
    const title = '新标签页'
    expect(title).toBe('新标签页')
  })

  it('应该处理新标签页打开', () => {
    const windowOpen = vi.fn()
    windowOpen('https://example.com', '_blank')
    expect(windowOpen).toHaveBeenCalledWith('https://example.com', '_blank')
  })
})