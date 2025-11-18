import { defineStore } from 'pinia'
import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

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
            // 改进数据验证逻辑，增加容错性
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
                      component: 'admin/system/user/index',
                      name: 'User',
                      meta: { title: '用户管理', icon: 'user' }
                    },
                    {
                      path: 'role',
                      component: 'admin/system/role/index',
                      name: 'Role',
                      meta: { title: '角色管理', icon: 'peoples' }
                    },
                    {
                      path: 'menu',
                      component: 'admin/system/menu/index',
                      name: 'Menu',
                      meta: { title: '菜单管理', icon: 'tree-table' }
                    },
                    {
                      path: 'dept',
                      component: 'admin/system/dept/index',
                      name: 'Dept',
                      meta: { title: '部门管理', icon: 'tree' }
                    },
                    {
                      path: 'post',
                      component: 'admin/system/post/index',
                      name: 'Post',
                      meta: { title: '岗位管理', icon: 'post' }
                    },
                    {
                      path: 'dict',
                      component: 'admin/system/dict/index',
                      name: 'Dict',
                      meta: { title: '字典管理', icon: 'dict' }
                    },
                    {
                      path: 'config',
                      component: 'admin/system/config/index',
                      name: 'Config',
                      meta: { title: '参数设置', icon: 'edit' }
                    },
                    {
                      path: 'notice',
                      component: 'admin/system/notice/index',
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
              resolve([])
            }
          }).catch(error => {
            console.error('获取路由失败:', error)
            resolve([])
          })
        })
      }
    }
  })

// 优化后的路由过滤函数 - 标记权限而非过滤
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
    
    // 统一为管理类路由添加/admin前缀
    const isExternal = route.path.startsWith('http')
    const isAlreadyAdmin = route.path.startsWith('/admin')
    const isBlogRoute = route.path.startsWith('/blog')
    const isPublicRoute = ['/login', '/register', '/404', '/401', '/index', '/redirect'].some(path => route.path.startsWith(path))
    const isSystemRoute = ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(path => route.path.startsWith(path))
    
    if (!isExternal && !isAlreadyAdmin && !isBlogRoute && !isPublicRoute && isSystemRoute) {
      route.path = '/admin' + route.path
    }
    
    // 处理重定向路径
    if (route.redirect && !route.redirect.startsWith('http') && !route.redirect.startsWith('/admin') && 
        !route.redirect.startsWith('/blog') && route.redirect !== 'noRedirect' && 
        ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(path => route.redirect.startsWith(path))) {
      route.redirect = '/admin' + route.redirect
    }
    
    // 🔥 关键改进1: 放宽权限验证 - 标记而非过滤
    try {
      let hasPermission = true
      let permissionReason = ''
      
      if (route.permissions) {
        if (!auth.hasPermiOr(route.permissions)) {
          hasPermission = false
          permissionReason = `权限不足: ${route.permissions.join(', ')}`
        }
      }
      if (route.roles) {
        if (!auth.hasRoleOr(route.roles)) {
          hasPermission = false
          permissionReason = `角色不足: ${route.roles.join(', ')}`
        }
      }
      
      // 在meta中标记权限状态，而不是过滤路由
      if (!hasPermission) {
        if (!route.meta) route.meta = {}
        route.meta.requiresAuth = true
        route.meta.hasPermission = false
        route.meta.permissionReason = permissionReason
        console.warn(`权限验证失败，保留路由但标记权限不足: ${route.path} - ${permissionReason}`)
      }
      
    } catch (error) {
      console.warn('权限验证出错，保留路由:', route.path, error)
      // 出错时保留路由，标记为权限验证异常
      if (!route.meta) route.meta = {}
      route.meta.requiresAuth = true
      route.meta.hasPermission = false
      route.meta.permissionReason = '权限验证异常'
    }
    
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    
    // 🔥 关键改进2: 优化组件路径解析
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
    
    // 处理子路由
    if (route.children != null && route.children && route.children.length) {
      try {
        route.children = filterAsyncRouter(route.children, route, type)
        // 即使子路由全部被过滤，也保留父路由
      } catch (error) {
        console.error('处理子路由时出错:', route.path, error)
        route.children = []
      }
    } else {
      delete route['children']
      delete route['redirect']
    }
    
    return true // 🔥 关键改进: 始终返回true，保留所有路由
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
    // 🔥 关键改进: 标记权限而非过滤
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        route.meta = route.meta || {}
        route.meta.hasPermission = true
        res.push(route)
      } else {
        route.meta = route.meta || {}
        route.meta.hasPermission = false
        route.meta.permissionReason = `权限不足: ${route.permissions.join(', ')}`
        res.push(route) // 保留路由但标记权限不足
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        route.meta = route.meta || {}
        route.meta.hasPermission = true
        res.push(route)
      } else {
        route.meta = route.meta || {}
        route.meta.hasPermission = false
        route.meta.permissionReason = `角色不足: ${route.roles.join(', ')}`
        res.push(route) // 保留路由但标记权限不足
      }
    } else {
      // 没有权限要求，直接添加
      route.meta = route.meta || {}
      route.meta.hasPermission = true
      res.push(route)
    }
  })
  return res
}

// 🔥 关键改进3: 优化组件路径解析，增加容错处理
export const loadView = (view) => {
  /**
   * 组件加载函数 - 优化版本
   * 增加多路径尝试和容错机制
   */
  
  // 1. 特殊页面处理
  if (view === '404') {
    return () => import(/* @vite-ignore */ '@/views/error/404.vue')
  }
  if (view === '401') {
    return () => import(/* @vite-ignore */ '@/views/error/401.vue')
  }
  
  // 2. 路径规范化处理
  let normalizedPath = view.trim()
  
  // 移除末尾的 index 后缀
  if (normalizedPath.endsWith('/index')) {
    normalizedPath = normalizedPath.slice(0, -6)
  } else if (normalizedPath.endsWith('/index.vue')) {
    normalizedPath = normalizedPath.slice(0, -10)
  }
  
  // 🔥 关键改进: 多路径尝试策略
  const generatePaths = (basePath) => {
    const paths = []
    
    // 标准路径: @/views/admin/{path}/index.vue
    paths.push(`@/views/admin/${basePath}/index.vue`)
    
    // 简化路径: @/views/admin/{path}.vue
    paths.push(`@/views/admin/${basePath}.vue`)
    
    // 备选路径: @/views/{path}/index.vue
    paths.push(`@/views/${basePath}/index.vue`)
    
    // 最终备选: @/views/{path}.vue
    paths.push(`@/views/${basePath}.vue`)
    
    return paths
  }
  
  // 3. 根据路径类型生成尝试路径
  let attemptPaths = []
  
  if (normalizedPath.startsWith('admin/')) {
    // 已包含admin前缀
    const adminPath = normalizedPath.slice(6)
    attemptPaths = generatePaths(adminPath)
  } else if (normalizedPath.startsWith('blog/')) {
    // 前台博客组件
    const blogPath = normalizedPath.slice(5)
    attemptPaths = [
      `@/views/blog/${blogPath}/index.vue`,
      `@/views/blog/${blogPath}.vue`,
      `@/views/${blogPath}/index.vue`,
      `@/views/${blogPath}.vue`
    ]
  } else {
    // 默认作为后台管理组件处理
    attemptPaths = generatePaths(normalizedPath)
  }
  
  // 4. 返回异步加载函数
  return async () => {
    const errors = []
    
    // 依次尝试所有可能的路径
    for (let i = 0; i < attemptPaths.length; i++) {
      const path = attemptPaths[i]
      try {
        console.log(`尝试加载组件: ${view} -> ${path}`)
        const module = await import(/* @vite-ignore */ path)
        console.log(`组件加载成功: ${view} <- ${path}`)
        return module
      } catch (error) {
        errors.push({ path, error })
        console.warn(`组件加载失败: ${path}`, error.message)
      }
    }
    
    // 所有路径都尝试失败，记录详细错误信息
    console.error(`loadView: 无法加载组件 ${view}，已尝试以下路径:`, attemptPaths)
    errors.forEach(({ path, error }) => {
      console.error(`  - ${path}: ${error.message}`)
    })
    
    // 返回404页面作为最后的备选
    return import('@/views/error/404.vue')
  }
}

export default usePermissionStore