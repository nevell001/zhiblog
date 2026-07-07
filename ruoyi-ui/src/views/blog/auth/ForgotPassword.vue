<template>
  <div class="blog-forgot-password-container">
    <BlogNav />

    <div class="forgot-password-wrapper">
      <div class="forgot-password-card">
        <div class="forgot-password-header">
          <h1>找回密码</h1>
          <p>通过邮箱重置您的密码</p>
        </div>

        <el-steps
          :active="currentStep"
          align-center
          class="steps"
        >
          <el-step title="验证邮箱" />
          <el-step title="重置密码" />
          <el-step title="完成" />
        </el-steps>

        <!-- 步骤1: 验证邮箱 -->
        <div
          v-if="currentStep === 0"
          class="step-content"
        >
          <el-form
            ref="emailFormRef"
            :model="emailForm"
            :rules="emailRules"
            class="forgot-password-form"
          >
            <el-form-item prop="email">
              <el-input
                v-model="emailForm.email"
                placeholder="请输入注册邮箱"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="emailCode">
              <div class="email-code-row">
                <el-input
                  v-model="emailForm.emailCode"
                  placeholder="邮箱验证码"
                  size="large"
                  clearable
                  style="flex: 1"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <el-button
                  size="large"
                  :disabled="codeCountdown > 0"
                  :loading="codeSending"
                  @click="sendEmailCode"
                >
                  {{ codeCountdown > 0 ? `${codeCountdown}秒后重试` : '发送验证码' }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="submit-button"
                @click="verifyEmail"
              >
                下一步
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 步骤2: 重置密码 -->
        <div
          v-if="currentStep === 1"
          class="step-content"
        >
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            class="forgot-password-form"
          >
            <el-form-item prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="新密码"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="确认新密码"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="submit-button"
                @click="resetPassword"
              >
                重置密码
              </el-button>
              <el-button
                size="large"
                class="back-button"
                @click="currentStep = 0"
              >
                上一步
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 步骤3: 完成 -->
        <div
          v-if="currentStep === 2"
          class="step-content success-content"
        >
          <el-result
            icon="success"
            title="密码重置成功"
            sub-title="您的密码已成功重置"
          >
            <template #extra>
              <el-button
                type="primary"
                size="large"
                @click="goToLogin"
              >
                立即登录
              </el-button>
              <el-button
                size="large"
                @click="goToHome"
              >
                返回首页
              </el-button>
            </template>
          </el-result>
        </div>

        <div class="forgot-password-footer">
          <router-link
            to="/login"
            class="link"
          >
            返回登录
          </router-link>
          <router-link
            to="/blog/auth/register"
            class="link"
          >
            注册账号
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Lock, Key, Message } from '@element-plus/icons-vue'
import { sendResetCode, resetPassword as resetPasswordApi } from '@/api/blog/auth'
import BlogNav from '@/components/BlogNav.vue'

const router = useRouter()

const emailFormRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()
const loading = ref(false)
const codeSending = ref(false)
const codeCountdown = ref(0)
const currentStep = ref(0)

const emailForm = reactive({
  email: '',
  emailCode: ''
})

const passwordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

const validateEmail = (rule: any, value: any, callback: any) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!value) {
    callback(new Error('请输入邮箱地址'))
  } else if (!emailRegex.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const emailRules: FormRules = {
  email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
  emailCode: [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }]
}

const passwordRules: FormRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 5, max: 20, message: '密码长度在5到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }]
}

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!emailForm.email) {
    ElMessage.warning('请先输入邮箱地址')
    return
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(emailForm.email)) {
    ElMessage.warning('请输入正确的邮箱格式')
    return
  }

  try {
    codeSending.value = true
    await sendResetCode(emailForm.email)
    ElMessage.success('验证码已发送，请查收邮箱')

    // 开始倒计时
    codeCountdown.value = 60
    const timer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error: any) {
    ElMessage.error(error.message || '发送验证码失败')
  } finally {
    codeSending.value = false
  }
}

// 验证邮箱
const verifyEmail = async () => {
  if (!emailFormRef.value) return

  try {
    const valid = await emailFormRef.value.validate()
    if (!valid) return

    // 邮箱验证成功，进入下一步
    currentStep.value = 1
  } catch (error: any) {
    ElMessage.error(error.message || '验证失败')
  }
}

// 重置密码
const resetPassword = async () => {
  if (!passwordFormRef.value) return

  try {
    const valid = await passwordFormRef.value.validate()
    if (!valid) return

    loading.value = true

    await resetPasswordApi({
      email: emailForm.email,
      code: emailForm.emailCode,
      newPassword: passwordForm.newPassword,
      confirmPassword: passwordForm.confirmPassword
    })

    ElMessage.success('密码重置成功')
    currentStep.value = 2
  } catch (error: any) {
    ElMessage.error(error.message || '密码重置失败')
  } finally {
    loading.value = false
  }
}

// 跳转到统一登录页
const goToLogin = () => {
  router.push('/login')
}

// 返回首页
const goToHome = () => {
  router.push('/blog')
}
</script>

<style scoped>
.blog-forgot-password-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #4a7bff 0%, #6b8cff 100%);
  position: relative;
}

.forgot-password-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.forgot-password-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  animation: fadeInUp 0.5s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.forgot-password-header {
  text-align: center;
  margin-bottom: 32px;
}

.forgot-password-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.forgot-password-header p {
  color: #999;
  font-size: 14px;
  margin: 0;
}

.steps {
  margin-bottom: 32px;
}

.step-content {
  margin-top: 24px;
}

.forgot-password-form {
  margin-top: 24px;
}

.email-code-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.submit-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-top: 8px;
}

.back-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-top: 12px;
}

.success-content {
  padding: 20px 0;
}

.forgot-password-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
}

.forgot-password-footer .link {
  color: #4a7bff;
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password-footer .link:hover {
  color: #6b8cff;
}
</style>
