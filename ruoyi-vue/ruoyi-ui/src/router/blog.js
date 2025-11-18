// 博客前台路由配置 - 仅包含前台相关路由
export const blogRoutes = [
  {
    path: '/blog',
    component: () => import('@/views/blog/index'),
    name: 'BlogHome',
    meta: { title: '博客首页' }
  },
  {
    path: '/blog/article/:id(\\d+)',
    component: () => import('@/views/blog/article/detail.vue'),
    name: 'BlogArticleDetail',
    meta: { title: '文章详情' }
  },
  {
    path: '/blog/category',
    component: () => import('@/views/blog/category/index'),
    name: 'BlogCategoryList',
    meta: { title: '分类列表' }
  },
  {
    path: '/blog/category/:id(\\d+)',
    component: () => import('@/views/blog/category/index'),
    name: 'BlogCategory',
    meta: { title: '分类文章' }
  },
  {
    path: '/blog/tag',
    component: () => import('@/views/blog/tag/index'),
    name: 'BlogTagList',
    meta: { title: '标签列表' }
  },
  {
    path: '/blog/tag/:id(\\d+)',
    component: () => import('@/views/blog/tag/index'),
    name: 'BlogTag',
    meta: { title: '标签文章' }
  },
  {
    path: '/blog/archive',
    component: () => import('@/views/blog/archive/index'),
    name: 'BlogArchive',
    meta: { title: '文章归档' }
  },
  {
    path: '/blog/about',
    component: () => import('@/views/blog/about'),
    name: 'About',
    meta: { title: '关于我们' }
  },
  {
    path: '/index',
    component: () => import('@/views/blog/index'),
    name: 'Index',
    meta: { title: '首页' }
  },
  {
    path: '/about',
    component: () => import('@/views/blog/about'),
    name: 'AboutPage',
    meta: { title: '关于' }
  }
]

export default blogRoutes