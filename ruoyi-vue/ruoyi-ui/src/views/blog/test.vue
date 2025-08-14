<template>
  <div class="app-container">
    <h1>博客测试页面</h1>
    <p>如果你能看到这个页面，说明路由配置正确！</p>
    <el-button type="primary" @click="goBack">返回</el-button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.go(-1)
}
</script>
