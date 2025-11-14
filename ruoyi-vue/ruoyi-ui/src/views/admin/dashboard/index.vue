<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>博客后台管理系统</h2>
        <p>
          欢迎使用博客后台管理系统，这里可以管理您的博客内容、用户、系统设置等。
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="success">博客管理</el-tag>
          <el-tag type="primary">用户管理</el-tag>
          <el-tag type="warning">系统监控</el-tag>
        </p>
        <p>
          <el-button
            type="primary"
            icon="Document"
            plain
            @click="$router.push('/admin/blog/article')"
            >文章管理</el-button
          >
          <el-button
            icon="User"
            plain
            @click="$router.push('/admin/system/user')"
            >用户管理</el-button
          >
          <el-button
            icon="Setting"
            plain
            @click="$router.push('/admin/blog/setting')"
            >博客设置</el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>系统信息</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>博客统计</h4>
            <ul>
              <li>文章总数: {{ stats.articleCount || 0 }}</li>
              <li>分类数量: {{ stats.categoryCount || 0 }}</li>
              <li>标签数量: {{ stats.tagCount || 0 }}</li>
              <li>评论数量: {{ stats.commentCount || 0 }}</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>系统状态</h4>
            <ul>
              <li>在线用户: {{ stats.onlineUsers || 0 }}</li>
              <li>今日访问: {{ stats.todayVisits || 0 }}</li>
              <li>系统运行: {{ stats.systemUptime || '0天' }}</li>
              <li>内存使用: {{ stats.memoryUsage || '0%' }}</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <!-- 快速操作卡片 -->
    <el-row :gutter="20" style="margin-top: 30px">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="quick-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>文章管理</span>
              <el-button type="primary" text @click="$router.push('/admin/blog/article')">管理</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>管理博客文章，支持发布、编辑、删除等操作</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="quick-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>分类标签</span>
              <el-button type="primary" text @click="$router.push('/admin/blog/category')">管理</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>管理文章分类和标签，便于内容组织</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="quick-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>评论管理</span>
              <el-button type="primary" text @click="$router.push('/admin/blog/comment')">管理</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>审核和管理用户评论，维护社区秩序</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="quick-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>系统设置</span>
              <el-button type="primary" text @click="$router.push('/admin/blog/setting')">设置</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>配置博客基本信息、主题、SEO等设置</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElButton, ElCard, ElRow, ElCol, ElTag } from 'element-plus'

const version = ref('1.0.0')
const stats = ref({})

onMounted(() => {
  // 初始化统计数据
  loadStats()
})

const loadStats = () => {
  // 这里可以调用API获取统计数据
  stats.value = {
    articleCount: 0,
    categoryCount: 0,
    tagCount: 0,
    commentCount: 0,
    onlineUsers: 0,
    todayVisits: 0,
    systemUptime: '0天',
    memoryUsage: '0%'
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.quick-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}
</style>