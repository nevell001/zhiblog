<template>
  <div class="app-container">
    <el-card v-if="isProduction">
      <el-result
        icon="warning"
        title="系统接口已禁用"
        sub-title="在生产环境下，系统接口（Swagger）已禁用以确保系统安全"
      >
        <template #extra>
          <el-button
            type="primary"
            @click="goToDevEnvironment"
          >
            前往开发环境
          </el-button>
        </template>
      </el-result>
    </el-card>
    <i-frame
      v-else
      v-model:src="url"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

// 检测是否为生产环境
const isProduction = computed(() => {
  return import.meta.env?.VUE_APP_ENV === 'production' || import.meta.env?.MODE === 'production'
})

// 使用后端直接访问地址，避免通过代理导致的循环加载
const baseUrl = import.meta.env?.VITE_API_BASE_URL || 'http://localhost:8080'
const url = ref(`${baseUrl}/swagger-ui/index.html`)

// 前往开发环境
const goToDevEnvironment = () => {
  window.open('http://localhost:3000/admin/tool/swagger', '_blank')
}
</script>
