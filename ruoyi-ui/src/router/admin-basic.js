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
      },
      {
        path: 'comment',
        component: () => import('@/views/admin/blog/comment/index.vue'),
        name: 'BlogComment',
        meta: {
          title: '评论管理',
          icon: 'message',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'setting',
        component: () => import('@/views/admin/blog/setting/index.vue'),
        name: 'BlogSetting',
        meta: {
          title: '博客设置',
          icon: 'setting',
          roles: ['admin']
        }
      },
      {
        path: 'friendLink',
        component: () => import('@/views/admin/blog/friendLink/index.vue'),
        name: 'BlogFriendLink',
        meta: {
          title: '友链管理',
          icon: 'link',
          roles: ['admin']
        }
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: Layout,
    redirect: '/admin/monitor/actuator',
    name: 'SystemMonitor',
    meta: { title: '系统监控', icon: 'monitor', roles: ['admin'] },
    children: [
      {
        path: 'actuator',
        component: () => import('@/views/admin/monitor/actuator/index.vue'),
        name: 'MonitorActuator',
        meta: {
          title: 'Actuator监控',
          icon: 'monitor',
          roles: ['admin']
        }
      },
      {
        path: 'prometheus',
        component: () => import('@/views/admin/monitor/prometheus/index.vue'),
        name: 'MonitorPrometheus',
        meta: {
          title: 'Prometheus监控',
          icon: 'chart',
          roles: ['admin']
        }
      },
      {
        path: 'grafana',
        component: () => import('@/views/admin/monitor/grafana/index.vue'),
        name: 'MonitorGrafana',
        meta: {
          title: 'Grafana监控',
          icon: 'dashboard',
          roles: ['admin']
        }
      },
      {
        path: 'online',
        component: () => import('@/views/admin/monitor/online/index.vue'),
        name: 'MonitorOnline',
        meta: {
          title: '在线用户',
          icon: 'online',
          roles: ['admin']
        }
      },
      {
        path: 'logininfor',
        component: () => import('@/views/admin/monitor/logininfor/index.vue'),
        name: 'MonitorLoginLog',
        meta: {
          title: '登录日志',
          icon: 'logininfor',
          roles: ['admin']
        }
      },
      {
        path: 'operlog',
        component: () => import('@/views/admin/monitor/operlog/index.vue'),
        name: 'MonitorOperLog',
        meta: {
          title: '操作日志',
          icon: 'form',
          roles: ['admin']
        }
      },
      {
        path: 'server',
        component: () => import('@/views/admin/monitor/server/index.vue'),
        name: 'MonitorServer',
        meta: {
          title: '服务监控',
          icon: 'server',
          roles: ['admin']
        }
      },
      {
        path: 'cache',
        component: () => import('@/views/admin/monitor/cache/index.vue'),
        name: 'MonitorCache',
        meta: {
          title: '缓存监控',
          icon: 'redis',
          roles: ['admin']
        }
      },
      {
        path: 'job',
        component: () => import('@/views/admin/monitor/job/index.vue'),
        name: 'MonitorJob',
        meta: {
          title: '定时任务',
          icon: 'job',
          roles: ['admin']
        }
      }
    ]
  }
]

export default adminRoutes
