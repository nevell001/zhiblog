// 优化后的后台管理路由配置 - 消除路径冗余
import Layout from '@/layout/index.vue';

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
    path: '/admin/system',
    component: Layout,
    redirect: '/admin/system/user',
    name: 'System',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/admin/system/user/user/index.vue'),
        name: 'User',
        meta: { 
          title: '用户管理', 
          icon: 'user',
          permissions: ['system:user:list']
        }
      },
      {
        path: 'role',
        component: () => import('@/views/admin/system/role/role/index.vue'),
        name: 'Role',
        meta: { 
          title: '角色管理', 
          icon: 'peoples',
          permissions: ['system:role:list']
        }
      },
      {
        path: 'menu',
        component: () => import('@/views/admin/system/menu/menu/index.vue'),
        name: 'Menu',
        meta: { 
          title: '菜单管理', 
          icon: 'tree-table',
          permissions: ['system:menu:list']
        }
      },
      {
        path: 'dept',
        component: () => import('@/views/admin/system/dept/dept/index.vue'),
        name: 'Dept',
        meta: { 
          title: '部门管理', 
          icon: 'tree',
          permissions: ['system:dept:list']
        }
      },
      {
        path: 'post',
        component: () => import('@/views/admin/system/post/post/index.vue'),
        name: 'Post',
        meta: { 
          title: '岗位管理', 
          icon: 'post',
          permissions: ['system:post:list']
        }
      },
      {
        path: 'dict',
        component: () => import('@/views/admin/system/dict/dict/index.vue'),
        name: 'Dict',
        meta: { 
          title: '字典管理', 
          icon: 'dict',
          permissions: ['system:dict:list']
        }
      },
      {
        path: 'config',
        component: () => import('@/views/admin/system/config/config/index.vue'),
        name: 'Config',
        meta: { 
          title: '参数设置', 
          icon: 'edit',
          permissions: ['system:config:list']
        }
      },
      {
        path: 'notice',
        component: () => import('@/views/admin/system/notice/notice/index.vue'),
        name: 'Notice',
        meta: { 
          title: '通知公告', 
          icon: 'message',
          permissions: ['system:notice:list']
        }
      }
    ]
  },
  {
    path: '/admin/blog',
    component: Layout,
    redirect: '/admin/blog/article',
    name: 'Blog',
    meta: { title: '博客管理', icon: 'documentation', roles: ['admin', 'editor'] },
    children: [
      {
        path: 'article',
        component: () => import('@/views/admin/blog/article/article/index.vue'),
        name: 'Article',
        meta: { 
          title: '文章管理', 
          icon: 'edit',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'category',
        component: () => import('@/views/admin/blog/category/category/index.vue'),
        name: 'BlogCategory',
        meta: { 
          title: '分类管理', 
          icon: 'list',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'tag',
        component: () => import('@/views/admin/blog/tag/tag/index.vue'),
        name: 'BlogTag',
        meta: { 
          title: '标签管理', 
          icon: 'tag',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'comment',
        component: () => import('@/views/admin/blog/comment/comment/index.vue'),
        name: 'Comment',
        meta: { 
          title: '评论管理', 
          icon: 'message',
          roles: ['admin', 'editor']
        }
      },
      {
        path: 'setting',
        component: () => import('@/views/admin/blog/setting/setting/index.vue'),
        name: 'BlogSetting',
        meta: { 
          title: '博客设置', 
          icon: 'setting',
          roles: ['admin']
        }
      },
      {
        path: 'friendLink',
        component: () => import('@/views/admin/blog/friendLink/friendLink/index.vue'),
        name: 'FriendLink',
        meta: { 
          title: '友链管理', 
          icon: 'link',
          roles: ['admin']
        }
      }
    ]
  }
]

export default optimizedAdminRoutes