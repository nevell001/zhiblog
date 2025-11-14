<template>
  <div class="test-container">
    <h1>分类管理测试页面</h1>
    
    <div class="button-test">
      <h2>按钮测试</h2>
      <el-button type="primary" icon="Plus">新增按钮测试</el-button>
      <el-button type="success" icon="Edit">修改按钮测试</el-button>
      <el-button type="danger" icon="Delete">删除按钮测试</el-button>
    </div>

    <div class="api-test">
      <h2>API测试</h2>
      <el-button @click="testListAPI">测试获取分类列表</el-button>
      <div v-if="apiResult">
        <h3>API结果：</h3>
        <pre>{{ JSON.stringify(apiResult, null, 2) }}</pre>
      </div>
    </div>

    <div class="component-test">
      <h2>组件测试</h2>
      <el-table :data="testData">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="name" label="名称" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button link type="primary" icon="Edit">编辑</el-button>
            <el-button link type="primary" icon="Delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { listCategory } from '@/api/admin/blog/category'

const apiResult = ref(null)
const testData = ref([
  { id: 1, name: '测试分类1' },
  { id: 2, name: '测试分类2' }
])

const testListAPI = async () => {
  try {
    const response = await listCategory({ pageNum: 1, pageSize: 10 })
    apiResult.value = response
  } catch (error) {
    apiResult.value = { error: error.message }
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
}

.button-test, .api-test, .component-test {
  margin: 20px 0;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 3px;
  overflow-x: auto;
}
</style>