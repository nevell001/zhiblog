<template>
  <div class="unified-login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <h1>用户登录</h1>
          <p>统一登录入口</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <el-form-item prop="username">
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

          <el-form-item prop="password">
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

          <el-form-item v-if="captchaEnabled" prop="code">
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
              class="login-button"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <div class="footer-links">
            <router-link to="/blog/auth/register" class="link">
              用户注册
            </router-link>
            <span class="divider">|</span>
            <router-link to="/blog/auth/forgot-password" class="link">
              忘记密码？
            </router-link>
          </div>
        </div>
      </div>
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
  const isProduction = import.meta.env.MODE === 'production'
  
  // 生产环境：隐藏敏感信息，只显示通用错误
  if (isProduction) {
    // 所有认证相关的错误都统一为"用户名或密码错误"
    if (errorMessage.includes('用户') || errorMessage.includes('密码') || errorMessage.includes('账号')) {
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
    const token = res.token || res.data?.token

    if (!token) {
      throw new Error('登录失败：未获取到token')
    }

    // 统一使用Admin-Token存储
    setToken(token)
    // 同时更新 store 中的 token 状态
    userStore.token = token
    console.log('✅ Token 已设置:', token)

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
    console.log('🔄 跳转到:', redirect)
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
.unified-login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-wrapper {
  width: 100%;
  max-width: 420px;
}

.login-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
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

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.login-header p {
  color: #999;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-top: 24px;
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

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-top: 8px;
}

.login-footer {
  margin-top: 24px;
  text-align: center;
}

.footer-links {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-size: 14px;
}

.footer-links .link {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links .link:hover {
  color: #764ba2;
}

.footer-links .divider {
  color: #dcdfe6;
}
</style>
