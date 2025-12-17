import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/blog'
  },
  {
    path: '/blog',
    component: () => import('@/views/blog/index.vue')
  },
  {
    path: '/blog/article/:id',
    component: () => import('@/views/blog/article/detail.vue'),
    name: 'PublicBlogArticleDetail'
  },
  {
    path: '/blog/category/:id',
    component: () => import('@/views/blog/category/index.vue'),
    name: 'PublicBlogCategory'
  },
  {
    path: '/blog/tag/:id',
    component: () => import('@/views/blog/tag/index.vue'),
    name: 'PublicBlogTag'
  },
  {
    path: '/login',
    component: () => import('@/views/login.vue')
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
        path: 'test/image-compress',
        name: 'ImageCompressTest',
        component: () => import('@/views/admin/test/ImageCompressTest.vue'),
        meta: { title: '图片压缩测试', icon: 'picture' }
      }
    ]
  },
  // 系统管理
  {
    path: '/admin/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/system/user',
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
  // 数据统计
  {
    path: '/admin/statistics',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/statistics/overview',
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
      }
    ]
  },
  {
    path: '/admin/monitor',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/monitor/server',
    children: [
      {
        path: 'server',
        name: 'Server',
        component: () => import('@/views/admin/monitor/server/index.vue'),
        meta: { title: '服务监控', icon: 'server' }
      }
    ]
  },
  {
    path: '/admin/tool',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/tool/build',
    children: [
      {
        path: 'build',
        name: 'Build',
        component: () => import('@/views/admin/tool/gen/index.vue'),
        meta: { title: '表单构建', icon: 'build' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 添加错误处理
  scrollBehavior(to, from, savedPosition) {
    // 解决页面跳转后滚动位置问题
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 导出路由配置
export const constantRoutes = routes
// 动态路由（通常为空，因为我们的路由都是静态的）
export const dynamicRoutes = []

export default router
