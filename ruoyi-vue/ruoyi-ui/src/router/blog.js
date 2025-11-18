// 博客前台路由配置
export const blogRoutes = [
  {
    path: '/blog',
    component: () => import('@/views/blog/index'),
    name: 'BlogHome',
    meta: { title: '博客首页' }
  },
  {
    path: '/blog/article',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/blog/article',
    meta: { title: '文章管理', permissions: ['admin', 'editor'] }
  },
  {
    path: '/blog/article/:id',
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
    path: '/blog/category/:id',
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
  },
  {
    path: '/admin/statistics',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/statistics/overview',
    name: 'Statistics',
    meta: { title: '数据统计' },
    children: [
      {
        path: 'overview',
        component: () => import('@/views/admin/statistics/overview/index'),
        name: 'StatisticsOverview',
        meta: { title: '数据概览' }
      }
    ]
  },
  {
    path: '/admin/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/system/user',
    name: 'System',
    meta: { title: '系统管理' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/admin/system/user/user/index'),
        name: 'SystemUser',
        meta: { title: '用户管理' }
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/monitor/job',
    name: 'Monitor',
    meta: { title: '系统监控' },
    children: [
      {
        path: 'job',
        component: () => import('@/views/admin/monitor/job/index'),
        name: 'MonitorJob',
        meta: { title: '定时任务' }
      }
    ]
  },
  {
    path: '/admin/tool',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/tool/gen',
    name: 'Tool',
    meta: { title: '系统工具' },
    children: [
      {
        path: 'gen',
        component: () => import('@/views/admin/tool/gen/index'),
        name: 'ToolGen',
        meta: { title: '代码生成' }
      }
    ]
  }
]

export default blogRoutes