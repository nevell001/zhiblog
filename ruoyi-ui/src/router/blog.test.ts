import { describe, it, expect } from 'vitest'
import { blogRoutes } from './blog'

describe('Blog Routes 详细测试', () => {
  it('首页路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog')
    expect(route?.path).toBe('/blog')
    expect(route?.component).toBe('@/views/blog/index.vue')
    expect(route?.name).toBe('PublicBlogHome')
    expect(route?.meta?.title).toBe('博客首页')
  })

  it('文章详情路由应该有正确的路径、组件和别名', () => {
    const route = blogRoutes.find(r => r.path === '/blog/article/:id')
    expect(route?.path).toBe('/blog/article/:id')
    expect(route?.component).toBe('@/views/blog/article/detail.vue')
    expect(route?.name).toBe('PublicBlogArticleDetail')
    expect(route?.alias).toEqual(['/article/:id'])
  })

  it('分类列表路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/category')
    expect(route?.path).toBe('/blog/category')
    expect(route?.component).toBe('@/views/blog/category/index.vue')
    expect(route?.name).toBe('PublicBlogCategoryList')
    expect(route?.meta?.title).toBe('分类列表')
  })

  it('分类详情路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/category/:id')
    expect(route?.path).toBe('/blog/category/:id')
    expect(route?.component).toBe('@/views/blog/category/index.vue')
    expect(route?.name).toBe('PublicBlogCategory')
    expect(route?.meta?.title).toBe('分类文章')
  })

  it('标签列表路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/tag')
    expect(route?.path).toBe('/blog/tag')
    expect(route?.component).toBe('@/views/blog/tag/index.vue')
    expect(route?.name).toBe('PublicBlogTagList')
    expect(route?.meta?.title).toBe('标签列表')
  })

  it('标签详情路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/tag/:id')
    expect(route?.path).toBe('/blog/tag/:id')
    expect(route?.component).toBe('@/views/blog/tag/index.vue')
    expect(route?.name).toBe('PublicBlogTag')
    expect(route?.meta?.title).toBe('标签文章')
  })

  it('归档路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/archive')
    expect(route?.path).toBe('/blog/archive')
    expect(route?.component).toBe('@/views/blog/archive/index.vue')
    expect(route?.name).toBe('PublicBlogArchive')
    expect(route?.meta?.title).toBe('文章归档')
  })

  it('关于页面路由应该有正确的路径和组件', () => {
    const route = blogRoutes.find(r => r.path === '/blog/about')
    expect(route?.path).toBe('/blog/about')
    expect(route?.component).toBe('@/views/blog/about.vue')
    expect(route?.name).toBe('PublicAbout')
    expect(route?.meta?.title).toBe('关于我们')
  })

  it('用户登录路由应该重定向到后台登录', () => {
    const route = blogRoutes.find(r => r.path === '/blog/auth/login')
    expect(route?.redirect).toBe('/login')
    expect(route?.meta?.title).toBe('用户登录')
  })

  it('用户注册路由应该有正确的路径和标题', () => {
    const route = blogRoutes.find(r => r.path === '/blog/auth/register')
    expect(route?.path).toBe('/blog/auth/register')
    expect(route?.component).toBe('@/views/blog/auth/Register.vue')
    expect(route?.name).toBe('BlogRegister')
    expect(route?.meta?.title).toBe('用户注册')
  })

  it('找回密码路由应该有正确的路径和标题', () => {
    const route = blogRoutes.find(r => r.path === '/blog/auth/forgot-password')
    expect(route?.path).toBe('/blog/auth/forgot-password')
    expect(route?.component).toBe('@/views/blog/auth/ForgotPassword.vue')
    expect(route?.name).toBe('BlogForgotPassword')
    expect(route?.meta?.title).toBe('找回密码')
  })

  it('首页路由应该有 index 组件', () => {
    const route = blogRoutes.find(r => r.path === '/index')
    expect(route?.component).toBe('@/views/blog/index.vue')
    expect(route?.name).toBe('PublicIndex')
  })

  it('路由总数应该为 13 个', () => {
    expect(blogRoutes.length).toBe(13)
  })

  it('博客路由应该包含 12 个前台路由', () => {
    const blogFrontendRoutes = blogRoutes.filter(r => r.path.startsWith('/blog') && !r.path.includes('/auth'))

    expect(blogFrontendRoutes.length).toBe(12)
  })

  it('博客路由应该包含 1 个认证路由', () => {
    const blogAuthRoutes = blogRoutes.filter(r => r.path.includes('/auth'))

    expect(blogAuthRoutes.length).toBe(1)
  })

  it('所有博客路由都应该有 component 属性', () => {
    const routesWithoutComponent = blogRoutes.filter(r => !r.component)

    expect(routesWithoutComponent.length).toBe(0)
  })

  it('所有博客路由都应该有 name 属性', () => {
    const routesWithoutName = blogRoutes.filter(r => !r.name)

    expect(routesWithoutName.length).toBe(0)
  })

  it('所有博客路由都应该有 meta.title 属性', () => {
    const routesWithTitle = blogRoutes.filter(r => r.meta?.title)

    expect(routesWithTitle.length).toBeGreaterThan(0)
  })
})
