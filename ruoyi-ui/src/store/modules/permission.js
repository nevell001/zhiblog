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
          // 🔥 使用简化的前端菜单配置，直接提供菜单结构用于侧边栏显示
          console.log('🔧 使用前端菜单配置，直接提供菜单结构用于侧边栏显示')

          // 简化的菜单配置，不包含复杂的组件路径
          const frontendRoutes = [
                {
                  path: '/admin/dashboard',
                  name: 'AdminDashboard',
                  meta: { title: '后台首页', icon: 'dashboard' }
                },
                {
                  path: '/admin/blog',
                  name: 'Blog',
                  meta: { title: '博客管理', icon: 'documentation' },
                  children: [
                    {
                      path: 'article',
                      name: 'BlogArticle',
                      meta: { title: '文章管理', icon: 'documentation' }
                    },
                    {
                      path: 'category',
                      name: 'BlogCategory',
                      meta: { title: '分类管理', icon: 'component' }
                    },
                    {
                      path: 'tag',
                      name: 'BlogTag',
                      meta: { title: '标签管理', icon: 'tag' }
                    },
                    {
                      path: 'comment',
                      name: 'BlogComment',
                      meta: { title: '评论管理', icon: 'message' }
                    },
                    {
                      path: 'setting',
                      name: 'BlogSetting',
                      meta: { title: '博客设置', icon: 'edit' }
                    },
                    {
                      path: 'friendLink',
                      name: 'BlogFriendLink',
                      meta: { title: '友链管理', icon: 'link' }
                    }
                  ]
                },
                {
                  path: '/admin/system',
                  name: 'System',
                  meta: { title: '系统管理', icon: 'system' },
                  children: [
                    {
                      path: 'user',
                      name: 'User',
                      meta: { title: '用户管理', icon: 'user' }
                    },
                    {
                      path: 'role',
                      name: 'Role',
                      meta: { title: '角色管理', icon: 'peoples' }
                    },
                    {
                      path: 'menu',
                      name: 'Menu',
                      meta: { title: '菜单管理', icon: 'tree-table' }
                    }
                  ]
                },
                {
                  path: '/admin/statistics',
                  name: 'Statistics',
                  meta: { title: '数据统计', icon: 'chart' },
                  children: [
                    {
                      path: 'overview',
                      name: 'StatisticsOverview',
                      meta: { title: '数据概览', icon: 'overview' }
                    },
                    {
                      path: 'article',
                      name: 'StatisticsArticle',
                      meta: { title: '文章统计', icon: 'documentation' }
                    }
                  ]
                },
                {
                  path: '/admin/monitor',
                  name: 'Monitor',
                  meta: { title: '系统监控', icon: 'monitor' },
                  children: [
                    {
                      path: 'actuator',
                      name: 'MonitorActuator',
                      meta: { title: 'Actuator监控', icon: 'monitor' }
                    },
                    {
                      path: 'prometheus',
                      name: 'MonitorPrometheus',
                      meta: { title: 'Prometheus监控', icon: 'chart' }
                    },
                    {
                      path: 'grafana',
                      name: 'MonitorGrafana',
                      meta: { title: 'Grafana监控', icon: 'dashboard' }
                    },
                    {
                      path: 'online',
                      name: 'MonitorOnline',
                      meta: { title: '在线用户', icon: 'online' }
                    },
                    {
                      path: 'logininfor',
                      name: 'MonitorLoginLog',
                      meta: { title: '登录日志', icon: 'logininfor' }
                    },
                    {
                      path: 'operlog',
                      name: 'MonitorOperLog',
                      meta: { title: '操作日志', icon: 'form' }
                    },
                    {
                      path: 'druid',
                      name: 'MonitorDruid',
                      meta: { title: '数据监控', icon: 'druid' }
                    },
                    {
                      path: 'server',
                      name: 'Server',
                      meta: { title: '服务监控', icon: 'server' }
                    },
                    {
                      path: 'cache',
                      name: 'MonitorCache',
                      meta: { title: '缓存监控', icon: 'redis' }
                    },
                    {
                      path: 'job',
                      name: 'MonitorJob',
                      meta: { title: '定时任务', icon: 'job' }
                    }
                  ]
                }
              ]

              // 直接设置侧边栏路由，不需要复杂的组件解析
              this.setSidebarRouters(frontendRoutes)
              console.log('✅ 前端菜单配置完成，包含博客管理菜单:', frontendRoutes)
              resolve([]) // 不添加动态路由，使用静态路由
        })
      }
    }
  })

export default usePermissionStore