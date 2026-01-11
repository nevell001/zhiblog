<template>
  <div :class="{ 'has-logo': showLogo }" class="sidebar-container">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="getMenuBackground"
        :text-color="getMenuTextColor"
        :unique-opened="false"
        :active-text-color="theme"
        :collapse-transition="false"
        :router="true"
        mode="vertical"
        :class="sideTheme"
        @select="handleSelect"
        @open="handleOpen"
        @close="handleClose"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters.filter(r => r.meta && r.meta.title)"
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
import _Layout from '@/layout/index.vue'
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import router from '@/router'

const route = useRoute()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()

// 使用动态生成的路由数据
const sidebarRouters = computed(() => {
  console.log('📋 侧边栏路由数据:', permissionStore.sidebarRouters)

  // 检查博客管理菜单是否正确
  const blogMenu = permissionStore.sidebarRouters?.find(
    route => route.meta?.title === '博客管理' || route.name === '博客管理'
  )
  console.log('🔍 博客管理菜单:', blogMenu)
  if (blogMenu) {
    console.log('✅ 博客管理子菜单数量:', blogMenu.children?.length || 0)
  }

  return permissionStore.sidebarRouters || []
})
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

// 判断是否只有一个子菜单并生成标题
function onlyOneChild(children) {
  // 确保children是数组
  if (!Array.isArray(children)) {
    return false
  }

  const showChildren = children.filter(item => {
    // 确保item存在且未隐藏
    return item && !item.hidden
  })

  if (showChildren.length === 1) {
    return showChildren[0]
  }
  return false
}

// 🚫 移除全局菜单点击事件处理，避免干扰Element Plus的原生展开/收起功能
// function handleMenuClick(event) {
//   console.log('菜单点击事件:', event)
//
//   // 查找最近的el-menu-item或el-sub-menu元素
//   const menuItem = event.target.closest('el-menu-item, .el-sub-menu')
//   if (menuItem) {
//     // 获取index属性作为路由路径
//     const index = menuItem.getAttribute('index')
//     if (index && index.trim()) {
//       // 直接使用window.location跳转
//       console.log('从click事件获取的路径:', index)
//       window.location.href = index
//     }
//   }
// }

// 处理菜单选择事件
function handleSelect(key, keyPath) {
  if (key && key.trim()) {
    console.log('菜单选中:', key, keyPath)
    // Element Plus的:router="true"会自动处理路由跳转
    // 这里只需要做日志记录和额外的处理逻辑
  }
}

// 处理子菜单展开事件
function handleOpen(key, keyPath) {
  console.log('📂 菜单展开:', key, keyPath)
}

// 处理子菜单关闭事件
function handleClose(key, keyPath) {
  console.log('📁 菜单关闭:', key, keyPath)
}

// 组件挂载后执行初始化
onMounted(() => {
  console.log('Sidebar组件挂载完成')

  // 🚫 移除事件委托，避免干扰Element Plus原生菜单行为
  // 让Element Plus的el-menu和el-sub-menu组件正常工作
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

    .el-menu-item,
    .el-sub-menu__title {
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
