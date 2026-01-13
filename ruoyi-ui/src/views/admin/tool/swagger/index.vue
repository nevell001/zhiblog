<template>
  <i-frame v-model:src="url" />
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

// 使用后端直接访问地址，避免通过代理导致的循环加载
const url = ref(import.meta.env.VITE_API_BASE_URL + '/swagger-ui/index.html')
</script>
