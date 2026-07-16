<template>
  <div class="unified-login-container mo-auth-page">
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
        <h2>欢迎回来</h2>
        <p class="sub">登录你的账号，继续创作之旅</p>

        <div class="auth-tabs">
          <span class="tab active">登录</span>
          <router-link to="/blog/auth/register" class="tab">注册</router-link>
        </div>

        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username" class="form-group">
            <label>邮箱 / 用户名</label>
            <el-input
              ref="usernameInputRef"
              v-model="loginForm.username"
              placeholder="用户名/邮箱"
              size="large"
              clearable
              @keyup.enter="focusPassword"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password" class="form-group">
            <label>密码</label>
            <el-input
              ref="passwordInputRef"
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              size="large"
              show-password
              clearable
              @keyup.enter="handleLogin"
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
                ref="codeInputRef"
                v-model="loginForm.code"
                placeholder="验证码"
                size="large"
                clearable
                style="flex: 1"
                @keyup.enter="handleLogin"
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
              记住我
            </label>
            <router-link to="/blog/auth/forgot-password" class="forgot">忘记密码？</router-link>
          </div>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              class="auth-submit"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-divider">还没有账号？</div>
        <router-link to="/blog/auth/register" class="auth-secondary">创建墨 Blog 账号</router-link>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules, type InputInstance } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import { getCodeImg } from '@/api/login'
import { unifiedLogin } from '@/api/unifiedAuth'
import { setToken } from '@/utils/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const usernameInputRef = ref<InputInstance>()
const passwordInputRef = ref<InputInstance>()
const codeInputRef = ref<InputInstance>()
const loading = ref(false)
const captchaEnabled = ref(true)
const captchaUrl = ref('')

const loginForm = reactive({
  username: '',
  password: '',
  code: '',
  uuid: ''
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 聚焦到密码输入框
const focusPassword = () => {
  nextTick(() => {
    passwordInputRef.value?.focus()
  })
}

// 刷新验证码
const refreshCaptcha = async () => {
  try {
    const res = await getCodeImg()
    captchaUrl.value = 'data:image/gif;base64,' + res.img
    loginForm.uuid = res.uuid
  } catch (error) {
    console.error('获取验证码失败:', error)
    captchaEnabled.value = false
  }
}

// 获取友好的错误提示
const getErrorMessage = (error: any): string => {
  const errorMessage = error.message || error.msg || error.data?.msg || ''
  const isProduction = import.meta.env?.MODE === 'production'

  // 生产环境：隐藏敏感信息，只显示通用错误
  if (isProduction) {
    // 所有认证相关的错误都统一为"用户名或密码错误"
    if (
      errorMessage.includes('用户') ||
      errorMessage.includes('密码') ||
      errorMessage.includes('账号')
    ) {
      return '用户名或密码错误，请检查后重试'
    }

    // 验证码错误可以显示
    if (errorMessage.includes('验证码')) {
      return '验证码错误，请重新输入'
    }

    // 其他错误显示通用提示
    return '登录失败，请稍后重试'
  }

  // 开发环境：显示详细错误信息以便调试
  if (errorMessage.includes('用户不存在') || errorMessage.includes('用户名或密码错误')) {
    return '用户名或密码错误，请检查后重试'
  }

  if (errorMessage.includes('密码错误')) {
    return '密码错误，请重新输入'
  }

  if (errorMessage.includes('验证码')) {
    return '验证码错误，请重新输入'
  }

  if (errorMessage.includes('账号已被锁定')) {
    return '账号已被锁定，请联系管理员'
  }

  if (errorMessage.includes('账号已被禁用')) {
    return '账号已被禁用，请联系管理员'
  }

  return errorMessage || '登录失败，请稍后重试'
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return

    loading.value = true

    const res = await unifiedLogin({
      username: loginForm.username,
      password: loginForm.password,
      code: loginForm.code,
      uuid: loginForm.uuid
    })

    // 从响应中获取token
    const loginResponse = res as any
    const token = loginResponse.token || loginResponse.data?.token

    if (!token) {
      throw new Error('登录失败：未获取到token')
    }

    // 统一使用Admin-Token存储
    setToken(token)
    // 同时更新 store 中的 token 状态
    userStore.token = token

    // 立即获取用户信息，避免页面刷新后才显示登录状态
    try {
      await userStore.getInfo()
      ElMessage.success({
        message: '登录成功，欢迎回来！',
        duration: 2000
      })
    } catch (error) {
      console.error('获取用户信息失败:', error)
      ElMessage.warning({
        message: '登录成功，但获取用户信息失败，请刷新页面',
        duration: 3000
      })
    }

    // 跳转到首页，路由守卫会根据用户权限自动跳转
    const redirect = (route.query.redirect as string) || '/index'
    router.push(redirect)
  } catch (error: any) {
    const errorMessage = getErrorMessage(error)
    ElMessage.error({
      message: errorMessage,
      duration: 3000
    })
    // 刷新验证码
    if (captchaEnabled.value) {
      refreshCaptcha()
      // 清空验证码输入框
      loginForm.code = ''
      nextTick(() => {
        codeInputRef.value?.focus()
      })
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 获取验证码
  refreshCaptcha()
  // 自动聚焦到用户名输入框
  nextTick(() => {
    usernameInputRef.value?.focus()
  })
})
</script>

<style scoped>
.mo-auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: #fafaf9;
}

.auth-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  width: min(1100px, 100%);
  min-height: min(720px, calc(100vh - 40px));
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
  padding: 56px 48px;
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

.login-form {
  margin-top: 0;
}

.form-group {
  display: block;
  margin-bottom: 18px;
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

.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 22px;
  font-size: 14px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #57534e;
  font-size: 13px;
}

.remember input {
  width: 16px;
  height: 16px;
  accent-color: #4f46e5;
}

.forgot {
  color: #4f46e5;
  font-size: 13px;
  font-weight: 500;
  text-decoration: none;
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
  .mo-auth-page {
    align-items: stretch;
    padding: 0;
  }

  .auth-wrapper {
    grid-template-columns: 1fr;
    min-height: 100vh;
    box-shadow: none;
  }

  .auth-visual {
    display: none;
  }

  .auth-form {
    padding: 32px 24px;
  }
}
</style>
