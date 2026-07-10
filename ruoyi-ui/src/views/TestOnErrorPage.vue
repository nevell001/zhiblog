<template>
  <div class="test-container">
    <h1>测试 .on() 错误处理</h1>
    <p>这个页面用于测试全局保护机制是否能正确处理 .on() 错误</p>

    <div class="test-buttons">
      <el-button type="primary" @click="testUndefinedOn">测试 undefined.on()</el-button>
      <el-button type="primary" @click="testNullOn">测试 null.on()</el-button>
      <el-button type="primary" @click="testObjectOn">测试普通对象.on()</el-button>
    </div>

    <div v-if="result" class="test-result">
      <h3>测试结果:</h3>
      <pre>{{ result }}</pre>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const result = ref('')

// 测试 undefined.on()
const testUndefinedOn = () => {
  try {
    const obj: any = undefined
    obj.on('event')
    result.value = '✅ undefined.on() 调用成功，没有报错'
  } catch (error) {
    result.value = `❌ undefined.on() 调用失败: ${error}`
  }
}

// 测试 null.on()
const testNullOn = () => {
  try {
    const obj: any = null
    obj.on('event')
    result.value = '✅ null.on() 调用成功，没有报错'
  } catch (error) {
    result.value = `❌ null.on() 调用失败: ${error}`
  }
}

// 测试普通对象.on()
const testObjectOn = () => {
  try {
    const obj: any = {}
    const unsubscribe = obj.on('event')
    result.value = '✅ 普通对象.on() 调用成功，返回值: ' + typeof unsubscribe
  } catch (error) {
    result.value = `❌ 普通对象.on() 调用失败: ${error}`
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.test-buttons {
  margin: 20px 0;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.test-result {
  margin: 20px 0;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

pre {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
