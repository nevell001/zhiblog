<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">
          {{ greeting }}，{{ userName }}
        </h1>
        <p class="welcome-subtitle">
          欢迎回到博客后台管理系统，今天是 {{ currentDate }}
        </p>
      </div>
      <div class="welcome-actions">
        <el-button
          type="primary"
          @click="$router.push('/admin/blog/article')"
        >
          <el-icon><Document /></el-icon>
          写文章
        </el-button>
        <el-button
          @click="$router.push('/admin/blog/setting')"
        >
          <el-icon><Setting /></el-icon>
          博客设置
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row
      :gutter="20"
      class="stats-section"
    >
      <el-col
        :xs="24"
        :sm="12"
        :lg="6"
      >
        <div class="stat-card stat-card-blue">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              {{ formatNumber(stats.articleCount) }}
            </div>
            <div class="stat-label">
              文章总数
            </div>
            <div
              class="stat-trend"
              :class="getTrendClass(stats.articleTrend)"
            >
              <el-icon>
                <Top v-if="stats.articleTrend > 0" />
                <Bottom v-else />
              </el-icon>
              {{ Math.abs(stats.articleTrend) || 0 }}%
            </div>
          </div>
        </div>
      </el-col>
      <el-col
        :xs="24"
        :sm="12"
        :lg="6"
      >
        <div class="stat-card stat-card-green">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              {{ formatNumber(stats.userCount) }}
            </div>
            <div class="stat-label">
              用户数量
            </div>
            <div
              class="stat-trend"
              :class="getTrendClass(stats.userTrend)"
            >
              <el-icon>
                <Top v-if="stats.userTrend > 0" />
                <Bottom v-else />
              </el-icon>
              {{ Math.abs(stats.userTrend) || 0 }}%
            </div>
          </div>
        </div>
      </el-col>
      <el-col
        :xs="24"
        :sm="12"
        :lg="6"
      >
        <div class="stat-card stat-card-orange">
          <div class="stat-icon">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              {{ formatNumber(stats.commentCount) }}
            </div>
            <div class="stat-label">
              评论数量
            </div>
            <div
              class="stat-trend"
              :class="getTrendClass(stats.commentTrend)"
            >
              <el-icon>
                <Top v-if="stats.commentTrend > 0" />
                <Bottom v-else />
              </el-icon>
              {{ Math.abs(stats.commentTrend) || 0 }}%
            </div>
          </div>
        </div>
      </el-col>
      <el-col
        :xs="24"
        :sm="12"
        :lg="6"
      >
        <div class="stat-card stat-card-purple">
          <div class="stat-icon">
            <el-icon><View /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              {{ formatNumber(stats.viewCount) }}
            </div>
            <div class="stat-label">
              总浏览量
            </div>
            <div
              class="stat-trend"
              :class="getTrendClass(stats.viewTrend)"
            >
              <el-icon>
                <Top v-if="stats.viewTrend > 0" />
                <Bottom v-else />
              </el-icon>
              {{ Math.abs(stats.viewTrend) || 0 }}%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快速操作和系统状态 -->
    <el-row
      :gutter="20"
      class="content-section"
    >
      <el-col
        :xs="24"
        :lg="16"
      >
        <el-card
          class="quick-actions-card"
          shadow="never"
        >
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Grid /></el-icon>
                快速操作
              </span>
            </div>
          </template>
          <div class="quick-actions">
            <div
              class="quick-action-item"
              @click="$router.push('/admin/blog/article')"
            >
              <div class="action-icon action-icon-blue">
                <el-icon><Document /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  文章管理
                </div>
                <div class="action-desc">
                  发布、编辑、删除文章
                </div>
              </div>
            </div>
            <div
              class="quick-action-item"
              @click="$router.push('/admin/blog/category')"
            >
              <div class="action-icon action-icon-green">
                <el-icon><Folder /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  分类标签
                </div>
                <div class="action-desc">
                  管理分类和标签
                </div>
              </div>
            </div>
            <div
              class="quick-action-item"
              @click="$router.push('/admin/blog/comment')"
            >
              <div class="action-icon action-icon-orange">
                <el-icon><ChatDotRound /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  评论管理
                </div>
                <div class="action-desc">
                  审核和管理评论
                </div>
              </div>
            </div>
            <div
              class="quick-action-item"
              @click="$router.push('/admin/blog/friendLink')"
            >
              <div class="action-icon action-icon-purple">
                <el-icon><Link /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  友情链接
                </div>
                <div class="action-desc">
                  管理友情链接
                </div>
              </div>
            </div>
            <div
              class="quick-action-item"
              @click="$router.push('/admin/system/user')"
            >
              <div class="action-icon action-icon-cyan">
                <el-icon><User /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  用户管理
                </div>
                <div class="action-desc">
                  管理系统用户
                </div>
              </div>
            </div>
            <div
              class="quick-action-item"
              @click="$router.push('/admin/blog/setting')"
            >
              <div class="action-icon action-icon-pink">
                <el-icon><Setting /></el-icon>
              </div>
              <div class="action-text">
                <div class="action-title">
                  系统设置
                </div>
                <div class="action-desc">
                  配置博客设置
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col
        :xs="24"
        :lg="8"
      >
        <el-card
          class="system-status-card"
          shadow="never"
        >
          <template #header>
            <span class="card-title">
              <el-icon><Monitor /></el-icon>
              系统状态
            </span>
          </template>
          <div class="system-status">
            <div class="status-item">
              <div class="status-label">
                在线用户
              </div>
              <div class="status-value">
                {{ stats.onlineUsers || 0 }}
              </div>
            </div>
            <div class="status-item">
              <div class="status-label">
                今日访问
              </div>
              <div class="status-value">
                {{ formatNumber(stats.todayVisits) }}
              </div>
            </div>
            <div class="status-item">
              <div class="status-label">
                系统运行
              </div>
              <div class="status-value">
                {{ stats.systemUptime || '0天' }}
              </div>
            </div>
            <div class="status-item">
              <div class="status-label">
                内存使用
              </div>
              <div class="status-value">
                {{ stats.memoryUsage || '0%' }}
              </div>
            </div>
            <div class="status-item">
              <div class="status-label">
                系统版本
              </div>
              <div class="status-value">
                v{{ version }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Document,
  User,
  Setting,
  ChatDotRound,
  View,
  Grid,
  Folder,
  Link,
  Monitor,
  ArrowRight,
  Top,
  Bottom
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const version = ref('1.3.2')
const currentDate = ref('')

interface DashboardStats {
  articleCount: number
  userCount: number
  commentCount: number
  viewCount: number
  onlineUsers: number
  todayVisits: number
  systemUptime: string
  memoryUsage: string
  articleTrend: number
  userTrend: number
  commentTrend: number
  viewTrend: number
}

const stats = ref<DashboardStats>({
  articleCount: 0,
  userCount: 0,
  commentCount: 0,
  viewCount: 0,
  onlineUsers: 0,
  todayVisits: 0,
  systemUptime: '0天',
  memoryUsage: '0%',
  articleTrend: 0,
  userTrend: 0,
  commentTrend: 0,
  viewTrend: 0
})

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '夜深了'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 18) return '下午好'
  if (hour < 22) return '晚上好'
  return '夜深了'
})

const userName = computed(() => {
  return userStore.name || '管理员'
})

const formatNumber = (num: number | undefined): string => {
  if (!num) return '0'
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num.toString()
}

const getTrendClass = (trend: number | undefined): string => {
  if (!trend) return 'trend-neutral'
  return trend > 0 ? 'trend-up' : 'trend-down'
}

const updateCurrentDate = () => {
  const now = new Date()
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  }
  currentDate.value = now.toLocaleDateString('zh-CN', options)
}

onMounted(() => {
  updateCurrentDate()
  loadStats()
  loadVersion()
})

const loadVersion = async () => {
  try {
    const response = (await request.get('/system/version')) as any
    if (response.code === 200 && response.data) {
      version.value = response.data.version || '1.3.2'
    }
  } catch (error) {
    console.error('获取版本号失败:', error)
  }
}

const loadStats = async () => {
  try {
    const response = (await request.get('/statistics/overview')) as any
    if (response.code === 200 && response.data) {
      stats.value = {
        ...stats.value,
        ...response.data
      }
    }
  } catch (error) {
    console.error('获取统计数据时发生错误:', error)
    ElMessage.warning('统计数据加载失败，显示默认数据')
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

/* 欢迎区域 */
.welcome-section {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 24px;
  margin-bottom: 20px;
}

.welcome-content {
  margin-bottom: 20px;
}

.welcome-title {
  font-size: 24px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 8px 0;
}

.welcome-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0 0 16px 0;
}

.welcome-tags {
  display: flex;
  gap: 8px;
}

.welcome-actions {
  display: flex;
  gap: 12px;
}

/* 统计卡片区域 */
.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.stat-card-blue .stat-icon {
  background: #409eff;
  color: white;
}

.stat-card-green .stat-icon {
  background: #67c23a;
  color: white;
}

.stat-card-orange .stat-icon {
  background: #e6a23c;
  color: white;
}

.stat-card-purple .stat-icon {
  background: #909399;
  color: white;
}

.stat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.trend-up {
  color: #67c23a;
}

.trend-down {
  color: #f56c6c;
}

.trend-neutral {
  color: #909399;
}

/* 内容区域 */
.content-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-title .el-icon {
  font-size: 18px;
  color: #409eff;
}

/* 快速操作卡片 */
.quick-actions-card :deep(.el-card__header) {
  padding: 18px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.quick-actions-card :deep(.el-card__body) {
  padding: 20px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.quick-action-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.quick-action-item:hover {
  background: #ecf5ff;
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
  color: white;
}

.action-icon-blue {
  background: #409eff;
}

.action-icon-green {
  background: #67c23a;
}

.action-icon-orange {
  background: #e6a23c;
}

.action-icon-purple {
  background: #909399;
}

.action-icon-cyan {
  background: #00bcd4;
}

.action-icon-pink {
  background: #f56c6c;
}

.action-text {
  flex: 1;
}

.action-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.action-desc {
  font-size: 12px;
  color: #909399;
}

.action-arrow {
  color: #c0c4cc;
  font-size: 16px;
}

/* 系统状态卡片 */
.system-status-card {
  height: 100%;
}

.system-status-card :deep(.el-card__header) {
  padding: 18px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.system-status-card :deep(.el-card__body) {
  padding: 20px;
}

.system-status {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}

.status-label {
  font-size: 14px;
  color: #909399;
}

.status-value {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard-container {
    padding: 16px;
  }

  .welcome-section {
    padding: 20px;
  }

  .welcome-actions {
    flex-direction: column;
  }

  .welcome-actions .el-button {
    width: 100%;
  }

  .stat-card {
    padding: 16px;
  }

  .stat-value {
    font-size: 24px;
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .quick-action-item {
    padding: 14px;
  }
}
</style>
