<template>
  <div class="empty-state">
    <div class="empty-content">
      <el-icon class="empty-icon"><component :is="icon" /></el-icon>
      <h3>{{ title }}</h3>
      <p>{{ description }}</p>
      <router-link to="/" class="back-home-btn">
        <el-button type="primary">返回首页</el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Component } from 'vue'

withDefaults(defineProps<{ icon: Component; title?: string; description?: string }>(), {
  title: '暂无文章',
  description: ''
})
</script>

<style lang="scss" scoped>
.empty-state {
  padding: 80px 20px;
  color: var(--mo-n500);
  text-align: center;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  display: block;
  margin-bottom: 20px;
  color: var(--mo-n300);
  font-size: 4rem;
}

.empty-content h3 {
  margin: 0 0 10px 0;
  color: var(--mo-n800);
  font-size: 1.5rem;
}

.back-home-btn {
  margin-top: 20px;
  text-decoration: none;
}

html.dark .empty-state {
  color: var(--mo-n500);
}

html.dark .empty-icon {
  color: var(--mo-n400);
}

html.dark .empty-content h3 {
  color: var(--mo-n200);
}
</style>
