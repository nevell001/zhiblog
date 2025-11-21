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
      :collapse-transition="true"
      :router="false"
      mode="vertical"
      :class="sideTheme"
      @select="handleSelect"
      @open="handleOpen"
      @close="handleClose"
      @click="handleMenuClick"
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
  console.log('侧边栏路由数据:', permissionStore.sidebarRouters)
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

// 处理菜单点击事件 - 这是一个额外的保障，确保菜单点击能被捕获
function handleMenuClick(event) {
  console.log('菜单点击事件:', event)
  
  // 查找最近的el-menu-item或el-sub-menu元素
  const menuItem = event.target.closest('el-menu-item, .el-sub-menu')
  if (menuItem) {
    // 获取index属性作为路由路径
    const index = menuItem.getAttribute('index')
    if (index && index.trim()) {
      // 直接使用window.location跳转
      console.log('从click事件获取的路径:', index)
      window.location.href = index
    }
  }
}

// 处理菜单选择事件
function handleSelect(key, keyPath) {
  // 仅在有有效选中值时才记录日志，避免空数组重复打印
  if (key && key.trim()) {
    console.log('菜单选中:', key, keyPath)
    
    try {
      // 使用已导入的router对象进行路由跳转
      console.log(`尝试跳转到: ${key}`)
      
      // 🔥 关键修复: 避免路径重复
      let targetPath = key
      
      // 如果路径已经包含/admin/前缀，直接使用
      if (key && key.startsWith('/admin/')) {
        targetPath = key
      }
      
      // 直接使用window.location跳转
      console.log(`使用window.location跳转到: ${key}`)
      window.location.href = key
    } catch (e) {
      console.error('执行路由跳转时发生异常:', e)
      // 最后的备选方案
      try {
        window.location.href = key
      } catch (finalError) {
        console.error('所有导航方案均失败:', finalError)
      }
    }
  }
}

// 处理子菜单展开事件
function handleOpen(key, keyPath) {
  console.log('菜单展开:', key, keyPath)
}

// 处理子菜单关闭事件
function handleClose(key, keyPath) {
  console.log('菜单关闭:', key, keyPath)
}

// 组件挂载后执行初始化
onMounted(() => {
  console.log('Sidebar组件挂载完成')
  
  // 为所有菜单标题添加点击事件委托作为最后保障
  const menuContainer = document.querySelector('.sidebar-container')
  if (menuContainer) {
    menuContainer.addEventListener('click', (e) => {
      const menuTitle = e.target.closest('.menu-title')
      if (menuTitle && !e.defaultPrevented) {
        console.log('通过事件委托捕获菜单标题点击')
        
        // 查找对应的菜单项
        const menuItem = menuTitle.closest('el-menu-item, .el-sub-menu')
        if (menuItem) {
          const index = menuItem.getAttribute('index')
          if (index && index.trim()) {
            console.log('通过事件委托获取路径:', index)
            // 直接使用window.location跳转
            console.log('通过事件委托执行跳转到:', index)
            window.location.href = index
          }
        }
      }
    }, true) // 使用捕获阶段
  }
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
