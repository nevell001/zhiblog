<template>
  <Suspense>
    <template #default>
      <ArticlePage />
    </template>
    <template #fallback>
      <div class="loading-container">加载中...</div>
    </template>
  </Suspense>
</template>

<script setup lang="ts">
import { defineAsyncComponent } from 'vue'

const ArticlePage = defineAsyncComponent(() => import('@/views/admin/blog/article/index.vue'))
</script>

<style scoped>
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: #909399;
}
</style>
