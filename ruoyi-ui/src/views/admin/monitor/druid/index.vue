<template>
  <div>
    <i-frame :src="url" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/login.html')
</script>
