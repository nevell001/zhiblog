// 优化后的后台管理路由配置 - 消除路径冗余
export const optimizedAdminRoutes = [
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
        component: () => import('@/views/admin/system/user/index'), // 优化：移除重复的user目录
        name: 'User',
        meta: { 
          title: '用户管理', 
          icon: 'user',
          permissions: ['system:user:list'], // 细化权限控制
          actions: { // 功能级权限控制
            add: 'system:user:add',
            edit: 'system:user:edit',
            delete: 'system:user:remove',
            export: 'system:user:export',
            import: 'system:user:import'
          }
        }
      },
      {
        path: 'role',
        component: () => import('@/views/admin/system/role/index'), // 优化：移除重复的role目录
        name: 'Role',
        meta: { 
          title: '角色管理', 
          icon: 'peoples',
          permissions: ['system:role:list'],
          actions: {
            add: 'system:role:add',
            edit: 'system:role:edit',
            delete: 'system:role:remove',
            auth: 'system:role:auth'
          }
        }
      },
      {
        path: 'menu',
        component: () => import('@/views/admin/system/menu/index'), // 优化：移除重复的menu目录
        name: 'Menu',
        meta: { 
          title: '菜单管理', 
          icon: 'tree-table',
          permissions: ['system:menu:list'],
          actions: {
            add: 'system:menu:add',
            edit: 'system:menu:edit',
            delete: 'system:menu:remove'
          }
        }
      },
      {
        path: 'dept',
        component: () => import('@/views/admin/system/dept/index'), // 优化：移除重复的dept目录
        name: 'Dept',
        meta: { 
          title: '部门管理', 
          icon: 'tree',
          permissions: ['system:dept:list'],
          actions: {
            add: 'system:dept:add',
            edit: 'system:dept:edit',
            delete: 'system:dept:remove'
          }
        }
      },
      {
        path: 'post',
        component: () => import('@/views/admin/system/post/index'), // 优化：移除重复的post目录
        name: 'Post',
        meta: { 
          title: '岗位管理', 
          icon: 'post',
          permissions: ['system:post:list'],
          actions: {
            add: 'system:post:add',
            edit: 'system:post:edit',
            delete: 'system:post:remove'
          }
        }
      },
      {
        path: 'dict',
        component: () => import('@/views/admin/system/dict/index'), // 优化：移除重复的dict目录
        name: 'Dict',
        meta: { 
          title: '字典管理', 
          icon: 'dict',
          permissions: ['system:dict:list'],
          actions: {
            add: 'system:dict:add',
            edit: 'system:dict:edit',
            delete: 'system:dict:remove'
          }
        }
      },
      {
        path: 'config',
        component: () => import('@/views/admin/system/config/index'), // 优化：移除重复的config目录
        name: 'Config',
        meta: { 
          title: '参数设置', 
          icon: 'edit',
          permissions: ['system:config:list'],
          actions: {
            add: 'system:config:add',
            edit: 'system:config:edit',
            delete: 'system:config:remove',
            refresh: 'system:config:refresh'
          }
        }
      },
      {
        path: 'notice',
        component: () => import('@/views/admin/system/notice/index'), // 优化：移除重复的notice目录
        name: 'Notice',
        meta: { 
          title: '通知公告', 
          icon: 'message',
          permissions: ['system:notice:list'],
          actions: {
            add: 'system:notice:add',
            edit: 'system:notice:edit',
            delete: 'system:notice:remove'
          }
        }
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
        component: () => import('@/views/admin/blog/article/index'), // 优化：移除重复的article目录
        name: 'Article',
        meta: { 
          title: '文章管理', 
          icon: 'edit', 
          permissions: ['admin', 'editor'],
          actions: {
            add: 'blog:article:add',
            edit: 'blog:article:edit',
            delete: 'blog:article:remove',
            publish: 'blog:article:publish',
            draft: 'blog:article:draft'
          }
        }
      },
      {
        path: 'category',
        component: () => import('@/views/admin/blog/category/index'), // 优化：移除重复的category目录
        name: 'BlogCategory',
        meta: { 
          title: '分类管理', 
          icon: 'list', 
          permissions: ['admin', 'editor'],
          actions: {
            add: 'blog:category:add',
            edit: 'blog:category:edit',
            delete: 'blog:category:remove'
          }
        }
      },
      {
        path: 'tag',
        component: () => import('@/views/admin/blog/tag/index'), // 优化：移除重复的tag目录
        name: 'BlogTag',
        meta: { 
          title: '标签管理', 
          icon: 'tag', 
          permissions: ['admin', 'editor'],
          actions: {
            add: 'blog:tag:add',
            edit: 'blog:tag:edit',
            delete: 'blog:tag:remove'
          }
        }
      },
      {
        path: 'comment',
        component: () => import('@/views/admin/blog/comment/index'), // 优化：移除重复的comment目录
        name: 'Comment',
        meta: { 
          title: '评论管理', 
          icon: 'message', 
          permissions: ['admin', 'editor'],
          actions: {
            audit: 'blog:comment:audit',
            delete: 'blog:comment:remove',
            reply: 'blog:comment:reply'
          }
        }
      },
      {
        path: 'setting',
        component: () => import('@/views/admin/blog/setting/index'), // 优化：移除重复的setting目录
        name: 'BlogSetting',
        meta: { 
          title: '博客设置', 
          icon: 'setting', 
          permissions: ['admin'],
          actions: {
            edit: 'blog:setting:edit',
            refresh: 'blog:setting:refresh'
          }
        }
      },
      {
        path: 'friendLink',
        component: () => import('@/views/admin/blog/friendLink/index'), // 优化：移除重复的friendLink目录
        name: 'FriendLink',
        meta: { 
          title: '友链管理', 
          icon: 'link', 
          permissions: ['admin', 'editor'],
          actions: {
            add: 'blog:friendLink:add',
            edit: 'blog:friendLink:edit',
            delete: 'blog:friendLink:remove'
          }
        }
      }
    ]
  }
]

export default optimizedAdminRoutes