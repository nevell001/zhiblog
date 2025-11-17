// 后台管理路由配置
export const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/dashboard',
    meta: { title: '后台管理', icon: 'dashboard' },
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/admin/dashboard/index'),
        name: 'AdminDashboard',
        meta: { title: '后台首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/admin/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/system/user',
    name: 'System',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/admin/system/user/user/index'),
        name: 'User',
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'role',
        component: () => import('@/views/admin/system/role/role/index'),
        name: 'Role',
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'menu',
        component: () => import('@/views/admin/system/menu/menu/index'),
        name: 'Menu',
        meta: { title: '菜单管理', icon: 'tree-table' }
      },
      {
        path: 'dept',
        component: () => import('@/views/admin/system/dept/dept/index'),
        name: 'Dept',
        meta: { title: '部门管理', icon: 'tree' }
      },
      {
        path: 'post',
        component: () => import('@/views/admin/system/post/post/index'),
        name: 'Post',
        meta: { title: '岗位管理', icon: 'post' }
      },
      {
        path: 'dict',
        component: () => import('@/views/admin/system/dict/dict/index'),
        name: 'Dict',
        meta: { title: '字典管理', icon: 'dict' }
      },
      {
        path: 'config',
        component: () => import('@/views/admin/system/config/config/index'),
        name: 'Config',
        meta: { title: '参数设置', icon: 'edit' }
      },
      {
        path: 'notice',
        component: () => import('@/views/admin/system/notice/notice/index'),
        name: 'Notice',
        meta: { title: '通知公告', icon: 'message' }
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/monitor/online',
    name: 'Monitor',
    meta: { title: '系统监控', icon: 'monitor' },
    children: [
      {
        path: 'online',
        component: () => import('@/views/admin/monitor/online/index'),
        name: 'Online',
        meta: { title: '在线用户', icon: 'online' }
      },
      {
        path: 'logininfor',
        component: () => import('@/views/admin/monitor/logininfor/index'),
        name: 'Logininfor',
        meta: { title: '登录日志', icon: 'logininfor' }
      },
      {
        path: 'operlog',
        component: () => import('@/views/admin/monitor/operlog/index'),
        name: 'Operlog',
        meta: { title: '操作日志', icon: 'form' }
      },
      {
        path: 'druid',
        component: () => import('@/views/admin/monitor/druid/index'),
        name: 'Druid',
        meta: { title: '数据监控', icon: 'druid' }
      },
      {
        path: 'server',
        component: () => import('@/views/admin/monitor/server/index'),
        name: 'Server',
        meta: { title: '服务监控', icon: 'server' }
      },
      {
        path: 'cache',
        component: () => import('@/views/admin/monitor/cache/index'),
        name: 'Cache',
        meta: { title: '缓存监控', icon: 'redis' }
      },
      {
        path: 'job',
        component: () => import('@/views/admin/monitor/job/index'),
        name: 'Job',
        meta: { title: '定时任务', icon: 'job' }
      }
    ]
  },
  {
    path: '/admin/tool',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/tool/gen',
    name: 'Tool',
    meta: { title: '系统工具', icon: 'tool' },
    children: [
      {
        path: 'gen',
        component: () => import('@/views/admin/tool/gen/index'),
        name: 'Gen',
        meta: { title: '代码生成', icon: 'code' }
      },
      {
        path: 'swagger',
        component: () => import('@/views/admin/tool/swagger/index'),
        name: 'Swagger',
        meta: { title: '系统接口', icon: 'swagger' }
      }
    ]
  },
  {
    path: '/admin/blog',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/blog/article',
    name: 'Blog',
    meta: { title: '博客管理', icon: 'documentation', permissions: ['admin', 'editor'] },
    children: [
      {
        path: 'article',
        component: () => import('@/views/admin/blog/article/article/index'),
        name: 'Article',
        meta: { title: '文章管理', icon: 'edit', permissions: ['admin', 'editor'] }
      },
      {
        path: 'category',
        component: () => import('@/views/admin/blog/category/category/index'),
        name: 'BlogCategory',
        meta: { title: '分类管理', icon: 'list', permissions: ['admin', 'editor'] }
      },
      {
        path: 'tag',
        component: () => import('@/views/admin/blog/tag/tag/index'),
        name: 'BlogTag',
        meta: { title: '标签管理', icon: 'tag', permissions: ['admin', 'editor'] }
      },
      {
        path: 'comment',
        component: () => import('@/views/admin/blog/comment/comment/index'),
        name: 'Comment',
        meta: { title: '评论管理', icon: 'message', permissions: ['admin', 'editor'] }
      },
      {
        path: 'setting',
        component: () => import('@/views/admin/blog/setting/setting/index'),
        name: 'BlogSetting',
        meta: { title: '博客设置', icon: 'setting', permissions: ['admin'] }
      },
      {
        path: 'friendLink',
        component: () => import('@/views/admin/blog/friendLink/friendLink/index'),
        name: 'FriendLink',
        meta: { title: '友链管理', icon: 'link', permissions: ['admin', 'editor'] }
      }
    ]
  },
  {
    path: '/admin/statistics',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/statistics/overview',
    name: 'Statistics',
    meta: { title: '数据统计', icon: 'chart', permissions: ['admin'] },
    children: [
      {
        path: 'overview',
        component: () => import('@/views/admin/statistics/overview/index'),
        name: 'StatisticsOverview',
        meta: { title: '数据概览', icon: 'overview' }
      },
      {
        path: 'article',
        component: () => import('@/views/admin/statistics/article/index'),
        name: 'StatisticsArticle',
        meta: { title: '文章统计', icon: 'documentation' }
      },
      {
        path: 'user',
        component: () => import('@/views/admin/statistics/user/index'),
        name: 'StatisticsUser',
        meta: { title: '用户统计', icon: 'user' }
      }
    ]
  }

]

export default adminRoutes