import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import { h } from 'vue'
import ArticleEmptyState from './ArticleEmptyState.vue'

const stubs = {
  RouterLink: { props: ['to'], template: '<a class="stub-link" :data-to="to"><slot /></a>' },
  'el-button': { template: '<button><slot /></button>' },
  'el-icon': { template: '<span class="stub-icon"><slot /></span>' }
}

describe('ArticleEmptyState 组件', () => {
  it('应该渲染图标、标题、描述与返回首页入口', () => {
    const wrapper = mount(ArticleEmptyState, {
      props: {
        icon: h('svg', { class: 'fake-icon' }),
        description: '该分类下还没有文章，敬请期待...'
      },
      global: { stubs }
    })

    expect(wrapper.find('.fake-icon').exists()).toBe(true)
    expect(wrapper.find('.empty-content h3').text()).toBe('暂无文章')
    expect(wrapper.find('.empty-content p').text()).toBe('该分类下还没有文章，敬请期待...')
    expect(wrapper.find('.back-home-btn').attributes('data-to')).toBe('/')
  })

  it('应该支持覆盖默认标题', () => {
    const wrapper = mount(ArticleEmptyState, {
      props: { icon: h('span'), title: '空空如也' },
      global: { stubs }
    })

    expect(wrapper.find('.empty-content h3').text()).toBe('空空如也')
  })
})
