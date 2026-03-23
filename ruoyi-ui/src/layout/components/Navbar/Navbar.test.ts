import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Navbar from './index.vue'

// Mock router and stores
vi.mock('@/router', () => ({
  useRouter: vi.fn(),
  useRoute: vi.fn()
}))
vi.mock('@/stores/app', () => ({
  useAppStore: vi.fn()
}))
vi.mock('@/stores/settings', () => ({
  useSettingsStore: vi.fn()
}))
vi.mock('@/stores/user', () => ({
  useUserStore: vi.fn()
}))

describe('Navbar 组件测试', () => {
  const mockUseRouter = vi.mocked(useRouter)
  const mockUseRoute = vi.mocked(useRoute)
  const mockUseAppStore = vi.mocked(useAppStore)
  const mockUseSettingsStore = vi.mocked(useSettingsStore)
  const mockUseUserStore = vi.mocked(useUserStore)

  beforeEach(() => {
    vi.clearAllMocks()

    // Mock stores
    mockUseAppStore.mockReturnValue({
      sidebar: { opened: false },
      size: 'default',
      device: { isMobile: false, isTablet: false, isDesktop: true },
      topNav: false
    })
    mockUseSettingsStore.mockReturnValue({
      topNav: false,
      isDark: false,
      dynamicTitle: false
    })
    mockUseUserStore.mockReturnValue({
      name: 'testuser',
      token: 'test-token',
      roles: ['admin'],
      permissions: []
    })

    // Mock router
    mockUseRouter.mockReturnValue({
      push: vi.fn(),
      replace: vi.fn()
    })
    mockUseRoute.mockReturnValue({
      path: '/',
      query: {}
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该正确渲染组件', () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    expect(wrapper.find('.navbar').exists()).toBe(true)
    expect(wrapper.find('#hamburger-container').exists()).toBe(true)
    expect(wrapper.find('#breadcrumb-container').exists()).toBe(false)
  })

  it('应该显示汉堡菜单图标', () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    expect(wrapper.find('#hamburger-container').exists()).toBe(true)
  })

  it('应该点击汉堡菜单切换侧边栏', async () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    await wrapper.vm.toggleSideBar()

    expect(mockUseAppStore().sidebar.opened).toHaveBeenCalled()
  })

  it('应该根据设备类型渲染不同的菜单', () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    expect(wrapper.find('.right-menu').exists()).toBe(true)
    expect(wrapper.find('#header-search').exists()).toBe(false)
    expect(wrapper.find('#topmenu-container').exists()).toBe(false)
  })

  it('应该显示面包屑当 topNav 开启时', () => {
    mockUseSettingsStore.mockReturnValue({
      topNav: true
    })
    
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    expect(wrapper.find('#breadcrumb-container').exists()).toBe(true)
  })

  it('应该处理主题切换', async () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    await wrapper.vm.toggleTheme()

    expect(mockUseSettingsStore().setDark).toHaveBeenCalled()
  })

  it('应该处理全屏切换', async () => {
    const wrapper = mount(Navbar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-dropdown': true,
          'el-dropdown-menu': true
        }
      }
    })

    const screenfullBtn = wrapper.findAllComponents({ name: 'ElIcon' })
    const fullscreenBtn = screenfullBtn.find(icon => icon.attributes('id') === 'screenfull')
    
    if (fullscreenBtn) {
      await fullscreenBtn.trigger('click')
    }
  })
})
