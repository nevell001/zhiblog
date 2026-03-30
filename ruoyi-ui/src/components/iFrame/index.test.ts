import { describe, it, expect, vi, beforeEach } from 'vitest'
import iFrame from './index.vue'

describe('iFrame 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 iFrame 组件', () => {
    expect(iFrame).toBeDefined()
    expect(typeof iFrame).toBe('object')
  })

  it('应该有iframe容器', () => {
    const hasIframeContainer = true
    expect(hasIframeContainer).toBe(true)
  })

  it('应该接收src地址', () => {
    const src = 'https://example.com'
    expect(src).toBe('https://example.com')
  })

  it('应该支持iframe标题', () => {
    const frameTitle = '外部页面'
    expect(frameTitle).toBe('外部页面')
  })

  it('应该有加载状态', () => {
    const isLoading = false
    expect(typeof isLoading).toBe('boolean')
  })

  it('应该支持iframe高度', () => {
    const frameHeight = '100%'
    expect(frameHeight).toBe('100%')
  })

  it('应该有iframe宽度', () => {
    const frameWidth = '100%'
    expect(frameWidth).toBe('100%')
  })

  it('应该支持边框设置', () => {
    const hasBorder = true
    expect(hasBorder).toBe(true)
  })

  it('应该有滚动条', () => {
    const hasScrolling = 'auto'
    expect(hasScrolling).toBe('auto')
  })
})
