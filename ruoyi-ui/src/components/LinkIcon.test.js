import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import LinkIcon from '@/components/LinkIcon.vue'

// Mock SvgIcon 组件
const MockSvgIcon = {
  name: 'SvgIcon',
  props: ['iconClass'],
  template: '<div :data-icon="iconClass" @click="$emit(\'click\')"><slot /></div>',
  emits: ['click']
}

vi.mock('@/components/SvgIcon/index.vue', () => ({
  default: MockSvgIcon
}))

describe('LinkIcon 组件测试', () => {
  let windowOpenSpy

  beforeEach(() => {
    // Mock window.open
    windowOpenSpy = vi.spyOn(window, 'open').mockImplementation(() => {})
  })

  afterEach(() => {
    windowOpenSpy.mockRestore()
  })

  it('应该正确渲染组件', () => {
    const wrapper = mount(LinkIcon, {
      props: {
        iconClass: 'example-icon',
        url: 'https://example.com'
      },
      global: {
        components: {
          SvgIcon: MockSvgIcon
        }
      }
    })

    expect(wrapper.find('[data-icon="example-icon"]').exists()).toBe(true)
  })

  it('应该在点击时打开链接', async () => {
    const wrapper = mount(LinkIcon, {
      props: {
        iconClass: 'example-icon',
        url: 'https://example.com'
      },
      global: {
        components: {
          SvgIcon: MockSvgIcon
        }
      }
    })

    await wrapper.find('[data-icon="example-icon"]').trigger('click')

    expect(windowOpenSpy).toHaveBeenCalledWith('https://example.com')
  })

  it('应该正确传递 props', () => {
    const wrapper = mount(LinkIcon, {
      props: {
        iconClass: 'test-icon',
        url: 'https://test.com'
      },
      global: {
        components: {
          SvgIcon: MockSvgIcon
        }
      }
    })

    expect(wrapper.props().iconClass).toBe('test-icon')
    expect(wrapper.props().url).toBe('https://test.com')
  })

  it('应该处理空 URL', async () => {
    const wrapper = mount(LinkIcon, {
      props: {
        iconClass: 'test-icon',
        url: ''
      },
      global: {
        components: {
          SvgIcon: MockSvgIcon
        }
      }
    })

    await wrapper.find('[data-icon="test-icon"]').trigger('click')

    expect(windowOpenSpy).toHaveBeenCalledWith('')
  })
})