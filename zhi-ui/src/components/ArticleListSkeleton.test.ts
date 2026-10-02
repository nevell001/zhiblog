import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleListSkeleton from './ArticleListSkeleton.vue'

const stubs = {
  'el-skeleton': {
    props: ['loading', 'animated'],
    template:
      '<div class="stub-skeleton" :data-loading="String(loading)"><slot name="template" /></div>'
  },
  'el-skeleton-item': {
    props: ['variant'],
    template: '<span class="stub-skeleton-item" :data-variant="variant" />'
  }
}

describe('ArticleListSkeleton 组件', () => {
  it('默认渲染 6 张骨架卡片，且骨架为加载态', () => {
    const wrapper = mount(ArticleListSkeleton, { global: { stubs } })

    expect(wrapper.findAll('.stub-skeleton')).toHaveLength(6)
    expect(wrapper.find('.stub-skeleton').attributes('data-loading')).toBe('true')
  })

  it('可以自定义骨架卡片数量', () => {
    const wrapper = mount(ArticleListSkeleton, {
      props: { count: 3 },
      global: { stubs }
    })

    expect(wrapper.findAll('.stub-skeleton')).toHaveLength(3)
  })

  it('骨架结构应包含封面与正文占位', () => {
    const wrapper = mount(ArticleListSkeleton, {
      props: { count: 1 },
      global: { stubs }
    })

    expect(wrapper.find('.skeleton-cover').exists()).toBe(true)
    expect(wrapper.findAll('.stub-skeleton-item[data-variant="image"]')).toHaveLength(1)
    expect(wrapper.find('.skeleton-body').exists()).toBe(true)
  })
})
