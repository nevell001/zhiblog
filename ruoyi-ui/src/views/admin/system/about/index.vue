<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <i class="el-icon-user"></i>
            关于博主设置
          </span>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-form-item label="博主姓名" prop="blog_author">
              <el-input v-model="form.blog_author" placeholder="请输入博主姓名" maxlength="50" />
            </el-form-item>

            <el-form-item label="职位标题" prop="author_title">
              <el-input
                v-model="form.author_title"
                placeholder="请输入职位标题，如：全栈开发工程师"
                maxlength="100"
              />
            </el-form-item>

            <el-form-item label="个人简介" prop="blog_desc">
              <el-input
                v-model="form.blog_desc"
                type="textarea"
                :rows="4"
                placeholder="请输入个人简介"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>

            <el-form-item label="头像URL" prop="blog_avatar">
              <el-input v-model="form.blog_avatar" placeholder="请输入头像图片URL">
                <template #append>
                  <el-button @click="showImageUpload = true">上传</el-button>
                </template>
              </el-input>
              <div v-if="form.blog_avatar" class="avatar-preview">
                <img :src="form.blog_avatar" alt="头像预览" />
              </div>
            </el-form-item>
          </el-tab-pane>

          <!-- 技能专长 -->
          <el-tab-pane label="技能专长" name="skills">
            <el-form-item label="技能标签">
              <el-tag
                v-for="(skill, index) in skillsList"
                :key="index"
                closable
                style="margin-right: 10px; margin-bottom: 10px"
                @close="removeSkill(index)"
              >
                {{ skill }}
              </el-tag>
              <el-input
                v-if="inputVisible"
                ref="inputRef"
                v-model="inputValue"
                class="input-new-tag"
                size="small"
                @keyup.enter="handleInputConfirm"
                @blur="handleInputConfirm"
              />
              <el-button v-else class="button-new-tag" size="small" @click="showInput">
                + 添加技能
              </el-button>
            </el-form-item>

            <el-alert title="提示" type="info" :closable="false" style="margin-top: 20px">
              技能标签将在"关于博主"模块中展示，建议添加 4-8 个核心技能
            </el-alert>
          </el-tab-pane>

          <!-- 联系方式 -->
          <el-tab-pane label="联系方式" name="contact">
            <el-form-item label="邮箱地址" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱地址" type="email" />
            </el-form-item>

            <el-form-item label="GitHub" prop="github_url">
              <el-input v-model="form.github_url" placeholder="请输入GitHub主页链接">
                <template #prepend>https://</template>
              </el-input>
            </el-form-item>

            <el-form-item label="微博" prop="weibo_url">
              <el-input v-model="form.weibo_url" placeholder="请输入微博主页链接">
                <template #prepend>https://</template>
              </el-input>
            </el-form-item>

            <el-form-item label="微信二维码" prop="wechat_qr">
              <el-input v-model="form.wechat_qr" placeholder="请输入微信二维码图片URL">
                <template #append>
                  <el-button @click="showQRUpload = true">上传</el-button>
                </template>
              </el-input>
              <div v-if="form.wechat_qr" class="qr-preview">
                <img :src="form.wechat_qr" alt="二维码预览" />
              </div>
            </el-form-item>
          </el-tab-pane>

          <!-- 博客信息 -->
          <el-tab-pane label="博客信息" name="blog">
            <el-form-item label="博客名称" prop="blog_name">
              <el-input v-model="form.blog_name" placeholder="请输入博客名称" maxlength="100" />
            </el-form-item>

            <el-form-item label="博客副标题" prop="blog_subtitle">
              <el-input
                v-model="form.blog_subtitle"
                placeholder="请输入博客副标题"
                maxlength="200"
              />
            </el-form-item>

            <el-form-item label="关键词" prop="blog_keywords">
              <el-input
                v-model="form.blog_keywords"
                type="textarea"
                :rows="3"
                placeholder="请输入博客关键词，用逗号分隔"
              />
            </el-form-item>

            <el-form-item label="ICP备案号" prop="icp_number">
              <el-input v-model="form.icp_number" placeholder="请输入ICP备案号" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>

        <el-form-item style="margin-top: 20px">
          <el-button type="primary" :loading="loading" @click="submitForm">
            <i class="el-icon-check"></i>
            保存设置
          </el-button>
          <el-button @click="resetForm">
            <i class="el-icon-refresh"></i>
            重置
          </el-button>
          <el-button type="success" @click="previewSettings">
            <i class="el-icon-view"></i>
            预览效果
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图片上传对话框 -->
    <el-dialog v-model="showImageUpload" title="上传头像" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action="/api/common/upload"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
        </template>
      </el-upload>
    </el-dialog>

    <!-- 二维码上传对话框 -->
    <el-dialog v-model="showQRUpload" title="上传微信二维码" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action="/api/common/upload"
        :on-success="handleQRSuccess"
        :before-upload="beforeQRUpload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
        </template>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from '@/plugins/element-plus-service'
import { listSetting, updateSetting, addSetting } from '@/api/blog/setting'

const activeTab = ref('basic')
const loading = ref(false)
const showImageUpload = ref(false)
const showQRUpload = ref(false)
const inputVisible = ref(false)
const inputValue = ref('')
const inputRef = ref<any>(null)
const formRef = ref<any>(null)

const form = reactive({
  blog_author: '',
  author_title: '',
  blog_desc: '',
  blog_avatar: '',
  email: '',
  github_url: '',
  weibo_url: '',
  wechat_qr: '',
  blog_name: '',
  blog_subtitle: '',
  blog_keywords: '',
  icp_number: ''
})

const skillsList = ref([])

const rules = {
  blog_author: [{ required: true, message: '请输入博主姓名', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

// 加载设置
const loadSettings = async () => {
  try {
    const response = await listSetting({})
    if (response && response.rows) {
      const settings = response.rows
      settings.forEach(item => {
        if (item.settingKey === 'skills') {
          try {
            skillsList.value = JSON.parse(item.settingValue || '[]')
          } catch (e) {
            skillsList.value = []
          }
        } else if (Object.hasOwn(form, item.settingKey)) {
          form[item.settingKey] = item.settingValue || ''
        }
      })
    }
  } catch (error) {
    console.error('加载设置失败:', error)
    ElMessage.error('加载设置失败')
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()
    loading.value = true

    // 准备要保存的设置
    const settings = []

    // 基本信息
    Object.keys(form).forEach(key => {
      if (form[key]) {
        settings.push({
          settingKey: key,
          settingValue: form[key],
          description: getDescription(key)
        })
      }
    })

    // 技能列表
    if (skillsList.value.length > 0) {
      settings.push({
        settingKey: 'skills',
        settingValue: JSON.stringify(skillsList.value),
        description: '技能专长列表'
      })
    }

    // 批量保存设置
    for (const setting of settings) {
      try {
        // 先尝试更新，如果失败则新增
        const response = await listSetting({ settingKey: setting.settingKey })
        if (response.rows && response.rows.length > 0) {
          await updateSetting({
            id: response.rows[0].id,
            ...setting
          })
        } else {
          await addSetting(setting)
        }
      } catch (error) {
        console.error(`保存设置 ${setting.settingKey} 失败:`, error)
      }
    }

    ElMessage.success('保存成功')
    await loadSettings()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    loading.value = false
  }
}

// 获取设置描述
const getDescription = key => {
  const descriptions = {
    blog_author: '博主姓名',
    author_title: '职位标题',
    blog_desc: '个人简介',
    blog_avatar: '头像URL',
    email: '邮箱地址',
    github_url: 'GitHub链接',
    weibo_url: '微博链接',
    wechat_qr: '微信二维码',
    blog_name: '博客名称',
    blog_subtitle: '博客副标题',
    blog_keywords: '博客关键词',
    icp_number: 'ICP备案号'
  }
  return descriptions[key] || key
}

// 重置表单
const resetForm = () => {
  ElMessageBox.confirm('确定要重置表单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      formRef.value.resetFields()
      skillsList.value = []
      loadSettings()
    })
    .catch(() => {})
}

// 预览效果
const previewSettings = () => {
  window.open('/blog', '_blank')
}

// 技能标签相关
const removeSkill = index => {
  skillsList.value.splice(index, 1)
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    inputRef.value.focus()
  })
}

const handleInputConfirm = () => {
  if (inputValue.value) {
    skillsList.value.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

// 头像上传
const handleAvatarSuccess = response => {
  if (response.code === 200) {
    form.blog_avatar = response.url
    showImageUpload.value = false
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

const beforeAvatarUpload = file => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
  }
  return isImage && isLt2M
}

// 二维码上传
const handleQRSuccess = response => {
  if (response.code === 200) {
    form.wechat_qr = response.url
    showQRUpload.value = false
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

const beforeQRUpload = beforeAvatarUpload

onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
}

.card-title i {
  margin-right: 8px;
  color: #409eff;
}

.avatar-preview {
  margin-top: 10px;
}

.avatar-preview img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #409eff;
}

.qr-preview {
  margin-top: 10px;
}

.qr-preview img {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
}

.input-new-tag {
  width: 120px;
  margin-right: 10px;
}

.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

:deep(.el-tabs--border-card) {
  box-shadow: none;
  border: 1px solid #dcdfe6;
}

:deep(.el-tabs__content) {
  padding: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

.upload-demo {
  text-align: center;
}
</style>
