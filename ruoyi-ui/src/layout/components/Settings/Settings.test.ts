import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Settings from './index.vue'

// Mock stores
vi.mock('@/stores/settings', () => ({
  useSettingsStore: vi.fn()
}))
vi.mock('@/stores/permission', () => ({
  usePermissionStore: vi.fn()
}))

describe('Settings 组件测试', () => {
  const mockUseSettingsStore = vi.mocked(useSettingsStore)
  const mockUsePermissionStore = vi.mocked(usePermissionStore)

  beforeEach(() => {
    vi.clearAllMocks()

    // Mock store
    mockUseSettingsStore.mockReturnValue({
      sidebarLogo: true,
      sideTheme: 'theme-dark',
      theme: 'theme-dark'
      dynamicTitle: false
    })
    mockUsePermissionStore.mockReturnValue({
      permissions: ['system:user:edit']
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该正确渲染组件', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: {
          'el-drawer': true,
          'el-tooltip': true
        }
      }
    })

    expect(wrapper.find('.setting-drawer').exists()).toBe(true)
    expect(wrapper.find('.setting-drawer-title').exists()).toBe(true)
  })

  it('应该显示主题设置选项', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: {
          'el-drawer': true,
          'el-tooltip': true
        }
      }
    })

    const themeItems = wrapper.findAll('.setting-drawer-block-checbox-item')
    expect(themeItems.length).toBe(2)
  })

  it('应该处理主题切换', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: {
          'el-drawer': true,
          'el-tooltip': true
        }
      }
    })

    const themeOptions = wrapper.findAllComponents({ name: 'ElIcon' })
    const darkThemeIcon = themeOptions.find(icon => icon.classes().includes('moon'))

    if (darkThemeIcon) {
      await darkThemeIcon.trigger('click')
    }

    expect(mockUseSettingsStore().setTheme).toHaveBeenCalledWith('theme-dark')
  })

  it('应该在无权限时隐藏设置按钮', () => {
    mockUsePermissionStore.mockReturnValue({
      permissions: []
    })

    const wrapper = mount(Settings, {
      global: {
        stubs: {
          'el-drawer': true,
          'el-tooltip': true
        }
      }
    })

    const settingsBtn = wrapper.findAll('.setting-drawer-block-checbox-item')
    expect(settingsBtn.length).toBe(2)
  })
})
