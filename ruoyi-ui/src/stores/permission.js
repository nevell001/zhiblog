import { defineStore } from 'pinia'
import { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'
import useUserStore from '@/store/modules/user'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore('permission', {
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
    generateRoutes() {
      return new Promise(resolve => {
        // 向后端请求路由数据
        getRouters().then(res => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          const asyncRoutes = filterDynamicRoutes(dynamicRoutes)
          rewriteRoutes.push({ path: '/:pathMatch(.*)*', redirect: '/404', hidden: true })
          this.setRoutes(constantRoutes)
          this.setSidebarRouters(sidebarRoutes)
          this.setDefaultRoutes(sidebarRoutes)
          this.setTopbarRoutes(rewriteRoutes)
          resolve(rewriteRoutes)
        })
      })
    }
  }
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
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

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    // 过滤隐藏的子路由
    if (el.hidden) {
      return
    }
    
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
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

export const loadView = (view) => {
  let res;
  for (const path in modules) {
    const dir = path.replace('../../views/', '').replace('.vue', '');
    // 尝试直接匹配
    if (dir === view) {
      res = () => modules[path]();
      break;
    }
    // 尝试添加 admin/ 前缀匹配（博客管理）
    if (dir === `admin/${view}`) {
      res = () => modules[path]();
      break;
    }
    // 尝试添加 admin/ 并重复最后一层目录（系统管理、系统工具等）
    // 例如：system/user/index -> admin/system/user/user/index
    const parts = view.split('/');
    if (parts.length >= 2) {
      const lastPart = parts[parts.length - 1];
      const adminPath = `admin/${view.replace(lastPart, '')}${lastPart}/${lastPart}`;
      if (dir === adminPath) {
        res = () => modules[path]();
        break;
      }
    }
  }
  return res;
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
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
function hasPermission(requiredPermissions) {
  const all_permission = "*:*:*";
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
    // 如果用户没有权限信息，但路由需要权限，则检查用户角色
    return hasRole(['admin', 'editor']) // 默认允许管理员和编辑角色
  }
}

// 验证是否有角色
function hasRole(requiredRoles) {
  const super_admin = "admin";
  const userRoles = useUserStore().roles
  
  // 如果没有设置角色要求，默认允许访问
  if (!requiredRoles || requiredRoles.length === 0) {
    return true
  }
  
  if (userRoles && userRoles.length > 0) {
    return userRoles.some(v => {
      return super_admin === v || requiredRoles.includes(v)
    })
  } else {
    // 如果用户没有角色信息，默认允许访问（开发环境）
    return process.env.NODE_ENV === 'development'
  }
}

export default usePermissionStore
