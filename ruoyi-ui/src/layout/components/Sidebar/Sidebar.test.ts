import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Sidebar from './index.vue'

// Mock router and stores
vi.mock('@/router', () => ({
  useRouter: vi.fn()
}))
vi.mock('@/stores/app', () => ({
  useAppStore: vi.fn()
}))
vi.mock('@/stores/settings', () => ({
  useSettingsStore: vi.fn()
}))
vi.mock('@/stores/permission', () => ({
  usePermissionStore: vi.fn()
}))

describe('Sidebar 组件测试', () => {
  const mockUseRouter = vi.mocked(useRouter)
  const mockUseAppStore = vi.mocked(useAppStore)
  const mockUseSettingsStore = vi.mocked(useSettingsStore)
  const mockUsePermissionStore = vi.mocked(usePermissionStore)

  beforeEach(() => {
    vi.clearAllMocks()

    // Mock stores
    mockUseAppStore.mockReturnValue({
      sidebar: { opened: true },
      device: { isMobile: false, isTablet: false, isDesktop: true }
    })
    mockUseSettingsStore.mockReturnValue({
      sidebarLogo: true,
      sideTheme: 'theme-dark',
      theme: 'theme-dark',
      dynamicTitle: false
    })
    mockUsePermissionStore.mockReturnValue({
      sidebarRouters: [],
      permissions: ['system:user:list']
    })

    // Mock router
    mockUseRouter.mockReturnValue({
      push: vi.fn(),
      replace: vi.fn()
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该正确渲染组件', () => {
    const wrapper = mount(Sidebar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-scrollbar': true
        }
      }
    })

    expect(wrapper.find('.sidebar-container').exists()).toBe(true)
    expect(wrapper.find('.el-scrollbar').exists()).toBe(true)
  })

  it('应该显示 Logo', () => {
    const wrapper = mount(Sidebar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-scrollbar': true
        }
      }
    })

    expect(wrapper.find('logo').exists()).toBe(true)
  })

  it('应该渲染菜单项', () => {
    const mockRouters = [
      { path: '/dashboard', title: '首页', icon: 'House', hidden: false },
      { path: '/article', title: '文章管理', icon: 'Document', hidden: false }
    ]
    mockUsePermissionStore.mockReturnValue({
      sidebarRouters: mockRouters,
      permissions: ['system:user:list']
    })

    const wrapper = mount(Sidebar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-scrollbar': true
        }
      }
    })

    const menuItems = wrapper.findAll('.sidebar-item')
    expect(menuItems.length).toBe(2)
  })

  it('应该处理菜单点击', () => {
    const mockPush = vi.fn()
    mockUseRouter.mockReturnValue({
      push: mockPush
    })

    const wrapper = mount(Sidebar, {
      global: {
        stubs: {
          'el-tooltip': true,
          'el-scrollbar': true
        }
      }
    })

    const menuItems = wrapper.findAll('.sidebar-item')
    menuItems[0].trigger('click')

    expect(mockPush).toHaveBeenCalledWith('/dashboard')
  })
})
