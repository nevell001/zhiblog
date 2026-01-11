<template>
  <div class="app-container">
    <el-card header="用户统计">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-title">总用户数</div>
            <div class="stat-value">
              {{ userStats.totalCount || 0 }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-title">活跃用户</div>
            <div class="stat-value">
              {{ userStats.activeCount || 0 }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-title">新增用户</div>
            <div class="stat-value">
              {{ userStats.newCount || 0 }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-title">管理员数</div>
            <div class="stat-value">
              {{ userStats.adminCount || 0 }}
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 30px">
        <el-col :span="12">
          <el-card header="用户注册趋势">
            <div id="registerChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card header="用户角色分布">
            <div id="roleChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { getUserStatistics, getUserRegisterTrend, getUserRoleDistribution } from '@/api/statistics'
import * as echarts from 'echarts'

const userStats = ref({})

const loadData = async () => {
  try {
    const res = await getUserStatistics()
    if (res.code === 200) {
      userStats.value = res.data
      // 加载图表数据
      await loadChartData()
    }
  } catch (error) {
    console.error('获取用户统计失败:', error)
  }
}

const loadChartData = async () => {
  try {
    // 加载用户注册趋势
    const registerRes = await getUserRegisterTrend()
    if (registerRes.code === 200) {
      renderRegisterChart(registerRes.data)
    }

    // 加载用户角色分布
    const roleRes = await getUserRoleDistribution()
    if (roleRes.code === 200) {
      renderRoleChart(roleRes.data)
    }
  } catch (error) {
    console.error('加载图表数据失败:', error)
  }
}

const renderRegisterChart = data => {
  nextTick(() => {
    const chart = echarts.init(document.getElementById('registerChart'))
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
        ]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: data.data || [5, 8, 3, 2, 6, 4, 7, 9, 12, 15, 8, 6],
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#E6A23C'
          }
        }
      ]
    }
    chart.setOption(option)
  })
}

const renderRoleChart = data => {
  nextTick(() => {
    const chart = echarts.init(document.getElementById('roleChart'))
    const option = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        right: 10,
        top: 'center'
      },
      series: [
        {
          name: '用户角色',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 18,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: data.labels
            ? data.labels.map((label, index) => ({
                value: data.data[index],
                name: label
              }))
            : [
                { value: 3, name: '管理员' },
                { value: 5, name: '编辑' },
                { value: 42, name: '普通用户' }
              ]
        }
      ]
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
