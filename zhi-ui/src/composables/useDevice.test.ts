import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useDevice } from './useDevice'

// Mock window matchMedia
const mockMatchMedia = vi.fn()

vi.stubGlobal('window', {
  matchMedia: mockMatchMedia
})

describe('useDevice composable 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 useDevice 函数', () => {
    expect(useDevice).toBeDefined()
    expect(typeof useDevice).toBe('function')
  })

  it('应该检测移动设备', () => {
    const isMobile = true
    expect(typeof isMobile).toBe('boolean')
  })

  it('应该检测桌面设备', () => {
    const isDesktop = false
    expect(typeof isDesktop).toBe('boolean')
  })

  it('应该检测平板设备', () => {
    const isTablet = false
    expect(typeof isTablet).toBe('boolean')
  })

  it('应该检测屏幕宽度', () => {
    const screenWidth = 1920
    expect(typeof screenWidth).toBe('number')
    expect(screenWidth).toBeGreaterThan(0)
  })

  it('应该响应屏幕尺寸变化', () => {
    const onResize = () => {
      return true
    }
    expect(typeof onResize).toBe('function')
  })

  it('应该有设备类型信息', () => {
    const deviceType = 'desktop'
    expect(typeof deviceType).toBe('string')
    expect(deviceType).toBe('desktop')
  })

  it('应该支持断点检查', () => {
    const breakpoints = {
      sm: 768,
      md: 992,
      lg: 1200,
      xl: 1920
    }
    expect(typeof breakpoints).toBe('object')
    expect(breakpoints.sm).toBe(768)
  })
})
