<template>
  <div class="friendlink-apply-page">
    <BlogLayout>
      <div class="apply-container">
        <div class="apply-card">
          <h1 class="apply-title">申请友情链接</h1>
          <p class="apply-sub">提交后需要管理员审核，通过后会在网站底部展示</p>

          <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
            <el-form-item label="网站名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="你的网站名称"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
            <el-form-item label="网站地址" prop="url">
              <el-input
                v-model="form.url"
                placeholder="https://example.com"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item label="联系邮箱" prop="email">
              <el-input
                v-model="form.email"
                placeholder="用于接收审核结果（选填）"
                maxlength="100"
              />
            </el-form-item>
            <el-form-item label="站点描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="3"
                placeholder="一句话介绍你的网站（选填）"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="submitting" @click="handleSubmit">
                提交申请
              </el-button>
              <el-button @click="$router.push('/blog')">返回首页</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </BlogLayout>
  </div>
</template>

<script setup lang="ts" name="BlogFriendLinkApply">
import { reactive, ref } from 'vue'
import { ElMessage } from '@/plugins/element-plus-service'
import type { FormInstance, FormRules } from 'element-plus'
import BlogLayout from '@/components/BlogLayout.vue'
import { applyFriendLink } from '@/api/blog/friendLink'

const formRef = ref<FormInstance>()
const submitting = ref(false)
const form = reactive({
  name: '',
  url: '',
  email: '',
  description: ''
})

const validateUrl = (_rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error('请输入网站地址'))
  } else if (!/^https?:\/\/.+/.test(value)) {
    callback(new Error('请以 http(s):// 开头填写完整地址'))
  } else {
    callback()
  }
}

const rules: FormRules = {
  name: [{ required: true, message: '请输入网站名称', trigger: 'blur' }],
  url: [{ required: true, validator: validateUrl, trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    await applyFriendLink({ ...form })
    ElMessage.success('申请已提交，请等待管理员审核')
    form.name = ''
    form.url = ''
    form.email = ''
    form.description = ''
  } catch (error: any) {
    ElMessage.error(error?.message || '提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.friendlink-apply-page {
  min-height: 100vh;
}
.apply-container {
  max-width: 620px;
  margin: 0 auto;
  padding: 96px 20px 48px;
}
.apply-card {
  padding: 28px;
  background: var(--el-bg-color-overlay, #fff);
  border: 1px solid var(--el-border-color-lighter, #ebeef5);
  border-radius: 12px;
}
.apply-title {
  margin: 0 0 6px;
  font-size: 22px;
  color: var(--el-text-color-primary, #303133);
}
.apply-sub {
  margin: 0 0 22px;
  font-size: 13px;
  color: var(--el-text-color-secondary, #909399);
}
</style>
