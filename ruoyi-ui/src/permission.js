import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, removeToken } from '@/utils/auth'
import { isPathMatch } from '@/utils/validate'
import useUserStore from '@/store/modules/user'
import usePermissionStore from '@/store/modules/permission'
import useSettingsStore from '@/store/modules/settings'

NProgress.configure({ showSpinner: false })

const whiteList = [
  '/login',
  '/register',
  '/index',
  '/blog',
  '/blog/*',
  '/about',
  '/',
  '/blog/article/*',
  '/blog/category/*',
  '/blog/tag/*',
  '/blog/archive',
  '/blog/simple',
  '/blog/article',
  '/blog/category',
  '/blog/tag'
]

const isWhiteList = path => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

// 防止重复导航的标志
let isNavigating = false

router.beforeEach(async (to, from, next) => {
  // 如果已经在导航中，且目标路径相同，则取消导航
  if (isNavigating && to.path === from.path) {
    return next(false)
  }

  isNavigating = true
  NProgress.start()

  try {
    if (getToken()) {
      to.meta.title && useSettingsStore().setTitle(to.meta.title)
      /* has token*/
      if (to.path === '/login') {
        // 如果有redirect参数，则重定向到指定路径
        const redirect = to.query.redirect
        if (redirect && redirect !== '/login' && redirect !== '/' && redirect !== '/index') {
          // 使用 window.location.replace 而不是 window.location.href 避免重复历史记录
          window.location.replace(redirect)
        } else {
          // 使用 window.location.replace 而不是 window.location.href 避免重复历史记录
          window.location.replace('/blog')
        }
      } else {
        const userStore = useUserStore()
        const permissionStore = usePermissionStore()

        if (userStore.roles.length === 0) {
          // 判断当前用户是否已拉取完user_info信息
          try {
            await userStore.getInfo()
            // 生成可访问的路由表
            const accessRoutes = await permissionStore.generateRoutes()
            // 根据roles权限生成可访问的路由表
            if (accessRoutes && Array.isArray(accessRoutes)) {
              accessRoutes.forEach(route => {
                if (route && route.name && !router.hasRoute(route.name)) {
                  router.addRoute(route) // 动态添加可访问路由表
                }
              })
            }
            // 确保addRoutes已完成
            next({ ...to, replace: true })
            NProgress.done()
          } catch (err) {
            console.error('获取用户信息或生成路由失败:', err)
            // 直接清除用户信息并重定向
            userStore.token = ''
            userStore.roles = []
            userStore.permissions = []
            removeToken()
            // 使用 window.location.replace 避免重复历史记录和路由循环
            window.location.replace('/login')
          }
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
        // 避免循环重定向
        if (to.path !== '/login') {
          next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
          NProgress.done()
        } else {
          next()
        }
      }
    }
  } catch (error) {
    console.error('路由守卫错误:', error)
    next(false)
  } finally {
    isNavigating = false
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
