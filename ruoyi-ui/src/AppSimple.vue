<template>
  <div>
    <h1>简单应用测试</h1>
    <p>这是一个简单的Vue应用，用于测试和调试 .on() 错误。</p>
    <button @click="testError">测试 undefined.on() 错误</button>
    <div v-if="errorMessage" class="error">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const errorMessage = ref('')

const testError = () => {
  try {
    const obj = undefined
    obj.on('event')
  } catch (error) {
    errorMessage.value = `错误类型: ${error.name}\n错误信息: ${error.message}\n堆栈跟踪: ${error.stack}`
    console.error('捕获到错误:', error)
  }
}
</script>

<style scoped>
.error {
  background-color: #ffebee;
  color: #c62828;
  padding: 10px;
  margin: 10px 0;
  border-radius: 4px;
  font-family: monospace;
  white-space: pre-wrap;
}
</style>
