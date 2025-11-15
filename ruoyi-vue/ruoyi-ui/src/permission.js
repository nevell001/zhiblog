import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isPathMatch } from '@/utils/validate'
import useUserStore from '@/store/modules/user'
import usePermissionStore from '@/store/modules/permission'
import useSettingsStore from '@/store/modules/settings'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register', '/index', '/blog', '/blog/*', '/about', '/', '/blog/article/*', '/blog/category/*', '/blog/tag/*', '/blog/archive', '/blog/simple', '/blog/article', '/blog/category', '/blog/tag']

const isWhiteList = (path) => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

router.beforeEach((to, _from, next) => {
  NProgress.start()
  
  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      const userStore = useUserStore()
      const permissionStore = usePermissionStore()
      
      if (userStore.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        userStore.getInfo().then(() => {
          // 生成可访问的路由表
          permissionStore.generateRoutes().then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            if (accessRoutes && Array.isArray(accessRoutes)) {
              accessRoutes.forEach(route => {
                if (route && route.name && !router.hasRoute(route.name)) {
                  router.addRoute(route) // 动态添加可访问路由表
                }
              })
            }
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          }).catch(routeErr => {
            console.error('生成路由失败:', routeErr)
            // 路由生成失败时，尝试直接跳转
            next()
          })
        }).catch(err => {
          console.error('获取用户信息失败:', err)
          userStore.logOut().then(() => {
            next({ path: '/login' })
          }).catch(() => {
            next({ path: '/login' })
          })
        })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
