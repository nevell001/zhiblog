// 优化后的后台管理路由配置 - 简化层级结构，统一权限管理
import Layout from '@/layout/index.vue'

export const optimizedAdminRoutes = [
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
        path: 'article/detail/:id(\\d+)',
        component: () => import('@/views/blog/article/detail.vue'),
        name: 'BlogArticleDetail',
        meta: { 
          title: '文章详情', 
          activeMenu: '/admin/blog/article',
          roles: ['admin', 'editor']
        },
        hidden: true
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
    path: '/admin/system',
    component: Layout,
    redirect: '/admin/system/user',
    name: 'SystemManagement',
    meta: { title: '系统管理', icon: 'system', roles: ['admin'] },
    children: [
      {
        path: 'user',
        component: () => import('@/views/admin/system/user/user/index.vue'),
        name: 'SystemUser',
        meta: { 
          title: '用户管理', 
          icon: 'user',
          permissions: ['system:user:list']
        }
      },
      {
        path: 'role',
        component: () => import('@/views/admin/system/role/role/index.vue'),
        name: 'SystemRole',
        meta: { 
          title: '角色管理', 
          icon: 'peoples',
          permissions: ['system:role:list']
        }
      },
      {
        path: 'menu',
        component: () => import('@/views/admin/system/menu/menu/index.vue'),
        name: 'SystemMenu',
        meta: { 
          title: '菜单管理', 
          icon: 'tree-table',
          permissions: ['system:menu:list']
        }
      },
      {
        path: 'dept',
        component: () => import('@/views/admin/system/dept/dept/index.vue'),
        name: 'SystemDept',
        meta: { 
          title: '部门管理', 
          icon: 'tree',
          permissions: ['system:dept:list']
        }
      },
      {
        path: 'post',
        component: () => import('@/views/admin/system/post/post/index.vue'),
        name: 'SystemPost',
        meta: { 
          title: '岗位管理', 
          icon: 'post',
          permissions: ['system:post:list']
        }
      },
      {
        path: 'dict',
        component: () => import('@/views/admin/system/dict/dict/index.vue'),
        name: 'SystemDict',
        meta: { 
          title: '字典管理', 
          icon: 'dict',
          permissions: ['system:dict:list']
        }
      },
      {
        path: 'config',
        component: () => import('@/views/admin/system/config/config/index.vue'),
        name: 'SystemConfig',
        meta: { 
          title: '参数设置', 
          icon: 'edit',
          permissions: ['system:config:list']
        }
      },
      {
        path: 'notice',
        component: () => import('@/views/admin/system/notice/notice/index.vue'),
        name: 'SystemNotice',
        meta: { 
          title: '通知公告', 
          icon: 'message',
          permissions: ['system:notice:list']
        }
      }
    ]
  },
  {
    path: '/admin/statistics',
    component: Layout,
    redirect: '/admin/statistics/visit',
    name: 'DataStatistics',
    meta: { title: '数据统计', icon: 'chart', roles: ['admin'] },
    children: [
      {
        path: 'visit',
        component: () => import('@/views/admin/statistics/visit/index.vue'),
        name: 'StatisticsVisit',
        meta: { 
          title: '访问统计', 
          icon: 'eye',
          permissions: ['statistics:visit:list']
        }
      },
      {
        path: 'article',
        component: () => import('@/views/admin/statistics/article/index.vue'),
        name: 'StatisticsArticle',
        meta: { 
          title: '文章统计', 
          icon: 'documentation',
          permissions: ['statistics:article:list']
        }
      },
      {
        path: 'user',
        component: () => import('@/views/admin/statistics/user/index.vue'),
        name: 'StatisticsUser',
        meta: { 
          title: '用户统计', 
          icon: 'peoples',
          permissions: ['statistics:user:list']
        }
      },
      {
        path: 'comment',
        component: () => import('@/views/admin/statistics/comment/index.vue'),
        name: 'StatisticsComment',
        meta: { 
          title: '评论统计', 
          icon: 'message',
          permissions: ['statistics:comment:list']
        }
      },
      {
        path: 'traffic',
        component: () => import('@/views/admin/statistics/traffic/index.vue'),
        name: 'StatisticsTraffic',
        meta: { 
          title: '流量分析', 
          icon: 'guide',
          permissions: ['statistics:traffic:list']
        }
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: Layout,
    redirect: '/admin/monitor/online',
    name: 'SystemMonitor',
    meta: { title: '系统监控', icon: 'monitor', roles: ['admin'] },
    children: [
      {
        path: 'online',
        component: () => import('@/views/admin/monitor/online/index.vue'),
        name: 'MonitorOnline',
        meta: { 
          title: '在线用户', 
          icon: 'online',
          permissions: ['monitor:online:list']
        }
      },
      {
        path: 'logininfor',
        component: () => import('@/views/admin/monitor/logininfor/index.vue'),
        name: 'MonitorLoginLog',
        meta: { 
          title: '登录日志', 
          icon: 'logininfor',
          permissions: ['monitor:logininfor:list']
        }
      },
      {
        path: 'operlog',
        component: () => import('@/views/admin/monitor/operlog/index.vue'),
        name: 'MonitorOperLog',
        meta: { 
          title: '操作日志', 
          icon: 'form',
          permissions: ['monitor:operlog:list']
        }
      },
      {
        path: 'druid',
        component: () => import('@/views/admin/monitor/druid/index.vue'),
        name: 'MonitorDruid',
        meta: { 
          title: '数据监控', 
          icon: 'druid',
          permissions: ['monitor:druid:list']
        }
      },
      {
        path: 'server',
        component: () => import('@/views/admin/monitor/server/index.vue'),
        name: 'MonitorServer',
        meta: { 
          title: '服务监控', 
          icon: 'server',
          permissions: ['monitor:server:list']
        }
      },
      {
        path: 'cache',
        component: () => import('@/views/admin/monitor/cache/index.vue'),
        name: 'MonitorCache',
        meta: { 
          title: '缓存监控', 
          icon: 'redis',
          permissions: ['monitor:cache:list']
        }
      },
      {
        path: 'job',
        component: () => import('@/views/admin/monitor/job/index.vue'),
        name: 'MonitorJob',
        meta: { 
          title: '定时任务', 
          icon: 'job',
          permissions: ['monitor:job:list']
        }
      }
    ]
  },
  {
    path: '/admin/tool',
    component: Layout,
    redirect: '/admin/tool/gen',
    name: 'SystemTool',
    meta: { title: '系统工具', icon: 'tool', roles: ['admin'] },
    children: [
      {
        path: 'gen',
        component: () => import('@/views/admin/tool/gen/index.vue'),
        name: 'ToolGen',
        meta: { 
          title: '代码生成', 
          icon: 'code',
          permissions: ['tool:gen:list']
        }
      },
      {
        path: 'swagger',
        component: () => import('@/views/admin/tool/swagger/index.vue'),
        name: 'ToolSwagger',
        meta: { 
          title: '系统接口', 
          icon: 'swagger',
          permissions: ['tool:swagger:list']
        }
      }
    ]
  }
]

export default optimizedAdminRoutes