// 基础后台管理路由配置
import Layout from '@/layout/index.vue'

export const adminRoutes = [
  {
    path: '/admin',
    component: Layout,
    redirect: '/admin/dashboard',
    meta: { title: '后台管理', icon: 'dashboard' },
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/admin/dashboard/index.vue'),
        name: 'AdminDashboard',
        meta: { title: '后台首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/admin/blog',
    component: Layout,
    redirect: '/admin/blog/article',
    name: 'BlogManagement',
    meta: { title: '博客管理', icon: 'documentation', roles: ['admin', 'editor'] },
    children: [
      {
        path: 'article',
        component: () => import('@/views/admin/blog/article/index.vue'),
        name: 'BlogArticle',
        meta: {
          title: '文章管理',
          icon: 'edit',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'category',
        component: () => import('@/views/admin/blog/category/index.vue'),
        name: 'BlogCategory',
        meta: {
          title: '分类管理',
          icon: 'list',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'tag',
        component: () => import('@/views/admin/blog/tag/index.vue'),
        name: 'BlogTag',
        meta: {
          title: '标签管理',
          icon: 'tag',
          roles: ['admin', 'editor']
        }
      }
    ]
  }
]

export default adminRoutes