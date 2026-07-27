import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
  type RouterScrollBehavior
} from 'vue-router'
import blogRoutes from './blog'

// 扩展 RouteRecordRaw 类型，添加 hidden 属性
interface ExtendedRouteRecordRaw extends Omit<RouteRecordRaw, 'children'> {
  hidden?: boolean
  permissions?: string[]
  children?: ExtendedRouteRecordRaw[]
}

const routes: ExtendedRouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/blog'
  },
  {
    path: '/login',
    component: () => import('@/views/UnifiedLogin.vue')
  },
  {
    path: '/index',
    redirect: '/blog'
  },
  // 个人中心（所有登录用户可访问）
  {
    path: '/user',
    component: () => import('@/layout/index.vue'),
    redirect: '/user/profile',
    hidden: true,
    children: [
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/admin/system/user/user/profile/index.vue'),
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  // 博客前台路由
  ...blogRoutes,
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    hidden: true
  }
]

const scrollBehavior: RouterScrollBehavior = (_to, _from, savedPosition) => {
  // 解决页面跳转后滚动位置问题
  if (savedPosition) {
    return savedPosition
  } else {
    return { top: 0 }
  }
}

const router = createRouter({
  history: createWebHistory(),
  routes: routes as RouteRecordRaw[],
  // 添加错误处理
  scrollBehavior
})

// 导出路由配置
export const constantRoutes = routes
// 动态路由（从后端获取，根据用户权限动态添加）
export const dynamicRoutes: RouteRecordRaw[] = []

export default router
