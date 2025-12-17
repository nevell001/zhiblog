<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>🎨 图片压缩功能测试</span>
          <el-button type="primary" @click="testConfig">测试配置</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <!-- 智能压缩测试 -->
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <div class="clearfix">
                <span>📦 智能压缩测试</span>
              </div>
            </template>

            <el-upload
              class="upload-demo"
              :action="uploadCompressedUrl"
              :headers="headers"
              :on-success="handleCompressedSuccess"
              :before-upload="handleBeforeUpload"
              :show-file-list="false"
              drag
            >
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  支持 jpg/png/gif 文件，自动压缩
                </div>
              </template>
            </el-upload>

            <div v-if="compressedResult" class="result-info">
              <h4>压缩结果:</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="原始文件名">
                  {{ compressedResult.originalFilename }}
                </el-descriptions-item>
                <el-descriptions-item label="压缩标识">
                  <el-tag type="success">{{ compressedResult.compressed ? '已压缩' : '未压缩' }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="文件URL">
                  <el-link :href="compressedResult.url" target="_blank" type="primary">
                    {{ compressedResult.url }}
                  </el-link>
                </el-descriptions-item>
              </el-descriptions>

              <img v-if="compressedResult.url" :src="compressedResult.url" class="preview-image" />
            </div>
          </el-card>
        </el-col>

        <!-- 头像压缩测试 -->
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <div class="clearfix">
                <span>👤 头像压缩测试 (200x200)</span>
              </div>
            </template>

            <el-upload
              class="upload-demo"
              :action="uploadAvatarUrl"
              :headers="headers"
              :on-success="handleAvatarSuccess"
              :before-upload="handleBeforeUpload"
              :show-file-list="false"
              drag
            >
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                将头像拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  自动压缩为200x200正方形头像
                </div>
              </template>
            </el-upload>

            <div v-if="avatarResult" class="result-info">
              <h4>头像结果:</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="类型">
                  <el-tag type="primary">{{ avatarResult.type }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="文件名">
                  {{ avatarResult.fileName }}
                </el-descriptions-item>
                <el-descriptions-item label="文件URL">
                  <el-link :href="avatarResult.url" target="_blank" type="primary">
                    {{ avatarResult.url }}
                  </el-link>
                </el-descriptions-item>
              </el-descriptions>

              <img v-if="avatarResult.url" :src="avatarResult.url" class="avatar-preview" />
            </div>
          </el-card>
        </el-col>

        <!-- 缩略图压缩测试 -->
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <div class="clearfix">
                <span>🖼️ 缩略图压缩测试 (400x400)</span>
              </div>
            </template>

            <el-upload
              class="upload-demo"
              :action="uploadThumbnailUrl"
              :headers="headers"
              :on-success="handleThumbnailSuccess"
              :before-upload="handleBeforeUpload"
              :show-file-list="false"
              drag
            >
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                将图片拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  自动压缩为400x400缩略图
                </div>
              </template>
            </el-upload>

            <div v-if="thumbnailResult" class="result-info">
              <h4>缩略图结果:</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="类型">
                  <el-tag type="warning">{{ thumbnailResult.type }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="文件名">
                  {{ thumbnailResult.fileName }}
                </el-descriptions-item>
                <el-descriptions-item label="文件URL">
                  <el-link :href="thumbnailResult.url" target="_blank" type="primary">
                    {{ thumbnailResult.url }}
                  </el-link>
                </el-descriptions-item>
              </el-descriptions>

              <img v-if="thumbnailResult.url" :src="thumbnailResult.url" class="thumbnail-preview" />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 配置信息 -->
      <el-card shadow="hover" style="margin-top: 20px;">
        <template #header>
          <div class="clearfix">
            <span>⚙️ 压缩配置信息</span>
            <el-button type="info" size="small" @click="testConfig">刷新配置</el-button>
          </div>
        </template>

        <el-descriptions v-if="configInfo" :column="2" border>
          <el-descriptions-item label="压缩启用状态">
            <el-tag :type="configInfo.enabled ? 'success' : 'danger'">
              {{ configInfo.enabled ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="压缩阈值">
            {{ configInfo.thresholdSize }}
          </el-descriptions-item>
          <el-descriptions-item label="默认质量">
            {{ (configInfo.defaultQuality * 100).toFixed(0) }}%
          </el-descriptions-item>
          <el-descriptions-item label="头像尺寸">
            {{ configInfo.avatarSize }}x{{ configInfo.avatarSize }}
          </el-descriptions-item>
          <el-descriptions-item label="缩略图尺寸">
            {{ configInfo.thumbnailSize }}x{{ configInfo.thumbnailSize }}
          </el-descriptions-item>
          <el-descriptions-item label="头像质量">
            {{ (configInfo.avatarQuality * 100).toFixed(0) }}%
          </el-descriptions-item>
        </el-descriptions>

        <div v-if="!configInfo" class="config-unavailable">
          <el-empty description="配置信息不可用" />
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElCard } from 'element-plus'
import { getToken } from "@/utils/auth"

const baseUrl = import.meta.env.VITE_APP_BASE_API
const uploadCompressedUrl = baseUrl + '/common/upload/compressed'
const uploadAvatarUrl = baseUrl + '/common/upload/avatar'
const uploadThumbnailUrl = baseUrl + '/common/upload/thumbnail'
const testConfigUrl = baseUrl + '/test/image/config'

// 获取图片URL的基础地址
const getImageBaseUrl = () => {
  // 在开发环境中，通过Vite代理访问 /profile 路径
  // 在生产环境中，使用当前域名
  const isDev = import.meta.env.DEV
  if (isDev) {
    return window.location.origin  // 开发环境通过代理访问
  }
  return window.location.origin  // 生产环境直接访问
}

const headers = ref({ Authorization: "Bearer " + getToken() })

// 结果数据
const compressedResult = ref(null)
const avatarResult = ref(null)
const thumbnailResult = ref(null)
const configInfo = ref(null)

/**
 * 上传前检查
 */
function handleBeforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!')
    return false
  }

  return true
}

/**
 * 智能压缩成功回调
 */
function handleCompressedSuccess(response, uploadFile) {
  if (response.code === 200) {
    let imageUrl = response.url

    if (!response.url.startsWith('http')) {
      // 确保URL格式正确，避免双斜杠
      const baseUrl = getImageBaseUrl()
      const cleanUrl = response.url.startsWith('/') ? response.url.substring(1) : response.url
      imageUrl = `${baseUrl}/${cleanUrl}`
    }

    compressedResult.value = {
      ...response,
      url: imageUrl
    }
    ElMessage.success('智能压缩上传成功!')
  } else {
    ElMessage.error('智能压缩上传失败: ' + response.msg)
  }
}

/**
 * 头像压缩成功回调
 */
function handleAvatarSuccess(response, uploadFile) {
  if (response.code === 200) {
    let imageUrl = response.url

    if (!response.url.startsWith('http')) {
      // 确保URL格式正确，避免双斜杠
      const baseUrl = getImageBaseUrl()
      const cleanUrl = response.url.startsWith('/') ? response.url.substring(1) : response.url
      imageUrl = `${baseUrl}/${cleanUrl}`
    }

    avatarResult.value = {
      ...response,
      url: imageUrl
    }
    ElMessage.success('头像压缩上传成功!')
  } else {
    ElMessage.error('头像压缩上传失败: ' + response.msg)
  }
}

/**
 * 缩略图压缩成功回调
 */
function handleThumbnailSuccess(response, uploadFile) {
  if (response.code === 200) {
    let imageUrl = response.url

    if (!response.url.startsWith('http')) {
      // 确保URL格式正确，避免双斜杠
      const baseUrl = getImageBaseUrl()
      const cleanUrl = response.url.startsWith('/') ? response.url.substring(1) : response.url
      imageUrl = `${baseUrl}/${cleanUrl}`
    }

    thumbnailResult.value = {
      ...response,
      url: imageUrl
    }
    ElMessage.success('缩略图压缩上传成功!')
  } else {
    ElMessage.error('缩略图压缩上传失败: ' + response.msg)
  }
}

/**
 * 测试配置
 */
async function testConfig() {
  try {
    const response = await fetch(testConfigUrl, {
      headers: {
        'Authorization': 'Bearer ' + getToken()
      }
    })
    const data = await response.json()

    console.log('配置响应数据:', data) // 添加调试日志

    if (data.code === 200) {
      // 处理不同的响应格式
      if (data.data && data.data.config) {
        // 如果有config对象
        configInfo.value = data.data.config
      } else if (data.data && data.data.enabled !== undefined) {
        // 如果配置直接在data中
        configInfo.value = data.data
      } else if (data.data && typeof data.data === 'object') {
        // 如果data.data就是配置对象
        configInfo.value = data.data
      } else {
        // 如果配置不可用，使用默认值
        configInfo.value = {
          enabled: data.data?.configAvailable || false,
          thresholdSize: '2MB',
          avatarSize: 200,
          thumbnailSize: 400,
          avatarQuality: 0.9,
          thumbnailQuality: 0.8,
          defaultQuality: 0.85,
          maxWidth: 1920,
          maxHeight: 1080
        }
      }
      ElMessage.success('配置获取成功: ' + (data.data?.message || ''))
    } else {
      ElMessage.error('配置获取失败: ' + (data.msg || '未知错误'))
    }
  } catch (error) {
    console.error('配置请求错误:', error)
    ElMessage.error('配置请求失败: ' + error.message)
  }
}

onMounted(() => {
  testConfig()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-demo {
  text-align: center;
}

.result-info {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.result-info h4 {
  margin: 0 0 15px 0;
  color: #409EFF;
}

.preview-image {
  max-width: 100%;
  max-height: 200px;
  margin-top: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-top: 10px;
  border: 2px solid #409EFF;
}

.thumbnail-preview {
  max-width: 150px;
  max-height: 150px;
  margin-top: 10px;
  border-radius: 4px;
  border: 1px solid #E6A23C;
}

.config-unavailable {
  text-align: center;
  padding: 20px;
}

.el-upload {
  --el-upload-bg-color: #f5f7fa;
}

.el-upload__tip {
  color: #666;
  font-size: 12px;
  margin-top: 10px;
}
</style>