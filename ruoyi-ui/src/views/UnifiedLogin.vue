<template>
  <div class="unified-login-container mo-auth-page">
    <section class="auth-card">
      <router-link to="/blog" class="auth-brand">
        <span class="brand-mark">知</span>
        <span>ZhiBlog</span>
      </router-link>

      <div class="auth-head">
        <h2>登录</h2>
        <p class="sub">继续写作与管理内容</p>
      </div>

      <div class="auth-switch">
        <span class="switch-item active">登录</span>
        <router-link to="/blog/auth/register" class="switch-item">注册</router-link>
      </div>

      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username" class="form-group">
          <label>邮箱 / 用户名</label>
          <el-input
            ref="usernameInputRef"
            v-model="loginForm.username"
            placeholder="用户名或邮箱"
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
            placeholder="输入密码"
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
              placeholder="计算结果"
              clearable
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

        <el-button type="primary" :loading="loading" class="auth-submit" @click="handleLogin">
          {{ loading ? '登录中...' : '登录' }}
        </el-button>
      </el-form>

      <p class="auth-footer">
        还没有账号？
        <router-link to="/blog/auth/register">立即注册</router-link>
      </p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from '@/plugins/element-plus-service'
import type { FormInstance, FormRules, InputInstance } from 'element-plus'
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
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      captchaUrl.value = 'data:image/jpeg;base64,' + res.img
      loginForm.uuid = res.uuid
      loginForm.code = ''
    } else {
      captchaUrl.value = ''
      loginForm.uuid = ''
      loginForm.code = ''
    }
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
  padding: 24px;
  background: radial-gradient(circle at top left, var(--mo-p50), transparent 32%), var(--mo-n50);
}

.auth-card {
  width: min(100%, 380px);
  max-width: 380px;
  padding: 28px;
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
  margin-bottom: 22px;
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
  margin: 0 0 18px;
  color: var(--mo-n500);
  font-size: 14px;
}

.auth-switch {
  display: flex;
  gap: 4px;
  padding: 4px;
  margin-bottom: 18px;
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

.login-form {
  margin-top: 0;
}

.form-group {
  display: block;
  margin-bottom: 14px;
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

.captcha-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.captcha-row :deep(.el-input) {
  flex: 1;
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 2px 0 16px;
  font-size: 13px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--mo-n600);
  font-size: 13px;
}

.remember input {
  width: 14px;
  height: 14px;
  accent-color: var(--mo-p600);
}

.forgot {
  color: var(--mo-p600);
  font-size: 13px;
  font-weight: 500;
  text-decoration: none;
}

.auth-submit {
  width: 100%;
  height: 38px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
}

.auth-footer {
  margin: 18px 0 0;
  color: var(--mo-n500);
  font-size: 13px;
  text-align: center;
}

.auth-footer a {
  color: var(--mo-p600);
  font-weight: 600;
  text-decoration: none;
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
}
</style>
