import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleCard from './ArticleCard.vue'

const stubs = {
  RouterLink: {
    props: ['to', 'title'],
    template: '<a class="stub-link" :data-to="to"><slot /></a>'
  },
  'el-icon': { template: '<span class="stub-icon"><slot /></span>' }
}

const mountCard = (article: Record<string, unknown>, index = 0) =>
  mount(ArticleCard, { props: { article: article as never, index }, global: { stubs } })

describe('ArticleCard 组件', () => {
  it('应该渲染标题、元信息、摘要与文章标签', () => {
    const wrapper = mountCard({
      id: 7,
      title: '深色模式令牌契约',
      coverUrl: '/cover.png',
      categoryName: '前端工程',
      createTime: '2026-10-01 10:00:00',
      viewCount: 12,
      likeCount: 3,
      commentCount: 1,
      summary: '一段摘要',
      tags: [
        { id: 1, name: 'Vue' },
        { id: 2, name: 'CSS' },
        { id: 3, name: '主题' },
        { id: 4, name: '不应出现' }
      ]
    })

    expect(wrapper.text()).toContain('深色模式令牌契约')
    expect(wrapper.text()).toContain('12 阅读')
    expect(wrapper.text()).toContain('一段摘要')
    expect(wrapper.text()).toContain('前端工程')
    expect(wrapper.find('img').attributes('src')).toBe('/cover.png')

    const chips = wrapper.findAll('.article-card-tag')
    expect(chips).toHaveLength(3)
    expect(chips.map(c => c.text())).not.toContain('不应出现')
  })

  it('缺封面时不渲染封面与分类角标', () => {
    const wrapper = mountCard({ id: 1, title: '无封面', summary: 's' })

    expect(wrapper.find('.article-card-cover').exists()).toBe(false)
    expect(wrapper.find('.article-card-badge').exists()).toBe(false)
  })

  it('没有摘要时应该用去掉 HTML 标签的正文兜底', () => {
    const wrapper = mountCard({
      id: 2,
      title: '兜底摘要',
      content: '<p>正文<strong>加粗</strong></p>'
    })

    const summary = wrapper.find('.article-card-summary').text()
    expect(summary).toContain('正文加粗')
    expect(summary).not.toContain('<p>')
    expect(summary).not.toContain('<strong>')
  })

  it('文章详情链接应该指向文章 id', () => {
    const wrapper = mountCard({ id: 42, title: 't', summary: 's' })

    expect(wrapper.find('.article-card-more').attributes('data-to')).toBe('/blog/article/42')
  })

  it('入场动画延迟应该跟随索引', () => {
    const wrapper = mountCard({ id: 1, title: 't', summary: 's' }, 5)

    expect(wrapper.find('.article-card').attributes('style')).toContain('animation-delay: 0.5s')
  })
})
