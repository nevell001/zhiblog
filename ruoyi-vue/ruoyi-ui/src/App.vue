<template>
  <router-view />
</template>

<script setup>
import { onMounted, nextTick } from 'vue'
import { useSettingsStore } from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'
import { initBlogSettings } from '@/utils/blogSettings'

onMounted(() => {
  nextTick(async () => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    
    // 初始化博客个性化设置
    await initBlogSettings()
    
    // 在开发环境下记录路由信息
  if (import.meta.env.DEV) {
    setTimeout(() => {
      console.log('应用启动完成，路由系统初始化中...')
    }, 1500)
  }
  })
})
</script>
