import { describe, it, expect, beforeEach, vi } from 'vitest'
import { scrollTo } from './scroll-to'

// Mock window and document
const mockScrollTop: any = {
  value: 0
}

vi.stubGlobal('window', {
  requestAnimationFrame: vi.fn((cb: Function) => setTimeout(cb, 16)),
  webkitRequestAnimationFrame: vi.fn((cb: Function) => setTimeout(cb, 16)),
  mozRequestAnimationFrame: vi.fn((cb: Function) => setTimeout(cb, 16)),
  setTimeout: vi.fn((cb: Function, delay: number) => setTimeout(cb, delay))
})

vi.stubGlobal('document', {
  documentElement: {
    get scrollTop() {
      return mockScrollTop.value
    },
    set scrollTop(val: number) {
      mockScrollTop.value = val
    }
  },
  body: {
    parentNode: {
      scrollTop: 0
    },
    get scrollTop() {
      return mockScrollTop.value
    },
    set scrollTop(val: number) {
      mockScrollTop.value = val
    }
  }
})

describe('scroll-to 工具测试', () => {
  beforeEach(() => {
    mockScrollTop.value = 0
    vi.useFakeTimers()
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  describe('scrollTo', () => {
    it('应该存在 scrollTo 函数', () => {
      expect(scrollTo).toBeDefined()
      expect(typeof scrollTo).toBe('function')
    })

    it('应该平滑滚动到指定位置', (done) => {
      const callback = vi.fn()
      scrollTo(500, 100, callback)

      vi.advanceTimersByTime(120)

      setTimeout(() => {
        expect(mockScrollTop.value).toBe(500)
        expect(callback).toHaveBeenCalled()
        done()
      }, 10)
    })

    it('应该使用默认持续时间 500ms', (done) => {
      const callback = vi.fn()
      scrollTo(200, undefined, callback)

      vi.advanceTimersByTime(520)

      setTimeout(() => {
        expect(callback).toHaveBeenCalled()
        done()
      }, 10)
    })

    it('应该滚动到 0 位置', (done) => {
      mockScrollTop.value = 1000
      const callback = vi.fn()
      scrollTo(0, 200, callback)

      vi.advanceTimersByTime(220)

      setTimeout(() => {
        expect(mockScrollTop.value).toBe(0)
        expect(callback).toHaveBeenCalled()
        done()
      }, 10)
    })

    it('应该在不提供回调时正常工作', (done) => {
      scrollTo(300, 200)

      vi.advanceTimersByTime(220)

      setTimeout(() => {
        expect(mockScrollTop.value).toBe(300)
        done()
      }, 10)
    })
  })

  describe('Math.easeInOutQuad', () => {
    it('应该在 t < d/2 时返回正确的缓动值', () => {
      const result = (Math as any).easeInOutQuad(10, 0, 100, 100)
      expect(result).toBeGreaterThan(0)
      expect(result).toBeLessThan(100)
    })

    it('应该在 t >= d/2 时返回正确的缓动值', () => {
      const result = (Math as any).easeInOutQuad(75, 0, 100, 100)
      expect(result).toBeGreaterThan(0)
      expect(result).toBeLessThan(100)
    })
  })
})
