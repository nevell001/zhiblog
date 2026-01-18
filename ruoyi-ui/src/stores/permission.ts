import { defineStore } from 'pinia'
import { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'
import { useUserStore } from '@/stores/user'
import type { RouteRecordRaw } from 'vue-router'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

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
        // 向后端请求路由数据
        getRouters().then((res: any) => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          rewriteRoutes.push({ path: '/:pathMatch(.*)*', redirect: '/404', hidden: true })
          this.setRoutes(constantRoutes)
          this.setSidebarRouters(sidebarRoutes)
          this.setDefaultRoutes(sidebarRoutes)
          this.setTopbarRoutes(rewriteRoutes)
          resolve(rewriteRoutes)
        }).catch(error => {
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
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = (view: string) => {
  let res: any
  for (const path in modules) {
    const dir = path.replace('../../views/', '').replace('.vue', '')
    // 尝试直接匹配
    if (dir === view) {
      res = () => modules[path]()
      break
    }
    // 尝试添加 admin/ 前缀匹配（博客管理）
    if (dir === `admin/${view}`) {
      res = () => modules[path]()
      break
    }
    // 尝试添加 admin/ 并重复最后一层目录（系统管理、系统工具等）
    const parts = view.split('/')
    if (parts.length >= 3) {
      const lastPart = parts[parts.length - 1]
      const secondLastPart = parts[parts.length - 2]
      // 如果倒数两部分相同，说明已经是正确的格式
      if (secondLastPart === lastPart) {
        const adminPath = `admin/${view}`
        if (dir === adminPath) {
          res = () => modules[path]()
          break
        }
      } else if (lastPart === 'index') {
        const basePath = view.substring(0, view.lastIndexOf('/'))
        const adminPath = `admin/${basePath}/${basePath.split('/').pop()}/index`
        if (dir === adminPath) {
          res = () => modules[path]()
          break
        }
      }
    }
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
    return process.env.NODE_ENV === 'development'
  }
}

export { usePermissionStore }
