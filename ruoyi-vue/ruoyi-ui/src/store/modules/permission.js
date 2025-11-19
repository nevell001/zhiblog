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
                  path: '/admin/blog',
                  component: 'Layout',
                  redirect: '/admin/blog/article',
                  name: 'Blog',
                  meta: { title: '博客管理', icon: 'documentation' },
                  children: [
                    {
                      path: 'article',
                      component: 'admin/blog/article/article/index',
                      name: 'BlogArticle',
                      meta: { title: '文章管理', icon: 'documentation' }
                    },
                    {
                      path: 'category',
                      component: 'admin/blog/category/category/index',
                      name: 'BlogCategory',
                      meta: { title: '分类管理', icon: 'component' }
                    },
                    {
                      path: 'tag',
                      component: 'admin/blog/tag/tag/index',
                      name: 'BlogTag',
                      meta: { title: '标签管理', icon: 'tag' }
                    },
                    {
                      path: 'comment',
                      component: 'admin/blog/comment/comment/index',
                      name: 'BlogComment',
                      meta: { title: '评论管理', icon: 'message' }
                    },
                    {
                      path: 'setting',
                      component: 'admin/blog/setting/setting/index',
                      name: 'BlogSetting',
                      meta: { title: '博客设置', icon: 'edit' }
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

// 优化后的路由过滤函数 - 性能优化版本
function filterAsyncRouter(asyncRouterMap, _lastRouter = false, type = false) {
  // 缓存权限验证结果，避免重复计算
  const permissionCache = new Map()
  
  return asyncRouterMap.filter(route => {
    // 1. 快速路径：隐藏的路由直接过滤
    if (route.hidden === true) {
      return false
    }
    
    // 2. 路径规范化 - 批量处理提升性能
    if (route.path && !route.path.startsWith('/') && !route.path.startsWith('http')) {
      route.path = '/' + route.path
    }
    
    // 3. 路径前缀优化 - 减少判断次数
    const pathType = getPathType(route.path)
    if (pathType.needsAdminPrefix) {
      route.path = '/admin' + route.path
    }
    
    // 4. 重定向路径处理
    if (route.redirect && pathType.needsAdminRedirect) {
      route.redirect = '/admin' + route.redirect
    }
    
    // 🔥 性能优化1: 缓存权限验证结果
    try {
      const cacheKey = `${route.permissions?.join(',') || ''}_${route.roles?.join(',') || ''}`
      let permissionResult = permissionCache.get(cacheKey)
      
      if (!permissionResult) {
        let hasPermission = true
        let permissionReason = ''
        
        // 只检查必要的权限
        if (route.permissions && route.permissions.length > 0) {
          hasPermission = auth.hasPermiOr(route.permissions)
          if (!hasPermission) {
            permissionReason = `权限不足: ${route.permissions.join(', ')}`
          }
        }
        
        if (hasPermission && route.roles && route.roles.length > 0) {
          hasPermission = auth.hasRoleOr(route.roles)
          if (!hasPermission) {
            permissionReason = `角色不足: ${route.roles.join(', ')}`
          }
        }
        
        permissionResult = { hasPermission, permissionReason }
        permissionCache.set(cacheKey, permissionResult)
      }
      
      // 在meta中标记权限状态，而不是过滤路由
      if (!permissionResult.hasPermission) {
        if (!route.meta) route.meta = {}
        route.meta.requiresAuth = true
        route.meta.hasPermission = false
        route.meta.permissionReason = permissionResult.permissionReason
      }
      
    } catch (error) {
      // 减少错误日志，提升性能
      if (!route.meta) route.meta = {}
      route.meta.requiresAuth = true
      route.meta.hasPermission = false
      route.meta.permissionReason = '权限验证异常'
    }
    
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    
    // 🔥 性能优化2: 组件解析优化
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        // 延迟组件加载，不在路由过滤时立即加载
        route.component = loadView(route.component)
      }
    }
    
    // 5. 子路由处理优化
    if (route.children && route.children.length > 0) {
      try {
        route.children = filterAsyncRouter(route.children, route, type)
      } catch (error) {
        route.children = []
      }
    } else {
      delete route['children']
      delete route['redirect']
    }
    
    return true // 🔥 关键改进: 始终返回true，保留所有路由
  })
}

// 🔥 新增辅助函数：路径类型判断 - 提升判断性能
function getPathType(path) {
  if (!path) {
    return { needsAdminPrefix: false, needsAdminRedirect: false }
  }
  
  const isExternal = path.startsWith('http')
  const isAlreadyAdmin = path.startsWith('/admin')
  const isBlogRoute = path.startsWith('/blog')
  const isPublicRoute = ['/login', '/register', '/404', '/401', '/index', '/redirect'].some(p => path.startsWith(p))
  const isSystemRoute = ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(p => path.startsWith(p))
  
  return {
    needsAdminPrefix: !isExternal && !isAlreadyAdmin && !isBlogRoute && !isPublicRoute && isSystemRoute,
    needsAdminRedirect: !path.startsWith('http') && !path.startsWith('/admin') && !path.startsWith('/blog') && 
                         path !== 'noRedirect' && ['/system', '/monitor', '/tool', '/statistics', '/blog-setting'].some(p => path.startsWith(p))
  }
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

// 🔥 关键改进3: 优化组件路径解析，提升性能
export const loadView = (view) => {
  /**
   * 组件加载函数 - 性能优化版本
   * 缓存已加载的组件，减少重复加载
   */
  
  // 组件缓存，避免重复加载
  const componentCache = new Map()
  
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
  
  // 🔥 性能优化: 优先级路径策略，减少尝试次数
  const generateOptimizedPaths = (basePath) => {
    const paths = []
    
    // 根据常见组件结构确定优先级
    if (basePath.includes('/')) {
      // 嵌套路径优先使用完整路径
      paths.push(`@/views/admin/${basePath}/index.vue`)
      paths.push(`@/views/${basePath}/index.vue`)
    } else {
      // 简单路径优先使用简化路径
      paths.push(`@/views/admin/${basePath}.vue`)
      paths.push(`@/views/${basePath}.vue`)
    }
    
    // 备选路径
    paths.push(`@/views/admin/${basePath}/index.vue`)
    paths.push(`@/views/${basePath}/index.vue`)
    
    return paths
  }
  
  // 3. 根据路径类型生成尝试路径
  let attemptPaths = []
  
  if (normalizedPath.startsWith('admin/')) {
    // 已包含admin前缀
    const adminPath = normalizedPath.slice(6)
    attemptPaths = generateOptimizedPaths(adminPath)
  } else if (normalizedPath.startsWith('blog/')) {
    // 前台博客组件 - 优化路径尝试顺序
    const blogPath = normalizedPath.slice(5)
    attemptPaths = [
      `@/views/blog/${blogPath}/index.vue`,
      `@/views/blog/${blogPath}.vue`
    ]
  } else {
    // 默认作为后台管理组件处理
    attemptPaths = generateOptimizedPaths(normalizedPath)
  }
  
  // 4. 返回异步加载函数 - 优化错误处理和性能
  return async () => {
    // 检查缓存
    if (componentCache.has(view)) {
      return componentCache.get(view)
    }
    
    const errors = []
    
    // 依次尝试所有可能的路径
    for (let i = 0; i < attemptPaths.length; i++) {
      const path = attemptPaths[i]
      try {
        // 减少日志输出，提升性能
        const module = await import(/* @vite-ignore */ path)
        
        // 缓存成功加载的组件
        componentCache.set(view, module)
        return module
      } catch (error) {
        errors.push({ path, error })
        // 只在开发环境下输出警告日志
        if (import.meta.env.DEV) {
          console.warn(`组件加载失败: ${path}`)
        }
      }
    }
    
    // 只在开发环境下输出详细错误信息
    if (import.meta.env.DEV) {
      console.error(`loadView: 无法加载组件 ${view}，已尝试以下路径:`, attemptPaths)
    }
    
    // 返回404页面作为最后的备选
    return import('@/views/error/404.vue')
  }
}

export default usePermissionStore