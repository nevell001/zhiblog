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
  })
})
</script>
