<template>
  <div class="blog-register-container mo-auth-page">
    <section class="auth-card">
      <router-link to="/blog" class="auth-brand">
        <span class="brand-mark">知</span>
        <span>ZhiBlog</span>
      </router-link>

      <div class="auth-head">
        <h2>注册</h2>
        <p class="sub">创建账号，开始发布与评论</p>
      </div>

      <div class="auth-switch">
        <router-link to="/login" class="switch-item">登录</router-link>
        <span class="switch-item active">注册</span>
      </div>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
      >
        <el-form-item prop="username" class="form-group">
          <label>昵称</label>
          <el-input v-model="registerForm.username" placeholder="你的昵称" clearable>
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email" class="form-group">
          <label>邮箱</label>
          <el-input v-model="registerForm.email" placeholder="name@example.com" clearable>
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="emailCode" class="form-group">
          <label>邮箱验证码</label>
          <div class="email-code-row">
            <el-input v-model="registerForm.emailCode" placeholder="邮箱验证码" clearable>
              <template #prefix>
                <el-icon><Key /></el-icon>
              </template>
            </el-input>
            <el-button
              class="code-button"
              :disabled="codeCountdown > 0"
              :loading="codeSending"
              @click="sendEmailCode"
            >
              {{ codeCountdown > 0 ? `${codeCountdown}秒` : '发送' }}
            </el-button>
          </div>
        </el-form-item>

        <div class="password-grid">
          <el-form-item prop="password" class="form-group">
            <label>密码</label>
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="5-20 位"
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
              placeholder="再次输入"
              show-password
              clearable
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </div>

        <el-form-item v-if="captchaEnabled" prop="code" class="form-group">
          <label>验证码</label>
          <div class="captcha-row">
            <el-input v-model="registerForm.code" placeholder="计算结果" clearable>
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
            同意用户协议和隐私政策
          </label>
        </div>

        <el-button type="primary" :loading="loading" class="auth-submit" @click="handleRegister">
          {{ loading ? '注册中...' : '注册' }}
        </el-button>
      </el-form>

      <p class="auth-footer">
        已有账号？
        <router-link to="/login">返回登录</router-link>
      </p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from '@/plugins/element-plus-service'
import type { FormInstance, FormRules } from 'element-plus'
import { User, Lock, Key, Message } from '@element-plus/icons-vue'
import { useBlogUserStore } from '@/stores/blogUser'
import { getCodeImg } from '@/api/blog/auth'
import { logger } from '@/utils/logger'

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
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      captchaUrl.value = 'data:image/jpeg;base64,' + res.img
      registerForm.uuid = res.uuid
      registerForm.code = ''
    } else {
      captchaUrl.value = ''
      registerForm.uuid = ''
      registerForm.code = ''
    }
  } catch (error) {
    logger.error('获取验证码失败:', error)
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
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: radial-gradient(circle at top left, var(--mo-p50), transparent 32%), var(--mo-n50);
}

.auth-card {
  width: min(100%, 420px);
  max-width: 420px;
  padding: 26px 28px;
  background: var(--mo-bg-card, #fff);
  border: 1px solid var(--mo-n200);
  border-radius: 8px;
  box-shadow: 0 16px 40px rgba(15, 23, 42, 0.08);
}

.auth-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  width: fit-content;
  margin-bottom: 18px;
  color: var(--mo-n900);
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
}

.brand-mark {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  color: #fff;
  background: var(--mo-p600);
  border-radius: 8px;
}

.auth-head h2 {
  margin: 0 0 6px;
  color: var(--mo-n900);
  font-size: 24px;
  font-weight: 700;
}

.sub {
  margin: 0 0 16px;
  color: var(--mo-n500);
  font-size: 14px;
}

.auth-switch {
  display: flex;
  gap: 4px;
  padding: 4px;
  margin-bottom: 16px;
  background: var(--mo-n100);
  border-radius: 8px;
}

.switch-item {
  flex: 1;
  padding: 7px 10px;
  color: var(--mo-n500);
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  text-decoration: none;
  border-radius: 6px;
  transition: all 0.2s;
}

.switch-item:hover {
  color: var(--mo-n700);
}

.switch-item.active {
  color: var(--mo-p600);
  background: var(--mo-bg-card, #fff);
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.08);
}

.register-form {
  margin-top: 0;
}

.form-group {
  display: block;
  margin-bottom: 12px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: var(--mo-n700);
  font-size: 13px;
  font-weight: 500;
}

.form-group :deep(.el-form-item__content) {
  display: block;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px var(--mo-n300) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow:
    0 0 0 1px var(--mo-p400) inset,
    0 0 0 3px var(--mo-p50);
}

.email-code-row,
.captcha-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.email-code-row :deep(.el-input),
.captcha-row :deep(.el-input) {
  flex: 1;
}

.code-button {
  width: 82px;
  border-radius: 8px;
}

.captcha-img {
  width: 106px;
  height: 32px;
  object-fit: cover;
  cursor: pointer;
  border: 1px solid var(--mo-n300);
  border-radius: 8px;
  transition: border-color 0.2s;
}

.captcha-img:hover {
  border-color: var(--mo-p400);
}

.form-options {
  margin: 2px 0 16px;
  font-size: 13px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--mo-n600);
}

.remember input {
  width: 14px;
  height: 14px;
  accent-color: var(--mo-p600);
}

.auth-submit {
  width: 100%;
  height: 38px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
}

.auth-footer {
  margin: 16px 0 0;
  color: var(--mo-n500);
  font-size: 13px;
  text-align: center;
}

.auth-footer a {
  color: var(--mo-p600);
  font-weight: 600;
  text-decoration: none;
}

.password-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 10px;
}

html.dark .mo-auth-page {
  background:
    radial-gradient(circle at top left, rgba(129, 140, 248, 0.12), transparent 34%), var(--mo-bg);
}

html.dark .auth-card {
  background: var(--mo-bg-card);
  border-color: var(--mo-border);
}

@media (max-width: 768px) {
  .mo-auth-page {
    padding: 16px;
  }

  .auth-card {
    padding: 22px;
  }

  .password-grid {
    grid-template-columns: 1fr;
    gap: 0;
  }
}
</style>
