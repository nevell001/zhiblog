import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import SizeSelect from './index.vue'
import { useAppStore } from '@/stores/app'

// Mock Pinia store
const createTestingPinia = () => {
  const pinia = createPinia()
  setActivePinia(pinia)
  return pinia
}

describe('SizeSelect 组件测试', () => {
  let pinia: ReturnType<typeof createTestingPinia>
  const mountSizeSelect = () =>
    mount(SizeSelect, {
      global: {
        plugins: [pinia],
        mocks: {
          $modal: {
            loading: vi.fn()
          }
        },
        stubs: {
          SvgIcon: {
            name: 'SvgIcon',
            template: '<i class="svg-icon-stub" />'
          },
          ElDropdown: {
            name: 'ElDropdown',
            template: '<div class="el-dropdown-stub"><slot /><slot name="dropdown" /></div>'
          },
          ElDropdownMenu: {
            name: 'ElDropdownMenu',
            template: '<div class="el-dropdown-menu-stub"><slot /></div>'
          },
          ElDropdownItem: {
            name: 'ElDropdownItem',
            props: ['disabled', 'command'],
            template: '<button class="el-dropdown-item-stub"><slot /></button>'
          }
        }
      }
    })

  beforeEach(() => {
    pinia = createTestingPinia()
    vi.clearAllMocks()
  })

  it('应该正确渲染组件', () => {
    const wrapper = mountSizeSelect()

    expect(wrapper.find('.size-icon--style').exists()).toBe(true)
  })

  it('应该显示大小选项下拉菜单', () => {
    const wrapper = mountSizeSelect()

    expect(wrapper.findComponent({ name: 'ElDropdown' }).exists()).toBe(true)
    expect(wrapper.findComponent({ name: 'ElDropdownMenu' }).exists()).toBe(true)
  })

  it('应该包含三个大小选项', () => {
    const wrapper = mountSizeSelect()

    const dropdownItems = wrapper.findAllComponents({ name: 'ElDropdownItem' })
    expect(dropdownItems.length).toBe(3)
  })

  it('应该禁用当前选中的大小选项', () => {
    const appStore = useAppStore()
    appStore.setSize('large')

    const wrapper = mountSizeSelect()

    const dropdownItems = wrapper.findAllComponents({ name: 'ElDropdownItem' })
    // 第一个选项（large）应该被禁用
    expect(dropdownItems[0].props('disabled')).toBe(true)
    // 其他选项应该可用
    expect(dropdownItems[1].props('disabled')).toBe(false)
    expect(dropdownItems[2].props('disabled')).toBe(false)
  })
})
