<template>
  <div class="avatar-test-container">
    <el-card class="test-card">
      <template #header>
        <div class="card-header">
          <span>头像显示测试</span>
          <el-button type="primary" @click="runTests">开始测试</el-button>
        </div>
      </template>

      <div class="test-content">
        <!-- 测试结果显示 -->
        <div v-if="testResults.length > 0" class="test-results">
          <h3>测试结果:</h3>
          <div v-for="(result, index) in testResults" :key="index" class="test-result" :class="result.type">
            <div class="result-header">
              <el-icon>
                <Check v-if="result.type === 'success'" />
                <Close v-else-if="result.type === 'error'" />
                <Warning v-else />
              </el-icon>
              <span class="result-title">{{ result.title }}</span>
            </div>
            <div class="result-content">{{ result.message }}</div>
          </div>
        </div>

        <!-- 头像显示测试 -->
        <div class="avatar-tests">
          <h3>头像显示测试:</h3>

          <!-- 默认头像 -->
          <div class="test-item">
            <h4>1. 默认头像显示</h4>
            <div class="avatar-row">
              <span>默认头像:</span>
              <img :src="defaultAvatar" alt="默认头像" class="test-avatar" />
            </div>
          </div>

          <!-- 不同格式的头像测试 -->
          <div class="test-item">
            <h4>2. 不同格式头像测试</h4>
            <div class="avatar-row">
              <span>JPG头像:</span>
              <img :src="testJpgAvatar" alt="JPG头像" class="test-avatar" @error="handleError" />
              <span>PNG头像:</span>
              <img :src="testPngAvatar" alt="PNG头像" class="test-avatar" @error="handleError" />
              <span>无效头像:</span>
              <img :src="invalidAvatar" alt="无效头像" class="test-avatar" @error="handleError" />
            </div>
          </div>

          <!-- Base64头像测试 -->
          <div class="test-item">
            <h4>3. Base64头像处理测试</h4>
            <div class="avatar-row">
              <span>Base64头像(应被拒绝):</span>
              <img :src="base64Avatar" alt="Base64头像" class="test-avatar" @error="handleError" />
              <span>处理后应显示默认头像</span>
            </div>
          </div>

          <!-- 组件测试 -->
          <div class="test-item">
            <h4>4. AvatarUpload组件测试</h4>
            <div class="avatar-row">
              <span>头像上传组件:</span>
              <AvatarUpload
                v-model="testAvatarUrl"
                :preview-size="80"
                :editable="true"
                :show-info="true"
                @upload-success="handleUploadSuccess"
                @upload-error="handleUploadError"
              />
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Check, Close, Warning } from '@element-plus/icons-vue'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { processAvatarUrl, checkAvatarExists } from '@/api/blog/avatar'

// 测试数据
const testResults = ref([])
const testAvatarUrl = ref('')

// 测试头像URL
const defaultAvatar = ref('')
const testJpgAvatar = ref('/profile/upload/2025/12/18/test-avatar.jpg')
const testPngAvatar = ref('/profile/upload/2025/12/18/test-avatar.png')
const invalidAvatar = ref('/profile/upload/invalid/path/avatar.jpg')
const base64Avatar = ref('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==')

// 初始化默认头像
onMounted(() => {
  defaultAvatar.value = processAvatarUrl('')
})

// 运行测试
const runTests = async () => {
  testResults.value = []

  // 测试1: 默认头像处理
  try {
    const defaultUrl = processAvatarUrl('')
    if (defaultUrl && defaultUrl.startsWith('data:image/svg+xml')) {
      addTestResult('success', '默认头像处理', '成功生成默认SVG头像')
    } else {
      addTestResult('error', '默认头像处理', '默认头像格式不正确')
    }
  } catch (error) {
    addTestResult('error', '默认头像处理', `错误: ${error.message}`)
  }

  // 测试2: Base64头像处理
  try {
    const processedUrl = processAvatarUrl(base64Avatar.value)
    if (processedUrl && processedUrl.startsWith('data:image/svg+xml')) {
      addTestResult('success', 'Base64头像处理', '成功拒绝Base64格式并使用默认头像')
    } else {
      addTestResult('error', 'Base64头像处理', '未能正确拒绝Base64格式')
    }
  } catch (error) {
    addTestResult('error', 'Base64头像处理', `错误: ${error.message}`)
  }

  // 测试3: 相对路径处理
  try {
    const relativePath = '/profile/avatar/test.jpg'
    const processedUrl = processAvatarUrl(relativePath)
    if (processedUrl && processedUrl.includes(window.location.origin)) {
      addTestResult('success', '相对路径处理', '成功转换为绝对路径')
    } else {
      addTestResult('error', '相对路径处理', '相对路径转换失败')
    }
  } catch (error) {
    addTestResult('error', '相对路径处理', `错误: ${error.message}`)
  }

  // 测试4: 空值处理
  try {
    const emptyUrl = processAvatarUrl(null)
    if (emptyUrl && emptyUrl.startsWith('data:image/svg+xml')) {
      addTestResult('success', '空值处理', '空值正确处理为默认头像')
    } else {
      addTestResult('error', '空值处理', '空值处理失败')
    }
  } catch (error) {
    addTestResult('error', '空值处理', `错误: ${error.message}`)
  }

  // 测试5: 文件存在性检查
  try {
    // 测试不存在的文件
    const exists = await checkAvatarExists('/profile/nonexistent/avatar.jpg')
    if (!exists) {
      addTestResult('success', '文件存在性检查', '正确识别不存在的文件')
    } else {
      addTestResult('warning', '文件存在性检查', '文件存在性检查可能有问题')
    }
  } catch (error) {
    addTestResult('error', '文件存在性检查', `错误: ${error.message}`)
  }

  ElMessage.success('测试完成，请查看结果')
}

// 添加测试结果
const addTestResult = (type, title, message) => {
  testResults.value.push({ type, title, message })
}

// 处理图片错误
const handleError = (e) => {
  console.log('图片加载错误:', e.target.src)
  const defaultUrl = processAvatarUrl('')
  if (e.target.src !== defaultUrl) {
    e.target.src = defaultUrl
  }
  e.target.onerror = null
}

// 处理上传成功
const handleUploadSuccess = (data) => {
  console.log('头像上传成功:', data)
  ElMessage.success('头像上传成功')
  addTestResult('success', '头像上传', `上传成功: ${data.url}`)
}

// 处理上传错误
const handleUploadError = (error) => {
  console.log('头像上传失败:', error)
  addTestResult('error', '头像上传', `上传失败: ${error.message}`)
}
</script>

<style scoped>
.avatar-test-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.test-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.test-content {
  padding: 20px;
}

.test-results {
  margin-bottom: 30px;
}

.test-results h3 {
  margin-bottom: 15px;
  color: #333;
}

.test-result {
  padding: 12px;
  margin-bottom: 10px;
  border-radius: 6px;
  border-left: 4px solid;
}

.test-result.success {
  background-color: #f0f9ff;
  border-left-color: #67c23a;
}

.test-result.error {
  background-color: #fef0f0;
  border-left-color: #f56c6c;
}

.test-result.warning {
  background-color: #fdf6ec;
  border-left-color: #e6a23c;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  margin-bottom: 5px;
}

.result-title {
  color: #333;
}

.result-content {
  color: #666;
  font-size: 14px;
}

.avatar-tests {
  margin-top: 30px;
}

.avatar-tests h3 {
  margin-bottom: 20px;
  color: #333;
}

.test-item {
  margin-bottom: 25px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.test-item h4 {
  margin-bottom: 15px;
  color: #409eff;
}

.avatar-row {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.avatar-row span {
  color: #666;
  font-size: 14px;
}

.test-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 2px solid #e0e0e0;
  object-fit: cover;
  transition: all 0.3s ease;
}

.test-avatar:hover {
  border-color: #409eff;
  transform: scale(1.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .avatar-test-container {
    padding: 10px;
  }

  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: stretch;
  }

  .avatar-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .test-avatar {
    width: 50px;
    height: 50px;
  }
}
</style>