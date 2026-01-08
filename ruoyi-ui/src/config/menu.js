// 统一菜单配置
// 这里定义前端和后端共享的菜单结构

export const menuConfig = {
  // 前台博客菜单
  frontend: [
    {
      name: '首页',
      path: '/index',
      icon: 'home',
      type: 'frontend'
    },
    {
      name: '分类',
      path: '/blog/category',
      icon: 'category',
      type: 'frontend'
    },
    {
      name: '标签',
      path: '/blog/tag',
      icon: 'tag',
      type: 'frontend'
    },
    {
      name: '归档',
      path: '/blog/archive',
      icon: 'archive',
      type: 'frontend'
    },
    {
      name: '关于',
      path: '/about',
      icon: 'info',
      type: 'frontend'
    }
  ],

  // 后台管理菜单
  backend: [
    {
      name: '后台首页',
      path: '/admin/dashboard',
      icon: 'dashboard',
      permissions: ['admin'],
      type: 'backend'
    },
    {
      name: '博客管理',
      icon: 'documentation',
      permissions: ['admin', 'editor'],
      type: 'backend',
      children: [
        {
          name: '文章管理',
          path: '/admin/blog/article',
          icon: 'edit',
          permissions: ['admin', 'editor']
        },
        {
          name: '分类管理',
          path: '/admin/blog/category',
          icon: 'list',
          permissions: ['admin', 'editor']
        },
        {
          name: '标签管理',
          path: '/admin/blog/tag',
          icon: 'tag',
          permissions: ['admin', 'editor']
        },
        {
          name: '评论管理',
          path: '/admin/blog/comment',
          icon: 'message',
          permissions: ['admin', 'editor']
        },
        {
          name: '博客设置',
          path: '/admin/blog/setting',
          icon: 'setting',
          permissions: ['admin']
        },
        {
          name: '友链管理',
          path: '/admin/blog/friendLink',
          icon: 'link',
          permissions: ['admin']
        }
      ]
    },
    {
      name: '系统管理',
      icon: 'system',
      permissions: ['admin'],
      type: 'backend',
      children: [
        {
          name: '用户管理',
          path: '/admin/system/user',
          icon: 'user',
          permissions: ['admin']
        },
        {
          name: '角色管理',
          path: '/admin/system/role',
          icon: 'peoples',
          permissions: ['admin']
        },
        {
          name: '菜单管理',
          path: '/admin/system/menu',
          icon: 'tree-table',
          permissions: ['admin']
        },
        {
          name: '部门管理',
          path: '/admin/system/dept',
          icon: 'tree',
          permissions: ['admin']
        },
        {
          name: '岗位管理',
          path: '/admin/system/post',
          icon: 'post',
          permissions: ['admin']
        },
        {
          name: '字典管理',
          path: '/admin/system/dict',
          icon: 'dict',
          permissions: ['admin']
        },
        {
          name: '参数设置',
          path: '/admin/system/config',
          icon: 'edit',
          permissions: ['admin']
        },
        {
          name: '通知公告',
          path: '/admin/system/notice',
          icon: 'message',
          permissions: ['admin']
        }
      ]
    },
    {
      name: '系统监控',
      icon: 'monitor',
      permissions: ['admin'],
      type: 'backend',
      children: [
        {
          name: 'Actuator监控',
          path: '/admin/monitor/actuator',
          icon: 'monitor',
          permissions: ['admin']
        },
        {
          name: 'Prometheus监控',
          path: '/admin/monitor/prometheus',
          icon: 'chart',
          permissions: ['admin']
        },
        {
          name: 'Grafana监控',
          path: '/admin/monitor/grafana',
          icon: 'dashboard',
          permissions: ['admin']
        },
        {
          name: '在线用户',
          path: '/admin/monitor/online',
          icon: 'online',
          permissions: ['admin']
        },
        {
          name: '登录日志',
          path: '/admin/monitor/logininfor',
          icon: 'logininfor',
          permissions: ['admin']
        },
        {
          name: '操作日志',
          path: '/admin/monitor/operlog',
          icon: 'form',
          permissions: ['admin']
        },
        {
          name: '服务监控',
          path: '/admin/monitor/server',
          icon: 'server',
          permissions: ['admin']
        },
        {
          name: '缓存监控',
          path: '/admin/monitor/cache',
          icon: 'redis',
          permissions: ['admin']
        },
        {
          name: '定时任务',
          path: '/admin/monitor/job',
          icon: 'job',
          permissions: ['admin']
        }
      ]
    },
    {
      name: '系统工具',
      icon: 'tool',
      permissions: ['admin'],
      type: 'backend',
      children: [
        {
          name: '代码生成',
          path: '/admin/tool/gen',
          icon: 'code',
          permissions: ['admin']
        },
        {
          name: '系统接口',
          path: '/admin/tool/swagger',
          icon: 'swagger',
          permissions: ['admin']
        }
      ]
    },
    {
      name: '数据统计',
      icon: 'chart',
      permissions: ['admin'],
      type: 'backend',
      children: [
        {
          name: '数据概览',
          path: '/admin/statistics/overview',
          icon: 'overview',
          permissions: ['admin']
        },
        {
          name: '文章统计',
          path: '/admin/statistics/article',
          icon: 'documentation',
          permissions: ['admin']
        },
        {
          name: '用户统计',
          path: '/admin/statistics/user',
          icon: 'user',
          permissions: ['admin']
        }
      ]
    }
  ]
}

// 根据用户权限过滤菜单
export const getFilteredMenus = (userRole, menuType = 'backend') => {
  const menus = menuConfig[menuType] || []
  
  const filterMenus = (menuList) => {
    return menuList.filter(menu => {
      if (menu.permissions && !menu.permissions.includes(userRole)) {
        return false
      }
      
      if (menu.children) {
        menu.children = filterMenus(menu.children)
        // 如果子菜单为空，则过滤掉父菜单
        return menu.children.length > 0
      }
      
      return true
    })
  }
  
  return filterMenus(menus)
}

// 获取面包屑导航
export const getBreadcrumb = (path, menuType = 'backend') => {
  const menus = menuConfig[menuType] || []
  
  const findPath = (menuList, breadcrumb = []) => {
    for (const menu of menuList) {
      if (menu.path === path) {
        return [...breadcrumb, menu]
      }
      
      if (menu.children) {
        const result = findPath(menu.children, [...breadcrumb, menu])
        if (result) return result
      }
    }
    return null
  }
  
  return findPath(menus) || []
}

export default menuConfig