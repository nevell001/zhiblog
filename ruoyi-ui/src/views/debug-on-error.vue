<template>
  <div class="debug-container">
    <h2>调试 .on() 错误</h2>
    <div
      v-if="errorMsg"
      class="error-message"
    >
      <h3>捕获到错误：</h3>
      <p>{{ errorMsg }}</p>
    </div>
    <div class="test-section">
      <h3>测试对象：</h3>
      <el-button
        type="danger"
        @click="testUndefinedOn"
      >
        测试 undefined.on()
      </el-button>
      <el-button
        type="warning"
        @click="testNullOn"
      >
        测试 null.on()
      </el-button>
      <el-button
        type="primary"
        @click="testAPI"
      >
        测试 API 导入
      </el-button>
    </div>
    <div class="info-section">
      <h3>环境信息：</h3>
      <p>Vue 版本: {{ vueVersion }}</p>
      <p>路由: {{ currentRoute }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import * as vue from 'vue'

const route = useRoute()
const errorMsg = ref('')
const vueVersion = vue.version

const currentRoute = computed(() => route.path)

function testUndefinedOn() {
  try {
    const obj: any = undefined
    obj.on('test')
    errorMsg.value = '未捕获到错误（可能被全局拦截）'
  } catch (e: any) {
    errorMsg.value = `捕获到错误: ${e.message}`
  }
}

function testNullOn() {
  try {
    const obj: any = null
    obj.on('test')
    errorMsg.value = '未捕获到错误（可能被全局拦截）'
  } catch (e: any) {
    errorMsg.value = `捕获到错误: ${e.message}`
  }
}

async function testAPI() {
  try {
    // 动态导入 API
    const apiModule = await import('@/api/blog')
    const functions = Object.keys(apiModule)
    errorMsg.value = `API 模块导出: ${functions.join(', ')}`
  } catch (e: any) {
    errorMsg.value = `API 导入失败: ${e.message}`
  }
}
</script>

<style scoped>
.debug-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.error-message {
  background: #fef0f0;
  border: 1px solid #fbc4c4;
  color: #f56c6c;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.test-section, .info-section {
  margin-bottom: 20px;
}

.el-button {
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>
