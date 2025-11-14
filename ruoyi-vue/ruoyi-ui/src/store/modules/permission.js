import { defineStore } from 'pinia'
import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      // 设置默认的侧边栏路由，确保菜单能够显示
      sidebarRouters: [
        {
          path: '/home',
          component: Layout,
          name: 'Home',
          meta: { title: '首页', icon: 'home' },
          children: [
            {
              path: 'index',
              component: () => import('@/views/index.vue'),
              name: 'Dashboard',
              meta: { title: '控制台', icon: 'dashboard' }
            }
          ]
        },
        {
          path: '/demo',
          component: Layout,
          name: 'Demo',
          meta: { title: '演示菜单', icon: 'example' },
          children: [
            {
              path: 'test',
              component: () => import('@/views/index.vue'),
              name: 'TestPage',
              meta: { title: '测试页面', icon: 'test' }
            }
          ]
        }
      ]
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
      generateRoutes(roles) {
        return new Promise(resolve => {
          console.log('generateRoutes被调用了!')
          console.log('传入的roles:', roles)
          
          // 直接使用mock路由数据，确保菜单能够显示
          const mockRoutes = [
            {
              path: '/home',
              component: Layout,
              name: 'Home',
              meta: { title: '首页', icon: 'home' },
              children: [
                {
                  path: 'index',
                  component: () => import('@/views/index.vue'),
                  name: 'Dashboard',
                  meta: { title: '控制台', icon: 'dashboard' }
                }
              ]
            },
            {
              path: '/demo',
              component: Layout,
              name: 'Demo',
              meta: { title: '演示菜单', icon: 'example' },
              children: [
                {
                  path: 'test',
                  component: () => import('@/views/index.vue'),
                  name: 'TestPage',
                  meta: { title: '测试页面', icon: 'test' }
                }
              ]
            }
          ]
          
          console.log('mock路由数据:', mockRoutes)
          // 直接使用mockRoutes作为侧边栏路由，确保菜单显示
          this.setSidebarRouters(mockRoutes)
          console.log('设置后sidebarRouters:', this.sidebarRouters)
          this.setRoutes(mockRoutes)
          this.setDefaultRoutes(mockRoutes)
          this.setTopbarRoutes(mockRoutes)
          
          console.log('路由生成完成!')
          resolve(mockRoutes)
        })
      }
    }
  })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
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
  // 使用Vite兼容的静态路径导入
  if (view === '404') {
    return () => import('@/views/error/404.vue')
  }
  if (view === '401') {
    return () => import('@/views/error/401.vue')
  }
  
  // 使用try-catch处理动态导入
  try {
    // 为Vite添加忽略警告注释
    return () => import(/* @vite-ignore */ `@/views/${view}.vue`)
  } catch (error) {
    try {
      return () => import(/* @vite-ignore */ `@/views/${view}/index.vue`)
    } catch (e) {
      console.warn(`未找到组件: ${view}，将使用404页面替代`)
      return () => import('@/views/error/404.vue')
    }
  }
}

// 为了确保侧边栏路由被正确设置，添加一个初始化函数
export function initPermissionStore() {
  const store = usePermissionStore()
  // 直接使用mock路由数据，确保菜单能够显示
  const mockRoutes = [
    {
      path: '/home',
      component: Layout,
      name: 'Home',
      meta: { title: '首页', icon: 'home' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/index.vue'),
          name: 'Dashboard',
          meta: { title: '控制台', icon: 'dashboard' }
        }
      ]
    },
    {
      path: '/demo',
      component: Layout,
      name: 'Demo',
      meta: { title: '演示菜单', icon: 'example' },
      children: [
        {
          path: 'test',
          component: () => import('@/views/index.vue'),
          name: 'TestPage',
          meta: { title: '测试页面', icon: 'test' }
        }
      ]
    }
  ]
  
  if (store.sidebarRouters.length === 0) {
    store.setSidebarRouters(mockRoutes)
    console.log('初始化默认侧边栏路由:', store.sidebarRouters)
  }
}

export default usePermissionStore
