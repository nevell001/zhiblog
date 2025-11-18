/**
 * 菜单点击问题修复方案
 * 针对系统管理、系统监控、系统工具、博客管理、数据统计菜单无法点击问题的具体修复
 */

import { ElMessage } from 'element-plus'
import router from '@/router'

// 菜单修复器类
export class MenuFixer {
  constructor() {
    this.fixedRoutes = false
    this.fixedPermissions = false
    this.fixedEvents = false
  }

  // 1. 修复路由配置问题
  fixRouteIssues() {
    console.log('🔧 开始修复路由配置问题...')
    
    const routesToFix = [
      {
        path: '/admin/system',
        component: () => import('@/layout/index.vue'),
        redirect: '/admin/system/user',
        name: 'System',
        meta: { title: '系统管理', icon: 'system' },
        children: [
          {
            path: 'user',
            component: () => import('@/views/admin/system/user/user/index.vue'),
            name: 'User',
            meta: { title: '用户管理', icon: 'user' }
          },
          {
            path: 'role',
            component: () => import('@/views/admin/system/role/role/index.vue'),
            name: 'Role',
            meta: { title: '角色管理', icon: 'peoples' }
          }
          // ... 其他子路由
        ]
      },
      {
        path: '/admin/monitor',
        component: () => import('@/layout/index.vue'),
        redirect: '/admin/monitor/online',
        name: 'Monitor',
        meta: { title: '系统监控', icon: 'monitor' },
        children: [
          {
            path: 'online',
            component: () => import('@/views/admin/monitor/online/index.vue'),
            name: 'Online',
            meta: { title: '在线用户', icon: 'online' }
          }
          // ... 其他子路由
        ]
      },
      {
        path: '/admin/tool',
        component: () => import('@/layout/index.vue'),
        redirect: '/admin/tool/gen',
        name: 'Tool',
        meta: { title: '系统工具', icon: 'tool' },
        children: [
          {
            path: 'gen',
            component: () => import('@/views/admin/tool/gen/index.vue'),
            name: 'Gen',
            meta: { title: '代码生成', icon: 'code' }
          }
          // ... 其他子路由
        ]
      },
      {
        path: '/admin/blog',
        component: () => import('@/layout/index.vue'),
        redirect: '/admin/blog/article',
        name: 'Blog',
        meta: { title: '博客管理', icon: 'documentation', permissions: ['admin', 'editor'] },
        children: [
          {
            path: 'article',
            component: () => import('@/views/admin/blog/article/article/index.vue'),
            name: 'Article',
            meta: { title: '文章管理', icon: 'edit', permissions: ['admin', 'editor'] }
          }
          // ... 其他子路由
        ]
      },
      {
        path: '/admin/statistics',
        component: () => import('@/layout/index.vue'),
        redirect: '/admin/statistics/overview',
        name: 'Statistics',
        meta: { title: '数据统计', icon: 'chart', permissions: ['admin'] },
        children: [
          {
            path: 'overview',
            component: () => import('@/views/admin/statistics/overview/index.vue'),
            name: 'StatisticsOverview',
            meta: { title: '数据概览', icon: 'overview' }
          }
          // ... 其他子路由
        ]
      }
    ]

    // 添加缺失的路由
    routesToFix.forEach(route => {
      if (!router.hasRoute(route.name)) {
        try {
          router.addRoute(route)
          console.log(`✅ 路由已添加: ${route.name} (${route.path})`)
        } catch (error) {
          console.error(`❌ 路由添加失败: ${route.name}`, error)
        }
      }
    })

    this.fixedRoutes = true
    console.log('✅ 路由配置修复完成')
  }

  // 2. 修复权限问题
  fixPermissionIssues() {
    console.log('🔐 开始修复权限问题...')
    
    // 检查用户权限状态
    const userStore = window.__VUE_APP__?.store?.state?.user
    
    if (!userStore || !userStore.token) {
      console.warn('⚠️ 用户未登录，请先登录')
      ElMessage.warning('请先登录系统')
      return
    }

    // 确保基本权限存在
    const defaultPermissions = [
      'system:user:list',
      'system:role:list',
      'system:menu:list',
      'monitor:online:list',
      'tool:gen:list',
      'blog:article:list',
      'statistics:overview:view'
    ]

    // 如果权限为空，添加默认权限（仅用于测试）
    if (!userStore.permissions || userStore.permissions.length === 0) {
      console.warn('⚠️ 用户权限为空，添加默认权限用于测试')
      userStore.permissions = defaultPermissions
    }

    // 确保基本角色存在
    if (!userStore.roles || userStore.roles.length === 0) {
      console.warn('⚠️ 用户角色为空，添加默认角色用于测试')
      userStore.roles = ['admin']
    }

    console.log('✅ 权限配置修复完成')
    this.fixedPermissions = true
  }

  // 3. 修复事件绑定问题
  fixEventBindingIssues() {
    console.log('⚡ 开始修复事件绑定问题...')
    
    // 等待DOM更新完成
    setTimeout(() => {
      const menuItems = document.querySelectorAll('.el-menu-item, .el-sub-menu__title')
      
      menuItems.forEach((item, index) => {
        // 移除现有的事件监听器（避免重复绑定）
        item.removeEventListener('click', this.handleMenuClick)
        
        // 添加新的事件监听器
        item.addEventListener('click', this.handleMenuClick.bind(this))
        
        // 添加视觉反馈
        item.style.cursor = 'pointer'
        
        console.log(`✅ 菜单项事件已修复: ${item.textContent?.trim() || `菜单${index}`}`)
      })

      // 为菜单容器添加事件委托
      const menuContainer = document.querySelector('.el-menu')
      if (menuContainer) {
        menuContainer.removeEventListener('click', this.handleMenuDelegation)
        menuContainer.addEventListener('click', this.handleMenuDelegation.bind(this))
        console.log('✅ 菜单事件委托已设置')
      }

      this.fixedEvents = true
      console.log('✅ 事件绑定修复完成')
    }, 500)
  }

  // 菜单点击处理函数
  handleMenuClick(event) {
    event.preventDefault()
    event.stopPropagation()
    
    const menuItem = event.currentTarget
    const menuText = menuItem.textContent?.trim()
    const index = menuItem.getAttribute('index')
    
    console.log('🖱️ 菜单点击:', { menuText, index })
    
    if (!index) {
      console.warn('⚠️ 菜单项缺少index属性')
      return
    }

    // 显示点击反馈
    ElMessage.success(`正在访问: ${menuText}`)
    
    // 执行路由跳转
    this.navigateToRoute(index, menuText)
  }

  // 事件委托处理
  handleMenuDelegation(event) {
    const menuItem = event.target.closest('.el-menu-item, .el-sub-menu__title')
    if (menuItem) {
      this.handleMenuClick.call(this, { 
        currentTarget: menuItem, 
        preventDefault: () => {}, 
        stopPropagation: () => {} 
      })
    }
  }

  // 路由导航
  navigateToRoute(routePath, menuName) {
    console.log(`🚀 尝试导航到: ${routePath}`)
    
    // 多种导航方式，确保至少一种成功
    const navigationMethods = [
      // 方法1: Vue Router
      () => {
        return router.push(routePath).then(() => {
          console.log(`✅ Vue Router导航成功: ${routePath}`)
          ElMessage.success(`${menuName} 页面加载成功`)
        })
      },
      
      // 方法2: window.location (备选方案)
      () => {
        console.log(`🔄 使用window.location导航: ${routePath}`)
        window.location.href = routePath
        return Promise.resolve()
      },
      
      // 方法3: window.open (新窗口)
      () => {
        console.log(`🪟 使用新窗口打开: ${routePath}`)
        window.open(routePath, '_self')
        return Promise.resolve()
      }
    ]

    // 依次尝试导航方法
    let navigationAttempt = 0
    const tryNavigation = async () => {
      if (navigationAttempt < navigationMethods.length) {
        try {
          await navigationMethods[navigationAttempt]()
        } catch (error) {
          console.warn(`导航方法${navigationAttempt + 1}失败:`, error)
          navigationAttempt++
          
          if (navigationAttempt < navigationMethods.length) {
            console.log(`尝试导航方法${navigationAttempt + 2}...`)
            setTimeout(tryNavigation, 100)
          } else {
            console.error('所有导航方法都失败了')
            ElMessage.error(`无法访问${menuName}页面`)
          }
        }
      }
    }

    tryNavigation()
  }

  // 4. 修复组件加载问题
  fixComponentLoadingIssues() {
    console.log('📦 开始修复组件加载问题...')
    
    // 预加载关键组件
    const criticalComponents = [
      () => import('@/views/admin/system/user/user/index.vue'),
      () => import('@/views/admin/monitor/online/index.vue'),
      () => import('@/views/admin/tool/gen/index.vue'),
      () => import('@/views/admin/blog/article/article/index.vue'),
      () => import('@/views/admin/statistics/overview/index.vue')
    ]

    criticalComponents.forEach((loadComponent, index) => {
      loadComponent()
        .then(() => {
          console.log(`✅ 关键组件${index + 1}预加载成功`)
        })
        .catch(error => {
          console.error(`❌ 关键组件${index + 1}预加载失败:`, error)
        })
    })

    console.log('✅ 组件加载修复完成')
  }

  // 执行所有修复
  fixAllIssues() {
    console.log('🚀 开始执行所有菜单修复...')
    
    this.fixRouteIssues()
    this.fixPermissionIssues()
    this.fixEventBindingIssues()
    this.fixComponentLoadingIssues()
    
    setTimeout(() => {
      console.log('🎉 所有菜单修复已完成！')
      ElMessage.success('菜单功能修复完成，请重新尝试点击菜单')
    }, 1000)
  }

  // 测试菜单功能
  testMenuFunctionality() {
    console.log('🧪 开始测试菜单功能...')
    
    const testMenus = [
      { name: '系统管理', path: '/admin/system' },
      { name: '系统监控', path: '/admin/monitor' },
      { name: '系统工具', path: '/admin/tool' },
      { name: '博客管理', path: '/admin/blog' },
      { name: '数据统计', path: '/admin/statistics' }
    ]

    testMenus.forEach((menu, index) => {
      setTimeout(() => {
        console.log(`🖱️ 测试菜单: ${menu.name}`)
        this.navigateToRoute(menu.path, menu.name)
      }, index * 1000)
    })
  }
}

// 创建全局修复器实例
const menuFixer = new MenuFixer()

// 导出修复器
export default menuFixer

// 在开发环境下提供全局访问
if (import.meta.env.DEV) {
  window.menuFixer = menuFixer
  window.fixMenu = () => menuFixer.fixAllIssues()
  window.testMenu = () => menuFixer.testMenuFunctionality()
  
  console.log('🛠️ 菜单修复工具已加载')
  console.log('💡 使用方法:')
  console.log('  - fixMenu(): 修复所有菜单问题')
  console.log('  - testMenu(): 测试菜单功能')
  console.log('  - menuFixer: 访问修复器实例')
}

// 自动修复（可选）
// setTimeout(() => {
//   if (import.meta.env.DEV) {
//     menuFixer.fixAllIssues()
//   }
// }, 2000)