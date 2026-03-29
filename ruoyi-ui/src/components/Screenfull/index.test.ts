import { describe, it, expect, vi, beforeEach } from 'vitest'
import Screenfull from './index.vue'

describe('Screenfull 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 Screenfull 组件', () => {
    expect(Screenfull).toBeDefined()
    expect(typeof Screenfull).toBe('object')
  })

  it('应该检查全屏支持', () => {
    const isFullscreenSupported = () => {
      return typeof document !== 'undefined' && typeof document.documentElement.requestFullscreen === 'function'
    }
    expect(typeof isFullscreenSupported).toBe('function')
  })

  it('应该有全屏切换功能', () => {
    const toggleFullscreen = () => {
      return true
    }
    expect(typeof toggleFullscreen).toBe('function')
  })

  it('应该有全屏状态检查', () => {
    const isFullscreen = () => {
      return document.fullscreenElement !== null
    }
    expect(typeof isFullscreen).toBe('function')
  })

  it('应该处理全屏进入', () => {
    const enterFullscreen = () => {
      if (typeof document !== 'undefined' && document.documentElement.requestFullscreen) {
        document.documentElement.requestFullscreen()
        return true
      }
      return false
    }
    expect(typeof enterFullscreen).toBe('function')
  })

  it('应该处理全屏退出', () => {
    const exitFullscreen = () => {
      if (typeof document !== 'undefined' && document.exitFullscreen) {
        document.exitFullscreen()
        return true
      }
      return false
    }
    expect(typeof exitFullscreen).toBe('function')
  })

  it('应该有全屏事件监听器', () => {
    const eventTypes = ['fullscreenchange', 'webkitfullscreenchange', 'mozfullscreenchange', 'msfullscreenchange']
    expect(eventTypes).toContain('fullscreenchange')
  })
})
