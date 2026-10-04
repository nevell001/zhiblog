import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import StatCard from './StatCard.vue'

const mountCard = (props: { label: string; value: string | number }) => mount(StatCard, { props })

describe('StatCard 组件', () => {
  it('渲染标签与数值', () => {
    const wrapper = mountCard({ label: '区间 UV', value: 1234 })

    expect(wrapper.find('.stat-card__label').text()).toBe('区间 UV')
    expect(wrapper.find('.stat-card__value').text()).toBe('1234')
  })

  it('数值为 0 时照常渲染，不被当成空值', () => {
    const wrapper = mountCard({ label: '今日 PV', value: 0 })

    expect(wrapper.find('.stat-card__value').text()).toBe('0')
  })

  it('数值变化后跟随更新', async () => {
    const wrapper = mountCard({ label: '发布文章数', value: 1 })
    await wrapper.setProps({ value: 42 })

    expect(wrapper.find('.stat-card__value').text()).toBe('42')
  })
})
