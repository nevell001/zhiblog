<template>
  <div class="app-container">
    <el-card header="文章统计">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">发布文章数</div>
            <div class="stat-value">
              {{ articleStats.publishedCount || 0 }}
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">草稿文章数</div>
            <div class="stat-value">
              {{ articleStats.draftCount || 0 }}
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">平均浏览量</div>
            <div class="stat-value">
              {{ articleStats.avgViews || 0 }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 30px">
        <el-col :span="12">
          <el-card header="文章分类分布">
            <div v-if="categoryReady" id="categoryChart" style="height: 300px"></div>
            <el-empty v-else description="暂无分类数据" :image-size="70" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="热门标签">
            <div v-if="tagReady" id="tagChart" style="height: 300px"></div>
            <el-empty v-else description="暂无标签数据" :image-size="70" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 30px">
        <el-col :span="24">
          <el-card header="每日阅读 PV/UV（近 30 天）">
            <div id="dailyPvUvChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, nextTick } from 'vue'
import {
  getArticleStatistics,
  getArticleCategoryDistribution,
  getHotTags,
  getDailyPvUv
} from '@/api/statistics'
import { loadEcharts, getChartThemeColors } from '@/utils/echarts'
import { useSettingsStore } from '@/stores/settings'
import { logger } from '@/utils/logger'

interface ArticleStats {
  publishedCount?: number
  draftCount?: number
  avgViews?: number
}

const articleStats = ref<ArticleStats>({})
const settingsStore = useSettingsStore()
const categoryChart = ref<any>(null)
const tagChartRef = ref<any>(null)
const categoryReady = ref(false)
const tagReady = ref(false)
let categoryData: any = null
let tagData: any = null

const loadData = async () => {
  try {
    const res = await getArticleStatistics()
    if (res.code === 200) {
      articleStats.value = res.data
      // 加载图表数据
      await loadChartData()
    }
  } catch (error) {
    logger.error('获取文章统计失败:', error)
  }
}

const loadChartData = async () => {
  try {
    // 加载文章分类分布
    const categoryRes = await getArticleCategoryDistribution()
    if (categoryRes.code === 200) {
      await renderCategoryChart(categoryRes.data || { labels: [], data: [] })
      categoryReady.value = true
    } else {
      categoryReady.value = false
    }

    // 加载热门标签
    const tagsRes = await getHotTags()
    if (tagsRes.code === 200) {
      await renderTagsChart(tagsRes.data || { labels: [], data: [] })
      tagReady.value = true
    } else {
      tagReady.value = false
    }

    // 加载每日 PV/UV
    try {
      const dailyRes = await getDailyPvUv(30)
      if (dailyRes.code === 200) {
        await renderDailyChart(dailyRes.data || [])
      }
    } catch (error) {
      logger.warn('每日 PV/UV 加载失败:', error)
    }
  } catch (error) {
    logger.error('加载图表数据失败:', error)
  }
}

const dailyChartRef = ref<any>(null)
let dailyData: any = null

const renderDailyChart = async rows => {
  dailyData = rows
  await nextTick()
  const chartElement = document.getElementById('dailyPvUvChart')
  if (!chartElement) return

  const echarts = await loadEcharts()
  if (!dailyChartRef.value) {
    dailyChartRef.value = echarts.init(chartElement)
  }
  const colors = getChartThemeColors()
  const list = Array.isArray(rows) ? rows : []
  const labels = list.map(row => row.statDate)
  const pvData = list.map(row => Number(row.pv) || 0)
  const uvData = list.map(row => Number(row.uv) || 0)
  const option = {
    tooltip: {
      trigger: 'axis',
      textStyle: { color: colors.textColor }
    },
    legend: {
      data: ['PV', 'UV'],
      textStyle: { color: colors.secondaryColor }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: labels,
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
        name: 'PV',
        type: 'line',
        smooth: true,
        data: pvData,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: 'UV',
        type: 'line',
        smooth: true,
        data: uvData,
        itemStyle: { color: '#67C23A' }
      }
    ]
  }
  dailyChartRef.value.setOption(option)
}

const renderCategoryChart = async data => {
  categoryData = data
  await nextTick()
  const chartElement = document.getElementById('categoryChart')
  if (!chartElement) return

  const echarts = await loadEcharts()
  if (!categoryChart.value) {
    categoryChart.value = echarts.init(chartElement)
  }
  const colors = getChartThemeColors()
  const option = {
    tooltip: {
      trigger: 'item',
      textStyle: { color: colors.textColor }
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      textStyle: { color: colors.secondaryColor }
    },
    series: [
      {
        name: '文章分类',
        type: 'pie',
        radius: '50%',
        data: data.labels
          ? data.labels.map((label, index) => ({
              value: data.data[index],
              name: label
            }))
          : [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        label: { color: colors.textColor }
      }
    ]
  }
  categoryChart.value.setOption(option)
}

const renderTagsChart = async data => {
  tagData = data
  await nextTick()
  const chartElement = document.getElementById('tagChart')
  if (!chartElement) return

  const echarts = await loadEcharts()
  if (!tagChartRef.value) {
    tagChartRef.value = echarts.init(chartElement)
  }
  const colors = getChartThemeColors()
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      textStyle: { color: colors.textColor }
    },
    xAxis: {
      type: 'category',
      data: data.labels || [],
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
        data: data.data || [],
        type: 'bar',
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }
  tagChartRef.value.setOption(option)
}

// 深色模式切换时用最新主题色重绘图表
watch(
  () => settingsStore.isDark,
  () => {
    if (categoryData) renderCategoryChart(categoryData)
    if (tagData) renderTagsChart(tagData)
    if (dailyData) renderDailyChart(dailyData)
  }
)

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 20px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
}

.stat-title {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}
</style>
