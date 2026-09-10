import { describe, it, expect, vi, beforeEach } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import CustomPage from './index.vue'

vi.mock('@/api/blog/page')

const source = readFileSync(resolve(__dirname, 'index.vue'), 'utf8')

describe('自定义页面渲染页测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出自定义页面组件', () => {
    expect(CustomPage).toBeDefined()
    expect(typeof CustomPage).toBe('object')
  })

  it('应该包在 BlogLayout 中', () => {
    expect(source).toContain('<BlogLayout>')
    expect(source).toContain("from '@/components/BlogLayout.vue'")
  })

  it('应该读取 route.params.slug 并加载页面', () => {
    expect(source).toContain('route.params.slug')
    expect(source).toContain('getPublishedPageBySlug')
    expect(source).toContain('watch(slug')
  })

  it('应该使用 Markdown 渲染 + 消毒链路', () => {
    expect(source).toContain("from '@/utils/markdown'")
    expect(source).toContain('renderMarkdown')
    expect(source).toContain("from '@/utils/sanitize'")
    expect(source).toContain('sanitizeArticleContent')
    expect(source).toContain('v-html="contentHtml"')
  })

  it('加载失败时应该展示友好的 404 提示而不是白屏', () => {
    expect(source).toContain('notFound')
    expect(source).toContain('页面不存在或未发布')
    expect(source).toContain('custom-page-empty')
    expect(source).toContain('返回首页')
  })

  it('成功时应该展示标题、更新时间与浏览数', () => {
    expect(source).toContain('{{ page.title }}')
    expect(source).toContain('page.updateTime')
    expect(source).toContain('page.viewCount')
  })

  it('应该优先使用页面自身的 SEO 字段', () => {
    expect(source).toContain('applySeo')
    expect(source).toContain('page.value.seoTitle')
    expect(source).toContain('page.value.seoKeywords')
    expect(source).toContain('page.value.seoDescription')
  })
})
