<template>
  <div class="blog-register-container mo-auth-page">
    <BlogLayout>
      <div class="auth-wrapper">
        <section class="auth-visual">
          <div class="brand-lg">
            墨
            <span>Blog</span>
          </div>
          <div class="tagline">记录思考，分享洞见</div>
          <div class="features">
            <div class="feat">
              <span class="check">✓</span>
              沉浸式阅读体验，衬线排版
            </div>
            <div class="feat">
              <span class="check">✓</span>
              Markdown 写作，草稿自动保存
            </div>
            <div class="feat">
              <span class="check">✓</span>
              标签与分类，内容组织有序
            </div>
            <div class="feat">
              <span class="check">✓</span>
              评论互动，思想碰撞
            </div>
          </div>
        </section>

        <section class="auth-form">
          <h2>创建账号</h2>
          <p class="sub">填写信息，即刻开始创作</p>

          <div class="auth-tabs">
            <router-link to="/blog/auth/login" class="tab">登录</router-link>
            <span class="tab active">注册</span>
          </div>

          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            class="register-form"
          >
            <el-form-item prop="username" class="form-group">
              <label>昵称</label>
              <el-input
                v-model="registerForm.username"
                placeholder="你的昵称"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="email" class="form-group">
              <label>邮箱</label>
              <el-input
                v-model="registerForm.email"
                placeholder="name@example.com"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="emailCode" class="form-group">
              <label>邮箱验证码</label>
              <div class="email-code-row">
                <el-input
                  v-model="registerForm.emailCode"
                  placeholder="请输入验证码"
                  size="large"
                  clearable
                  style="flex: 1"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <el-button
                  class="code-button"
                  size="large"
                  :disabled="codeCountdown > 0"
                  :loading="codeSending"
                  @click="sendEmailCode"
                >
                  {{ codeCountdown > 0 ? `${codeCountdown}秒后重试` : '发送验证码' }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item prop="password" class="form-group">
              <label>密码</label>
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="至少 8 位，含字母和数字"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="confirmPassword" class="form-group">
              <label>确认密码</label>
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="再次输入密码"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item v-if="captchaEnabled" prop="code" class="form-group">
              <label>验证码</label>
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
                <img :src="captchaUrl" class="captcha-img" alt="验证码" @click="refreshCaptcha" />
              </div>
            </el-form-item>

            <div class="form-options">
              <label class="remember">
                <input type="checkbox" checked />
                我已阅读并同意用户协议和隐私政策
              </label>
            </div>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="auth-submit"
                @click="handleRegister"
              >
                {{ loading ? '注册中...' : '注 册' }}
              </el-button>
            </el-form-item>

            <div class="auth-divider">已有账号？</div>
            <router-link to="/blog/auth/login" class="auth-secondary">返回登录</router-link>
          </el-form>
        </section>
      </div>
    </BlogLayout>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Key, Message } from '@element-plus/icons-vue'
import { useBlogUserStore } from '@/stores/blogUser'
import { getCodeImg } from '@/api/blog/auth'
import BlogLayout from '@/components/BlogLayout.vue'

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
.mo-auth-page {
  min-height: 100vh;
  padding-top: 64px;
  background: #fafaf9;
}

.auth-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: calc(100vh - 60px);
  width: 100%;
  max-width: 1100px;
  margin: 0 auto;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
}

.auth-visual {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 56px 48px;
  overflow: hidden;
  color: #fff;
  background: linear-gradient(135deg, #4f46e5, #3730a3);
}

.auth-visual::before,
.auth-visual::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
}

.auth-visual::before {
  top: -60px;
  right: -60px;
  width: 240px;
  height: 240px;
}

.auth-visual::after {
  bottom: -40px;
  left: -40px;
  width: 180px;
  height: 180px;
}

.brand-lg {
  position: relative;
  z-index: 1;
  margin-bottom: 8px;
  font-size: 38px;
  font-weight: 700;
}

.brand-lg span {
  color: #c7d2fe;
}

.tagline {
  position: relative;
  z-index: 1;
  margin-bottom: 36px;
  font-size: 16px;
  opacity: 0.8;
}

.features {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.feat {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  opacity: 0.9;
}

.check {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
}

.auth-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  max-width: 420px;
  margin: 0 auto;
  padding: 48px;
}

.auth-form h2 {
  margin: 0 0 6px;
  color: #1c1917;
  font-size: 26px;
  font-weight: 700;
}

.sub {
  margin: 0 0 28px;
  color: #78716c;
  font-size: 14px;
}

.auth-tabs {
  display: flex;
  margin-bottom: 28px;
  border-bottom: 1px solid #e7e5e4;
}

.tab {
  flex: 1;
  padding: 10px 0;
  color: #78716c;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  text-decoration: none;
  border-bottom: 2px solid transparent;
  transition: all 0.15s;
}

.tab:hover {
  color: #44403c;
}

.tab.active {
  color: #4f46e5;
  border-bottom-color: #4f46e5;
}

.register-form {
  margin-top: 0;
}

.form-group {
  display: block;
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #44403c;
  font-size: 13px;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #d6d3d1 inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow:
    0 0 0 1px #818cf8 inset,
    0 0 0 3px #eef2ff;
}

.email-code-row,
.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.code-button {
  border-radius: 8px;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
  border: 1px solid #d6d3d1;
  border-radius: 8px;
  transition: all 0.3s;
}

.captcha-img:hover {
  border-color: #818cf8;
}

.form-options {
  margin-bottom: 22px;
  font-size: 13px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #57534e;
}

.remember input {
  width: 16px;
  height: 16px;
  accent-color: #4f46e5;
}

.auth-submit {
  width: 100%;
  height: 46px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
}

.auth-divider {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 24px 0 16px;
  color: #a8a29e;
  font-size: 12px;
}

.auth-divider::before,
.auth-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e7e5e4;
}

.auth-secondary {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 46px;
  color: #4f46e5;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  border: 1px solid #d6d3d1;
  border-radius: 8px;
  transition: all 0.2s;
}

.auth-secondary:hover {
  border-color: #818cf8;
  background: #eef2ff;
}

@media (max-width: 768px) {
  .auth-wrapper {
    grid-template-columns: 1fr;
    box-shadow: none;
  }

  .auth-visual {
    display: none;
  }

  .auth-form {
    padding: 32px 24px;
  }

  .email-code-row {
    align-items: stretch;
    flex-direction: column;
  }
}
</style>
