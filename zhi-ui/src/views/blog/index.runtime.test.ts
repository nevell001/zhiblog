import { describe, expect, it, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { createMemoryHistory, createRouter } from 'vue-router'
import BlogHome from './index.vue'
import { useUserStore } from '@/stores/user'

vi.mock('@/api/blog', () => ({
  getArticleList: vi.fn().mockResolvedValue({ rows: [], total: 0 }),
  getBlogSettings: vi.fn().mockResolvedValue({ data: {} }),
  getHotArticles: vi.fn().mockResolvedValue({ rows: [], total: 0 }),
  searchArticles: vi.fn().mockResolvedValue({ rows: [], total: 0 })
}))

vi.mock('@/api/blog/category', () => ({
  getCategoryList: vi.fn().mockResolvedValue({ rows: [] })
}))

vi.mock('@/api/blog/tag', () => ({
  getTagCloud: vi.fn().mockResolvedValue({ data: [] })
}))

const router = createRouter({
  history: createMemoryHistory(),
  routes: [{ path: '/blog', component: BlogHome }]
})

describe('博客首页运行时渲染', () => {
  it('没有后端数据时也应该渲染首屏结构', async () => {
    router.push('/blog')
    await router.isReady()

    const wrapper = mount(BlogHome, {
      global: {
        plugins: [createPinia(), router],
        stubs: {
          'el-avatar': { template: '<span><slot /></span>' },
          'el-button': { template: '<button><slot /></button>' },
          'el-dropdown': { template: '<div><slot /><slot name="dropdown" /></div>' },
          'el-dropdown-item': { template: '<div><slot /></div>' },
          'el-dropdown-menu': { template: '<div><slot /></div>' },
          'el-icon': { template: '<span><slot /></span>' }
        }
      }
    })

    await Promise.resolve()

    expect(wrapper.find('.mo-home-page').exists()).toBe(true)
    expect(wrapper.find('.home-hero h1').text()).toBe('记录与分享')
    expect(wrapper.find('.blog-top-nav').exists()).toBe(true)
  })

  it('存在旧 token 时也应该渲染首屏，不应被用户下拉菜单阻断', async () => {
    router.push('/blog')
    await router.isReady()

    const pinia = createPinia()
    const userStore = useUserStore(pinia)
    userStore.token = 'stale-token'

    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})

    const wrapper = mount(BlogHome, {
      global: {
        plugins: [pinia, router]
      }
    })

    await Promise.resolve()

    expect(wrapper.find('.mo-home-page').exists()).toBe(true)
    expect(wrapper.find('.home-hero h1').text()).toBe('记录与分享')
    expect(consoleErrorSpy).not.toHaveBeenCalledWith(
      expect.stringContaining('Vue运行时错误:'),
      expect.anything()
    )

    consoleErrorSpy.mockRestore()
  })
})
