<template>
  <div class="app-container">
    <el-card header="文章统计">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">发布文章数</div>
            <div class="stat-value">{{ articleStats.publishedCount || 0 }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">草稿文章数</div>
            <div class="stat-value">{{ articleStats.draftCount || 0 }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-title">平均浏览量</div>
            <div class="stat-value">{{ articleStats.avgViews || 0 }}</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 30px;">
        <el-col :span="12">
          <el-card header="文章分类分布">
            <div id="categoryChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="热门标签">
            <div id="tagChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { getArticleStatistics, getArticleCategoryDistribution, getHotTags } from '@/api/statistics'
import * as echarts from 'echarts'

const articleStats = ref({})

const loadData = async () => {
  try {
    const res = await getArticleStatistics()
    if (res.code === 200) {
      articleStats.value = res.data
      // 加载图表数据
      await loadChartData()
    }
  } catch (error) {
    console.error('获取文章统计失败:', error)
  }
}

const loadChartData = async () => {
  try {
    // 加载文章分类分布
    const categoryRes = await getArticleCategoryDistribution()
    if (categoryRes.code === 200) {
      renderCategoryChart(categoryRes.data)
    }
    
    // 加载热门标签
    const tagsRes = await getHotTags()
    if (tagsRes.code === 200) {
      renderTagsChart(tagsRes.data)
    }
  } catch (error) {
    console.error('加载图表数据失败:', error)
  }
}

const renderCategoryChart = (data) => {
  nextTick(() => {
    const chart = echarts.init(document.getElementById('categoryChart'))
    const option = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [{
        name: '文章分类',
        type: 'pie',
        radius: '50%',
        data: data.labels ? data.labels.map((label, index) => ({
          value: data.data[index],
          name: label
        })) : [
          { value: 25, name: '技术' },
          { value: 18, name: '生活' },
          { value: 12, name: '学习' },
          { value: 8, name: '其他' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }]
    }
    chart.setOption(option)
  })
}

const renderTagsChart = (data) => {
  nextTick(() => {
    const chart = echarts.init(document.getElementById('tagChart'))
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: data.labels || ['Java', 'Spring', 'Vue', 'React', '数据库', 'Linux']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: data.data || [15, 12, 8, 6, 9, 7],
        type: 'bar',
        itemStyle: {
          color: '#67C23A'
        }
      }]
    }
    chart.setOption(option)
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
</style>