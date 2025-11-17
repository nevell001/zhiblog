import { defineStore } from 'pinia'
import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
// const _modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: []
    }),
    actions: {
      setRoutes(routes) {
        this.addRoutes = routes
        this.routes = constantRoutes.concat(routes)
      },
      setDefaultRoutes(routes) {
        this.defaultRoutes = constantRoutes.concat(routes)
      },
      setTopbarRoutes(routes) {
        this.topbarRouters = routes
      },
      setSidebarRouters(routes) {
        this.sidebarRouters = routes
      },
      generateRoutes(_roles) {
        return new Promise(resolve => {
          // 向后端请求路由数据
          getRouters().then(res => {
            // 增强数据验证逻辑和容错机制
            if (!res || res.code !== 200 || !res.data || !Array.isArray(res.data)) {
              console.warn('路由数据格式错误或请求失败，使用前端路由配置:', res)
              // 使用前端路由配置作为后备方案
              const frontendRoutes = filterAsyncRouter([
                {
                  path: '/admin/dashboard',
                  component: 'admin/dashboard/index',
                  name: 'AdminDashboard',
                  meta: { title: '后台首页', icon: 'dashboard' }
                },
                {
                  path: '/admin/system',
                  component: 'Layout',
                  redirect: '/admin/system/user',
                  name: 'System',
                  meta: { title: '系统管理', icon: 'system' },
                  children: [
                    {
                      path: 'user',
                      component: 'admin/system/user/user/index',
                      name: 'User',
                      meta: { title: '用户管理', icon: 'user' }
                    },
                    {
                      path: 'role',
                      component: 'admin/system/role/role/index',
                      name: 'Role',
                      meta: { title: '角色管理', icon: 'peoples' }
                    },
                    {
                      path: 'menu',
                      component: 'admin/system/menu/menu/index',
                      name: 'Menu',
                      meta: { title: '菜单管理', icon: 'tree-table' }
                    },
                    {
                      path: 'dept',
                      component: 'admin/system/dept/dept/index',
                      name: 'Dept',
                      meta: { title: '部门管理', icon: 'tree' }
                    },
                    {
                      path: 'post',
                      component: 'admin/system/post/post/index',
                      name: 'Post',
                      meta: { title: '岗位管理', icon: 'post' }
                    },
                    {
                      path: 'dict',
                      component: 'admin/system/dict/dict/index',
                      name: 'Dict',
                      meta: { title: '字典管理', icon: 'dict' }
                    },
                    {
                      path: 'config',
                      component: 'admin/system/config/config/index',
                      name: 'Config',
                      meta: { title: '参数设置', icon: 'edit' }
                    },
                    {
                      path: 'notice',
                      component: 'admin/system/notice/notice/index',
                      name: 'Notice',
                      meta: { title: '通知公告', icon: 'message' }
                    }
                  ]
                },
                {
                  path: '/admin/monitor',
                  component: 'Layout',
                  redirect: '/admin/monitor/online',
                  name: 'Monitor',
                  meta: { title: '系统监控', icon: 'monitor' },
                  children: [
                    {
                      path: 'online',
                      component: 'admin/monitor/online/index',
                      name: 'Online',
                      meta: { title: '在线用户', icon: 'online' }
                    },
                    {
                      path: 'logininfor',
                      component: 'admin/monitor/logininfor/index',
                      name: 'Logininfor',
                      meta: { title: '登录日志', icon: 'logininfor' }
                    },
                    {
                      path: 'operlog',
                      component: 'admin/monitor/operlog/index',
                      name: 'Operlog',
                      meta: { title: '操作日志', icon: 'form' }
                    },
                    {
                      path: 'druid',
                      component: 'admin/monitor/druid/index',
                      name: 'Druid',
                      meta: { title: '数据监控', icon: 'druid' }
                    },
                    {
                      path: 'server',
                      component: 'admin/monitor/server/index',
                      name: 'Server',
                      meta: { title: '服务监控', icon: 'server' }
                    },
                    {
                      path: 'cache',
                      component: 'admin/monitor/cache/index',
                      name: 'Cache',
                      meta: { title: '缓存监控', icon: 'redis' }
                    },
                    {
                      path: 'job',
                      component: 'admin/monitor/job/index',
                      name: 'Job',
                      meta: { title: '定时任务', icon: 'job' }
                    }
                  ]
                },
                {
                  path: '/admin/tool',
                  component: 'Layout',
                  redirect: '/admin/tool/gen',
                  name: 'Tool',
                  meta: { title: '系统工具', icon: 'tool' },
                  children: [
                    {
                      path: 'gen',
                      component: 'admin/tool/gen/index',
                      name: 'Gen',
                      meta: { title: '代码生成', icon: 'code' }
                    },
                    {
                      path: 'swagger',
                      component: 'admin/tool/swagger/index',
                      name: 'Swagger',
                      meta: { title: '系统接口', icon: 'swagger' }
                    }
                  ]
                },
                {
                  path: '/admin/statistics',
                  component: 'Layout',
                  redirect: '/admin/statistics/overview',
                  name: 'Statistics',
                  meta: { title: '数据统计', icon: 'chart' },
                  children: [
                    {
                      path: 'overview',
                      component: 'admin/statistics/overview/index',
                      name: 'StatisticsOverview',
                      meta: { title: '数据概览', icon: 'overview' }
                    },
                    {
                      path: 'article',
                      component: 'admin/statistics/article/index',
                      name: 'StatisticsArticle',
                      meta: { title: '文章统计', icon: 'documentation' }
                    },
                    {
                      path: 'user',
                      component: 'admin/statistics/user/index',
                      name: 'StatisticsUser',
                      meta: { title: '用户统计', icon: 'user' }
                    }
                  ]
                }
              ])
              this.setSidebarRouters(constantRoutes.concat(frontendRoutes))
              resolve(frontendRoutes)
              return
            }
            
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            const defaultData = JSON.parse(JSON.stringify(res.data))
            
            try {
              const sidebarRoutes = filterAsyncRouter(sdata)
              const rewriteRoutes = filterAsyncRouter(rdata, false, true)
              const defaultRoutes = filterAsyncRouter(defaultData)
              const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
              
              // 清除现有动态路由
              const existingRoutes = router.getRoutes()
              existingRoutes.forEach(route => {
                if (route.name && route.name.startsWith('DynamicRoute_')) {
                  router.removeRoute(route.name)
                }
              })
              
              // 添加新的动态路由
              asyncRoutes.forEach((route, _index) => {
                if (route && route.name && !router.hasRoute(route.name)) {
                  router.addRoute(route)
                }
              })
              
              this.setRoutes(rewriteRoutes)
              this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
              this.setDefaultRoutes(sidebarRoutes)
              this.setTopbarRoutes(defaultRoutes)
              
              resolve(rewriteRoutes)
            } catch (error) {
              console.error('处理路由数据时出错:', error)
              // 返回空数组而不是抛出错误
              resolve([])
            }
          }).catch(error => {
            console.error('获取路由失败:', error)
            // 返回空数组而不是抛出错误
            resolve([])
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, _lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    // 检查路由是否隐藏
    if (route.hidden === true) {
      return false
    }
    
    // 修复路由路径，确保以/开头
    if (route.path && !route.path.startsWith('/') && !route.path.startsWith('http')) {
      route.path = '/' + route.path
    }
    
    // 统一为管理类路由添加/admin前缀，确保与实际路由定义一致
    // 需要添加/admin前缀的条件：
    // 1. 不是以http开头的外部链接
    // 2. 不是以/admin开头的已有管理路由
    // 3. 不是前台博客相关路由（以/blog开头）
    // 4. 不是登录、注册、404等公共路由
    // 5. 是系统管理、监控、工具等后台管理相关路由
    const isExternal = route.path.startsWith('http')
    const isAlreadyAdmin = route.path.startsWith('/admin')
    const isBlogRoute = route.path.startsWith('/blog')
    const isPublicRoute = ['/login', '/register', '/404', '/401', '/index', '/redirect'].some(path => route.path.startsWith(path))
    const isSystemRoute = ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(path => route.path.startsWith(path))
    
    // 如果是系统管理类路由且没有/admin前缀，则添加前缀
    if (!isExternal && !isAlreadyAdmin && !isBlogRoute && !isPublicRoute && isSystemRoute) {
      route.path = '/admin' + route.path
    }
    
    // 同样处理重定向路径
    if (route.redirect && !route.redirect.startsWith('http') && !route.redirect.startsWith('/admin') && 
        !route.redirect.startsWith('/blog') && route.redirect !== 'noRedirect' && 
        ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(path => route.redirect.startsWith(path))) {
      route.redirect = '/admin' + route.redirect
    }
    
    // 检查权限控制 - 增强错误处理和容错机制
    try {
      if (route.permissions) {
        if (!auth.hasPermiOr || !auth.hasPermiOr(route.permissions)) {
          console.warn('权限验证失败，跳过路由:', route.path, route.permissions)
          return false
        }
      }
      if (route.roles) {
        if (!auth.hasRoleOr || !auth.hasRoleOr(route.roles)) {
          console.warn('角色验证失败，跳过路由:', route.path, route.roles)
          return false
        }
      }
    } catch (error) {
      console.warn('权限验证出错，跳过路由:', route.path, error)
      return false
    }
    
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    
    // 组件加载处理 - 增强容错机制
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        try {
          route.component = loadView(route.component)
        } catch (error) {
          console.error('组件加载失败:', route.component, error)
          // 使用默认错误页面替代，但保留路由结构
          route.component = () => import('@/views/error/404.vue')
        }
      }
    }
    
    // 处理子路由 - 增强容错机制
    if (route.children != null && route.children && route.children.length) {
      try {
        route.children = filterAsyncRouter(route.children, route, type)
        // 如果子路由全部被过滤掉，则隐藏父路由
        if (route.children.length === 0) {
          return false
        }
      } catch (error) {
        console.error('处理子路由时出错:', route.path, error)
        // 保留父路由，清空子路由
        route.children = []
      }
    } else {
      delete route['children']
      delete route['redirect']
    }
    
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach(el => {
    el.path = lastRouter ? lastRouter.path + '/' + el.path : el.path
    if (el.children && el.children.length && el.component === 'ParentView') {
      children = children.concat(filterChildren(el.children, el))
    } else {
      children.push(el)
    }
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  /**
   * 组件加载函数
   * 根据统一的组件命名和路径规范加载组件，最多尝试2个路径
   * 遵循 COMPONENT_NAMING_AND_PATHING_CONVENTIONS.md 文档中的规范
   */
  
  // 1. 特殊页面处理 - 直接返回已知路径
  if (view === '404') {
    return () => import('@/views/error/404.vue')
  }
  if (view === '401') {
    return () => import('@/views/error/401.vue')
  }
  
  // 2. 路径规范化处理
  let normalizedPath = view
  
  // 移除末尾的 index 后缀（如有）
  if (normalizedPath.endsWith('/index')) {
    normalizedPath = normalizedPath.slice(0, -6)
  } else if (normalizedPath.endsWith('/index.vue')) {
    normalizedPath = normalizedPath.slice(0, -10)
  }
  
  // 3. 确定组件类型并构建加载路径
  // 根据规范，组件路径主要分为三类：
  // - 后台管理组件: @/views/admin/{module}/{submodule}/index.vue
  // - 前台博客组件: @/views/blog/{component}/index.vue
  // - 其他组件: @/views/{path}/index.vue
  
  // 处理已包含 admin/ 前缀的路径
  if (normalizedPath.startsWith('admin/')) {
    // 提取实际路径（移除 admin/ 前缀）
    const adminPath = normalizedPath.slice(6)
    
    // 构建主要尝试路径 - 遵循规范的标准路径
    const mainPath = `@/views/admin/${adminPath}/index.vue`
    
    // 返回异步加载函数，最多尝试2个路径
    return async () => {
      try {
        // 尝试主要路径 - 标准组件路径格式
        return await import(mainPath)
      } catch (err1) {
        // 尝试备选路径 - 组件直接在模块目录下
        try {
          return await import(`@/views/admin/${adminPath}.vue`)
        } catch (err2) {
          console.error(`loadView: 无法加载后台组件 ${view}，尝试路径: ${mainPath}`, err2)
          return import('@/views/error/404.vue')
        }
      }
    }
  }
  
  // 处理已包含 blog/ 前缀的前台组件
  if (normalizedPath.startsWith('blog/')) {
    const blogPath = normalizedPath.slice(5)
    const mainPath = `@/views/blog/${blogPath}/index.vue`
    
    return async () => {
      try {
        return await import(mainPath)
      } catch (err1) {
        try {
          return await import(`@/views/blog/${blogPath}.vue`)
        } catch (err2) {
          console.error(`loadView: 无法加载前台组件 ${view}，尝试路径: ${mainPath}`, err2)
          return import('@/views/error/404.vue')
        }
      }
    }
  }
  
  // 4. 通用组件路径处理（默认情况）
  // 假设是后台管理组件，并添加 admin/ 前缀
  const mainPath = `@/views/admin/${normalizedPath}/index.vue`
  
  return async () => {
    try {
      // 尝试主要路径 - 标准后台组件路径
      return await import(mainPath)
    } catch (err1) {
      // 尝试备选路径 - 无前缀的通用路径
      try {
        return await import(`@/views/${normalizedPath}/index.vue`)
      } catch (err2) {
        console.error(`loadView: 无法加载组件 ${view}，尝试路径: ${mainPath}`, err2)
        return import('@/views/error/404.vue')
      }
    }
  }
}


export default usePermissionStore
