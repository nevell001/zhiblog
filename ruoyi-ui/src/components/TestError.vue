<template>
  <div class="test-error">
    <h1>错误测试组件</h1>
    <button @click="testUndefinedOn">
      测试 undefined.on()
    </button>
    <button @click="testNullOn">
      测试 null.on()
    </button>
    <button @click="testObjectOn">
      测试普通对象.on()
    </button>
    <button @click="testWindowOn">
      测试 window.on()
    </button>
    <button @click="testDocumentOn">
      测试 document.on()
    </button>
    <button @click="loadTinyMCE">
      测试 TinyMCE 加载
    </button>
    <div
      ref="resultRef"
      class="test-result"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { loadTinymce } from '@/utils/tinymce'

const resultRef = ref<HTMLElement | null>(null)

const log = (message: string) => {
  if (resultRef.value) {
    resultRef.value.innerHTML += `<div>${message}</div>`
  }
  console.log(message)
}

const error = (message: string, err: Error) => {
  if (resultRef.value) {
    resultRef.value.innerHTML += `<div class="error">${message}: ${err.message}</div>`
  }
  console.error(message, err)
}

const testUndefinedOn = () => {
  try {
    const obj = undefined
    ;(obj as any).on('event')
    log('测试 undefined.on() 成功（不应该发生）')
  } catch (err) {
    error('测试 undefined.on() 失败（预期结果）', err as Error)
  }
}

const testNullOn = () => {
  try {
    const obj = null
    ;(obj as any).on('event')
    log('测试 null.on() 成功（不应该发生）')
  } catch (err) {
    error('测试 null.on() 失败（预期结果）', err as Error)
  }
}

const testObjectOn = () => {
  try {
    const obj = {}
    ;(obj as any).on('event')
    log('测试普通对象.on() 成功（不应该发生）')
  } catch (err) {
    error('测试普通对象.on() 失败（预期结果）', err as Error)
  }
}

const testWindowOn = () => {
  try {
    // 测试 window.on()
    ;(window as any).on('event')
    log('测试 window.on() 成功')
  } catch (err) {
    error('测试 window.on() 失败', err as Error)
  }
}

const testDocumentOn = () => {
  try {
    // 测试 document.on()
    ;(document as any).on('event')
    log('测试 document.on() 成功')
  } catch (err) {
    error('测试 document.on() 失败', err as Error)
  }
}

const loadTinyMCE = async () => {
  try {
    log('开始加载 TinyMCE...')
    const tinymce = await loadTinymce()
    if (tinymce) {
      log('TinyMCE 加载成功')
      log(`TinyMCE 类型: ${typeof tinymce}`)
      log(`TinyMCE 有 init 方法: ${typeof tinymce.init === 'function'}`)
    } else {
      log('TinyMCE 加载失败，返回 null')
    }
  } catch (err) {
    error('加载 TinyMCE 时出错', err as Error)
  }
}
</script>

<style scoped>
.test-error {
  padding: 20px;
  font-family: Arial, sans-serif;
}

button {
  margin: 5px;
  padding: 10px 15px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #66b1ff;
}

.test-result {
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-height: 100px;
}

.error {
  color: red;
  font-weight: bold;
}
</style>
