import { describe, it, expect } from 'vitest'
import { blogRoutes } from './blog'

describe('Blog Routes 测试', () => {
  it('应该导出 blogRoutes 数组', () => {
    expect(blogRoutes).toBeDefined()
    expect(Array.isArray(blogRoutes)).toBe(true)
  })

  it('应该包含首页路由', () => {
    const homeRoute = blogRoutes.find(route => route.path === '/blog')
    expect(homeRoute).toBeDefined()
    expect(homeRoute?.name).toBe('PublicBlogHome')
    expect(homeRoute?.component).toBe('@/views/blog/index.vue')
  })

  it('应该包含文章详情路由', () => {
    const articleRoute = blogRoutes.find(route => route.path === '/blog/article/:id')
    expect(articleRoute).toBeDefined()
    expect(articleRoute?.name).toBe('PublicBlogArticleDetail')
    expect(articleRoute?.component).toBe('@/views/blog/article/detail.vue')
  })

  it('应该包含分类列表路由', () => {
    const categoryRoute = blogRoutes.find(route => route.path === '/blog/category')
    expect(categoryRoute).toBeDefined()
    expect(categoryRoute?.name).toBe('PublicBlogCategoryList')
    expect(categoryRoute?.component).toBe('@/views/blog/category/index.vue')
  })

  it('应该包含标签列表路由', () => {
    const tagRoute = blogRoutes.find(route => route.path === '/blog/tag')
    expect(tagRoute).toBeDefined()
    expect(tagRoute?.name).toBe('PublicBlogTagList')
    expect(tagRoute?.component).toBe('@/views/blog/tag/index.vue')
  })

  it('应该包含归档路由', () => {
    const archiveRoute = blogRoutes.find(route => route.path === '/blog/archive')
    expect(archiveRoute).toBeDefined()
    expect(archiveRoute?.name).toBe('PublicBlogArchive')
    expect(archiveRoute?.component).toBe('@/views/blog/archive/index.vue')
  })

  it('应该包含关于页面路由', () => {
    const aboutRoute = blogRoutes.find(route => route.path === '/blog/about')
    expect(aboutRoute).toBeDefined()
    expect(aboutRoute?.name).toBe('PublicAbout')
    expect(aboutRoute?.component).toBe('@/views/blog/about.vue')
  })

  it('应该包含用户登录路由', () => {
    const loginRoute = blogRoutes.find(route => route.path === '/blog/auth/login')
    expect(loginRoute).toBeDefined()
    expect(loginRoute?.redirect).toBe('/login')
    expect(loginRoute?.meta?.title).toBe('用户登录')
  })

  it('应该包含用户注册路由', () => {
    const registerRoute = blogRoutes.find(route => route.path === '/blog/auth/register')
    expect(registerRoute).toBeDefined()
    expect(registerRoute?.meta?.title).toBe('用户注册')
  })

  it('应该包含找回密码路由', () => {
    const forgotRoute = blogRoutes.find(route => route.path === '/blog/auth/forgot-password')
    expect(forgotRoute).toBeDefined()
    expect(forgotRoute?.meta?.title).toBe('找回密码')
  })

  it('路由数组应该包含所有预期的路由', () => {
    expect(blogRoutes.length).toBeGreaterThanOrEqual(10)
  })
})
