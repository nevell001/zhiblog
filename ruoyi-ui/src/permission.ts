import router from './router'
// 暂时禁用 NProgress 以排查问题
// import NProgress from 'nprogress'
// import 'nprogress/nprogress.css'
import { getToken, removeToken } from '@/utils/auth'
import { isPathMatch } from '@/utils/validate'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'
import { useSettingsStore } from '@/stores/settings'
import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router'

// 确保路由已初始化
if (!router) {
  console.error('Router is not initialized')
}

// 暂时禁用 NProgress 以排查问题
// NProgress.configure({ showSpinner: false })

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

const isWhiteList = (path: string): boolean => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

// 防止重复导航的标志
let isNavigating = false

router.beforeEach(
  async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    console.log('🔄 路由守卫触发: from =', from.path, 'to =', to.path)

    // 如果已经在导航中，且目标路径相同，则取消导航
    if (isNavigating && to.path === from.path) {
      console.log('⚠️ 防止重复导航')
      return next(false)
    }

    isNavigating = true
    // 暂时禁用 NProgress 以排查问题
    // NProgress.start()

    try {
      if (getToken()) {
        console.log('✅ 有 token')
        to.meta.title && useSettingsStore().setTitle(to.meta.title as string)
        /* has token*/
        if (to.path === '/login') {
          console.log('🔄 有 token 但访问登录页，重定向到首页')
          // 如果有redirect参数，则重定向到指定路径
          const redirect = to.query.redirect as string
          if (redirect && redirect !== '/login' && redirect !== '/' && redirect !== '/index') {
            // 使用 router.replace 而不是 window.location.replace，避免页面刷新
            next({ path: redirect, replace: true })
          } else {
            // 使用 router.replace 而不是 window.location.replace，避免页面刷新
            next({ path: '/blog', replace: true })
          }
        } else {
          const userStore = useUserStore()
          const permissionStore = usePermissionStore()

          if (userStore.roles.length === 0) {
            console.log('📋 roles 为空，开始获取用户信息和路由...')
            // 判断当前用户是否已拉取完user_info信息
            try {
              await userStore.getInfo()
              console.log('✅ 获取用户信息成功, roles =', userStore.roles)
              // 生成可访问的路由表
              const accessRoutes = await permissionStore.generateRoutes()
              console.log('✅ 生成路由成功, 路由数量 =', accessRoutes.length)
              // 根据roles权限生成可访问的路由表
              if (accessRoutes && Array.isArray(accessRoutes)) {
                accessRoutes.forEach(route => {
                  if (route && route.name && !router.hasRoute(route.name as string)) {
                    console.log('➕ 添加路由:', route.name, route.path)
                    router.addRoute(route) // 动态添加可访问路由表
                  }
                })
              }
              console.log('✅ 所有路由添加完成，准备跳转到:', to.path)
              // 确保addRoutes已完成
              next({ ...to, replace: true })
              // NProgress.done()
            } catch (err) {
              console.error('❌ 获取用户信息或生成路由失败:', err)
              // 直接清除用户信息并重定向
              userStore.token = ''
              userStore.roles = []
              userStore.permissions = []
              removeToken()
              // 使用 router.replace 而不是 window.location.replace，避免页面刷新
              next({ path: '/login', replace: true })
            }
          } else {
            console.log('✅ roles 不为空，直接跳转')
            next()
          }
        }
      } else {
        console.log('❌ 没有 token')
        // 没有token
        if (isWhiteList(to.path)) {
          console.log('✅ 在白名单中，直接进入')
          // 在免登录白名单，直接进入
          next()
        } else {
          console.log('⚠️ 不在白名单中，重定向到登录页')
          // 避免循环重定向
          if (to.path !== '/login') {
            next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
            // NProgress.done()
          } else {
            next()
          }
        }
      }
    } catch (error) {
      console.error('❌ 路由守卫错误:', error)
      next(false)
    } finally {
      isNavigating = false
    }
  }
)

router.afterEach(() => {
  // NProgress.done()
})

export default router
