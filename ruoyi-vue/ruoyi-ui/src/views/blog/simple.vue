<template>
  <div class="app-container">
    <h1>简单测试页面</h1>
    <p>这是一个简单的测试页面，用于验证路由是否正常工作。</p>
    <el-button type="primary" @click="testClick">测试按钮</el-button>
    <p>点击次数: {{ clickCount }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const clickCount = ref(0)

const testClick = () => {
  clickCount.value++
  console.log('按钮被点击了', clickCount.value, '次')
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>
