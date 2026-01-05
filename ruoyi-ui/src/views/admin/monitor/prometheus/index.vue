<template>
   <div>
      <i-frame :src="url"></i-frame>
   </div>
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/manage/actuator/prometheus')
</script>