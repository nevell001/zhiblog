<template>
  <router-view />
</template>

<script setup>
import { onMounted, nextTick } from 'vue'
import useSettingsStore from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'
import { initBlogSettings } from '@/utils/blogSettings'

onMounted(() => {
  nextTick(async () => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    
    // 初始化博客个性化设置
    await initBlogSettings()
    
    // 在开发环境下启用菜单调试和兼容性测试
  if (import.meta.env.DEV) {
    setTimeout(() => {
      console.log('开始菜单元素调试...')
      import('@/utils/menuDebug').then(({ debugMenuElements, addMenuEventListeners }) => {
        try {
          // 检查菜单元素的z-index层级
          debugMenuElements()
          
          // 添加额外的事件监听器
          addMenuEventListeners()
          
          // 运行菜单兼容性测试
          import('@/utils/compatibilityTest').then(({ runCompatibilityTests }) => {
            // 获取router实例
            const router = window.$vueApp?._context?.app?.config?.globalProperties?.$router || window.$router
            if (router) {
              runCompatibilityTests(router)
            } else {
              console.warn('未找到router实例，无法运行完整兼容性测试')
            }
          })
        } catch (error) {
          console.error('菜单调试出错:', error)
        }
      })
    }, 1500)
  }
  })
})
</script>
