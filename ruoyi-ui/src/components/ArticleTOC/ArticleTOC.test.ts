import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleTOC from './index.vue'

// Mock window methods
const mockGetBoundingClientRect = vi.fn()
const mockQuerySelector = vi.fn()
const mockQuerySelectorAll = vi.fn()
const mockAddEventListener = vi.fn()
const mockRemoveEventListener = vi.fn()
const mockScrollTo = vi.fn()
const mockGetElementById = vi.fn()

beforeEach(() => {
  vi.clearAllMocks()

  // Mock document methods
  Object.defineProperty(document, 'querySelector', {
    value: mockQuerySelector,
    writable: true,
    configurable: true
  })
  Object.defineProperty(document, 'querySelectorAll', {
    value: mockQuerySelectorAll,
    writable: true,
    configurable: true
  })
  Object.defineProperty(document, 'getElementById', {
    value: mockGetElementById,
    writable: true,
    configurable: true
  })
  Object.defineProperty(document, 'createElement', {
    value: vi.fn(() => ({ querySelectorAll: vi.fn() })),
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
  Object.defineProperty(window, 'scrollTo', {
    value: mockScrollTo,
    writable: true,
    configurable: true
  })
})

afterEach(() => {
  vi.restoreAllMocks()
})

describe('ArticleTOC 组件测试', () => {
  it('应该正确渲染组件', () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题1</h1><h2>标题2</h2>'
      }
    })

    expect(wrapper.find('.article-toc').exists()).toBe(true)
    expect(wrapper.find('.toc-header').exists()).toBe(true)
    expect(wrapper.find('.toc-title').exists()).toBe(true)
  })

  it('应该显示切换按钮', () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    expect(wrapper.find('.toc-toggle').exists()).toBe(true)
  })

  it('应该在没有内容时显示空状态', () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: ''
      }
    })

    expect(wrapper.find('.toc-empty').exists()).toBe(true)
    expect(wrapper.find('.toc-empty p').text()).toBe('暂无目录')
  })

  it('应该在有内容时生成目录项', () => {
    const mockHeadingElements = [
      { tagName: 'H1', textContent: '标题1' },
      { tagName: 'H2', textContent: '标题2' },
      { tagName: 'H3', textContent: '标题3' }
    ]
    mockQuerySelectorAll.mockReturnValue(mockHeadingElements)

    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题1</h1><h2>标题2</h2>'
      }
    })

    expect(wrapper.findAll('.toc-item').length).toBe(3)
  })

  it('应该根据标题级别设置正确的 class', () => {
    const mockHeadingElements = [
      { tagName: 'H1', textContent: '标题1' },
      { tagName: 'H2', textContent: '标题2' },
      { tagName: 'H3', textContent: '标题3' },
      { tagName: 'H4', textContent: '标题4' },
      { tagName: 'H5', textContent: '标题5' },
      { tagName: 'H6', textContent: '标题6' }
    ]
    mockQuerySelectorAll.mockReturnValue(mockHeadingElements)

    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题1</h1><h2>标题2</h2><h3>标题3</h3>'
      }
    })

    const tocItems = wrapper.findAll('.toc-item')
    expect(tocItems[0].classes()).toContain('toc-level-1')
    expect(tocItems[1].classes()).toContain('toc-level-2')
    expect(tocItems[2].classes()).toContain('toc-level-3')
  })

  it('应该生成唯一的 ID', () => {
    const mockHeadingElements = [
      { tagName: 'H1', textContent: '重复标题' },
      { tagName: 'H1', textContent: '重复标题' },
      { tagName: 'H2', textContent: '唯一标题' }
    ]
    mockQuerySelectorAll.mockReturnValue(mockHeadingElements)

    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>重复标题</h1><h1>重复标题</h1><h2>唯一标题</h2>'
      }
    })

    const tocItems = wrapper.findAll('.toc-item')
    const ids = tocItems.map(item => item.attributes('id'))
    expect(new Set(ids).size).toBeGreaterThan(2)
  })

  it('应该滚动到指定的标题', async () => {
    const mockElement = {
      getBoundingClientRect: vi.fn(() => ({ top: 100 })),
      id: 'heading-1-1'
    }
    mockGetElementById.mockReturnValue(mockElement)

    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题1</h1>'
      }
    })

    await wrapper.vm.scrollToHeading('heading-1-1')

    expect(mockGetElementById).toHaveBeenCalledWith('heading-1-1')
    expect(mockElement.getBoundingClientRect).toHaveBeenCalled()
    expect(mockScrollTo).toHaveBeenCalledWith({
      top: expect.any(Number),
      behavior: 'smooth'
    })
  })

  it('应该添加滚动事件监听器', () => {
    mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    expect(mockAddEventListener).toHaveBeenCalledWith('scroll', expect.any(Function))
  })

  it('应该在卸载时移除事件监听器', () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    wrapper.unmount()

    expect(mockRemoveEventListener).toHaveBeenCalledWith('scroll', expect.any(Function))
  })

  it('应该正确显示切换图标', () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    const toggleIcon = wrapper.find('.toc-toggle')
    expect(toggleIcon.exists()).toBe(true)
    expect(toggleIcon.attributes('icon')).toBe(expect.any(String))
  })

  it('应该切换展开/收起状态', async () => {
    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    expect(wrapper.vm.isExpanded).toBe(true)
    await wrapper.vm.toggleExpand()
    expect(wrapper.vm.isExpanded).toBe(false)
    await wrapper.vm.toggleExpand()
    expect(wrapper.vm.isExpanded).toBe(true)
  })

  it('应该在标题进入视口时设置为活动状态', async () => {
    const mockWindow = {
      pageYOffset: 100,
      innerHeight: 800
    }
    Object.defineProperty(window, 'pageYOffset', {
      value: mockWindow.pageYOffset,
      writable: true,
      configurable: true
    })
    Object.defineProperty(window, 'innerHeight', {
      value: mockWindow.innerHeight,
      writable: true,
      configurable: true
    })

    const wrapper = mount(ArticleTOC, {
      props: {
        content: '<h1>标题</h1>'
      }
    })

    await wrapper.vm.checkActiveHeading()

    expect(wrapper.vm.activeIndex).toBe(0)
  })
})
