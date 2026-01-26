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
              v-model="loginForm.username"
              placeholder="用户名/邮箱"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
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
            <router-link to="/register" class="link">
              管理员注册
            </router-link>
            <span class="divider">|</span>
            <router-link to="/blog/auth/register" class="link">
              博客用户注册
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import { getCodeImg } from '@/api/login'
import { unifiedLogin } from '@/api/unifiedAuth'
import { setToken, setBlogToken } from '@/utils/auth'

const router = useRouter()
const route = useRoute()

const loginFormRef = ref<FormInstance>()
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

    // 从响应中获取token和用户类型
    const token = res.token || res.data?.token
    const userType = res.userType || res.data?.userType

    if (!token) {
      throw new Error('登录失败：未获取到token')
    }

    // 根据用户类型设置不同的token
    // '00' = 管理员, '01' = 博客用户
    if (userType === '01') {
      // 博客用户
      setBlogToken(token)
      ElMessage.success('博客用户登录成功')

      // 跳转到博客首页
      const redirect = (route.query.redirect as string) || '/blog'
      router.push(redirect)
    } else {
      // 管理员
      setToken(token)
      ElMessage.success('管理员登录成功')

      // 跳转到管理后台首页
      const redirect = (route.query.redirect as string) || '/index'
      router.push(redirect)
    }
  } catch (error: any) {
    ElMessage.error(error.message || '登录失败')
    // 刷新验证码
    if (captchaEnabled.value) {
      refreshCaptcha()
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 获取验证码
  refreshCaptcha()
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
