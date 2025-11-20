import { createRouter, createWebHistory } from 'vue-router'
import useUserStore from '@/store/modules/user'
import auth from '@/plugins/auth'

/* Layout */
import Layout from '@/layout/index.vue'

// 博客前台路由
import blogRoutes from './blog'
// 后台管理路由
import adminRoutes from './admin-optimized'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/',
    redirect: '/blog',
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/admin/system/user/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/admin/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/admin/system/user/user/authRole'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/admin/system/user' }
      }
    ]
  },
  {
    path: '/admin/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/admin/system/role/role/authUser'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/admin/system/role' }
      }
    ]
  },
  {
    path: '/admin/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/admin/system/dict/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/admin/system/dict' }
      }
    ]
  },
  {
    path: '/admin/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/admin/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/admin/monitor/job' }
      }
    ]
  },
  {
    path: '/admin/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/admin/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/admin/tool/gen' }
      }
    ]
  }
]

// 合并所有路由 - 调整注册顺序确保博客路由优先匹配
const routes = [
  // 首先注册静态路由
  ...constantRoutes,
  // 然后注册博客相关路由
  ...blogRoutes,
  // 最后注册管理路由和动态路由
  ...adminRoutes,
  ...dynamicRoutes,
  // 确保404页面放在最后
  { path: '/:pathMatch(.*)*', redirect: '/404' }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  scrollBehavior: () => ({ top: 0 })
})

// 路由配置完成

// 添加路由守卫，处理权限验证和路由匹配
router.beforeEach((to, from, next) => {
  // 获取用户信息
  const userStore = useUserStore()
  const token = userStore.token
  
  // 白名单路由（无需登录即可访问）
  const whiteList = ['/login', '/register', '/404', '/401', '/about']
  
  // 首先检查是否为管理后台路由，如果是且用户未登录，直接重定向到登录页
  if (to.path.startsWith('/admin') && !token) {
    console.log(`🔐 未登录用户访问管理后台页面 ${to.path}，重定向到登录页`)
    next(`/login?redirect=${to.path}`)
    return
  }
  
  // 检查是否为白名单路由或博客相关路由
  // 确保所有博客相关路由（包括首页和about页面）都能匿名访问
  if (whiteList.includes(to.path) || to.path.startsWith('/blog') || to.path === '/' || to.path === '/index') {
    console.log(`✅ 白名单路由或博客路由，允许匿名访问: ${to.path}`)
    next()
    return
  }
  
  // 检查用户是否已登录
  if (token) {
    console.log(`🔑 用户已登录，访问: ${to.path}`)
    
    // 已登录用户访问登录页，重定向到首页
    if (to.path === '/login') {
      next({ path: '/' })
      return
    }
    
    // 检查用户权限
    if (to.meta && to.meta.permissions) {
      if (!auth.hasPermiOr(to.meta.permissions)) {
        console.log(`❌ 权限不足，需要权限: ${to.meta.permissions}`)
        next('/401')
        return
      }
    }
    
    // 检查用户角色
    if (to.meta && to.meta.roles) {
      if (!auth.hasRoleOr(to.meta.roles)) {
        console.log(`❌ 角色不足，需要角色: ${to.meta.roles}`)
        console.log(`👤 当前用户角色: ${userStore.roles}`)
        next('/401')
        return
      }
    }
    
    console.log(`✅ 权限验证通过，访问: ${to.path}`)
    next()
  } else {
    console.log(`🔒 用户未登录，访问: ${to.path}`)
    
    // 检查是否为博客相关路由，如果是则直接允许访问
    if (to.path.startsWith('/blog') || to.path === '/' || to.path === '/index') {
      console.log(`✅ 博客相关路由，允许匿名用户访问: ${to.path}`)
      next()
      return
    }
    
    // 未登录用户访问需要权限的页面，重定向到登录页
    if (to.meta && (to.meta.permissions || to.meta.roles)) {
      console.log(`🔐 需要权限，重定向到登录页: ${to.path}`)
      next(`/login?redirect=${to.path}`)
      return
    }
    
    // 未登录用户访问管理后台，重定向到登录页
    if (to.path.startsWith('/admin')) {
      console.log(`🔐 未登录用户访问管理页面 ${to.path}，重定向到登录页`)
      next(`/login?redirect=${to.path}`)
      return
    }
    
    // 其他非博客、非管理、非权限页面，允许匿名访问
    next()
  }
})

export default router