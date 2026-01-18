import type { RouteRecordRaw } from 'vue-router'

// 博客前台路由配置 - 仅包含前台相关路由
export const blogRoutes: RouteRecordRaw[] = [
  {
    path: '/blog',
    component: () => import('@/views/blog/index.vue'),
    name: 'PublicBlogHome',
    meta: { title: '博客首页' }
  },
  {
    path: '/blog/article/:id',
    component: () => import('@/views/blog/article/detail.vue'),
    name: 'PublicBlogArticleDetail',
    meta: { title: '文章详情' },
    alias: ['/article/:id']
  },
  {
    path: '/blog/category',
    component: () => import('@/views/blog/category/index.vue'),
    name: 'PublicBlogCategoryList',
    meta: { title: '分类列表' }
  },
  {
    path: '/blog/category/:id',
    component: () => import('@/views/blog/category/index.vue'),
    name: 'PublicBlogCategory',
    meta: { title: '分类文章' }
  },
  {
    path: '/blog/tag',
    component: () => import('@/views/blog/tag/index.vue'),
    name: 'PublicBlogTagList',
    meta: { title: '标签列表' }
  },
  {
    path: '/blog/tag/:id',
    component: () => import('@/views/blog/tag/index.vue'),
    name: 'PublicBlogTag',
    meta: { title: '标签文章' }
  },
  {
    path: '/blog/archive',
    component: () => import('@/views/blog/archive/index.vue'),
    name: 'PublicBlogArchive',
    meta: { title: '文章归档' }
  },
  {
    path: '/blog/about',
    component: () => import('@/views/blog/about.vue'),
    name: 'PublicAbout',
    meta: { title: '关于我们' }
  },
  {
    path: '/index',
    component: () => import('@/views/blog/index.vue'),
    name: 'PublicIndex',
    meta: { title: '首页' }
  },
  {
    path: '/about',
    component: () => import('@/views/blog/about.vue'),
    name: 'PublicAboutPage',
    meta: { title: '关于' }
  }
]

export default blogRoutes
