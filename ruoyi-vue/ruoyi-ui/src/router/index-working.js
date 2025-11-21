import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/blog'
  },
  {
    path: '/blog',
    component: () => import('@/views/blog/index'),
    name: 'PublicBlogHome',
    meta: { title: '博客首页' }
  },
  {
    path: '/blog/article/:id',
    component: () => import('@/views/blog/article/detail.vue'),
    name: 'PublicBlogArticleDetail',
    meta: { title: '文章详情' }
  },
  {
    path: '/blog/category',
    component: () => import('@/views/blog/category/index'),
    name: 'PublicBlogCategoryList',
    meta: { title: '分类列表' }
  },
  {
    path: '/blog/category/:id',
    component: () => import('@/views/blog/category/index'),
    name: 'PublicBlogCategory',
    meta: { title: '分类文章' }
  },
  {
    path: '/blog/tag',
    component: () => import('@/views/blog/tag/index'),
    name: 'PublicBlogTagList',
    meta: { title: '标签列表' }
  },
  {
    path: '/blog/tag/:id',
    component: () => import('@/views/blog/tag/index'),
    name: 'PublicBlogTag',
    meta: { title: '标签文章' }
  },
  {
    path: '/blog/archive',
    component: () => import('@/views/blog/archive/index'),
    name: 'PublicBlogArchive',
    meta: { title: '文章归档' }
  },
  {
    path: '/blog/about',
    component: () => import('@/views/blog/about'),
    name: 'PublicAbout',
    meta: { title: '关于我们' }
  },
  {
    path: '/login',
    component: () => import('@/views/login')
  },
  {
    path: '/admin',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/admin/dashboard/index.vue')
      },
      {
        path: 'statistics',
        component: () => import('@/views/admin/statistics/index.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router