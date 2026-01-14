import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAppStore } from '@/stores/app'

// Mock js-cookie
vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn(),
    set: vi.fn()
  }
}))

// Get the mocked module
const mockCookies = vi.mocked(require('js-cookie'))

describe('App Store 测试', () => {
  let appStore

  beforeEach(() => {
    setActivePinia(createPinia())
    appStore = useAppStore()
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('state 初始化', () => {
    it('应该正确初始化 sidebar 状态', () => {
      expect(appStore.sidebar).toHaveProperty('opened')
      expect(appStore.sidebar).toHaveProperty('withoutAnimation')
      expect(appStore.sidebar).toHaveProperty('hide')
    })

    it('应该正确初始化 device 状态', () => {
      expect(appStore.device).toBe('desktop')
    })

    it('应该正确初始化 size 状态', () => {
      expect(appStore.size).toBe('default')
    })

    it('应该从 Cookies 读取 sidebarStatus', () => {
      mockCookies.get.mockReturnValue('1')
      setActivePinia(createPinia())
      const newAppStore = useAppStore()
      expect(newAppStore.sidebar.opened).toBe(true)
    })

    it('应该从 Cookies 读取 size', () => {
      mockCookies.get.mockReturnValue('large')
      setActivePinia(createPinia())
      const newAppStore = useAppStore()
      expect(newAppStore.size).toBe('large')
    })
  })

  describe('toggleSideBar action', () => {
    it('应该切换侧边栏状态', () => {
      appStore.sidebar.opened = true
      appStore.toggleSideBar()
      expect(appStore.sidebar.opened).toBe(false)
      expect(appStore.sidebar.withoutAnimation).toBe(false)
    })

    it('应该在侧边栏打开时设置 Cookie 为 1', () => {
      appStore.sidebar.opened = false
      appStore.toggleSideBar()
      expect(mockCookies.set).toHaveBeenCalledWith('sidebarStatus', 1)
    })

    it('应该在侧边栏关闭时设置 Cookie 为 0', () => {
      appStore.sidebar.opened = true
      appStore.toggleSideBar()
      expect(mockCookies.set).toHaveBeenCalledWith('sidebarStatus', 0)
    })

    it('应该重置 withoutAnimation 为 false', () => {
      appStore.sidebar.withoutAnimation = true
      appStore.toggleSideBar()
      expect(appStore.sidebar.withoutAnimation).toBe(false)
    })
  })

  describe('closeSideBar action', () => {
    it('应该关闭侧边栏', () => {
      appStore.sidebar.opened = true
      appStore.closeSideBar(false)
      expect(appStore.sidebar.opened).toBe(false)
    })

    it('应该设置 sidebarStatus Cookie 为 0', () => {
      appStore.closeSideBar(false)
      expect(mockCookies.set).toHaveBeenCalledWith('sidebarStatus', 0)
    })

    it('应该设置 withoutAnimation 状态', () => {
      appStore.closeSideBar(true)
      expect(appStore.sidebar.withoutAnimation).toBe(true)

      appStore.closeSideBar(false)
      expect(appStore.sidebar.withoutAnimation).toBe(false)
    })
  })

  describe('toggleDevice action', () => {
    it('应该切换设备类型', () => {
      appStore.toggleDevice('mobile')
      expect(appStore.device).toBe('mobile')

      appStore.toggleDevice('desktop')
      expect(appStore.device).toBe('desktop')
    })

    it('应该支持所有设备类型', () => {
      const devices = ['desktop', 'mobile', 'tablet']
      devices.forEach(device => {
        appStore.toggleDevice(device)
        expect(appStore.device).toBe(device)
      })
    })
  })

  describe('setSize action', () => {
    it('应该设置尺寸并保存到 Cookie', () => {
      appStore.setSize('large')
      expect(appStore.size).toBe('large')
      expect(mockCookies.set).toHaveBeenCalledWith('size', 'large')
    })

    it('应该支持所有尺寸选项', () => {
      const sizes = ['default', 'small', 'medium', 'large']
      sizes.forEach(size => {
        appStore.setSize(size)
        expect(appStore.size).toBe(size)
        expect(mockCookies.set).toHaveBeenCalledWith('size', size)
      })
    })
  })

  describe('toggleSideBarHide action', () => {
    it('应该设置侧边栏隐藏状态', () => {
      appStore.toggleSideBarHide(true)
      expect(appStore.sidebar.hide).toBe(true)

      appStore.toggleSideBarHide(false)
      expect(appStore.sidebar.hide).toBe(false)
    })

    it('应该支持布尔值切换', () => {
      appStore.sidebar.hide = false
      appStore.toggleSideBarHide(true)
      expect(appStore.sidebar.hide).toBe(true)

      appStore.toggleSideBarHide(false)
      expect(appStore.sidebar.hide).toBe(false)
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的侧边栏操作流程', () => {
      // 初始状态：侧边栏打开
      expect(appStore.sidebar.opened).toBe(true)

      // 关闭侧边栏
      appStore.closeSideBar(false)
      expect(appStore.sidebar.opened).toBe(false)
      expect(mockCookies.set).toHaveBeenCalledWith('sidebarStatus', 0)

      // 切换侧边栏
      appStore.toggleSideBar()
      expect(appStore.sidebar.opened).toBe(true)
      expect(mockCookies.set).toHaveBeenCalledWith('sidebarStatus', 1)

      // 隐藏侧边栏
      appStore.toggleSideBarHide(true)
      expect(appStore.sidebar.hide).toBe(true)
    })

    it('应该支持完整的响应式切换流程', () => {
      // 桌面端
      appStore.toggleDevice('desktop')
      expect(appStore.device).toBe('desktop')

      // 移动端
      appStore.toggleDevice('mobile')
      expect(appStore.device).toBe('mobile')

      // 设置大尺寸
      appStore.setSize('large')
      expect(appStore.size).toBe('large')
      expect(mockCookies.set).toHaveBeenCalledWith('size', 'large')
    })
  })
})