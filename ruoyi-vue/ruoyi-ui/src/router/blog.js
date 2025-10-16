// 博客前台路由配置
export const blogRoutes = [
  {
    path: '/index',
    component: () => import('@/views/blog/index'),
    name: 'BlogHome',
    meta: { title: '博客首页' }
  },
  {
    path: '/blog/article/:id',
    component: () => import('@/views/blog/article/detail'),
    name: 'BlogArticleDetail',
    meta: { title: '文章详情' }
  },
  {
    path: '/blog/category/:id',
    component: () => import('@/views/blog/category/index'),
    name: 'BlogCategory',
    meta: { title: '分类文章' }
  },
  {
    path: '/blog/tag/:id',
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
    path: '/blog/simple',
    component: () => import('@/views/blog/simple'),
    name: 'BlogSimple',
    meta: { title: '简洁版博客' }
  }
]

export default blogRoutes