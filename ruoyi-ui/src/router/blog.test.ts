import { describe, it, expect } from 'vitest'
import { blogRoutes } from './blog'

describe('Blog Router 测试', () => {
  it('应该导出 blogRoutes 数组', () => {
    expect(blogRoutes).toBeDefined()
    expect(Array.isArray(blogRoutes)).toBe(true)
  })

  it('应该包含首页路由', () => {
    const homeRoute = blogRoutes.find(route => route.path === '/blog')
    expect(homeRoute).toBeDefined()
    expect(homeRoute?.name).toBe('PublicBlogHome')
  })

  it('应该包含文章详情路由', () => {
    const articleRoute = blogRoutes.find(route => route.path === '/blog/article/:id')
    expect(articleRoute).toBeDefined()
    expect(articleRoute?.name).toBe('PublicBlogArticleDetail')
  })

  it('应该包含文章别名', () => {
    const articleRoute = blogRoutes.find(route => route.name === 'PublicBlogArticleDetail')
    expect(articleRoute?.alias).toEqual(['/article/:id'])
  })

  it('应该包含分类列表路由', () => {
    const categoryRoute = blogRoutes.find(route => route.path === '/blog/category')
    expect(categoryRoute).toBeDefined()
    expect(categoryRoute?.name).toBe('PublicBlogCategoryList')
  })

  it('应该包含分类文章路由', () => {
    const categoryRoute = blogRoutes.find(route => route.path === '/blog/category/:id')
    expect(categoryRoute).toBeDefined()
    expect(categoryRoute?.name).toBe('PublicBlogCategory')
  })

  it('应该包含标签列表路由', () => {
    const tagRoute = blogRoutes.find(route => route.path === '/blog/tag')
    expect(tagRoute).toBeDefined()
    expect(tagRoute?.name).toBe('PublicBlogTagList')
  })

  it('应该包含标签文章路由', () => {
    const tagRoute = blogRoutes.find(route => route.path === '/blog/tag/:id')
    expect(tagRoute).toBeDefined()
    expect(tagRoute?.name).toBe('PublicBlogTag')
  })

  it('应该包含归档路由', () => {
    const archiveRoute = blogRoutes.find(route => route.path === '/blog/archive')
    expect(archiveRoute).toBeDefined()
    expect(archiveRoute?.name).toBe('PublicBlogArchive')
  })

  it('应该包含关于页面路由', () => {
    const aboutRoute = blogRoutes.find(route => route.path === '/blog/about')
    expect(aboutRoute).toBeDefined()
    expect(aboutRoute?.name).toBe('PublicAbout')
  })

  it('应该包含登录路由', () => {
    const loginRoute = blogRoutes.find(route => route.path === '/blog/auth/login')
    expect(loginRoute).toBeDefined()
    expect(loginRoute?.redirect).toBe('/login')
  })

  it('应该包含注册路由', () => {
    const registerRoute = blogRoutes.find(route => route.path === '/blog/auth/register')
    expect(registerRoute).toBeDefined()
    expect(registerRoute?.name).toBe('BlogRegister')
  })

  it('应该包含找回密码路由', () => {
    const forgotRoute = blogRoutes.find(route => route.path === '/blog/auth/forgot-password')
    expect(forgotRoute).toBeDefined()
    expect(forgotRoute?.name).toBe('BlogForgotPassword')
  })

  it('应该包含首页路由', () => {
    const indexRoute = blogRoutes.find(route => route.path === '/index')
    expect(indexRoute).toBeDefined()
    expect(indexRoute?.name).toBe('PublicIndex')
  })

  it('应该包含关于页面路由', () => {
    const aboutPageRoute = blogRoutes.find(route => route.path === '/about')
    expect(aboutPageRoute).toBeDefined()
    expect(aboutPageRoute?.name).toBe('PublicAboutPage')
  })

  it('应该导出默认路由', () => {
    const blogModule = require('./blog')
    expect(blogModule.default).toBe(blogRoutes)
  })

  it('所有路由都应该有 meta 标题', () => {
    blogRoutes.forEach(route => {
      expect(route.meta).toBeDefined()
      expect(route.meta?.title).toBeDefined()
    })
  })

  it('所有路由都应该有 component', () => {
    blogRoutes.forEach(route => {
      expect(route.component).toBeDefined()
      expect(typeof route.component).toBe('function')
    })
  })

  it('路由数量应该正确', () => {
    expect(blogRoutes.length).toBe(12)
  })
})
