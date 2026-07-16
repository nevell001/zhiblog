import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const homeView = readFileSync(resolve(__dirname, 'index.vue'), 'utf8')
const articleDetailView = readFileSync(resolve(__dirname, 'article/detail.vue'), 'utf8')
const blogLayout = readFileSync(resolve(__dirname, '../../components/BlogLayout.vue'), 'utf8')
const blogFooter = readFileSync(resolve(__dirname, '../../components/BlogFooter.vue'), 'utf8')
const articleTOC = readFileSync(resolve(__dirname, '../../components/ArticleTOC.vue'), 'utf8')
const forgotPasswordView = readFileSync(resolve(__dirname, 'auth/ForgotPassword.vue'), 'utf8')
const categoryView = readFileSync(resolve(__dirname, 'category/index.vue'), 'utf8')
const tagView = readFileSync(resolve(__dirname, 'tag/index.vue'), 'utf8')
const archiveView = readFileSync(resolve(__dirname, 'archive/index.vue'), 'utf8')
const aboutView = readFileSync(resolve(__dirname, 'about.vue'), 'utf8')
const blogNav = readFileSync(resolve(__dirname, '../../components/BlogNav.vue'), 'utf8')

const publicBlogSurfaces = [
  homeView,
  articleDetailView,
  blogLayout,
  blogFooter,
  articleTOC,
  forgotPasswordView,
  categoryView,
  tagView,
  archiveView,
  aboutView
]

describe('blog design layout guards', () => {
  it('首页搜索框应在 hero 区域居中', () => {
    expect(homeView).toContain('text-align: center;')
    expect(homeView).toContain('margin: 24px auto 0;')
  })

  it('文章详情页不应被旧卡片布局类压缩', () => {
    expect(articleDetailView).toContain('class="mo-article-page"')
    expect(articleDetailView).not.toContain('class="article-detail mo-article-page"')
    expect(articleDetailView).not.toContain('padding-top: 64px;')
  })

  it('文章详情页应支持主题深色变量并允许内容列收缩', () => {
    expect(articleDetailView).toContain('html.dark .mo-article-page')
    expect(articleDetailView).toContain('.mo-article-page .article-content {\n  min-width: 0;')
    expect(articleDetailView).toContain('.mo-article-page .article-side {\n  min-width: 0;')
    expect(articleDetailView).toContain(
      '.mo-article-page .content-body :deep(table) {\n  display: block;'
    )
  })

  it('博客核心页面不应混入旧版高饱和配色', () => {
    const legacyBlue = /#4a7bff|#6b8cff/i

    publicBlogSurfaces.forEach(surface => {
      expect(surface).not.toMatch(legacyBlue)
    })
    expect(homeView).not.toContain('tag-pink')
    expect(homeView).not.toContain('tag-green')
    expect(homeView).not.toContain('tag-amber')
    expect(homeView).not.toContain('.hero-section')
    expect(articleDetailView).not.toContain('.article-detail {')
    expect(articleDetailView).not.toContain('html.dark .article-detail {')
  })

  it('目录组件应使用当前主题色和可清理的滚动监听', () => {
    expect(articleTOC).not.toContain('@media (prefers-color-scheme: dark)')
    expect(articleTOC).toContain('html.dark .article-toc')
    expect(articleTOC).toContain('const handleScroll')
    expect(articleTOC).not.toContain("window.addEventListener('scroll', () =>")
  })

  it('分类和标签页主布局不应横向溢出', () => {
    expect(categoryView).toContain('min-width: 0;')
    expect(tagView).toContain('min-width: 0;')
    expect(categoryView).toContain('grid-template-columns: minmax(0, 1fr) 300px;')
    expect(tagView).toContain('grid-template-columns: minmax(0, 1fr) 300px;')
    expect(categoryView).not.toContain('minmax(380px, 1fr)')
    expect(tagView).not.toContain('minmax(380px, 1fr)')
    expect(categoryView).toContain('minmax(min(100%, 380px), 1fr)')
    expect(tagView).toContain('minmax(min(100%, 380px), 1fr)')
  })

  it('找回密码页应复用当前博客认证主题布局', () => {
    expect(forgotPasswordView).toContain('class="blog-forgot-password-container mo-auth-page"')
    expect(forgotPasswordView).toContain('<BlogLayout>')
    expect(forgotPasswordView).not.toContain('<BlogNav')
    expect(forgotPasswordView).not.toContain("from '@/components/BlogNav.vue'")
    expect(forgotPasswordView).toContain("from '@/components/BlogLayout.vue'")
    expect(forgotPasswordView).toContain('html.dark .mo-auth-page')
    expect(forgotPasswordView).toContain("router.push('/blog/auth/login')")
  })

  it('关于页应降噪为当前主题页面结构', () => {
    expect(aboutView).toContain('class="about-intro"')
    expect(aboutView).not.toContain('hero-section')
    expect(aboutView).not.toContain('hero-wave-bg')
    expect(aboutView).not.toContain('hero-bottom-wave')
    expect(aboutView).not.toContain('linear-gradient(135deg, #4f46e5 0%, #3730a3 100%)')
  })

  it('归档页不应保留旧浅紫和深紫独立配色', () => {
    const archiveLegacyColors = /#f8f9ff|#f5f5ff|#e8e9ff|#e5e5ff|#2e2e45|#3a3a55/i

    expect(archiveView).not.toMatch(archiveLegacyColors)
    expect(archiveView).toContain('background: #fafaf9;')
    expect(archiveView).toContain('html.dark .archive-page')
  })

  it('旧 BlogNav 应保持为未使用的兼容组件', () => {
    const publicViews = [
      homeView,
      articleDetailView,
      forgotPasswordView,
      categoryView,
      tagView,
      archiveView,
      aboutView
    ]

    publicViews.forEach(view => {
      expect(view).not.toContain('<BlogNav')
      expect(view).not.toContain("from '@/components/BlogNav.vue'")
    })
    expect(blogNav).toContain('Legacy compatibility navigation')
  })

  it('列表型页面头部和封面标记应保持低饱和', () => {
    const heavyHeaderGradient = 'linear-gradient(135deg, #4f46e5 0%, #3730a3 100%)'
    const heavyBadgeGradient =
      'linear-gradient(135deg, rgba(79, 70, 229, 0.95), rgba(67, 56, 202, 0.95))'
    const heavyOverlayGradient =
      'linear-gradient(135deg, rgba(79, 70, 229, 0.3) 0%, rgba(67, 56, 202, 0.3) 100%)'

    expect(categoryView).not.toContain(heavyHeaderGradient)
    expect(tagView).not.toContain(heavyHeaderGradient)
    expect(archiveView).not.toContain(heavyHeaderGradient)
    expect(categoryView).not.toContain(heavyBadgeGradient)
    expect(tagView).not.toContain(heavyBadgeGradient)
    expect(archiveView).not.toContain(heavyOverlayGradient)
    expect(categoryView).toContain('background: #fafaf9;')
    expect(tagView).toContain('background: #fafaf9;')
    expect(archiveView).toContain('background: #fafaf9;')
  })

  it('公共博客页面应减少装饰性动效和过大圆角', () => {
    expect(archiveView).not.toContain('animation: bounce 2s ease-in-out infinite')
    expect(archiveView).not.toContain('animation: pulse 2s ease-in-out infinite')
    expect(archiveView).not.toMatch(/border-radius:\s*(1[2-9]|[2-9][0-9])px/)
    expect(categoryView).not.toMatch(/border-radius:\s*(1[2-9]|[2-9][0-9])px/)
    expect(tagView).not.toMatch(/border-radius:\s*(1[2-9]|[2-9][0-9])px/)
    expect(blogFooter).toContain('minmax(min(100%, 300px), 1fr)')
  })
})
