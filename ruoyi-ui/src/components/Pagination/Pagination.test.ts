import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Pagination from './index.vue'

// Mock scrollTo function
vi.mock('@/utils/scroll-to', () => ({
  scrollTo: vi.fn()
}))

const mockScrollTo = vi.fn()

beforeEach(() => {
  vi.clearAllMocks()
  mockScrollTo.mockClear()
})

afterEach(() => {
  vi.restoreAllMocks()
})

describe('Pagination 组件测试', () => {
  it('应该正确渲染组件', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100
      }
    })

    expect(wrapper.find('.pagination-container').exists()).toBe(true)
  })

  it('应该显示默认的分页大小', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100
      }
    })

    const pagination = wrapper.findComponent({ name: 'ElPagination' })
    expect(pagination.exists()).toBe(true)
    expect(pagination.props('pageSizes')).toEqual([10, 20, 30, 50])
  })

  it('应该计算当前页和每页大小', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        page: 2
      }
    })

    expect(wrapper.vm.currentPage).toBe(2)
    expect(wrapper.vm.pageSize).toBe(20)
  })

  it('应该处理自定义限制', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        limit: 30
      }
    })

    expect(wrapper.vm.currentPage).toBe(1)
    expect(wrapper.vm.pageSize).toBe(30)
  })

  it('应该在页大小变化时重置当前页', async () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        page: 5,
        limit: 20
      }
    })

    expect(wrapper.vm.currentPage).toBe(5)
    
    await wrapper.vm.handleSizeChange(10)

    expect(wrapper.vm.currentPage).toBe(1)
    expect(wrapper.vm.pageSize).toBe(10)
  })

  it('应该在页码变化时发送更新事件', async () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100
      }
    })

    await wrapper.vm.handleCurrentChange(3)

    expect(wrapper.emitted('update:page')).toBeTruthy()
  })

  it('应该在页大小变化时发送限制更新事件', async () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100
      }
    })

    await wrapper.vm.handleSizeChange(30)

    expect(wrapper.emitted('update:limit')).toBeTruthy()
  })

  it('应该在启用自动滚动时调用 scrollTo', async () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        autoScroll: true
      }
    })

    await wrapper.vm.handleCurrentChange(1)

    expect(mockScrollTo).toHaveBeenCalledWith(0, 800)
  })

  it('应该支持隐藏状态', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        hidden: true
      }
    })

    expect(wrapper.find('.pagination-container').classes()).toContain('hidden')
  })

  it('应该支持自定义布局', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        layout: 'prev, pager, next'
      }
    })

    const pagination = wrapper.findComponent({ name: 'ElPagination' })
    expect(pagination.props('layout')).toBe('prev, pager, next')
  })

  it('应该支持隐藏背景', () => {
    const wrapper = mount(Pagination, {
      props: {
        total: 100,
        background: false
      }
    })

    const pagination = wrapper.findComponent({ name: 'ElPagination' })
    expect(pagination.props('background')).toBe(false)
  })
})
