<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <svg-icon icon-class="documentation" />
            </div>
            <div class="stat-info">
              <div class="stat-value">
                {{ stats.articleCount || 0 }}
              </div>
              <div class="stat-label">文章总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <svg-icon icon-class="user" />
            </div>
            <div class="stat-info">
              <div class="stat-value">
                {{ stats.userCount || 0 }}
              </div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <svg-icon icon-class="message" />
            </div>
            <div class="stat-info">
              <div class="stat-value">
                {{ stats.commentCount || 0 }}
              </div>
              <div class="stat-label">评论总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <svg-icon icon-class="eye" />
            </div>
            <div class="stat-info">
              <div class="stat-value">
                {{ stats.viewCount || 0 }}
              </div>
              <div class="stat-label">总浏览量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card header="文章发布趋势">
          <div id="articleChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="用户活跃度">
          <div id="userChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, nextTick } from 'vue'
import { getStatisticsOverview, getArticleTrend, getUserActivity } from '@/api/statistics'
import { loadEcharts, getChartThemeColors } from '@/utils/echarts'
import { useSettingsStore } from '@/stores/settings'
import { logger } from '@/utils/logger'

interface OverviewStats {
  articleCount?: number
  userCount?: number
  commentCount?: number
  viewCount?: number
}

const stats = ref<OverviewStats>({})
const settingsStore = useSettingsStore()
const articleChart = ref<any>(null)
const userChartRef = ref<any>(null)
let articleTrendData: any = null
let userActivityData: any = null

const loadData = async () => {
  try {
    const res = await getStatisticsOverview()
    if (res.code === 200) {
      stats.value = res.data
      // 加载图表数据
      await loadChartData()
    } else {
      // 使用模拟数据
      stats.value = {
        articleCount: 128,
        userCount: 256,
        commentCount: 512,
        viewCount: 1024
      }
      await loadChartData()
    }
  } catch (error) {
    logger.error('获取统计数据失败:', error)
    // 使用模拟数据
    stats.value = {
      articleCount: 128,
      userCount: 256,
      commentCount: 512,
      viewCount: 1024
    }
    await loadChartData()
  }
}

const loadChartData = async () => {
  try {
    // 加载文章发布趋势
    try {
      const articleTrendRes = await getArticleTrend()
      if (articleTrendRes.code === 200) {
        await renderArticleChart(articleTrendRes.data)
      } else {
        // 使用模拟数据
        await renderArticleChart({
          labels: [
            '1月',
            '2月',
            '3月',
            '4月',
            '5月',
            '6月',
            '7月',
            '8月',
            '9月',
            '10月',
            '11月',
            '12月'
          ],
          data: [12, 19, 3, 5, 2, 3, 15, 8, 12, 6, 9, 11]
        })
      }
    } catch (error) {
      logger.warn('文章趋势数据加载失败，使用模拟数据:', error)
      // 使用模拟数据
      await renderArticleChart({
        labels: [
          '1月',
          '2月',
          '3月',
          '4月',
          '5月',
          '6月',
          '7月',
          '8月',
          '9月',
          '10月',
          '11月',
          '12月'
        ],
        data: [12, 19, 3, 5, 2, 3, 15, 8, 12, 6, 9, 11]
      })
    }

    // 加载用户活跃度
    try {
      const userActivityRes = await getUserActivity()
      if (userActivityRes.code === 200) {
        await renderUserChart(userActivityRes.data)
      } else {
        // 使用模拟数据
        await renderUserChart({
          labels: [
            '1月',
            '2月',
            '3月',
            '4月',
            '5月',
            '6月',
            '7月',
            '8月',
            '9月',
            '10月',
            '11月',
            '12月'
          ],
          data: [45, 52, 38, 24, 33, 52, 35, 48, 42, 55, 60, 48]
        })
      }
    } catch (error) {
      logger.warn('用户活跃度数据加载失败，使用模拟数据:', error)
      // 使用模拟数据
      await renderUserChart({
        labels: [
          '1月',
          '2月',
          '3月',
          '4月',
          '5月',
          '6月',
          '7月',
          '8月',
          '9月',
          '10月',
          '11月',
          '12月'
        ],
        data: [45, 52, 38, 24, 33, 52, 35, 48, 42, 55, 60, 48]
      })
    }
  } catch (error) {
    logger.error('加载图表数据失败:', error)
  }
}

const renderArticleChart = async data => {
  articleTrendData = data
  await nextTick()
  const chartElement = document.getElementById('articleChart')
  if (!chartElement) return

  const echarts = await loadEcharts()
  if (!articleChart.value) {
    articleChart.value = echarts.init(chartElement)
  }
  const colors = getChartThemeColors()
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.labels || [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月'
      ],
      axisLabel: { color: colors.secondaryColor },
      axisLine: { lineStyle: { color: colors.borderColor } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: colors.secondaryColor },
      splitLine: { lineStyle: { color: colors.splitLineColor } }
    },
    series: [
      {
        data: data.data || [12, 19, 3, 5, 2, 3, 15, 8, 12, 6, 9, 11],
        type: 'line',
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  articleChart.value.setOption(option)
}

const renderUserChart = async data => {
  userActivityData = data
  await nextTick()
  const chartElement = document.getElementById('userChart')
  if (!chartElement) return

  const echarts = await loadEcharts()
  if (!userChartRef.value) {
    userChartRef.value = echarts.init(chartElement)
  }
  const colors = getChartThemeColors()
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.labels || [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月'
      ],
      axisLabel: { color: colors.secondaryColor },
      axisLine: { lineStyle: { color: colors.borderColor } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: colors.secondaryColor },
      splitLine: { lineStyle: { color: colors.splitLineColor } }
    },
    series: [
      {
        data: data.data || [45, 52, 38, 24, 33, 52, 35, 48, 42, 55, 60, 48],
        type: 'bar',
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }
  userChartRef.value.setOption(option)
}

// 深色模式切换时用最新主题色重绘图表
watch(
  () => settingsStore.isDark,
  () => {
    if (articleTrendData) renderArticleChart(articleTrendData)
    if (userActivityData) renderUserChart(userActivityData)
  }
)

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-right: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-top: 5px;
}
</style>
