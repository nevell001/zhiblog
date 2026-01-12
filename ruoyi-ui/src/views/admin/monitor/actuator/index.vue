<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Actuator 监控端点</span>
        </div>
      </template>
      <div v-loading="loading">
        <el-alert
          title="提示"
          type="info"
          description="以下是 Spring Boot Actuator 提供的所有监控端点，点击链接可查看详细信息"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        />
        <el-row :gutter="20">
          <el-col
            v-for="(endpoint, key) in endpoints"
            :key="key"
            :span="12"
            style="margin-bottom: 15px"
          >
            <el-card shadow="hover" class="endpoint-card">
              <template #header>
                <div class="endpoint-header">
                  <el-icon><Link /></el-icon>
                  <span class="endpoint-name">{{ formatEndpointName(key) }}</span>
                </div>
              </template>
              <div class="endpoint-content">
                <p class="endpoint-url">
                  {{ endpoint.href }}
                </p>
                <el-button type="primary" size="small" @click="viewEndpoint(endpoint.href, key)">
                  查看详情
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 端点详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="detailTitle"
      width="85%"
      top="3vh"
      destroy-on-close
    >
      <div v-loading="detailLoading">
        <div v-if="detailData" class="detail-content">
          <!-- 搜索框 -->
          <div v-if="isEnvEndpoint" style="margin-bottom: 15px">
            <el-input v-model="searchText" placeholder="搜索配置项..." clearable>
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <el-button
            type="primary"
            size="small"
            style="margin-bottom: 10px"
            @click="copyToClipboard"
          >
            <el-icon><CopyDocument /></el-icon>
            复制数据
          </el-button>
          <el-button
            type="success"
            size="small"
            style="margin-bottom: 10px"
            @click="openInNewWindow"
          >
            <el-icon><View /></el-icon>
            在新窗口打开
          </el-button>

          <!-- 如果是 health 端点，显示为折叠面板 -->
          <div v-if="isHealthEndpoint" class="health-display">
            <el-collapse v-model="activeNames">
              <el-collapse-item v-for="(component, key) in healthComponents" :key="key" :name="key">
                <template #title>
                  <div class="health-component-title">
                    <el-icon>
                      <CircleCheck v-if="component.status === 'UP'" />
                      <CircleClose v-else />
                    </el-icon>
                    <span class="component-name">{{ formatHealthComponentName(key) }}</span>
                    <el-tag size="small" :type="component.status === 'UP' ? 'success' : 'danger'">
                      {{ component.status }}
                    </el-tag>
                  </div>
                </template>
                <div class="health-component-detail">
                  <pre class="json-display">{{ JSON.stringify(component, null, 2) }}</pre>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>

          <!-- 如果是 info 端点，显示为折叠面板 -->
          <div v-if="isInfoEndpoint" class="info-display">
            <el-collapse v-model="activeNames">
              <el-collapse-item v-for="(value, key) in infoData" :key="key" :name="key">
                <template #title>
                  <div class="info-item-title">
                    <el-icon><Document /></el-icon>
                    <span class="info-name">{{ formatInfoName(key) }}</span>
                  </div>
                </template>
                <div class="info-item-detail">
                  <pre class="json-display">{{ JSON.stringify(value, null, 2) }}</pre>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>

          <!-- 如果是 configprops 端点，显示为折叠面板 -->
          <div v-if="isConfigPropsEndpoint" class="configprops-display">
            <el-input
              v-model="searchText"
              placeholder="搜索配置类..."
              clearable
              style="margin-bottom: 15px"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-collapse v-model="activeNames">
              <el-collapse-item v-for="(config, key) in filteredConfigProps" :key="key" :name="key">
                <template #title>
                  <div class="configprops-title">
                    <el-icon><Document /></el-icon>
                    <span class="config-name">{{ key }}</span>
                    <el-tag size="small" type="info">{{ getPropertyCount(config) }} 个属性</el-tag>
                  </div>
                </template>
                <div class="configprops-detail">
                  <el-table
                    :data="getPropertyList(config)"
                    style="width: 100%"
                    max-height="400"
                    size="small"
                  >
                    <el-table-column
                      prop="key"
                      label="属性名"
                      min-width="250"
                      show-overflow-tooltip
                    />
                    <el-table-column
                      prop="value"
                      label="属性值"
                      min-width="200"
                      show-overflow-tooltip
                    />
                  </el-table>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>

          <!-- 如果是 env 端点，显示为折叠面板 -->
          <div v-if="isEnvEndpoint" class="env-display">
            <el-input
              v-model="searchText"
              placeholder="搜索配置项..."
              clearable
              style="margin-bottom: 15px"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-collapse v-model="activeNames">
              <el-collapse-item
                v-for="(source, index) in filteredEnvSources"
                :key="index"
                :name="index"
              >
                <template #title>
                  <div class="env-source-title">
                    <el-icon><Document /></el-icon>
                    <span class="source-name">{{ source.name }}</span>
                    <el-tag size="small" type="info">
                      {{ getPropertyCount(source) }} 个配置项
                    </el-tag>
                  </div>
                </template>
                <div v-if="source.properties && Object.keys(source.properties).length > 0">
                  <el-table
                    :data="getPropertyList(source)"
                    style="width: 100%"
                    max-height="400"
                    size="small"
                  >
                    <el-table-column
                      prop="key"
                      label="配置键"
                      min-width="250"
                      show-overflow-tooltip
                    />
                    <el-table-column
                      prop="value"
                      label="配置值"
                      min-width="200"
                      show-overflow-tooltip
                    />
                    <el-table-column
                      prop="origin"
                      label="来源"
                      min-width="150"
                      show-overflow-tooltip
                    />
                  </el-table>
                </div>
                <div v-else class="empty-properties">
                  <el-empty description="无配置项" :image-size="60" />
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
          <!-- 如果是 metrics 列表，显示为表格 -->
          <div v-else-if="isMetricsList" class="metrics-list">
            <el-table :data="metricsListData" style="width: 100%" max-height="500">
              <el-table-column type="index" label="序号" width="80" />
              <el-table-column prop="name" label="指标名称" min-width="200" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="viewMetricDetail(scope.row.name)">
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <!-- 其他数据显示为 JSON -->
          <pre v-else class="json-display">{{ formattedData }}</pre>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Link,
  CopyDocument,
  View,
  Document,
  Search,
  CircleCheck,
  CircleClose
} from '@element-plus/icons-vue'

const loading = ref(true)
const endpoints = ref({})
const baseUrl = import.meta.env.VITE_APP_BASE_API + '/manage/actuator'

// 详情弹窗相关
const detailDialogVisible = ref(false)
const detailLoading = ref(false)
const detailData = ref(null)
const detailTitle = ref('')
const detailUrl = ref('')

// 搜索和折叠面板
const searchText = ref('')
const activeNames = ref([0])

// 格式化的 JSON 数据
const formattedData = computed(() => {
  if (!detailData.value) return ''

  // 如果是对象或数组，格式化为 JSON
  if (typeof detailData.value === 'object') {
    return JSON.stringify(detailData.value, null, 2)
  }

  // 如果是字符串，尝试解析后格式化
  if (typeof detailData.value === 'string') {
    try {
      const parsed = JSON.parse(detailData.value)
      return JSON.stringify(parsed, null, 2)
    } catch {
      // 不是 JSON，直接返回字符串
      return detailData.value
    }
  }

  // 其他类型，转换为字符串
  return String(detailData.value)
})

// 判断是否为 metrics 列表
const isMetricsList = computed(() => {
  return (
    detailData.value &&
    typeof detailData.value === 'object' &&
    detailData.value.names &&
    Array.isArray(detailData.value.names)
  )
})

// metrics 列表数据
const metricsListData = computed(() => {
  if (!isMetricsList.value) return []
  return detailData.value.names.map((name, index) => ({
    name: name,
    index: index + 1
  }))
})

// 判断是否为 health 端点
const isHealthEndpoint = computed(() => {
  return (
    detailData.value &&
    typeof detailData.value === 'object' &&
    detailData.value.status &&
    (detailData.value.components || detailData.value.details)
  )
})

// 健康检查组件数据
const healthComponents = computed(() => {
  if (!isHealthEndpoint.value) return {}
  return detailData.value.components || detailData.value.details || {}
})

// 判断是否为 info 端点
const isInfoEndpoint = computed(() => {
  return (
    detailData.value &&
    typeof detailData.value === 'object' &&
    !detailData.value.status &&
    !detailData.value.names &&
    !detailData.value.propertySources &&
    !detailData.value.contexts
  )
})

// info 端点数据
const infoData = computed(() => {
  if (!isInfoEndpoint.value) return {}
  return detailData.value
})

// 判断是否为 configprops 端点
const isConfigPropsEndpoint = computed(() => {
  return detailData.value && typeof detailData.value === 'object' && detailData.value.contexts
})

// 配置属性数据
const configPropsData = computed(() => {
  if (!isConfigPropsEndpoint.value) return {}

  const result = {}
  Object.keys(detailData.value.contexts || {}).forEach(contextName => {
    const context = detailData.value.contexts[contextName]
    if (context.beans) {
      Object.keys(context.beans).forEach(beanName => {
        result[beanName] = context.beans[beanName]
      })
    }
  })
  return result
})

// 过滤后的配置属性
const filteredConfigProps = computed(() => {
  if (!isConfigPropsEndpoint.value) return {}

  const props = configPropsData.value
  if (!searchText.value) return props

  const searchLower = searchText.value.toLowerCase()
  const filtered = {}
  Object.keys(props).forEach(key => {
    if (key.toLowerCase().includes(searchLower)) {
      filtered[key] = props[key]
    }
  })
  return filtered
})

// 判断是否为 env 端点
const isEnvEndpoint = computed(() => {
  return (
    detailData.value &&
    typeof detailData.value === 'object' &&
    detailData.value.propertySources &&
    Array.isArray(detailData.value.propertySources)
  )
})

// 过滤后的环境变量源
const filteredEnvSources = computed(() => {
  if (!isEnvEndpoint.value) return []

  const sources = detailData.value.propertySources
  if (!searchText.value) return sources

  const searchLower = searchText.value.toLowerCase()
  return sources
    .map(source => {
      const filteredProperties = {}
      Object.keys(source.properties || {}).forEach(key => {
        if (key.toLowerCase().includes(searchLower)) {
          filteredProperties[key] = source.properties[key]
        }
      })
      return {
        name: source.name,
        properties: filteredProperties
      }
    })
    .filter(source => Object.keys(source.properties).length > 0)
})

// 获取配置项数量
const getPropertyCount = source => {
  return source.properties ? Object.keys(source.properties).length : 0
}

// 获取配置项列表
const getPropertyList = source => {
  if (!source.properties) return []
  return Object.keys(source.properties).map(key => ({
    key: key,
    value: source.properties[key].value || 'N/A',
    origin: source.properties[key].origin || 'N/A'
  }))
}

// 格式化端点名称
const formatEndpointName = key => {
  const nameMap = {
    self: '根端点',
    health: '健康检查',
    'health-path': '健康检查路径',
    info: '应用信息',
    configprops: '配置属性',
    'configprops-prefix': '配置属性前缀',
    env: '环境变量',
    'env-toMatch': '环境变量匹配',
    'metrics-requiredMetricName': '指标详情',
    metrics: '所有指标',
    prometheus: 'Prometheus指标'
  }
  return nameMap[key] || key
}

// 格式化 health 组件名称
const formatHealthComponentName = key => {
  const nameMap = {
    db: '数据库',
    diskSpace: '磁盘空间',
    ping: 'Ping',
    redis: 'Redis',
    dynamicDataSource: '动态数据源',
    masterDataSource: '主数据源'
  }
  return nameMap[key] || key
}

// 格式化 info 名称
const formatInfoName = key => {
  const nameMap = {
    app: '应用信息',
    build: '构建信息',
    java: 'Java信息',
    os: '操作系统信息'
  }
  return nameMap[key] || key
}

// 查看端点详情
const viewEndpoint = async (url, key) => {
  detailDialogVisible.value = true
  detailTitle.value = `${formatEndpointName(key)} - 详情`
  detailUrl.value = url
  detailData.value = null
  detailLoading.value = true

  // 重置搜索框和折叠面板状态
  searchText.value = ''
  activeNames.value = [0]

  try {
    // 将容器内地址转换为本地访问地址
    const localUrl = url
      .replace('http://ruoyi-admin:8080', 'http://localhost:8080')
      .replace('http://host.docker.internal:8080', 'http://localhost:8080')

    const response = await fetch(localUrl)
    if (!response.ok) {
      throw new Error('获取端点详情失败')
    }

    // 获取响应文本
    const text = await response.text()

    // 尝试解析为 JSON
    try {
      // 先尝试直接解析
      detailData.value = JSON.parse(text)
    } catch (parseError) {
      // 如果直接解析失败，可能是 JSON 字符串，尝试解析字符串内容
      try {
        if (text.startsWith('"') && text.endsWith('"')) {
          // 去除外层引号
          const unescapedText = text.slice(1, -1)
          detailData.value = JSON.parse(unescapedText)
        } else {
          // 无法解析为 JSON，直接显示文本
          detailData.value = text
        }
      } catch (secondParseError) {
        // 所有解析都失败，显示原始文本
        detailData.value = text
      }
    }
  } catch (error) {
    console.error('获取端点详情失败:', error)
    ElMessage.error('获取端点详情失败: ' + error.message)
    detailData.value = { error: error.message }
  } finally {
    detailLoading.value = false
  }
}

// 复制到剪贴板
const copyToClipboard = async () => {
  try {
    await navigator.clipboard.writeText(formattedData.value)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败')
  }
}

// 在新窗口打开
const openInNewWindow = () => {
  const localUrl = detailUrl.value.replace('http://ruoyi-admin:8080', 'http://localhost:8080')
  window.open(localUrl, '_blank')
}

// 查看指标详情
const viewMetricDetail = metricName => {
  const metricUrl = `http://localhost:8080/manage/actuator/metrics/${metricName}`
  window.open(metricUrl, '_blank')
}

// 获取端点列表
const fetchEndpoints = async () => {
  try {
    loading.value = true
    const response = await fetch(baseUrl)
    if (!response.ok) {
      throw new Error('获取端点列表失败')
    }
    const data = await response.json()
    endpoints.value = data._links || {}
  } catch (error) {
    console.error('获取端点列表失败:', error)
    ElMessage.error('获取端点列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchEndpoints()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}

.endpoint-card {
  height: 100%;
}

.endpoint-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.endpoint-name {
  font-weight: 500;
  color: #303133;
}

.endpoint-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.endpoint-url {
  font-size: 12px;
  color: #909399;
  word-break: break-all;
  margin: 0;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.detail-content {
  max-height: 70vh;
  overflow-y: auto;
}

.metrics-list {
  margin-top: 10px;
}

.health-display {
  margin-top: 10px;
}

.health-component-title {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.component-name {
  flex: 1;
  font-weight: 500;
  color: #303133;
}

.health-component-detail {
  padding: 10px 0;
}

.info-display {
  margin-top: 10px;
}

.info-item-title {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.info-name {
  flex: 1;
  font-weight: 500;
  color: #303133;
}

.info-item-detail {
  padding: 10px 0;
}

.configprops-display {
  margin-top: 10px;
}

.configprops-title {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.config-name {
  flex: 1;
  font-weight: 500;
  color: #303133;
}

.configprops-detail {
  padding: 10px 0;
}

.env-display {
  margin-top: 10px;
}

.env-source-title {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.source-name {
  flex: 1;
  font-weight: 500;
  color: #303133;
}

.empty-properties {
  padding: 20px 0;
}

.json-display {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  line-height: 1.6;
  color: #303133;
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  overflow-x: auto;
}

.json-display::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.json-display::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.json-display::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.json-display::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
