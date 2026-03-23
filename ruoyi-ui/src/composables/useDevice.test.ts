import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useDevice } from './useDevice'

describe('useDevice hook 测试', () => {
  const mockAddEventListener = vi.fn()
  const mockRemoveEventListener = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    // Mock window methods
    Object.defineProperty(window, 'innerWidth', {
      value: 1024,
      writable: true,
      configurable: true
    })
    Object.defineProperty(window, 'addEventListener', {
      value: mockAddEventListener,
      writable: true,
      configurable: true
    })
    Object.defineProperty(window, 'removeEventListener', {
      value: mockRemoveEventListener,
      writable: true,
      configurable: true
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该导出 useDevice 函数', () => {
    expect(useDevice).toBeDefined()
    expect(typeof useDevice).toBe('function')
  })

  it('应该在桌面窗口时返回 isDesktop 为 true', () => {
    Object.defineProperty(window, 'innerWidth', {
      value: 1200,
      writable: true,
      configurable: true
    })

    const { isDesktop, isMobile, isTablet } = useDevice()

    expect(isDesktop.value).toBe(true)
    expect(isMobile.value).toBe(false)
    expect(isTablet.value).toBe(false)
  })

  it('应该在移动端窗口时返回 isMobile 为 true', () => {
    Object.defineProperty(window, 'innerWidth', {
      value: 375,
      writable: true,
      configurable: true
    })

    const { isDesktop, isMobile, isTablet } = useDevice()

    expect(isDesktop.value).toBe(false)
    expect(isMobile.value).toBe(true)
    expect(isTablet.value).toBe(false)
  })

  it('应该在平板窗口时返回 isTablet 为 true', () => {
    Object.defineProperty(window, 'innerWidth', {
      value: 800,
      writable: true,
      configurable: true
    })

    const { isDesktop, isMobile, isTablet } = useDevice()

    expect(isDesktop.value).toBe(false)
    expect(isMobile.value).toBe(false)
    expect(isTablet.value).toBe(true)
  })

  it('应该在挂载时添加 resize 事件监听器', () => {
    useDevice()

    expect(mockAddEventListener).toHaveBeenCalledWith('resize', expect.any(Function))
  })

  it('应该在卸载时移除 resize 事件监听器', () => {
    const { unmount } = useDevice()
    if (typeof unmount === 'function') {
      unmount()
    }

    expect(mockRemoveEventListener).toHaveBeenCalledWith('resize', expect.any(Function))
  })
})
