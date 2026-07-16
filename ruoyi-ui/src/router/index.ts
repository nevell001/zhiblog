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
  // 后台管理路由 - 简化版
  {
    path: '/admin',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/dashboard/index.vue'),
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  },
  // 博客管理
  {
    path: '/admin/blog',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/blog/article',
    hidden: true,
    meta: { title: '博客管理', icon: 'documentation' },
    children: [
      {
        path: 'article',
        name: 'BlogArticle',
        component: () => import('@/views/admin/blog/article/index.vue'),
        meta: { title: '文章管理', icon: 'documentation' }
      },
      {
        path: 'category',
        name: 'BlogCategory',
        component: () => import('@/views/admin/blog/category/index.vue'),
        meta: { title: '分类管理', icon: 'component' }
      },
      {
        path: 'tag',
        name: 'BlogTag',
        component: () => import('@/views/admin/blog/tag/index.vue'),
        meta: { title: '标签管理', icon: 'tag' }
      },
      {
        path: 'comment',
        name: 'BlogComment',
        component: () => import('@/views/admin/blog/comment/index.vue'),
        meta: { title: '评论管理', icon: 'message' }
      },
      {
        path: 'setting',
        name: 'BlogSetting',
        component: () => import('@/views/admin/blog/setting/index.vue'),
        meta: { title: '博客设置', icon: 'setting' }
      },
      {
        path: 'friendLink',
        name: 'BlogFriendLink',
        component: () => import('@/views/admin/blog/friendLink/index.vue'),
        meta: { title: '友链管理', icon: 'link' }
      }
    ]
  },
  // 系统管理
  {
    path: '/admin/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/system/user',
    hidden: true,
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'user',
        name: 'SystemUser',
        component: () => import('@/views/admin/system/user/user/index.vue'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'role',
        name: 'SystemRole',
        component: () => import('@/views/admin/system/role/role/index.vue'),
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'menu',
        name: 'SystemMenu',
        component: () => import('@/views/admin/system/menu/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'tree-table' }
      },
      {
        path: 'dept',
        name: 'SystemDept',
        component: () => import('@/views/admin/system/dept/dept/index.vue'),
        meta: { title: '部门管理', icon: 'tree' }
      },
      {
        path: 'post',
        name: 'SystemPost',
        component: () => import('@/views/admin/system/post/post/index.vue'),
        meta: { title: '岗位管理', icon: 'post' }
      },
      {
        path: 'dict',
        name: 'SystemDict',
        component: () => import('@/views/admin/system/dict/dict/index.vue'),
        meta: { title: '字典管理', icon: 'dict' }
      },
      {
        path: 'config',
        name: 'SystemConfig',
        component: () => import('@/views/admin/system/config/config/index.vue'),
        meta: { title: '参数设置', icon: 'edit' }
      },
      {
        path: 'notice',
        name: 'SystemNotice',
        component: () => import('@/views/admin/system/notice/notice/index.vue'),
        meta: { title: '通知公告', icon: 'message' }
      },
      {
        path: 'dict-data',
        component: () => import('@/layout/index.vue'),
        hidden: true,
        permissions: ['system:dict:list'],
        children: [
          {
            path: 'index/:dictId(\\d+)',
            component: () => import('@/views/admin/system/dict/dict/data.vue'),
            name: 'Data',
            meta: { title: '字典数据', activeMenu: '/admin/system/dict' }
          }
        ]
      }
    ]
  },
  // 个人中心
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
  // 数据统计
  {
    path: '/admin/statistics',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/statistics/overview',
    hidden: true,
    meta: { title: '数据统计', icon: 'chart' },
    children: [
      {
        path: 'overview',
        name: 'StatisticsOverview',
        component: () => import('@/views/admin/statistics/overview/index.vue'),
        meta: { title: '总览', icon: 'chart' }
      },
      {
        path: 'article',
        name: 'StatisticsArticle',
        component: () => import('@/views/admin/statistics/article/index.vue'),
        meta: { title: '文章统计', icon: 'documentation' }
      },
      {
        path: 'user',
        name: 'StatisticsUser',
        component: () => import('@/views/admin/statistics/user/index.vue'),
        meta: { title: '用户统计', icon: 'user' }
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/monitor/actuator',
    hidden: true,
    children: [
      {
        path: 'actuator',
        name: 'MonitorActuator',
        component: () => import('@/views/admin/monitor/actuator/index.vue'),
        meta: { title: 'Actuator监控', icon: 'monitor' }
      },
      {
        path: 'prometheus',
        name: 'MonitorPrometheus',
        component: () => import('@/views/admin/monitor/prometheus/index.vue'),
        meta: { title: 'Prometheus监控', icon: 'chart' }
      },
      {
        path: 'grafana',
        name: 'MonitorGrafana',
        component: () => import('@/views/admin/monitor/grafana/index.vue'),
        meta: { title: 'Grafana监控', icon: 'dashboard' }
      },
      {
        path: 'online',
        name: 'MonitorOnline',
        component: () => import('@/views/admin/monitor/online/index.vue'),
        meta: { title: '在线用户', icon: 'online' }
      },
      {
        path: 'logininfor',
        name: 'MonitorLoginLog',
        component: () => import('@/views/admin/monitor/logininfor/index.vue'),
        meta: { title: '登录日志', icon: 'logininfor' }
      },
      {
        path: 'operlog',
        name: 'MonitorOperLog',
        component: () => import('@/views/admin/monitor/operlog/index.vue'),
        meta: { title: '操作日志', icon: 'form' }
      },
      {
        path: 'server',
        name: 'Server',
        component: () => import('@/views/admin/monitor/server/index.vue'),
        meta: { title: '服务监控', icon: 'server' }
      },
      {
        path: 'cache',
        name: 'MonitorCache',
        component: () => import('@/views/admin/monitor/cache/index.vue'),
        meta: { title: '缓存监控', icon: 'redis' }
      },
      {
        path: 'cache/list',
        name: 'MonitorCacheList',
        component: () => import('@/views/admin/monitor/cache/list.vue'),
        meta: { title: '缓存列表', icon: 'redis' }
      },
      {
        path: 'job',
        name: 'MonitorJob',
        component: () => import('@/views/admin/monitor/job/index.vue'),
        meta: { title: '定时任务', icon: 'job' }
      },
      {
        path: 'druid',
        name: 'MonitorDruid',
        component: () => import('@/views/admin/monitor/druid/index.vue'),
        meta: { title: '数据监控', icon: 'druid' }
      }
    ]
  },
  {
    path: '/admin/tool',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/tool/build',
    hidden: true,
    children: [
      {
        path: 'build',
        name: 'Build',
        component: () => import('@/views/admin/tool/gen/index.vue'),
        meta: { title: '表单构建', icon: 'build' }
      }
    ]
  },
  // 博客前台路由
  ...blogRoutes,
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    hidden: true
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
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
// 动态路由（通常为空，因为我们的路由都是静态的）
export const dynamicRoutes: RouteRecordRaw[] = []

export default router
