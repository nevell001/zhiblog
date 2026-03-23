<template>
  <div class="blog-register-container">
    <BlogNav />

    <div class="register-wrapper">
      <div class="register-card">
        <div class="register-header">
          <h1>用户注册</h1>
          <p>创建您的账号</p>
        </div>

        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱地址"
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
                v-model="registerForm.emailCode"
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

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
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
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              size="large"
              show-password
              clearable
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item
            v-if="captchaEnabled"
            prop="code"
          >
            <div class="captcha-row">
              <el-input
                v-model="registerForm.code"
                placeholder="验证码"
                size="large"
                clearable
                style="flex: 1"
              >
                <template #prefix>
                  <el-icon><Key /></el-icon>
                </template>
              </el-input>
              <img
                :src="captchaUrl"
                class="captcha-img"
                alt="验证码"
                @click="refreshCaptcha"
              />
            </div>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              class="register-button"
              @click="handleRegister"
            >
              {{ loading ? '注册中...' : '注册' }}
            </el-button>
          </el-form-item>

          <div class="register-footer">
            <router-link
              to="/login"
              class="link"
            >
              已有账号？立即登录
            </router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Key, Message } from '@element-plus/icons-vue'
import { useBlogUserStore } from '@/stores/blogUser'
import { getCodeImg } from '@/api/blog/auth'
import BlogNav from '@/components/BlogNav.vue'

const router = useRouter()
const blogUserStore = useBlogUserStore()

const registerFormRef = ref<FormInstance>()
const loading = ref(false)
const codeSending = ref(false)
const codeCountdown = ref(0)
const captchaEnabled = ref(true)
const captchaUrl = ref('')

const registerForm = reactive({
  username: '',
  email: '',
  emailCode: '',
  password: '',
  confirmPassword: '',
  code: '',
  uuid: ''
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
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在2到20个字符', trigger: 'blur' }
  ],
  email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
  emailCode: [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 20, message: '密码长度在5到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 刷新验证码
const refreshCaptcha = async () => {
  try {
    const res = await getCodeImg()
    captchaUrl.value = 'data:image/gif;base64,' + res.img
    registerForm.uuid = res.uuid
  } catch (error) {
    console.error('获取验证码失败:', error)
    captchaEnabled.value = false
  }
}

// 发送邮箱验证码
const sendEmailCode = async () => {
  if (!registerForm.email) {
    ElMessage.warning('请先输入邮箱地址')
    return
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(registerForm.email)) {
    ElMessage.warning('请输入正确的邮箱格式')
    return
  }

  try {
    codeSending.value = true
    await blogUserStore.sendRegisterCode(registerForm.email)
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

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return

  try {
    const valid = await registerFormRef.value.validate()
    if (!valid) return

    loading.value = true

    await blogUserStore.register({
      username: registerForm.username,
      email: registerForm.email,
      emailCode: registerForm.emailCode,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword,
      code: registerForm.code,
      uuid: registerForm.uuid
    })

    // 注册成功后跳转到统一登录页面
    router.push('/login')
  } catch (error: any) {
    ElMessage.error(error.message || '注册失败')
    // 刷新验证码
    if (captchaEnabled.value) {
      refreshCaptcha()
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 如果已登录，跳转到首页
  if (blogUserStore.isLogin) {
    router.push('/blog')
    return
  }
  // 获取验证码
  refreshCaptcha()
})
</script>

<style scoped>
.blog-register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
}

.register-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.register-card {
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

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.register-header p {
  color: #999;
  font-size: 14px;
  margin: 0;
}

.register-form {
  margin-top: 24px;
}

.email-code-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s;
}

.captcha-img:hover {
  border-color: #667eea;
}

.register-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-top: 8px;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.register-footer .link {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s;
}

.register-footer .link:hover {
  color: #764ba2;
}
</style>
