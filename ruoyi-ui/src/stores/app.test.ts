import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import Cookies from 'js-cookie'
import { useAppStore } from './app'

// Mock Cookies
vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn(() => undefined),
    set: vi.fn()
  }
}))

describe('App Store 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    setActivePinia(createPinia())
  })

  describe('初始状态', () => {
    it('应该初始化为默认值', () => {
      const store = useAppStore()
      expect(store.sidebar).toHaveProperty('opened')
      expect(store.sidebar).toHaveProperty('withoutAnimation')
      expect(store.sidebar).toHaveProperty('hide')
      expect(store.device).toBe('desktop')
      expect(store.size).toBeDefined()
    })
  })

  describe('sidebar', () => {
    it('应该切换侧边栏状态', () => {
      const store = useAppStore()
      const initialOpened = store.sidebar.opened
      store.toggleSideBar()
      expect(store.sidebar.opened).toBe(!initialOpened)
    })

    it('应该关闭侧边栏', () => {
      const store = useAppStore()
      store.closeSideBar({ withoutAnimation: true })
      expect(store.sidebar.opened).toBe(false)
      expect(store.sidebar.withoutAnimation).toBe(true)
    })
  })

  describe('device', () => {
    it('应该设置设备类型', () => {
      const store = useAppStore()
      store.toggleDevice('mobile')
      expect(store.device).toBe('mobile')
    })
  })

  describe('size', () => {
    it('应该设置组件尺寸', () => {
      const store = useAppStore()
      store.setSize('large')
      expect(store.size).toBe('large')
    })

    it('应该支持所有尺寸类型', () => {
      const store = useAppStore()
      const sizes = ['large', 'default', 'small'] as const
      sizes.forEach(size => {
        store.setSize(size)
        expect(store.size).toBe(size)
      })
    })
  })

  describe('toggleSideBarHide', () => {
    it('应该切换侧边栏隐藏状态', () => {
      const store = useAppStore()
      store.toggleSideBarHide(true)
      expect(store.sidebar.hide).toBe(true)
    })
  })
})