<template>
  <div>
    <h2>测试 .on() 错误</h2>
    <button @click="testError">
      触发 .on() 错误
    </button>
    <div
      v-if="errorMessage"
      class="error-message"
    >
      {{ errorMessage }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const errorMessage = ref('')

const testError = () => {
  try {
    // 模拟 undefined.on() 调用
    const obj = undefined
    obj.on('event')
  } catch (error) {
    errorMessage.value = `错误类型: ${error.name}\n错误信息: ${error.message}\n堆栈跟踪: ${error.stack}`
    console.error('捕获到错误:', error)
  }
}
</script>

<style scoped>
.error-message {
  margin-top: 20px;
  padding: 10px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 4px;
  font-family: monospace;
  white-space: pre-wrap;
}
</style>
