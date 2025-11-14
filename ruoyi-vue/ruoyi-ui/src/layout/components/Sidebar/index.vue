<template>
  <div :class="{ 'has-logo': showLogo }" class="sidebar-container">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="getMenuBackground"
        :text-color="getMenuTextColor"
        :unique-opened="true"
        :active-text-color="theme"
        :collapse-transition="false"
        mode="vertical"
        :class="sideTheme"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.module.scss'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import Layout from '@/layout/index.vue'

const route = useRoute()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()

// 直接使用硬编码的菜单数据，确保菜单能够显示
const sidebarRouters = computed(() => [
  {
    path: '/home',
    component: () => import('@/layout/index.vue'),
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
      path: '/admin/blog',
      component: Layout,
      redirect: '/admin/blog/article',
      alwaysShow: true,
      meta: { title: '博客管理', icon: 'documentation' },
      children: [
        {
          path: 'article',
          component: () => import('@/views/admin/blog/article/article/index.vue'),
          name: 'Article',
          meta: { title: '文章管理', icon: 'edit' }
        },
        {
          path: 'category',
          component: () => import('@/views/admin/blog/category/category/index'),
          name: 'BlogCategory',
          meta: { title: '分类管理', icon: 'list' }
        },
        {
          path: 'tag',
          component: () => import('@/views/admin/blog/tag/tag/index'),
          name: 'BlogTag',
          meta: { title: '标签管理', icon: 'tag' }
        },
        {
          path: 'comment',
          component: () => import('@/views/admin/blog/comment/comment/index'),
          name: 'Comment',
          meta: { title: '评论管理', icon: 'message' }
        },
        {
          path: 'setting',
          component: () => import('@/views/admin/blog/setting/setting/index'),
          name: 'BlogSetting',
          meta: { title: '博客设置', icon: 'setting' }
        }
      ]
    },
  {
    path: '/admin/system',
    component: Layout,
    redirect: '/admin/system/user',
    alwaysShow: true,
    meta: { title: '博客设置', icon: 'system' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/admin/system/user/user/index'),
        name: 'User',
        meta: { title: '用户管理', icon: 'user' }
      }
    ]
  }
])
const showLogo = computed(() => settingsStore.sidebarLogo)
const sideTheme = computed(() => settingsStore.sideTheme)
const theme = computed(() => settingsStore.theme)
const isCollapse = computed(() => !appStore.sidebar.opened)

// 获取菜单背景色
const getMenuBackground = computed(() => {
  if (settingsStore.isDark) {
    return 'var(--sidebar-bg)'
  }
  return sideTheme.value === 'theme-dark' ? variables.menuBg : variables.menuLightBg
})

// 获取菜单文字颜色
const getMenuTextColor = computed(() => {
  if (settingsStore.isDark) {
    return 'var(--sidebar-text)'
  }
  return sideTheme.value === 'theme-dark' ? variables.menuText : variables.menuLightText
})

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})
</script>

<style lang="scss" scoped>
.sidebar-container {
  background-color: v-bind(getMenuBackground);
  
  .scrollbar-wrapper {
    background-color: v-bind(getMenuBackground);
  }

  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
    
    .el-menu-item, .el-sub-menu__title {
      &:hover {
        background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
      }
    }

    .el-menu-item {
      color: v-bind(getMenuTextColor);
      
      &.is-active {
        color: var(--menu-active-text, #409eff);
        background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
      }
    }

    .el-sub-menu__title {
      color: v-bind(getMenuTextColor);
    }
  }
}
</style>
