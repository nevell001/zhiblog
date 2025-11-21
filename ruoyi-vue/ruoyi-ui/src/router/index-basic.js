import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

// 静态路由
export const routes = [
  {
    path: '/',
    redirect: '/blog',
    hidden: true
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  // 博客路由
  {
    path: '/blog',
    component: () => import('@/views/blog/index'),
    name: 'Blog',
    meta: { title: '博客首页' }
  },
  // 管理后台路由
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
      },
      {
        path: 'system',
        component: () => import('@/views/admin/system/index.vue'),
        name: 'System',
        meta: { title: '系统管理', icon: 'system' }
      },
      {
        path: 'monitor',
        component: () => import('@/views/admin/monitor/index.vue'),
        name: 'Monitor',
        meta: { title: '系统监控', icon: 'monitor' }
      },
      {
        path: 'tool',
        component: () => import('@/views/admin/tool/index.vue'),
        name: 'Tool',
        meta: { title: '系统工具', icon: 'tool' }
      },
      {
        path: 'blog',
        component: () => import('@/views/admin/blog/index.vue'),
        name: 'BlogManagement',
        meta: { title: '博客管理', icon: 'documentation' }
      },
      {
        path: 'statistics',
        component: () => import('@/views/admin/statistics/index.vue'),
        name: 'Statistics',
        meta: { title: '数据统计', icon: 'chart' }
      }
    ]
  },
  // 404页面必须放在最后
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    hidden: true
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  scrollBehavior: () => ({ top: 0 })
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 获取token
  const token = localStorage.getItem("token")
  
  // 白名单路由
  const whiteList = ["/login", "/register", "/404", "/401", "/blog"]
  
  if (token) {
    // 已登录
    if (to.path === "/login") {
      next({ path: "/" })
    } else {
      next()
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path) || to.path.startsWith("/blog")) {
      next()
    } else {
      next("/login")
    }
  }
})

export default router