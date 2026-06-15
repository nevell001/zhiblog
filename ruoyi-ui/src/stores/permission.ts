import { defineStore } from 'pinia'
import { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'
import { useUserStore } from '@/stores/user'
import type { RouteRecordRaw } from 'vue-router'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('../views/**/*.vue')

// 调试：输出 modules 的键（只输出前几个）
console.log('📦 Modules 对象初始化完成，总共有', Object.keys(modules).length, '个组件')
console.log('📦 前5个组件路径:', Object.keys(modules).slice(0, 5))

interface ViewState {
  routes: RouteRecordRaw[]
  addRoutes: RouteRecordRaw[]
  defaultRoutes: RouteRecordRaw[]
  topbarRouters: RouteRecordRaw[]
  sidebarRouters: RouteRecordRaw[]
}

const usePermissionStore = defineStore('permission', {
  state: (): ViewState => ({
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  }),

  actions: {
    setRoutes(routes: RouteRecordRaw[]): void {
      this.addRoutes = routes
      this.routes = constantRoutes.concat(routes)
    },

    setDefaultRoutes(routes: RouteRecordRaw[]): void {
      this.defaultRoutes = constantRoutes.concat(routes)
    },

    setTopbarRoutes(routes: RouteRecordRaw[]): void {
      this.topbarRouters = routes
    },

    setSidebarRouters(routes: RouteRecordRaw[]): void {
      this.sidebarRouters = routes
    },

    generateRoutes(): Promise<RouteRecordRaw[]> {
      return new Promise((resolve, reject) => {
        console.log('🚀 开始获取路由数据...')
        // 向后端请求路由数据
        getRouters()
          .then((res: any) => {
            console.log('✅ 成功获取路由数据:', res)
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            console.log('📋 开始过滤路由...')
            const sidebarRoutes = filterAsyncRouter(sdata)
            console.log('📋 侧边栏路由:', sidebarRoutes)
            const rewriteRoutes = filterAsyncRouter(rdata, false, true)
            console.log('📋 重写路由:', rewriteRoutes)
            rewriteRoutes.push({ path: '/:pathMatch(.*)*', redirect: '/404', hidden: true })
            this.setRoutes(constantRoutes)
            this.setSidebarRouters(sidebarRoutes)
            this.setDefaultRoutes(sidebarRoutes)
            this.setTopbarRoutes(rewriteRoutes)
            console.log('✅ 路由生成完成，总共', rewriteRoutes.length, '个路由')
            resolve(rewriteRoutes)
          })
          .catch(error => {
            console.error('❌ 获取路由数据失败:', error)
            reject(error)
          })
      })
    }
  }
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: any[], _lastRouter = false, type = false): any[] {
  return asyncRouterMap.filter(route => {
    // 过滤隐藏的路由
    if (route.hidden) {
      return false
    }

    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap: any[], lastRouter = false): any[] {
  let children: any[] = []
  childrenMap.forEach((el, _index) => {
    // 过滤隐藏的子路由
    if (el.hidden) {
      return
    }

    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c: any) => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter && lastRouter !== true) {
      el.path = (lastRouter as any).path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = (view: string) => {
  let res: any

  // 特殊处理个人中心路径
  if (view === 'user/profile' || view === 'profile') {
    console.log(`🔍 loadView: 检测到个人中心路径 ${view}，使用固定路径`)
    res = () => import('../views/admin/system/user/user/profile/index.vue')
    return res
  }

  // 构建所有可能的路径模式
  const possiblePaths = new Set<string>()

  // 1. 直接匹配
  possiblePaths.add(view)

  // 2. 添加 admin 前缀
  possiblePaths.add(`admin/${view}`)

  // 3. 添加 admin 前缀和 index 后缀
  possiblePaths.add(`admin/${view}/index`)

  // 4. 处理 system/user/user/index 这样的路径
  const parts = view.split('/')
  if (parts.length >= 3) {
    const lastPart = parts[parts.length - 1]
    const secondLastPart = parts[parts.length - 2]

    // 如果倒数两部分相同，说明已经是正确的格式
    if (secondLastPart === lastPart) {
      possiblePaths.add(`admin/${view}`)
      possiblePaths.add(`admin/${view}/index`)
    } else if (lastPart === 'index') {
      // 如果已经是 index，尝试去掉 index 后的路径
      const basePath = view.substring(0, view.lastIndexOf('/'))
      const lastDir = basePath.split('/').pop()
      if (lastDir) {
        possiblePaths.add(`admin/${basePath}/${lastDir}/index`)
      }
    } else {
      // 如果最后不是 index，尝试添加 index
      possiblePaths.add(`admin/${view}/index`)
      // 尝试重复最后一部分
      possiblePaths.add(`admin/${view}/${lastPart}/index`)
    }
  }

  // 转换为数组
  const pathsArray = Array.from(possiblePaths)

  console.log(`🔍 loadView: 尝试加载组件 ${view}`)
  console.log('🔍 loadView: 尝试的路径:', pathsArray)

  // 在所有模块中查找匹配的路径
  for (const path in modules) {
    // 移除 ../views/ 前缀和 .vue 后缀，得到相对路径
    const dir = path.replace(/^\.\.\/views\//, '').replace('.vue', '')

    // 检查是否匹配任何可能的路径
    if (pathsArray.includes(dir)) {
      res = () => modules[path]()
      console.log(`✅ 成功加载组件: ${view} -> ${dir}`)
      break
    }
  }

  // 如果没有找到匹配的组件，返回一个默认组件
  if (!res) {
    console.error(`❌ 无法加载组件: ${view}，请检查组件路径是否正确`)
    console.log('尝试的路径:', pathsArray)
    // 只显示统计相关的可用路径，避免日志过多
    const relevantPaths = Object.keys(modules)
      .map(p => p.replace(/^\.\.\/views\//, '').replace('.vue', ''))
      .filter(
        p =>
          p.includes('statistics') ||
          p.includes('system') ||
          p.includes('monitor') ||
          p.includes('blog')
      )
    console.log('相关的可用组件路径:', relevantPaths)
  }

  return res
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: any[]): any[] {
  const res: any[] = []
  routes.forEach(route => {
    if (route.permissions) {
      if (hasPermission(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (hasRole(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

// 验证是否有权限
function hasPermission(requiredPermissions: string[]): boolean {
  const all_permission = '*:*:*'
  const userPermissions = useUserStore().permissions

  // 如果没有设置权限要求，默认允许访问
  if (!requiredPermissions || requiredPermissions.length === 0) {
    return true
  }

  if (userPermissions && userPermissions.length > 0) {
    return userPermissions.some(v => {
      return all_permission === v || requiredPermissions.includes(v)
    })
  } else {
    return hasRole(['admin', 'editor'])
  }
}

// 验证是否有角色
function hasRole(requiredRoles: string[]): boolean {
  const super_admin = 'admin'
  const userRoles = useUserStore().roles

  if (!requiredRoles || requiredRoles.length === 0) {
    return true
  }

  if (userRoles && userRoles.length > 0) {
    return userRoles.some(v => {
      return super_admin === v || requiredRoles.includes(v)
    })
  } else {
    return import.meta.env.DEV
  }
}

export { usePermissionStore }
