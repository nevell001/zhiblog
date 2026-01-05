<template>
   <div>
      <i-frame :src="url"></i-frame>
   </div>
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

const url = ref('http://localhost:3001')
</script>