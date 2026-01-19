<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>表单构建</span>
        </div>
      </template>
      <el-empty description="表单构建功能开发中..." />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineOptions({
  name: 'ToolBuild'
})

const loading = ref(false)
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>